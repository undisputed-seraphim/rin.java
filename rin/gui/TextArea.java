package rin.gui;

import javax.swing.JTextArea;

import rin.gui.GUIFactory.TextAreaEvent;

public class TextArea extends GUIComponent<TextArea, TextAreaEvent> {
	private static int items = 0;
	
	private static final String TAB = "   ";
	
	public TextArea() {}
	public TextArea( String id ) {
		this.id = id;
		this.canHaveChildren = false;
		
		this.target = new JTextArea();
		this.real().setFont( GUIFactory.DEFAULT_FONT );
		
		this.real().setLineWrap( true );
		this.real().setEditable( false );
		this.real().setWrapStyleWord( true );
		
		this.real().setColumns( 10 );
		this.real().setRows( 5 );
	}
	
	private JTextArea real() { return (JTextArea)this.target; }

	public TextArea setText( String text ) { this.real().setText( text ); return this.update(); }
	public TextArea addLine( String line ) { return this.setText( this.real().getText() + TextArea.TAB + line + "\n" ); }
}
