package rin.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import static rin.gui.GUIFactory.*;

public class GUIComponent<T, T2 extends GUIEvent<T>> implements ActionListener, KeyListener,
		MouseListener, ChangeListener, FocusListener, ComponentListener {
	protected String id = null;
	protected JComponent target = null;
	protected GUIComponent<?, ?> parent = null;
	
	protected int childCount = 0;
	protected boolean canHaveChildren = true;
	protected ArrayList<GUIComponent<?, ?>> children = new ArrayList<GUIComponent<?, ?>>();
	
	@SuppressWarnings("unchecked") public T actual() { return (T)this; }
	public Window toWindow() { return (Window)this; }
	public Container toContainer() { return (Container)this; }
	public Panel toPanel() { return (Panel)this; }
	
	public ScrollPane toScrollPane() { return (ScrollPane)this; }
	public TabbedPane toTabbedPane() { return (TabbedPane)this; }
	
	public Button toButton() { return (Button)this; }
	public TextField toTextField() { return (TextField)this; }
	public CheckBox toCheckBox() { return (CheckBox)this; }
	public ComboBox toComboBox() { return (ComboBox)this; }
	
	public Menu toMenu() { return (Menu)this; }
	public MenuItem toMenuItem() { return (MenuItem)this; }
	
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
	
	protected int getModifier( ModifierKey key ) {
		switch( key ) {
		
		case ALT: return ActionEvent.ALT_MASK;
		case SHIFT: return ActionEvent.SHIFT_MASK;
		case CTRL: return ActionEvent.CTRL_MASK;
		
		}
		return ActionEvent.META_MASK;
	}
	
	protected int getKeyCode( String key ) {
		int keycode = -1;
		if( !key.equals( "\0" ) ) {
			try {
				try {
					keycode = KeyEvent.class.getField( "VK_" + key ).getInt( null );
				} catch( IllegalArgumentException e ) {
					System.out.println( "unacceptable mnemonic requested." );
				} catch( IllegalAccessException e ) {
					System.out.println( "unacceptable mnemonic requested." );
				}
			} catch( SecurityException e ) {
				System.out.println( "unacceptable mnemonic requested." );
			} catch( NoSuchFieldException e ) {
				System.out.println( "unacceptable mnemonic requested." );
			}
		}
		return keycode;
	}
	
	public T setBackgroundColor( int r, int g, int b ) { return this.setBackgroundColor( r, g, b, 255 ); }
	public T setBackgroundColor( int r, int g, int b, int a ) { this.target.setBackground( new Color( r, g, b, a ) ); return this.update(); }
	public T setSize( int width, int height ) { this.target.setPreferredSize( new GUIFactory.Dimension( width, height ) ); return this.update(); }
	//public T setLayout( GUIManager.GUILayout layout ) { this.target.setLayout( layout ); return this.update(); }
	public T setToolTipText( String tip ) { this.target.setToolTipText( tip ); return this.update(); }
	
	public T update() { this.target.validate(); this.target.repaint(); return this.actual(); }
	public T enable() { this.target.setEnabled( true ); return this.update(); }
	public T disable() { this.target.setEnabled( false ); return this.update(); }
	public T show() { this.target.setVisible( true ); if( this.runOnLoad != null ) this.runOnLoad.run(); return this.actual(); }
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

			component.parent = this;
			this.children.add( component );
			this.target.add( this.children.get( this.childCount++ ).target );
			component.show();
			return this.update();
		}
		
		System.out.println( "[ERROR] This item may not have children." );
		return this.actual();
	}
	
	public T remove( String id ) { return this.remove( GUIManager.get( id ) ); }
	public T remove( GUIComponent<?, ?> component ) {
		if( this.children.remove( component ) ) {
			if( GUIManager.find( component.id ) )
				GUIManager.unwatch( component.id );
			component = component.destroy();
		}
			
		return this.update();
	}

	public T removeAll() {
		Stack<GUIComponent<?, ?>> children = new Stack<GUIComponent<?, ?>>();
		for( GUIComponent<?, ?> g : this.children )
			children.push( g );
		
		GUIComponent<?, ?> current;
		while( !children.empty() ) {
			current = children.pop();
			this.remove( current );
		}
		
		this.children.clear();
		return this.update();
	}
	
	protected T2 runOnKeyTyped = null;
	public T onKeyTyped( T2 e ) { this.runOnKeyTyped = e.<T2>setTarget( this.actual() ); return this.actual(); }
	@Override public void keyTyped( KeyEvent e ) { if( this.runOnKeyTyped != null ) this.runOnKeyTyped.run(); }
	
	protected T2 runOnKeyPressed = null;
	public T onKeyPressed( T2 e ) { this.runOnKeyPressed = e.<T2>setTarget( this.actual() ); return this.actual(); }
	@Override public void keyPressed( KeyEvent e ) { if( this.runOnKeyPressed != null ) this.runOnKeyPressed.run(); }
	
	protected T2 runOnKeyReleased = null;
	public T onKeyReleased( T2 e ) { this.runOnKeyReleased = e.<T2>setTarget( this.actual() ); return this.actual(); }
	@Override public void keyReleased( KeyEvent e ) { if( this.runOnKeyReleased != null ) this.runOnKeyReleased.run(); }
	
	protected T2 runOnAction = null;
	public T onAction( T2 e ) { this.runOnAction = e.<T2>setTarget( this.actual() ); return this.actual(); }
	@Override public void actionPerformed( ActionEvent e ) { if( this.runOnAction != null ) this.runOnAction.run(); }
	
	protected T2 runOnFocusGained = null;
	public T onFocusGained( T2 e ) { this.runOnFocusGained = e.<T2>setTarget( this.actual() ); return this.actual(); }
	@Override public void focusGained( FocusEvent e ) { if( this.runOnFocusGained != null ) this.runOnFocusGained.run(); }
	
	protected T2 runOnFocusLost = null;
	public T onFocusLost( T2 e ) { this.runOnFocusLost = e.<T2>setTarget( this.actual() ); return this.actual(); }
	@Override public void focusLost( FocusEvent e ) { if( this.runOnFocusLost != null ) this.runOnFocusLost.run(); }
	
	protected T2 runOnStateChanged = null;
	public T onStateChanged( T2 e ) { this.runOnStateChanged = e.<T2>setTarget( this.actual() ); return this.actual(); }
	@Override public void stateChanged( ChangeEvent e ) { if( this.runOnStateChanged != null ) this.runOnStateChanged.run(); }
	
	protected T2 runOnMouseClicked = null;
	public T onClick( T2 e ) { this.runOnMouseClicked = e.<T2>setTarget( this.actual() ); return this.actual(); }
	@Override public void mouseClicked( MouseEvent e ) { if( this.runOnMouseClicked != null ) this.runOnMouseClicked.run(); }
	
	@Override public void mouseEntered( MouseEvent e ) {}
	@Override public void mouseExited( MouseEvent e ) {}
	
	protected T2 runOnMousePressed = null;
	public T onMousePressed( T2 e ) { this.runOnMousePressed = e.<T2>setTarget( this.actual() ); return this.actual(); }
	@Override public void mousePressed( MouseEvent e ) { if( this.runOnMousePressed != null ) this.runOnMousePressed.run(); }
	
	@Override public void mouseReleased( MouseEvent e ) {}
	
	@Override public void componentHidden( ComponentEvent e ) { System.out.println( "hidden" ); }
	@Override public void componentMoved( ComponentEvent e ) {}
	@Override public void componentResized( ComponentEvent e ) {}
	@Override public void componentShown( ComponentEvent e ) { System.out.println( "Shown" ); }
	
	protected static class DoLater<T1, T2> implements Runnable {
		protected T1 t1;
		protected T2 t2;
		public DoLater( T1 t1, T2 t2 ) { this.t1 = t1; this.t2 = t2; SwingUtilities.invokeLater( this ); }
		@Override public void run() {}
	}
	
	protected OnLoadEvent runOnLoad = null;
	public T onWindowLoad( final OnLoadEvent e ) {
		if( e.target == null )
			e.setTargets( this.target, this );
		
		new DoLater<GUIComponent<T,?>, OnLoadEvent>( this, null ) {
			@Override public void run() {
				if( this.t1 instanceof Window ) {
					((Window) this.t1).onLoads.push( e );
					return;
				}
				
				GUIComponent<?, ?> tmp = this.t1.parent;
				while( tmp != null ) {
					if( tmp instanceof Window )
						((Window) tmp).onLoads.push( e );
					tmp = tmp.parent;
				}
			}
		};
		return this.actual();
	}
	
	protected GUIComponent<T, T2> destroy() {
		this.target.removeComponentListener( this );
		this.removeAll();
		
		if( GUIManager.find( this.id ) )
			GUIManager.unwatch( this.id );
		
		if( this.parent != null )
			if( this.parent.children.remove( this ) )
				this.parent.update();
		
		this.target.setVisible( false );
		if( this.target.getParent() != null )
			this.target.getParent().remove( this.target );
		
		this.parent = null;
		this.target = null;

		return null;
	}
}
