package rin.gui;

import java.awt.event.KeyEvent;

import javax.swing.JTextField;

import rin.gui.GUIManager.TextFieldEvent;

public class TextField extends GUIComponent<TextField, TextFieldEvent> {
	private static int items = 0;
	
	public TextField() { this( "TextField-" + TextField.items++ ); }
	public TextField( String id ) {
		this.id = id;
		this.target = new JTextField();
		this.canHaveChildren = false;
		this.real().setFont( GUIManager.DEFAULT_FONT );
		this.real().addKeyListener( this );
		this.setWidth( 15 );
	}
	
	private JTextField real() { return (JTextField)this.target; }
	
	public String value() { return this.real().getText(); }
	public String getText() { return this.value(); }
	public TextField setText( String text ) { this.real().setText( text ); return this.update(); }
	public TextField setWidth( int cols ) { this.real().setColumns( cols ); return this.update(); }
	
	private TextFieldEvent runOnEnter = null;
	public TextField onEnter( TextFieldEvent e ) {
		this.runOnEnter = e.<TextFieldEvent>setTarget( this );
		return this;
	}
	
	@Override public void keyReleased( KeyEvent e ) {
		if( e.getKeyCode() == KeyEvent.VK_ENTER )
			if( this.runOnEnter != null ) {
				this.runOnEnter.value = this.value();
				this.runOnEnter.execute( e );
			}
	}
	
	@Override public TextField destroy() {
		if( this.target != null )
			this.real().removeKeyListener( this );
		super.destroy();
		
		this.target = null;
		
		return null;
	}
}
