package rin.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import rin.gui.GUIManager.Alignment;

public class GUIComponent<T> implements ActionListener, KeyListener, ChangeListener, FocusListener {
	protected static final JLabel emptyLabel = new JLabel();
	
	protected String id = null;
	protected JComponent target = null;
	
	protected int childCount = 0;	
	protected boolean canHaveChildren = true;
	protected ArrayList<GUIComponent<?>> children = new ArrayList<GUIComponent<?>>();
	
	@SuppressWarnings("unchecked") public T actual() { return (T)this; }
	protected void init() {
		
	}
	
	protected GroupLayout.Alignment getGroupAlignment( Alignment alignment ) {
		switch( alignment ) {
		
		case LEFT: return GroupLayout.Alignment.LEADING;
		case CENTER: return GroupLayout.Alignment.CENTER;
		case RIGHT: return GroupLayout.Alignment.TRAILING;
		
		}
		return GroupLayout.Alignment.LEADING;
	}
	
	public T setBackgroundColor( int r, int g, int b ) { return this.setBackgroundColor( r, g, b, 255 ); }
	public T setBackgroundColor( int r, int g, int b, int a ) { this.target.setBackground( new Color( r, g, b, a ) ); return this.update(); }
	//public T setLayout( GUIManager.GUILayout layout ) { this.target.setLayout( layout ); return this.update(); }
	public T setToolTipText( String tip ) { this.target.setToolTipText( tip ); return this.update(); }
	
	public T update() { this.target.validate(); this.target.repaint(); return this.actual(); }
	public T enable() { this.target.setEnabled( true ); return this.update(); }
	public T disable() { this.target.setEnabled( false ); return this.update(); }
	public T show() { this.target.setVisible( true ); return this.actual(); }
	public T hide() { this.target.setVisible( false ); return this.actual(); }
	public T focus() { this.target.requestFocusInWindow(); return this.actual(); }
	
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
	
	/*public T remove( GUIComponent<?> component ) {
		if( this.canHaveChildren )
			if( this.children.remove( component ) )
				component = component.destroy();
			
		return this.update();
	}*/
	
	//TODO: this does not remove the IDs from guimanager!!!
	public T removeAll() {
		if( this.canHaveChildren )
			for( GUIComponent<?> g : this.children )
				g = g.destroy();

		this.children.clear();
		return this.update();
	}
	
	@Override public void keyTyped( KeyEvent e ) {}
	@Override public void keyPressed( KeyEvent e ) {}
	@Override public void keyReleased( KeyEvent e ) {}
	@Override public void actionPerformed( ActionEvent e ) {}
	@Override public void stateChanged( ChangeEvent e ) {}
	@Override public void focusGained( FocusEvent e ) {}
	@Override public void focusLost( FocusEvent e ) {}
	
	public GUIComponent<T> destroy() {
		this.target.setVisible( false );
		this.removeAll();

		this.target.getParent().remove( this.target );
		this.target = null;

		return null;
	}
}
