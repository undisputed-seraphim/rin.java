package rin.gui;

import java.awt.event.ComponentEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import rin.gui.GUIFactory.WindowEvent;

public class Window extends GUIComponent<Window, WindowEvent> implements WindowFocusListener, WindowListener {
	private static int items = 0;
	
	private JFrame window;
	private boolean sized = false;
	protected Stack<Runnable> onLoads = new Stack<Runnable>();
	
	public Window() { this( "Window-" + Window.items++ ); }
	public Window( String id ) {
		this.id = id;
		this.window = new JFrame();
		this.target = new JPanel( new GUIFactory.BorderLayout() );
		this.target.setVisible( true );
		
		this.window.setContentPane( this.target );
		this.window.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		this.window.addComponentListener( this );
		this.window.addWindowListener( this );
		
		this.onWindowLoad( new GUIFactory.OnLoadEvent() {
			@Override public void run() {
				((JFrame)this.target).addWindowFocusListener( component.toWindow() );
			}
		}.setTargets( this.window, this ) );
	}
	
	public Window addMenu( MenuBar menu ) {
		menu.parent = this;
		this.window.setJMenuBar( (JMenuBar)menu.target );
		return this.update();
	}
	
	public Window setTitle( String title ) { this.window.setTitle( title ); return this.update(); }
	@Override public Window setSize( int w, int h ) { this.sized = true; this.window.setSize( w, h ); return this.update(); }
	public Window setLocation( int x, int y ) { this.window.setLocation( x, y ); return this.update(); }
	public Window close() {
		this.window.dispatchEvent( new java.awt.event.WindowEvent( this.window, java.awt.event.WindowEvent.WINDOW_CLOSING ) );
		return this;
	}
	
	@Override public Window show() {
		this.window.setVisible( true );
		if( !this.sized )
			this.window.pack();
		
		if( this.runOnLoad != null )
			this.runOnLoad.run();
		
		return this.update();
	}
	
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
			this.runOnWindowFocusGained.run();
	}
	
	@Override public void windowLostFocus( java.awt.event.WindowEvent e ) {
		if( this.runOnWindowFocusLost != null )
			this.runOnWindowFocusLost.run();
	}
	
	@Override public void componentShown( ComponentEvent e ) {
		while( !this.onLoads.empty() )
			this.onLoads.pop().run();
	}
	
	@Override public void windowActivated( java.awt.event.WindowEvent e ) {}
	@Override public void windowClosed( java.awt.event.WindowEvent e ) {}
	@Override public void windowClosing( java.awt.event.WindowEvent e ) {}
	@Override public void windowDeactivated( java.awt.event.WindowEvent e ) {}
	@Override public void windowDeiconified(java.awt.event.WindowEvent arg0) {}
	@Override public void windowIconified( java.awt.event.WindowEvent e ) {}
	@Override public void windowOpened( java.awt.event.WindowEvent e ) {}
	
	@Override protected Window destroy() {
		this.window.removeWindowFocusListener( this );
		this.window.removeComponentListener( this );
		super.destroy();
		
		this.window.removeAll();
		this.window.setVisible( false );
		this.close();
		this.window.removeWindowListener( this );
		this.window = null;
		
		return null;
	}
}
