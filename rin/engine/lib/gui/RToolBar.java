package rin.engine.lib.gui;

import javax.swing.JToolBar;

public class RToolBar extends RComponent<JToolBar, RToolBar> {
	
	public RToolBar( String id ) {
		super( id, new JToolBar() );
		this.swing().setFloatable( false );
	}
	
	@Override
	public JToolBar swing() { return (JToolBar)this.target; }
	
	@Override
	protected RToolBar actual() { return this; }
	
	public RToolBar addSeparator() {
		this.swing().addSeparator();
		return this.update();
	}
	
	@Override
	public RToolBar add( RComponent<?, ?> ... components ) {
		return super.add( components );
	}

}
