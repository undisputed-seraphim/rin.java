package rin.gui;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import rin.gui.GUIFactory.CanvasEvent;

public class Canvas extends GUIComponent<Canvas, CanvasEvent> {
	private Component canvas;
	private Window window;
	
	public static HashMap<Integer, Boolean> keymap = new HashMap<Integer, Boolean>();
	
	public Canvas( String id ) {
		this.id = id;
		this.canHaveChildren = false;
		
		this.window = new Window();
		this.parent = this.window;
		
		this.canvas = new java.awt.Canvas();
		this.window.target.add( this.canvas );
		this.real().setVisible( true );
		this.real().setFocusable( true );
	}
	
	private java.awt.Canvas real() { return (java.awt.Canvas)this.canvas; }
	public javax.swing.JFrame getWindow() { return this.window.window; }
	public java.awt.Canvas getCanvas() { return this.real(); }
	
	@Override public Canvas setSize( int width, int height ) { this.real().setPreferredSize( new GUIFactory.Dimension( width, height ) ); return this; }
	@Override public Canvas show() { this.window.show(); if( this.runOnLoad != null ) this.runOnLoad.run(); return this; }
	
	@Override public void keyPressed( KeyEvent e ) {
		System.out.println( "down" );
		if( Canvas.keymap.containsKey( e.getKeyCode() ) ) {
			if( Canvas.keymap.get(e.getKeyCode() ) )
				Canvas.keymap.put( e.getKeyCode(), true );
		}
	}
	
	@Override public void keyReleased( KeyEvent e ) {
		System.out.println( "up" );
	}
	
}
