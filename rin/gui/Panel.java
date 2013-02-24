package rin.gui;

import javax.swing.JPanel;

import rin.gui.GUIFactory.PanelEvent;

public class Panel extends GUIComponent<Panel, PanelEvent> {
	private static int items = 0;
	
	public Panel() { this( "Panel-" + Panel.items++ ); }
	public Panel( String id ) {
		this.id = id;
		this.target = new JPanel( new GUIFactory.FlowLayout() );
	}
	
	public Panel setAlignment( GUIFactory.Alignment alignment ) {
		( (GUIFactory.FlowLayout)this.target.getLayout() ).setAlignment( this.getFlowAlignment( alignment ) );
		return this.update();
	}
}
