package rin.gl.gui;

import java.util.ArrayList;

import rin.gl.event.GLEvent.PickInEvent;
import rin.gl.gui.GLGUIFactory.GLGUIEvent;
import rin.gl.lib3d.Poly;

public class GLGUIComponent<T> extends Poly {
	protected GLGUIComponent<?> parent = null;
	private ArrayList<GLGUIComponent<?>> children = new ArrayList<GLGUIComponent<?>>();
	
	private GLGUIFactory.Position position = GLGUIFactory.Position.BOTTOM;
	private int margin = 5;
	
	public GLGUIComponent( String id ) {
		super( id );
	}

	@SuppressWarnings("unchecked") private T actual() { return (T)this; }
	
	public T setPosition( GLGUIFactory.Position position ) {
		
		return this.actual();
	}
	
	public T add( GLGUIComponent<?> component ) {
		component.parent = this;
		this.children.add( component );
		return this.update();
	}
	
	public T update() {
		
		return this.actual();
	}
	
	public T show() {
		if( !this.isVisible() ) {
			this.setVisible( true );
			
			if( this.runOnShow != null )
				this.runOnShow.run();
		}
		
		return this.update();
	}
	
	private GLGUIEvent runOnShow = null;
	public T onShow( GLGUIEvent e ) {
		/* catch any default animations and setup component properly */
		if( e.equals( GLGUIFactory.Transitions.SCALE_BURST_SHOW ) ) {
			this.setScale( 0, 0.02f, 1 );
		}
		
		this.runOnShow = e.setTarget( this );
		return this.update();
	}
	
	public T hide() {
		if( this.isVisible() ) {
			if( this.runOnHide != null )
				this.runOnHide.run();
			
			else
				this.setVisible( false );
		}
		
		return this.actual();
	}
	
	private GLGUIEvent runOnHide = null;
	public T onHide( GLGUIEvent e ) {
		this.runOnHide = e.setTarget( this );
		return this.actual();
	}
	
	private GLGUIFactory.GLGUIEvent runOnMouseIn = null;
	public T onMouseIn( GLGUIEvent e ) {
		this.runOnMouseIn = e.setTarget( this );
		return this.actual();
	}
	
	@Override public void processPickInEvent( PickInEvent e ) {
		System.out.println( "picked!" );
		
		if( this.runOnMouseIn != null )
			this.runOnMouseIn.run();
	}
	
}
