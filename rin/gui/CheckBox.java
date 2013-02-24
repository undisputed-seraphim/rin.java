package rin.gui;

import java.awt.event.ActionEvent;

import javax.swing.JCheckBox;

import rin.gui.GUIFactory.CheckBoxEvent;

public class CheckBox extends GUIComponent<CheckBox, CheckBoxEvent> {
	private static int items = 0;
	
	public CheckBox() { this( "CheckBox-" + CheckBox.items++ ); }
	public CheckBox( String id ) {
		this.id = id;
		this.canHaveChildren = false;
		this.target = new JCheckBox();
		this.real().setFont( GUIFactory.DEFAULT_FONT );
		
		this.onWindowLoad( new GUIFactory.OnLoadEvent() {
			public void run() {
				((JCheckBox)this.target).addActionListener( this.component.toCheckBox() );
			}
		}.setTargets( this.target, this ) );
	}
	
	private JCheckBox real() { return (JCheckBox)this.target; }
	public CheckBox check() {
		this.onWindowLoad( new GUIFactory.OnLoadEvent() {
			@Override public void run() {
				((JCheckBox)this.target).doClick();
			}
		}.setTargets( this.target, this ) );
		return this.update();
	}
	
	public CheckBox setLabel( String str ) { this.real().setText( str ); return this.update(); }
	public CheckBox setLabelPositionX( GUIFactory.Position position ) {
		this.real().setHorizontalTextPosition( position.value );
		return this.update();
	}
	
	private CheckBoxEvent runOnCheck = null;	
	public CheckBox onCheck( CheckBoxEvent e ) {
		this.runOnCheck = e.<CheckBoxEvent>setTarget( this );
		return this;
	}
	
	private CheckBoxEvent runOnUnCheck = null;
	public CheckBox onUnCheck( CheckBoxEvent e ) {
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
	
	@Override protected CheckBox destroy() {
		this.real().removeActionListener( this );
		super.destroy();
		
		this.runOnCheck = null;
		this.runOnUnCheck = null;
		
		return null;
	}
}
