package rin.gui;

import javax.swing.JTextField;

public class TextField extends GUIComponent<TextField> {
	private static int items = 0;
	
	public TextField() { this( "TextField-" + TextField.items++ ); }
	public TextField( int cols ) { this( "TextField-" + TextField.items++, cols ); }
	public TextField( String id ) { this( id, 15 ); }
	public TextField( String id, int cols ) {
		this.id = id;
		this.target = new JTextField();
		this.real().setFont( GUIManager.DEFAULT_FONT );
		this.setColumns( cols );
	}
	
	private JTextField real() { return (JTextField)this.target; }
	
	public TextField setText( String text ) { this.real().setText( text ); return this.update(); }
	public TextField setColumns( int cols ) { this.real().setColumns( cols ); return this.update(); }
}
