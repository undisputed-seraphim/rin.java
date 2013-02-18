package rin.gui;

import java.awt.FlowLayout;

import javax.swing.JPanel;

import rin.gui.GUIComponent;

public class Panel extends GUIComponent<Panel> {
	private static int items = 0;
	
	public Panel() { this( "Panel-" + Panel.items++ ); }
	public Panel( String id ) {
		this.id = id;
		this.target = new JPanel( new GUIManager.GUIFlowLayout() );
	}
	
	public Panel setAlignment( GUIManager.Alignment alignment ) {
		switch( alignment ) {
		
		case LEFT: ((FlowLayout)this.target.getLayout()).setAlignment( FlowLayout.LEFT ); break;
		case CENTER: ((FlowLayout)this.target.getLayout()).setAlignment( FlowLayout.CENTER ); break;
		case RIGHT: ((FlowLayout)this.target.getLayout()).setAlignment( FlowLayout.RIGHT ); break;
		
		}
		return this.update();
	}
}
