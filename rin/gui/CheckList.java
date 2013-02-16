package rin.gui;

import java.awt.Dimension;

public class CheckList extends GUIComponent {
	public CheckList() {}
	public CheckList( int rows, int cols ) {
		
	}
	
	public CheckList setSize( int width, int height ) {
		this.target.setPreferredSize( new Dimension( width, height ) );
		return this;
	}
}
