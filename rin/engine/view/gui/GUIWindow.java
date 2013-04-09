package rin.engine.view.gui;

import javax.swing.JFrame;

public class GUIWindow extends GUIComponent<JFrame> {

	public GUIWindow( String id ) {
		super( id, new JFrame() );
		this.swing().setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
	}
	
	@Override
	protected JFrame swing() { return (JFrame)this.target; }
	
}
