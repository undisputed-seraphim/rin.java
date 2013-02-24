package rin.gui;

import javax.swing.Box;
import javax.swing.JMenuBar;

import rin.gui.GUIFactory.MenuBarEvent;

public class MenuBar extends GUIComponent<MenuBar, MenuBarEvent> {
	private static int items = 0;
	
	public MenuBar() { this( "MenuBar-" + MenuBar.items++ ); }
	public MenuBar( String id ) {
		this.id = id;
		this.target = new JMenuBar();
		this.real().setFont( GUIFactory.DEFAULT_FONT );
	}
	
	private JMenuBar real() { return (JMenuBar)this.target; }
	
	@Override public MenuBar add( GUIComponent<?, ?> component ) {
		if( !(component instanceof Menu) ) {
			System.out.println( "[ERROR] Only Menus may be added to MenuBars." );
			return this;
		}
		
		return super.add( component );
	}
	
	public MenuBar addHorizontalSeparator() {
		this.real().add( Box.createHorizontalGlue() );
		return this.update();
	}
	
	@Override protected MenuBar destroy() {
		super.destroy();
		
		return null;
	}
}
