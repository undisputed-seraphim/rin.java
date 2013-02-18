package rin.gui;

import javax.swing.JTextField;

public class TextField extends GUIComponent<TextField> {
	private static int items = 0;
	
	public TextField() { this( "TextField-" + TextField.items++ ); }
	public TextField( String id ) {
		this.id = id;
		this.target = new JTextField();
		this.canHaveChildren = false;
		this.real().setFont( GUIManager.DEFAULT_FONT );
		this.setWidth( 15 );
	}
	
	private JTextField real() { return (JTextField)this.target; }
	public String value() { return this.real().getText(); }
	
	public TextField setText( String text ) { this.real().setText( text ); return this.update(); }
	public TextField setWidth( int cols ) { this.real().setColumns( cols ); return this.update(); }
}
