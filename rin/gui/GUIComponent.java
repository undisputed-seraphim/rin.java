package rin.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import static rin.gui.GUIManager.*;

public class GUIComponent<T, T2 extends GUIEvent<T>> implements ActionListener, KeyListener, MouseListener, ChangeListener, FocusListener {
	protected String id = null;
	protected JComponent target = null;
	
	protected int childCount = 0;	
	protected boolean canHaveChildren = true;
	protected ArrayList<GUIComponent<?, ?>> children = new ArrayList<GUIComponent<?, ?>>();
	
	@SuppressWarnings("unchecked") public T actual() { return (T)this; }
	public String getId() { return this.id; }
	
	protected GroupLayout.Alignment getGroupAlignment( Alignment alignment ) {
		switch( alignment ) {
		
		case LEFT: return GroupLayout.Alignment.LEADING;
		case CENTER: return GroupLayout.Alignment.CENTER;
		case RIGHT: return GroupLayout.Alignment.TRAILING;
		
		}
		return GroupLayout.Alignment.LEADING;
	}
	
	protected int getFlowAlignment( Alignment alignment ) {
		switch( alignment ) {
		
		case LEFT: return FlowLayout.LEFT;
		case CENTER: return FlowLayout.CENTER;
		case RIGHT: return FlowLayout.RIGHT;

		}
		return FlowLayout.LEFT;
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
	public T addTo( GUIComponent<?, ?> component ) { component.add( this ); return this.actual(); }
	public T add( String id ) { return this.add( GUIManager.get( id ) ); }
	public T add( GUIComponent<?, ?> component ) {
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
			for( GUIComponent<?, ?> g : this.children )
				g = g.destroy();

		this.children.clear();
		return this.update();
	}
	
	protected T2 runOnKeyTyped = null;
	public T onKeyTyped( T2 e ) { this.runOnKeyTyped = e.<T2>setTarget( this.actual() ); return this.actual(); }
	@Override public void keyTyped( KeyEvent e ) { if( this.runOnKeyTyped != null ) this.runOnKeyTyped.execute( e ); }
	
	protected T2 runOnKeyPressed = null;
	public T onKeyPressed( T2 e ) { this.runOnKeyPressed = e.<T2>setTarget( this.actual() ); return this.actual(); }
	@Override public void keyPressed( KeyEvent e ) { if( this.runOnKeyPressed != null ) this.runOnKeyPressed.execute( e ); }
	
	protected T2 runOnKeyReleased = null;
	public T onKeyReleased( T2 e ) { this.runOnKeyReleased = e.<T2>setTarget( this.actual() ); return this.actual(); }
	@Override public void keyReleased( KeyEvent e ) { if( this.runOnKeyReleased != null ) this.runOnKeyReleased.execute( e ); }
	
	protected T2 runOnAction = null;
	public T onAction( T2 e ) { this.runOnAction = e.<T2>setTarget( this.actual() ); return this.actual(); }
	@Override public void actionPerformed( ActionEvent e ) { if( this.runOnAction != null ) this.runOnAction.execute( e ); }
	
	protected T2 runOnFocusGained = null;
	public T onFocusGained( T2 e ) { this.runOnFocusGained = e.<T2>setTarget( this.actual() ); return this.actual(); }
	@Override public void focusGained( FocusEvent e ) { if( this.runOnFocusGained != null ) this.runOnFocusGained.execute( e ); }
	
	protected T2 runOnFocusLost = null;
	public T onFocusLost( T2 e ) { this.runOnFocusLost = e.<T2>setTarget( this.actual() ); return this.actual(); }
	@Override public void focusLost( FocusEvent e ) { if( this.runOnFocusLost != null ) this.runOnFocusLost.execute( e ); }
	
	protected T2 runOnStateChanged = null;
	public T onStateChanged( T2 e ) { this.runOnStateChanged = e.<T2>setTarget( this.actual() ); return this.actual(); }
	@Override public void stateChanged( ChangeEvent e ) { if( this.runOnStateChanged != null ) this.runOnStateChanged.execute( null ); }
	
	protected T2 runOnMouseClicked = null;
	public T onClick( T2 e ) { this.runOnMouseClicked = e.<T2>setTarget( this.actual() ); return this.actual(); }
	@Override public void mouseClicked( MouseEvent e ) { if( this.runOnMouseClicked != null ) this.runOnMouseClicked.execute( e ); }
	
	@Override public void mouseEntered( MouseEvent e ) {}
	@Override public void mouseExited( MouseEvent e ) {}
	
	protected T2 runOnMousePressed = null;
	public T onMousePressed( T2 e ) { this.runOnMousePressed = e.<T2>setTarget( this.actual() ); return this.actual(); }
	@Override public void mousePressed( MouseEvent e ) { if( this.runOnMousePressed != null ) this.runOnMousePressed.execute( e ); }
	
	@Override public void mouseReleased( MouseEvent e ) {}
	
	public GUIComponent<T, T2> destroy() {
		if( this.target != null ) {
			this.removeAll();
			this.target.setVisible( false );
			this.target.getParent().remove( this.target );
			this.target = null;
		} else {
			this.children.clear();
		}

		return null;
	}
}
