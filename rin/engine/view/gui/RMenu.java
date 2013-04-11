package rin.engine.view.gui;

import javax.swing.JMenu;

public class RMenu extends RComponent<JMenu, RMenu> {
	
	public RMenu( String id, String text ) {
		super( id, new JMenu( text ) );
		this.setValidParents( RMenuBar.class, RMenu.class, RContextMenu.class );
		this.setValidChildren( RMenu.class, RMenuItem.class );
	}
	
	@Override
	public JMenu swing() { return (JMenu)this.target; }
	
	@Override
	protected RMenu actual() { return this; }

}
