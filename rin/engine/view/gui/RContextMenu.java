package rin.engine.view.gui;

import javax.swing.JPopupMenu;

public class RContextMenu extends RComponent<JPopupMenu, RContextMenu> {

	public RContextMenu( String id ) {
		super( id, new JPopupMenu() );
		this.setValidParents( Void.class );
		this.setValidChildren( RMenuItem.class, RMenu.class );
	}
	
	@Override
	public JPopupMenu swing() { return (JPopupMenu)this.target; }
	
	@Override
	protected RContextMenu actual() { return this; }
}
