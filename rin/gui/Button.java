package rin.gui;

import javax.swing.JButton;

import rin.gui.GUIManager.ButtonEvent;
import rin.gui.GUIManager.GUIEvent;

public class Button extends GUIComponent<Button, ButtonEvent> {
	private static int items = 0;
	
	public Button() { this( "Button-" + Button.items ); }
	public Button( String id ) {
		this.id = id;
		this.canHaveChildren = false;
		this.target = new JButton();
		this.real().addActionListener( this );
		this.setText( this.id );
	}
	
	public JButton real() { return (JButton)this.target; }
	
	public Button setText( String text ) { this.real().setText( text ); return this.update(); }
	
	public Button onClick( GUIEvent<Button> e ) {
		this.runOnAction = e.<ButtonEvent>setTarget( this );
		return this;
	}
}
