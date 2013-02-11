package rin.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;

import javax.swing.JFrame;
import javax.swing.JPanel;

import rin.gui.GUIComponent;

public class Window extends GUIComponent {
	protected JFrame window;
	
	public Window() { this( new BorderLayout() ); }
	public Window( Object layout ) {
		this.window = new JFrame();
		this.window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		this.target = new JPanel( (LayoutManager)layout );
		this.window.setContentPane( this.target );
		this.window.setVisible( true );
		this.target.setVisible( true );
	}
	
	public Window setTitle( String title ) { this.window.setTitle( title ); return this; }
	public Window setSize( int w, int h ) { this.window.setSize( w, h ); return this; }
	public Window setLocation( int x, int y ) { this.window.setLocation( x, y ); return this; }
	
	
	/*public Window add( GUIComponent component ) {
		this.children.add( component );
		this.window.getContentPane().add( this.children.get( this.childCount++ ).target );
		component.show();
		this.window.getContentPane().repaint();
		this.target.validate();
		return this;
	}*/
	
	public Window destroy() {
		super.destroy();
		
		this.window.setVisible( false );
		this.window.dispose();
		this.window = null;
		
		return null;
	}
}
