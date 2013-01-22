package rin.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Manages all interaction between a gui window and the user
 */
public class GUI {
	
	/**
	 * Window object used to contain everything within a gui window
	 */
	public static class Window {
		protected JFrame window;
		protected JPanel target;
		
		public Window() {
			this.window = new JFrame();
			this.window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
			this.target = new JPanel( new GridLayout( 2, 1, 0, 0 ) );
			this.window.setContentPane( this.target );
		}
		
		public Window setTitle( String title ) { this.window.setTitle( title ); return this; }
		public Window setSize( int w, int h ) { this.window.setSize( w, h ); return this; }
		public Window setLocation( int x, int y ) { this.window.setLocation( x, y ); return this; }
		public Window setBackgroundColor( int r, int g, int b ) { this.target.setBackground( new Color( r, g, b ) ); return this; }
		
		public Window show() { this.window.setVisible( true ); return this; }
		public Window hide() { this.window.setVisible( false ); return this; }
		
		public Window add( GUIComponent component ) {
			this.window.getContentPane().add( component.target );
			component.show();
			this.window.getContentPane().repaint();
			return this;
		}
	}
	
	/**
	 * Content object that mimics an html div and adding to it adds single line entries
	 */
	public static class TextList extends GUIComponent {
		protected Vector<String> contents = new Vector<String>();
		protected Vector<String> ids = new Vector<String>();
		protected JList<String> list;
		
		public TextList() {
			this.list = new JList<String>();
			this.target = new JScrollPane( this.list );
		}
		
		public GUIComponent setBackgroundColor( int r, int g, int b ) { this.list.setBackground( new Color( r, g, b ) ); return this; }
		private void applyContents() { this.list.setListData( this.contents ); 	this.list.repaint(); }
		public GUIComponent addListItem( String id, String entry ) {
			this.ids.add( id );
			this.contents.add( entry );
			this.applyContents();
			return this;
		}
		public GUIComponent removeListItem( String id ) {
			for( String s : this.ids )
				if( s.equals( id ) ) {
					this.contents.remove( this.ids.indexOf( s ) );
					this.ids.remove( s );
					this.applyContents();
					return this;
				}
			return this;
		}
	}
	
	public static class Panel extends GUIComponent {
		public Panel() {
			this.target = new JPanel();
		}
	}
	
	public static class GUIComponent {
		protected JComponent target;
		
		public GUIComponent setSize( int w, int h ) { this.target.setSize( w, h ); return this; }
		public GUIComponent setLocation( int x, int y ) { this.target.setLocation( x, y ); return this; }
		public GUIComponent setBackgroundColor( int r, int g, int b ) { this.target.setBackground( new Color( r, g, b ) ); return this; }
		
		public GUIComponent show() { this.target.setVisible( true ); return this; }
		public GUIComponent hide() { this.target.setVisible( false ); return this; }
		
		public GUIComponent addTo( Window window ) { window.add( this ); return this; }
		public GUIComponent addTo( GUIComponent component ) { component.add( this ); return this; }
		public GUIComponent add( GUIComponent component ) {
			this.target.add( component.target );
			component.show();
			return this;
		}
		
		/* convenience methods for obtaining proper type without manually casting */
		public Panel toPanel() { return ( Panel ) this; }
		public TextList toTextList() { return ( TextList ) this; }
	}
}
