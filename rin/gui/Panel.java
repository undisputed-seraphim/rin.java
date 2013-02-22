package rin.gui;

import java.awt.FlowLayout;

import javax.swing.JPanel;

import rin.gui.GUIManager.PanelEvent;

public class Panel extends GUIComponent<Panel, PanelEvent> {
	private static int items = 0;
	
	public Panel() { this( "Panel-" + Panel.items++ ); }
	public Panel( String id ) {
		this.id = id;
		this.target = new JPanel( new GUIManager.GUIFlowLayout() );
		this.target.addMouseListener( this );
	}
	
	public Panel setAlignment( GUIManager.Alignment alignment ) {
		( (FlowLayout)this.target.getLayout() ).setAlignment( this.getFlowAlignment( alignment ) );
		return this.update();
	}
}
