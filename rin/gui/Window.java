package rin.gui;

import java.awt.event.WindowFocusListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import rin.gui.GUIManager.WindowEvent;

public class Window extends GUIComponent<Window, WindowEvent> implements WindowFocusListener {
	private static int items = 0;
	
	private JFrame window;
	private boolean sized = false;
	
	public Window() { this( "Window-" + Window.items++ ); }
	public Window( String id ) { this( id, new GUIManager.GUIBorderLayout() ); }
	public Window( String id, GUIManager.GUILayout layout ) {
		this.id = id;
		this.window = new JFrame();
		this.window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		this.target = new JPanel( layout );
		this.window.setContentPane( this.target );
		this.window.addWindowFocusListener( this );
		this.target.setVisible( true );
	}
	
	public Window setTitle( String title ) { this.window.setTitle( title ); return this; }
	public Window setSize( int w, int h ) { this.sized = true; this.window.setSize( w, h ); return this; }
	public Window setLocation( int x, int y ) { this.window.setLocation( x, y ); return this; }
	
	@Override public Window show() { this.window.setVisible( true ); if( !this.sized ) this.window.pack(); return this; }
	@Override public Window hide() { this.window.setVisible( false ); return this; }
	
	private WindowEvent runOnWindowFocusGained = null;
	private WindowEvent runOnWindowFocusLost = null;
	
	public Window onWindowFocusGained( WindowEvent e ) {
		this.runOnWindowFocusGained = e.<WindowEvent>setTarget( this );
		return this;
	}
	
	public Window onWindowFocusLost( WindowEvent e ) {
		this.runOnWindowFocusLost = e.<WindowEvent>setTarget( this );
		return this;
	}
	
	@Override public void windowGainedFocus( java.awt.event.WindowEvent e ) {
		if( this.runOnWindowFocusGained != null )
			this.runOnWindowFocusGained.execute( e );
	}
	
	@Override public void windowLostFocus( java.awt.event.WindowEvent e ) {
		if( this.runOnWindowFocusLost != null )
			this.runOnWindowFocusLost.execute( e );
	}
	
	@Override public Window destroy() {
		super.destroy();
		
		this.window.removeAll();
		this.window.setVisible( false );
		this.window.dispose();
		this.window = null;
		
		return null;
	}
}
