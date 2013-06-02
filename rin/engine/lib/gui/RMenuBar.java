package rin.engine.lib.gui;

import javax.swing.Box;
import javax.swing.JMenuBar;

public class RMenuBar extends RComponent<JMenuBar, RMenuBar> {
	
	public RMenuBar( String id ) {
		super( id, new JMenuBar() );
		this.setValidParents( RWindow.class, RDesktopWindow.class );
		this.setValidChildren( RMenu.class );
	}
	
	@Override
	public JMenuBar swing() { return (JMenuBar)this.target; }
	
	@Override
	protected RMenuBar actual() { return this; }
	
	public RMenuBar addHorizontalSeparator() {
		this.swing().add( Box.createHorizontalGlue() );
		return this.update();
	}
	
}
