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
	protected String id = null;
	protected int childCount = 0;
	
	protected boolean canHaveChildren = true;
	
	@SuppressWarnings("unchecked") public T actual() { return (T)this; }
	
	public T setBackgroundColor( int r, int g, int b ) { return this.setBackgroundColor( r, g, b, 255 ); }
	public T setBackgroundColor( int r, int g, int b, int a ) { this.target.setBackground( new Color( r, g, b, a ) ); return this.update(); }
	public T setLayout( GUIManager.GUILayout layout ) { this.target.setLayout( layout ); return this.update(); }
	public T setToolTip( String tip ) { this.target.setToolTipText( tip ); return this.update(); }
	
	public T update() { this.target.validate(); this.target.repaint(); return this.actual(); }
	public T enable() { this.target.setEnabled( true ); return this.update(); }
	public T disable() { this.target.setEnabled( false ); return this.update(); }
	public T show() { this.target.setVisible( true ); return this.actual(); }
	public T hide() { this.target.setVisible( false ); return this.actual(); }
	
	public T addTo( String id ) { return this.addTo( GUIManager.get( id ) ); }
	public T addTo( GUIComponent<?> component ) { component.add( this ); return this.actual(); }
	public T add( String id ) { return this.add( GUIManager.get( id ) ); }
	public T add( GUIComponent<?> component ) {
		if( this.canHaveChildren ) {
			if( component == null ) {
				System.out.println( "[ERROR] Attempted to add nonexistant component." );
				return this.actual();
			}
			
			this.children.add( component );
			this.target.add( this.children.get( this.childCount++ ).target );
			component.show();
			return this.update();
		}
		
		System.out.println( "[ERROR] This item may not have children." );
		return this.actual();
	}
	
	public T removeAll() {
		for( GUIComponent<?> g : this.children ) {
			if( GUIManager.find( g.id ) )
				GUIManager.remove( g.id );
			g = g.destroy();
		}
		return this.update();
	}
	
	@Override public void keyTyped( KeyEvent e ) {}
	@Override public void keyPressed( KeyEvent e ) {}
	@Override public void keyReleased( KeyEvent e ) {}
	@Override public void actionPerformed( ActionEvent e ) {}
	@Override public void stateChanged( ChangeEvent e ) {}
	public void focused() {}
	
	public GUIComponent<T> destroy() {
		this.children.clear();
		
		this.target.removeAll();
		this.target.setVisible( false );
		this.target = null;
		
		return null;
	}
}
