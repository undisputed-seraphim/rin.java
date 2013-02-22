package rin.gui;

import javax.swing.Box;
import javax.swing.JMenuBar;

import rin.gui.GUIManager.MenuBarEvent;

public class MenuBar extends GUIComponent<MenuBar, MenuBarEvent> {
	private static int items = 0;
	
	public MenuBar() { this( "MenuBar-" + MenuBar.items++ ); }
	public MenuBar( String id ) {
		this.id = id;
		this.target = new JMenuBar();
		this.real().setFont( GUIManager.DEFAULT_FONT );
	}
	
	private JMenuBar real() { return (JMenuBar)this.target; }
	
	public MenuBar addHorizontalSeparator() {
		this.real().add( Box.createHorizontalGlue() );
		return this.update();
	}
	
	@Override public MenuBar destroy() {
		super.destroy();
		
		return null;
	}
}
