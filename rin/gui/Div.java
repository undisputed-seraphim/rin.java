package rin.gui;

import java.awt.LayoutManager;
import javax.swing.JPanel;

public class Div extends GUIComponent {
	public Div() {
		this.target = new JPanel();
		this.setAligned( true );
		this.target.setLayout( new GUIManager.GUIFlowLayout() );
	}
}
