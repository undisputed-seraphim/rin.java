package rin.gui;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JPanel;

public class Container extends GUIComponent<Container>{
	private ParallelGroup pgroupH;
	private SequentialGroup pgroupV;
	
	public Container() {
		this.target = new JPanel();
		
		GroupLayout layout = new GUIManager.GUIGroupLayout( this.target );
		this.pgroupH = layout.createParallelGroup();
		this.pgroupV = layout.createSequentialGroup();
		layout.setHorizontalGroup( this.pgroupH );
		layout.setVerticalGroup( this.pgroupV );
		
		this.target.setLayout( layout );
	}
	
	public Container add( GUIComponent<?> component ) {
		pgroupH.addComponent( component.target, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE );
		pgroupV.addComponent( component.target, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE );
		
		this.children.add( component );
		component.show();
		this.target.validate();
		this.target.repaint();
		return this;
	}
}
