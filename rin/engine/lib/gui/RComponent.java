package rin.engine.lib.gui;

import java.awt.Container;
import java.awt.Component;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JComponent;

import rin.engine.lib.gui.GUIFactory.GUIEvent;
import rin.engine.lib.gui.GUIFactory.WindowEvent;
import rin.engine.lib.gui.event.GUIEventAdapter;
import rin.engine.lib.gui.event.GUIFocusListener;
import rin.engine.lib.gui.event.GUIMouseListener;

/**
 * Represents a component in the Graphical User Interface.
 * <p>
 * @param <T> Underlying Swing component type
 * @param <G> Actual RComponent type (subclass)
 */
public abstract class RComponent<T extends Component, G extends RComponent<T, G>> extends GUIEventAdapter<G>
		implements GUIMouseListener<G>, GUIFocusListener<G> {

	protected String id;
	protected Component target;
	protected RComponent<?, ?> parent;
	
	protected boolean canHaveChildren = true;
	private Class<?>[] validParents = null;
	private Class<?>[] validChildren = null;
	protected HashMap<String, RComponent<?, ?>> children = new HashMap<String, RComponent<?, ?>>();
	
	protected RComponent( String id, Component target ) {
		this.id = id;
		this.target = target;
		
		GUIManager.add( id, this );
	}
	
	public abstract T swing();
	protected abstract G actual();
	protected void setValidParents( Class<?> ... classes ) { this.validParents = classes; }
	protected void setValidChildren( Class<?> ... classes ) { this.validChildren = classes; }
	protected boolean isValidChild( RComponent<?,?> component ) {
		// can this element even have children?
		if( this.canHaveChildren == false )
			return false;
		
		// if the component has a strict set of valid parents, check them
		if( component.validParents != null ) {
			boolean valid = false;
			for( Class<?> cls : component.validParents )
				valid = valid || cls.isInstance( this );
			
			if( !valid ) {
				System.out.println( this.getClass() + " is not a valid parent for " + component.getClass() );
				return false;
			}
		}
		
		// if this component has a strict set of valid children, check them
		if( this.validChildren != null ) {
			boolean valid = false;
			for( Class<?> cls : this.validChildren )
				valid = valid || cls.isInstance( component );
			
			if( !valid ) {
				System.out.println( component.getClass() + " is not a valid child for " + this.getClass() );
				return false;
			}
		}
		
		return true;
	}
	
	public String getId() { return this.id; }
	public int getWidth() { return this.target.getPreferredSize().width; }
	public int getHeight() { return this.target.getPreferredSize().height; }
	
	public boolean isVisible() { return this.target.isVisible(); }
	public boolean isEnabled() { return this.target.isEnabled(); }
	public boolean isFocused() { return this.target.hasFocus(); }
	
	public G setWidth( int width ) {
		this.target.setPreferredSize( new Dimension( width, this.getHeight() ) );
		return this.update();
	}
	
	public G setHeight( int height ) {
		this.target.setPreferredSize( new Dimension( this.getWidth(), height ) );
		return this.update();
	}
	
	public G setSize( int width, int height ) {
		this.setWidth( width );
		this.setHeight( height );
		
		return this.update();
	}
	
	public G setContextMenu( RContextMenu menu ) {
		if( this.target instanceof JComponent ) {
			((JComponent)this.target).setComponentPopupMenu( menu.swing() );
			this.setMouseListening( true );
			this.contextMenu = menu;
		}
		
		return this.actual();
	}
	
	public G show() {
		this.swing().setVisible( true );
		return this.update();
	}
	
	public G hide() {
		this.swing().setVisible( false );
		return this.update();
	}
	
	public G enable() {
		this.swing().setEnabled( true );
		return this.update();
	}
	
	public G disable() {
		this.swing().setEnabled( false );
		return this.update();
	}
	
	public G focus() {
		this.swing().requestFocusInWindow();
		return this.actual();
	}
	
	public G update() {
		this.swing().validate();
		this.swing().repaint();
		
		return this.actual();
	}
	
	public G add( RComponent<?, ?> ... components ) {		
		for( RComponent<?, ?> component : components )
			this.add( component );
		
		return this.actual();
	}
	
	/**
	 * <p>
	 * NOTE: Unless overriden, default behaivor is to add components to the return value of {@link #swing}.
	 * @param component RComponent to be added
	 * @return calling instance
	 */
	public G add( RComponent<?, ?> component ) {
		// ensure component being added is a valid child
		if( !this.isValidChild( component ) )
			return this.actual();
		
		// remove component being added from it's parent
		if( component.parent != null )
			component.parent.remove( component );
		
		component.parent = this;
		((Container)target).add( component.swing() );
		this.children.put( component.getId(), component );
		
		return this.update();
	}

	public G remove( RComponent<?,?> component ) {
		if( this.children.containsKey( component.getId() ) ) {
			this.children.remove( component.getId() );
			((Container)target).remove( component.swing() );
			
			if( this.swing().getParent() != null )
				this.swing().getParent().remove( this.swing() );
		}
		
		return this.update();
	}
	
	public G removeAndDestroy( RComponent<?,?> component ) {
		this.remove( component );
		component.destroy();
		
		return this.update();
	}
	
	public G removeAndDestroyAll() {
		LinkedList<String> childs = new LinkedList<String>();
		for( String s : this.children.keySet() )
			childs.add( s );
		
		String current;
		while( (current = childs.poll()) != null )
			this.removeAndDestroy( this.children.get( current ) );
		
		if( this.children.size() > 0 )
			System.out.println( "removeAndDestroyAll failed! " + this.children.size() );
		this.children.clear();
		
		return this.update();
	}
	
	// MOUSE EVENTS
	
	private boolean isMouseListening = false;
	
	@Override
	public boolean isMouseListening() { return this.isMouseListening; }
	
	@Override
	public G setMouseListening( boolean listen ) {
		if( !this.isMouseListening && listen ) {
			this.swing().addMouseListener( this );
			this.isMouseListening = true;
		} else if( this.isMouseListening && !listen ) {
			this.swing().removeMouseListener( this );
			this.isMouseListening = false;
		}
		
		return this.actual();
	}
	
	@Override
	public G onClick( GUIEvent<G> e ) {
		this.setMouseListening( true );
		this.runOnClick = e.setTarget( this.actual() );
		return this.actual();
	}
	
	@Override
	public G onMouseIn( GUIEvent<G> e ) {
		this.setMouseListening( true );
		this.runOnMouseIn = e.setTarget( this.actual() );
		return this.actual();
	}
	
	@Override
	public G onMouseOut( GUIEvent<G> e ) {
		this.setMouseListening( true );
		this.runOnMouseOut = e.setTarget( this.actual() );
		return this.actual();
	}
	
	@Override
	public G onMouseUp( GUIEvent<G> e ) {
		this.setMouseListening( true );
		this.runOnMouseUp = e.setTarget( this.actual() );
		return this.actual();
	}
	
	@Override
	public G onMouseDown( GUIEvent<G> e ) {
		this.setMouseListening( true );
		this.runOnMouseDown = e.setTarget( this.actual() );
		return this.actual();
	}
	
	// FOCUS EVENTS
	
	private boolean isFocusListening = false;
	
	@Override
	public boolean isFocusListening() { return this.isFocusListening; }
	
	@Override
	public G setFocusListening( boolean listen ) {
		if( this instanceof RWindow ) {
			((RWindow)this).setWindowFocusListening( listen );
			return this.actual();
		}
		
		if( !this.isFocusListening && listen ) {
			this.swing().addFocusListener( this );
			this.isFocusListening = true;
		} else if( this.isFocusListening && !listen ) {
			this.swing().removeFocusListener( this );
			this.isFocusListening = false;
		}
		
		return this.actual();
	}
	
	@Override
	public G onFocusGained( GUIEvent<G> e ) {
		// if this component is a top level container, promote to onWindowFocusGained
		if( this instanceof RWindow ) {
			((RWindow)this).onWindowFocusGained( (WindowEvent)e );
			return this.actual();
		}
		
		this.setFocusListening( true );
		this.runOnFocusGained = e.setTarget( this.actual() );
		return this.actual();
	}
	
	@Override
	public G onFocusLost( GUIEvent<G> e ) {
		if( this instanceof RWindow ) {
			((RWindow)this).onWindowFocusLost( (WindowEvent)e );
			return this.actual();
		}
		
		this.setFocusListening( true );
		this.runOnFocusLost = e.setTarget( this.actual() );
		return this.actual();
	}
	
	public void destroy() {
		this.setFocusListening( false );
		this.setMouseListening( false );

		this.removeAndDestroyAll();
		GUIManager.remove( this );
		
		if( this.contextMenu != null )
			this.contextMenu.destroy();
		
		if( this.parent != null ) {
			this.parent.remove( this );
			this.parent.update();
		}
		
		this.target = null;
	}
	
}
