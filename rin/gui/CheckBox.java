package rin.gui;

import java.awt.event.ActionEvent;

import javax.swing.JCheckBox;

import rin.gui.GUIManager.GUIEvent;
import rin.gui.GUIManager.CheckBoxEvent;

public class CheckBox extends GUIComponent<CheckBox, CheckBoxEvent> {
	private static int items = 0;
	
	public CheckBox() { this( "CheckBox-" + CheckBox.items++ ); }
	public CheckBox( String id ) {
		this.id = id;
		this.canHaveChildren = false;
		this.target = new JCheckBox();
		this.real().addActionListener( this );
		this.real().setFont( GUIManager.DEFAULT_FONT );
	}
	
	private JCheckBox real() { return (JCheckBox)this.target; }
	
	public CheckBox setLabel( String str ) { this.real().setText( str ); return this; }
	public CheckBox setLabelPositionH( GUIManager.Position position ) { this.real().setHorizontalTextPosition( position.value ); return this; }
	
	private GUIEvent<CheckBox> runOnCheck = null;
	private GUIEvent<CheckBox> runOnUnCheck = null;
	
	public CheckBox onCheck( GUIEvent<CheckBox> e ) {
		this.runOnCheck = e.<CheckBoxEvent>setTarget( this );
		return this;
	}
	
	public CheckBox onUnCheck( GUIEvent<CheckBox> e ) {
		this.runOnUnCheck = e.<CheckBoxEvent>setTarget( this );
		return this;
	}
	
	@Override public void actionPerformed( ActionEvent e ) {
		if( this.real().isSelected() ) {
			if( this.runOnCheck != null )
				this.runOnCheck.run();
		}
		
		else if( this.runOnUnCheck != null )
			this.runOnUnCheck.run();
	}
	
	@Override public CheckBox destroy() {
		if( this.target != null )
			this.real().removeActionListener( this );
		super.destroy();
		
		this.runOnCheck = null;
		this.runOnUnCheck = null;
		
		return null;
	}
}
