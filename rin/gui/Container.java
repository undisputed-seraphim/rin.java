package rin.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

public class Container extends GUIComponent {
	
	public Container() {
		this.setAligned( true );
		this.target = new JPanel( new GUIManager.GUIGridBagLayout() );
	}
	
	public Container add( GUIComponent component ) {
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.3;
		c.ipadx = 5;
		c.ipady = 5;
		c.gridx = 1;
		c.gridy = this.childCount + 1;
		/*
button = new JButton("Long-Named Button 4");
c.fill = GridBagConstraints.HORIZONTAL;
c.ipady = 40;      //make this component tall
c.weightx = 0.0;
c.gridwidth = 3;
c.gridx = 0;
c.gridy = 1;
pane.add(button, c);

button = new JButton("5");
c.fill = GridBagConstraints.HORIZONTAL;
c.ipady = 0;       //reset to default
c.weighty = 1.0;   //request any extra vertical space
c.anchor = GridBagConstraints.PAGE_END; //bottom of space
c.insets = new Insets(10,0,0,0);  //top padding
c.gridx = 1;       //aligned with button 2
c.gridwidth = 2;   //2 columns wide
c.gridy = 2;       //third row*/
		if( this.aligned )
			component.setAlignment( this.alignX, this.alignY );
		
		this.children.add( component );
		this.target.add( this.children.get( this.childCount++ ).target, c );
		component.show();
		this.target.validate();
		this.target.repaint();
		return this;
	}
}
