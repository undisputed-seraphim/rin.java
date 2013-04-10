package rin.engine.view.gui;

import javax.swing.JMenu;

public class RMenu extends RComponent<JMenu, RMenu> {
	
	public RMenu( String id, String text ) {
		super( id, new JMenu( text ) );
		this.validParents = new Class<?>[] { RMenuBar.class, RMenu.class };
	}
	
	@Override
	public JMenu swing() { return (JMenu)this.target; }
	
	@Override
	protected RMenu actual() { return this; }

}
