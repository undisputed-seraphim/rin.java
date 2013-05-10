package rin.engine.lib.gui;

import javax.swing.JDesktopPane;

public class RDesktop extends RWindow {

	public RDesktop( String id ) {
		super( id );
		this.setValidChildren( RDesktopWindow.class );
		this.swing().remove( this.target );
		this.target = new JDesktopPane();
		this.swing().add( this.target );
	}
	
	public JDesktopPane desktop() { return (JDesktopPane)this.target; }
	
	@Override
	protected RDesktop actual() { return this; }
	
}
