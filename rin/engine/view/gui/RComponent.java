package rin.engine.view.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.util.HashMap;

import rin.engine.meta.RinChainable;
import rin.engine.view.gui.GUIFactory.GUIEvent;
import rin.engine.view.gui.GUIFactory.WindowEvent;
import rin.engine.view.gui.event.GUIEventAdapter;
import rin.engine.view.gui.event.GUIFocusListener;
import rin.engine.view.gui.event.GUIMouseListener;

/**
 * Represents a component in the Graphical User Interface.
 * <p>
 * @param <T> Swing component type
 * @param <G> Actual RComponent type (subclass)
 */
public abstract class RComponent<T extends Container, G extends RComponent<T, G>> extends GUIEventAdapter<G>
		implements GUIMouseListener<G>, GUIFocusListener<G> {

	protected String id;
	protected Container target;
	protected RComponent<?, ?> parent;
	
	protected boolean canHaveChildren = true;
	private Class<?>[] validParents = null;
	private Class<?>[] validChildren = null;
	protected HashMap<String, RComponent<?, ?>> children = new HashMap<String, RComponent<?, ?>>();
	
	protected RComponent( String id, Container target ) {
		this.id = id;
		this.target = target;
		
		if( this instanceof RWindow )
			GUIManager.get().windows.put( id, (RWindow)this );
		
		else
			GUIManager.get().components.put( id, this );
	}
	
	public abstract T swing();
	protected abstract G actual();
	protected void setValidParents( Class<?> ... classes ) { this.validParents = classes; }
	protected void setValidChildren( Class<?> ... classes ) { this.validChildren = classes; }
	
	public String getId() { return this.id; }
	public int getWidth() { return this.target.getPreferredSize().width; }
	public int getHeight() { return this.target.getPreferredSize().height; }
	
	public boolean isVisible() { return this.target.isVisible(); }
	public boolean isEnabled() { return this.target.isEnabled(); }
	public boolean isFocused() { return this.target.hasFocus(); }
	
	@RinChainable
	public G setWidth( int width ) {
		this.target.setPreferredSize( new Dimension( width, this.getHeight() ) );
		return this.update();
	}
	
	@RinChainable
	public G setHeight( int height ) {
		this.target.setPreferredSize( new Dimension( this.getWidth(), height ) );
		return this.update();
	}
	
	@RinChainable
	public G setSize( int width, int height ) {
		this.setWidth( width );
		this.setHeight( height );
		
		return this.update();
	}
	
	@RinChainable
	public G setContextMenu( RContextMenu menu ) {
		this.contextMenu = menu;
		return this.actual();
	}
	
	@RinChainable
	public G show() {
		this.target.setVisible( true );
		return this.update();
	}
	
	@RinChainable
	public G hide() {
		this.target.setVisible( false );
		return this.update();
	}
	
	@RinChainable
	public G enable() {
		this.target.setEnabled( true );
		return this.update();
	}
	
	@RinChainable
	public G disable() {
		this.target.setEnabled( false );
		return this.update();
	}
	
	@RinChainable
	public G focus() {
		this.target.requestFocusInWindow();
		return this.actual();
	}
	
	@RinChainable
	public G update() {
		this.target.revalidate();
		this.target.repaint();
		
		return this.actual();
	}
	
	@RinChainable
	public G add( RComponent<?, ?> ... components ) {
		// can this element even have children?
		if( this.canHaveChildren == false )
			return this.actual();
		
		for( RComponent<?, ?> component: components ) {			
			// if the component has a strict set of valid parents, check them
			if( component.validParents != null ) {
				boolean valid = false;
				for( Class<?> cls : component.validParents )
					valid = valid || cls.isInstance( this );
				
				if( !valid ) {
					System.out.println( this.getClass() + " is not a valid parent for " + component.getClass() );
					continue;
				}
			}
			
			// if this component has a strict set of valid children, check them
			if( this.validChildren != null ) {
				boolean valid = false;
				for( Class<?> cls : this.validChildren )
					valid = valid || cls.isInstance( component );
				
				if( !valid ) {
					System.out.println( component.getClass() + " is not a valid child for " + this.getClass() );
					continue;
				}
			}
					
			component.parent = this;
			this.target.add( component.target );
			this.children.put( component.id, component );
		}
		
		return this.update();
	}

	// MOUSE EVENTS
	
	private boolean isMouseListening = false;
	
	@Override
	@RinChainable
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
	@RinChainable
	public G onClick( GUIEvent<G> e ) {
		this.setMouseListening( true );
		this.runOnClick = e.setTarget( this.actual() );
		return this.actual();
	}
	
	@Override
	@RinChainable
	public G onMouseIn( GUIEvent<G> e ) {
		this.setMouseListening( true );
		this.runOnMouseIn = e.setTarget( this.actual() );
		return this.actual();
	}
	
	@Override
	@RinChainable
	public G onMouseOut( GUIEvent<G> e ) {
		this.setMouseListening( true );
		this.runOnMouseOut = e.setTarget( this.actual() );
		return this.actual();
	}
	
	@Override
	@RinChainable
	public G onMouseUp( GUIEvent<G> e ) {
		this.setMouseListening( true );
		this.runOnMouseUp = e.setTarget( this.actual() );
		return this.actual();
	}
	
	@Override
	@RinChainable
	public G onMouseDown( GUIEvent<G> e ) {
		this.setMouseListening( true );
		this.runOnMouseDown = e.setTarget( this.actual() );
		return this.actual();
	}
	
	// FOCUS EVENTS
	
	private boolean isFocusListening = false;
	
	@Override
	@RinChainable
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
	@RinChainable
	public G onFocusGained( GUIEvent<G> e ) {
		if( this instanceof RWindow ) {
			((RWindow)this).onWindowFocusGained( (WindowEvent)e );
			return this.actual();
		}
		
		this.setFocusListening( true );
		this.runOnFocusGained = e.setTarget( this.actual() );
		return this.actual();
	}
	
	@Override
	@RinChainable
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
		
	}
	
}
