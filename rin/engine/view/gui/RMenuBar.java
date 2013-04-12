package rin.engine.view.gui;

import javax.swing.Box;
import javax.swing.JMenuBar;

import rin.engine.meta.RinChainable;

public class RMenuBar extends RComponent<JMenuBar, RMenuBar> {
	
	public RMenuBar( String id ) {
		super( id, new JMenuBar() );
		this.setValidParents( RWindow.class );
		this.setValidChildren( RMenu.class );
	}
	
	@Override
	public JMenuBar swing() { return (JMenuBar)this.target; }
	
	@Override
	protected RMenuBar actual() { return this; }
	
	@RinChainable
	public RMenuBar addHorizontalSeparator() {
		this.swing().add( Box.createHorizontalGlue() );
		return this.update();
	}
	
}
