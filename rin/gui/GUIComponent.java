package rin.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GUIComponent<T> implements ActionListener, KeyListener, ChangeListener {
	protected ArrayList<GUIComponent<?>> children = new ArrayList<GUIComponent<?>>();
	protected JComponent target = null;
	protected int childCount = 0;
	
	protected boolean aligned = false;
	protected GUIManager.Alignment alignX = GUIManager.Alignment.LEFT;
	protected GUIManager.Alignment alignY = GUIManager.Alignment.CENTER;
	
	public GUIComponent<T> setBackgroundColor( int r, int g, int b ) { return this.setBackgroundColor( r, g, b, 255 ); }
	public GUIComponent<T> setBackgroundColor( int r, int g, int b, int a ) { this.target.setBackground( new Color( r, g, b, a ) ); return this; }
	public GUIComponent<T> setLayout( GUIManager.GUILayout layout ) { this.target.setLayout( layout ); return this; }
	public GUIComponent<T> setToolTip( String tip ) { this.target.setToolTipText( tip ); return this; }
	
	public GUIComponent<T> setAligned( boolean val ) {
		this.aligned = val;
		return this;
	}
	
	public GUIComponent<T> setAligned( boolean val, GUIManager.Alignment alignX ) {
		this.setAligned( val );
		this.setAlignX( alignX );
		return this;
	}
	
	public GUIComponent<T> setAligned( boolean val, GUIManager.Alignment alignX, GUIManager.Alignment alignY ) {
		this.setAligned( val );
		this.setAlignX( alignX );
		this.setAlignY( alignY );
		return this;		
	}
	
	public GUIComponent<T> setAlignX( GUIManager.Alignment alignment ) { this.alignX = alignment; return this; }
	public GUIComponent<T> setAlignY( GUIManager.Alignment alignment ) { this.alignY = alignment; return this; }
	
	public GUIComponent<T> setAlignmentX( GUIManager.Alignment alignment ) {
		this.target.setAlignmentX( alignment.value );
		return this;
	}
	
	public GUIComponent<T> setAlignmentY( GUIManager.Alignment alignment ) {
		this.target.setAlignmentY( alignment.value );
		return this;
	}
	
	public GUIComponent<T> setAlignment( GUIManager.Alignment alignX, GUIManager.Alignment alignY ) {
		this.setAlignmentX( alignX );
		this.setAlignmentY( alignY );
		return this;
	}
	
	public GUIComponent<T> show() { this.target.setVisible( true ); return this; }
	public GUIComponent<T> hide() { this.target.setVisible( false ); return this; }
	
	public GUIComponent<T> addTo( GUIComponent<?> component ) { component.add( this ); return this; }
	public GUIComponent<T> add( GUIComponent<?> component ) {
		if( this.aligned )
			component.setAlignment( this.alignX, this.alignY );
		
		this.children.add( component );
		this.target.add( this.children.get( this.childCount++ ).target );
		component.show();
		this.target.validate();
		this.target.repaint();
		return this;
	}
	
	@Override public void keyTyped( KeyEvent e ) {}
	@Override public void keyPressed( KeyEvent e ) {}
	@Override public void keyReleased( KeyEvent e ) {}
	@Override public void actionPerformed( ActionEvent e ) {}
	@Override public void stateChanged( ChangeEvent e ) {}
	
	public void focused() {
		System.out.println( "focus" );
	}
	
	public GUIComponent<T> destroy() {
		for( GUIComponent<?> g : this.children )
			g = g.destroy();
		this.children.clear();
		
		this.target.setVisible( false );
		this.target = null;
		
		return null;
	}
}
