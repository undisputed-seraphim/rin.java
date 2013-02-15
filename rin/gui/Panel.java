package rin.gui;

import javax.swing.JPanel;

import rin.gui.GUIComponent;

public class Panel extends GUIComponent {
	public Panel() { this( new GUIManager.GUIFlowLayout() ); }
	public Panel( GUIManager.GUILayout layout ) {
		this.target = new JPanel( layout );
	}
}
