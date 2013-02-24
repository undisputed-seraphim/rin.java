package rin.gui;

import javax.swing.JButton;

import rin.gui.GUIFactory.ButtonEvent;

public class Button extends GUIComponent<Button, ButtonEvent> {
	private static int items = 0;
	
	public Button() { this( "Button-" + Button.items ); }
	public Button( String id ) {
		this.id = id;
		this.canHaveChildren = false;
		this.target = new JButton();
		
		this.real().setFont( GUIFactory.DEFAULT_FONT );
		this.setText( this.id );
		
		this.onWindowLoad( new GUIFactory.OnLoadEvent() {
			public void run() {
				((JButton)this.target).addActionListener( this.component.toButton() );
			}
		}.setTargets( this.target, this ) );
	}
	
	public JButton real() { return (JButton)this.target; }
	
	public Button setText( String text ) { this.real().setText( text ); return this.update(); }
	public Button click() { this.real().doClick(); return this; }
	
	@Override public Button onClick( ButtonEvent e ) {
		this.runOnAction = e.<ButtonEvent>setTarget( this );
		return this;
	}
	
	@Override protected Button destroy() {
		this.real().removeActionListener( this );
		super.destroy();
		
		return null;
	}
}
