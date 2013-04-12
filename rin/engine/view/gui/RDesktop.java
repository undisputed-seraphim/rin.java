package rin.engine.view.gui;

import javax.swing.JDesktopPane;

public class RDesktop extends RWindow {

	public RDesktop( String id ) {
		super( id );
		this.swing().remove( this.target );
		this.target = new JDesktopPane();
		this.swing().setContentPane( this.target );
	}
	
	public JDesktopPane desktop() { return (JDesktopPane)this.target; }
	
	@Override
	protected RDesktop actual() { return this; }
	
}
