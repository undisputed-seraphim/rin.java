package rin.gui;

import java.awt.event.ActionEvent;

import javax.swing.JCheckBox;

public class CheckBox extends GUIComponent<CheckBox> {
	private Runnable runOnTrue = null;
	private Runnable runOnFalse = null;
	
	public CheckBox() {
		this.target = new JCheckBox();
		((JCheckBox)this.target).addActionListener( this );
		((JCheckBox)this.target).setFont( GUIManager.font );
	}
	private JCheckBox real() { return (JCheckBox)this.target; }
	
	public CheckBox onTrue( Runnable r ) { this.runOnTrue = r; return this; }
	public CheckBox onFalse( Runnable r ) { this.runOnFalse = r; return this; }
	
	public CheckBox setLabel( String str ) { this.real().setText( str ); return this; }
	public CheckBox setLabelPositionH( GUIManager.Position position ) { this.real().setHorizontalTextPosition( position.value ); return this; }
	
	private void trueSelected() {
		if( this.runOnTrue != null )
			this.runOnTrue.run();
	}
	
	private void falseSelected() {
		if( this.runOnFalse != null )
			this.runOnFalse.run();
	}
	
	@Override public void actionPerformed( ActionEvent e ) {
		if( ((JCheckBox)this.target).isSelected() )
			this.trueSelected();
		else this.falseSelected();
	}
}
