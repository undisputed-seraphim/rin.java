package rin.engine.view.gui;

import javax.swing.JMenuItem;

public class RMenuItem extends RComponent<JMenuItem, RMenuItem> {
	
	public RMenuItem( String id, String text ) {
		super( id, new JMenuItem( text ) );
		this.canHaveChildren = false;
		this.setValidParents( RMenu.class, RContextMenu.class );
	}
	
	@Override
	public JMenuItem swing() { return (JMenuItem)this.target; }
	
	@Override
	protected RMenuItem actual() { return this; }

}
