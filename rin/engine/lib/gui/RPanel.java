package rin.engine.lib.gui;

import javax.swing.JPanel;

public class RPanel extends RComponent<JPanel, RPanel> {
	
	public RPanel( String id ) {
		super( id, new JPanel() );
	}
	
	@Override
	public JPanel swing() { return (JPanel)this.target; }
	
	@Override
	protected RPanel actual() { return this; }

}
