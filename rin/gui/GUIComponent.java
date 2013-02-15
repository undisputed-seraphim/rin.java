package rin.gui;

import java.awt.Color;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GUIComponent implements ActionListener, KeyListener, ChangeListener {
	protected ArrayList<GUIComponent> children = new ArrayList<GUIComponent>();
	protected JComponent target = null;
	protected int childCount = 0;
	
	protected boolean aligned = false;
	protected float alignX = GUIManager.ALIGN_LEFT;
	protected float alignY = GUIManager.ALIGN_CENTER;
	
	public GUIComponent setBackgroundColor( int r, int g, int b ) { return this.setBackgroundColor( r, g, b, 255 ); }
	public GUIComponent setBackgroundColor( int r, int g, int b, int a ) { this.target.setBackground( new Color( r, g, b, a ) ); return this; }
	public GUIComponent setLayout( GUIManager.GUILayout layout ) { this.target.setLayout( (LayoutManager) layout ); return this; }
	
	public GUIComponent setAligned( boolean val ) { return this.setAligned( val, this.alignX, this.alignY ); }
	public GUIComponent setAligned( boolean val, float alignX ) { return this.setAligned( val, alignX, this.alignY ); }
	public GUIComponent setAligned( boolean val, float alignX, float alignY ) {
		this.aligned = val;
		this.alignX = alignX;
		this.alignY = alignY;
		return this;		
	}
	
	public GUIComponent setAlignX( float alignment ) { this.alignX = alignment; return this; }
	public GUIComponent setAlignY( float alignment ) { this.alignY = alignment; return this; }
	public GUIComponent setAlignment( float alignX, float alignY ) { this.setAlignmentX( alignX ); return this.setAlignmentY( alignY ); }
	public GUIComponent setAlignmentX( float alignment ) { this.target.setAlignmentX( alignment ); return this; }
	public GUIComponent setAlignmentY( float alignment ) { this.target.setAlignmentY( alignment ); return this; }
	
	public GUIComponent show() { this.target.setVisible( true ); return this; }
	public GUIComponent hide() { this.target.setVisible( false ); return this; }
	
	public GUIComponent addTo( GUIComponent component ) { component.add( this ); return this; }
	public GUIComponent add( GUIComponent component ) {
		if( this.aligned )
			component.setAlignment( this.alignX, this.alignY );
		
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
	
	@Override public void keyTyped( KeyEvent e ) {}
	@Override public void keyPressed( KeyEvent e ) {}
	@Override public void keyReleased( KeyEvent e ) {}
	@Override public void actionPerformed( ActionEvent e ) {}
	@Override public void stateChanged( ChangeEvent e ) {}
	
	public void focused() {
		System.out.println( "focus" );
	}
}
