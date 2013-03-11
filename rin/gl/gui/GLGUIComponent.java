package rin.gl.gui;

import java.util.ArrayList;

import rin.gl.gui.GLGUIFactory.GLGUIEvent;
import rin.gl.lib3d.Poly;

public class GLGUIComponent<T> extends Poly {
	protected GLGUIComponent<?> parent = null;
	private ArrayList<GLGUIComponent<?>> children = new ArrayList<GLGUIComponent<?>>();
	
	protected float left, right, bottom, top;
	
	private GLGUIFactory.Position position = GLGUIFactory.Position.BOTTOM;
	public GLGUIFactory.Position getGUIPosition() { return this.position; }
	
	private GLGUIFactory.Alignment alignment = GLGUIFactory.Alignment.CENTER;
	public GLGUIFactory.Alignment getGUIAlignment() { return this.alignment; }
	
	private Integer width = null, height = null;
	public int getWindowWidth() { return GLGUIFactory.getRootGUI().getWidth(); }
	public int getWindowHeight() { return GLGUIFactory.getRootGUI().getHeight(); }
	
	public float[][] getGUIVertices() {
		
		float l = 0, t = 0, r = 0, b = 0, mh = 0, mw = 0;
		
		/* no width or height set; item should take up entire space of desired position */
		int width, height;
		mh = (float)this.margin / (float)this.getWindowHeight();
		mw = (float)this.margin / (float)this.getWindowWidth();
		
		switch( this.position ) {
		
		case BOTTOM:
			if( this.width == null && this.height == null ) {
				b = -1 + mh;
				t = 0 - mh;
				r = 1 - mw;
				l = -1 + mw;
			} else {
				
			}
			break;
		
		case TOP:
			if( this.width == null && this.height == null ) {
				b = 0 + mh;
				t = 1 - mh;
				r = 1 - mw;
				l = -1 + mw;
			} else {
				
			}
			break;
			
		case CENTER:
			if( this.width == null && this.height == null ) {
				b = -1 + mh;
				t = 1 - mh;
				r = 1 - mw;
				l = -1 + mw;
			} else {
				
			}
			break;
			
		case RIGHT:
			if( this.width == null && this.height == null ) {
				b = -1 + mh;
				t = 1 - mh;
				r = 1 - mw;
				l = 0 + mw;
			} else {
				
			}
			break;
			
		}
		
		this.top = t;
		this.left = l;
		this.right = r;
		this.bottom = b;
		
		return new float[][] {
				{ l, t, -1.0f }, { r, t, -1.0f },
				{ l, b, -1.0f }, { r, b, -1.0f }
		};
	}
	
	private int margin, padding;
	
	public GLGUIComponent( String id, GLGUIFactory.GLGUIParams p ) {
		super( id );		
		this.width = p.width;
		this.height = p.height;
		this.margin = p.margin;
		this.padding = p.padding;
		this.position = p.position;
		this.alignment = p.alignment;
		
		this.setMouseControlled( true );
		this.setVisible( false );
	}

	@SuppressWarnings("unchecked") private T actual() { return (T)this; }
	public T setWidth( Integer width ) { this.width = width; return this.update(); }
	public T setHeight( Integer height ) { this.height = height; return this.update(); }
	
	public T setGUIPosition( GLGUIFactory.Position position ) {
		
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
	
	public T show() { return this.show( false ); }
	public T show( boolean children ) {
		if( !this.isVisible() ) {
			this.setVisible( true );
			
			if( this.runOnShow != null )
				this.runOnShow.run();
			
			if( children )
				for( GLGUIComponent<?> g : this.children )
					g.show( true );
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
	
	public T onMouseIn( GLGUIEvent e ) {
		this.runOnPickIn = e.setTarget( this );
		return this.actual();
	}
	
	public T onMouseOut( GLGUIEvent e ) {
		this.runOnPickOut = e.setTarget( this );
		return this.actual();
	}
	
	public T onClick( GLGUIEvent e ) {
		this.runOnPickClick = e.setTarget( this );
		return this.actual();
	}
	
}
