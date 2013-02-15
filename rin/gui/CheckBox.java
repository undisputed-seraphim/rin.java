package rin.gui;

import java.awt.event.ActionEvent;

import javax.swing.JCheckBox;

public class CheckBox extends GUIComponent {
	private Runnable runOnTrue = null;
	private Runnable runOnFalse = null;
	
	public CheckBox() {
		this.target = new JCheckBox();
		((JCheckBox)this.target).addActionListener( this );
	}
	private JCheckBox actual() { return (JCheckBox)this.target; }
	
	public CheckBox onTrue( Runnable r ) { this.runOnTrue = r; return this; }
	public CheckBox onFalse( Runnable r ) { this.runOnFalse = r; return this; }
	
	public CheckBox setLabel( String str ) { this.actual().setText( str ); return this; }
	public CheckBox setLabelPositionH( int pos ) { this.actual().setHorizontalTextPosition( pos ); return this; }
	
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
