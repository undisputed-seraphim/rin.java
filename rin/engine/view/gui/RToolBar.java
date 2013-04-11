package rin.engine.view.gui;

import javax.swing.JToolBar;

import rin.engine.meta.RinChainable;

public class RToolBar extends RComponent<JToolBar, RToolBar> {
	
	public RToolBar( String id ) {
		super( id, new JToolBar() );
		this.swing().setFloatable( false );
	}
	
	@Override
	public JToolBar swing() { return (JToolBar)this.target; }
	
	@Override
	protected RToolBar actual() { return this; }
	
	@Override
	@RinChainable
	public RToolBar add( RComponent<?, ?> ... components ) {
		return super.add( components );
	}

}
