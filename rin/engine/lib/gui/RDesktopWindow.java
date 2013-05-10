package rin.engine.lib.gui;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import rin.engine.meta.RinChainable;

public class RDesktopWindow extends RComponent<JInternalFrame, RDesktopWindow> {
	
	private JInternalFrame window;
	
	public RDesktopWindow( String id ) {
		super( id, new JPanel() );
		this.setValidParents( RDesktop.class );
		this.window = new JInternalFrame( null, true, true, true );
		this.window.add( this.target );
	}
	
	@Override
	public JInternalFrame swing() { return this.window; }
	
	@Override
	protected RDesktopWindow actual() { return this; }
	
	@Override
	@RinChainable
	public RDesktopWindow update() {
		super.update();
		this.swing().pack();
		
		return this;
	}
	
	@Override
	@RinChainable
	public RDesktopWindow setSize( int width, int height ) {
		this.swing().setSize( width, height );
		return this.update();
	}

}
