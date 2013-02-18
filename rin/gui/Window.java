package rin.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;

import rin.gui.GUIComponent;

public class Window extends GUIComponent<Window> {
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
		this.target.setVisible( true );
	}
	
	public Window setTitle( String title ) { this.window.setTitle( title ); return this; }
	public Window setSize( int w, int h ) { this.sized = true; this.window.setSize( w, h ); return this; }
	public Window setLocation( int x, int y ) { this.window.setLocation( x, y ); return this; }
	
	@Override public Window show() { this.window.setVisible( true ); if( !this.sized ) this.window.pack(); return this; }
	@Override public Window hide() { this.window.setVisible( false ); return this; }
	
	@Override public Window destroy() {
		super.destroy();
		
		this.window.setVisible( false );
		this.window.dispose();
		this.window = null;
		
		return null;
	}
}
