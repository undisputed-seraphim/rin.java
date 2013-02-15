package rin.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;

import rin.gui.GUIComponent;

public class Window extends GUIComponent {
	protected JFrame window;
	
	public Window() { this( new GUIManager.GUIBorderLayout() ); }
	public Window( GUIManager.GUILayout layout ) {
		this.window = new JFrame();
		this.window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		this.target = new JPanel( layout );
		this.window.setContentPane( this.target );
		this.window.setVisible( true );
		this.target.setVisible( true );
	}
	
	public Window setTitle( String title ) { this.window.setTitle( title ); return this; }
	public Window setSize( int w, int h ) { this.window.setSize( w, h ); return this; }
	public Window setLocation( int x, int y ) { this.window.setLocation( x, y ); return this; }
	
	public Window destroy() {
		super.destroy();
		
		this.window.setVisible( false );
		this.window.dispose();
		this.window = null;
		
		return null;
	}
}
