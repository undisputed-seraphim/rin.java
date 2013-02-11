package rin.gui;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JComponent;

public class GUIComponent {
	protected JComponent target;
	protected ArrayList<GUIComponent> children = new ArrayList<GUIComponent>();
	protected int childCount = 0;
	
	public GUIComponent setSize( int w, int h ) { this.target.setSize( w, h ); return this; }
	public GUIComponent setLocation( int x, int y ) { this.target.setLocation( x, y ); return this; }
	public GUIComponent setBackgroundColor( int r, int g, int b ) { return this.setBackgroundColor( r, g, b, 255 ); }
	public GUIComponent setBackgroundColor( int r, int g, int b, int a ) { this.target.setBackground( new Color( r, g, b, a ) ); return this; }
	
	public GUIComponent show() { this.target.setVisible( true ); return this; }
	public GUIComponent hide() { this.target.setVisible( false ); return this; }
	
	public GUIComponent addTo( GUIComponent component ) { component.add( this ); return this; }
	public GUIComponent add( GUIComponent component ) {
		this.children.add( component );
		this.target.add( this.children.get( this.childCount++ ).target );
		component.show();
		this.target.validate();
		this.target.repaint();
		return this;
	}
	
	public GUIComponent destroy() {
		for( GUIComponent g : this.children )
			g = g.destroy();
		this.children.clear();
		
		this.target.setVisible( false );
		this.target = null;
		
		return null;
	}
}
