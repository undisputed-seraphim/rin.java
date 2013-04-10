package rin.engine.view.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

import rin.engine.meta.RinChainable;
import rin.engine.view.gui.GUIFactory.GUIEvent;

/**
 * Represents a component in the Graphical User Interface.
 * <p>
 * @param <T> Swing component type
 * @param <G> Actual RComponent type (subclass)
 */
public abstract class RComponent<T extends Container, G extends RComponent<T, G>> extends REventAdapter<G> {

	protected String id;
	protected Container target;
	protected RComponent<?, ?> parent;
	protected boolean canHaveChildren = true;
	protected Class<?>[] validParents = null;
	protected HashMap<String, RComponent<?, ?>> children = new HashMap<String, RComponent<?, ?>>();
	
	protected RComponent( String id, Container target ) {
		this.id = id;
		this.target = target;
		GUIManager.get().components.put( id, this );
	}
	
	public abstract T swing();
	protected abstract G actual();
	
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
	public G add( RComponent<?, ?> component ) {
		// GUIWindows may only be top level containers
		if( component instanceof RWindow )
			return this.actual();
		
		// some elements may not have children
		else if ( this.canHaveChildren == false )
			return this.actual();
		
		// if this component has a strict set of valid parents, check them
		if( component.validParents != null ) {
			boolean valid = false;
			for( Class<?> cls : component.validParents )
				valid = valid || cls.isInstance( this );
			
			if( !valid ) {
				System.out.println( this.getClass() + " is not a valid parent for " + component.getClass() );
				return this.actual();
			}
		}
				
		component.parent = this;
		this.target.add( component.target );
		this.children.put( component.id, component );
		return this.update();
	}

	public G onClick( GUIEvent<G> event ) {
		this.runOnClick = event.setTarget( this.actual() );
		return this.actual();
	}
	
	public void destroy() {
		
	}
	
}
