package rin.gui;

import java.awt.event.ActionEvent;

import javax.swing.JCheckBox;

public class CheckBox extends GUIComponent<CheckBox> {
	private static int items = 0;
	
	private Runnable runOnTrue = null;
	private Runnable runOnFalse = null;
	
	public CheckBox() { this( "CheckBox-" + CheckBox.items++ ); }
	public CheckBox( String id ) {
		this.id = id;
		this.canHaveChildren = false;
		this.target = new JCheckBox();
		this.real().addActionListener( this );
		this.real().setFont( GUIManager.DEFAULT_FONT );
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
		if( this.real().isSelected() )
			this.trueSelected();
		else this.falseSelected();
	}
	
	@Override public CheckBox destroy() {
		super.destroy();
		
		this.runOnFalse = null;
		this.runOnTrue = null;
		
		return null;
	}
}
