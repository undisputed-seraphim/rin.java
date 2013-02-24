package rin.gui;

import javax.swing.JMenu;
import javax.swing.event.MenuListener;

import rin.gui.GUIFactory.MenuEvent;

public class Menu extends GUIComponent<Menu, MenuEvent> implements MenuListener {
	private static int items = 0;

	public Menu() { this( "Menu-" + Menu.items++, "N/A", "\0" ); }
	public Menu( String id, String text, String mnemonic ) {
		this.id = id;
		this.target = new JMenu( text );
		this.real().setFont( GUIFactory.DEFAULT_FONT );
		this.setShortcutLetter( mnemonic );
		
		this.onWindowLoad( new GUIFactory.OnLoadEvent() {
			public void run() {
				((JMenu)this.target).addMenuListener( this.component.toMenu() );
			}
		}.setTargets( this.target, this ) );
	}

	private JMenu real() { return (JMenu)this.target; }

	@Override public Menu add( GUIComponent<?, ?> component ) {
		if( component instanceof Menu || component instanceof MenuItem )
			return super.add( component );
		System.out.println( "[ERROR] Only MenuItems and Menus may be added as children of a Menu." );
		return this;
	}

	public Menu addSeparator() { this.real().addSeparator(); return this.update(); }
	public Menu setShortcutLetter( String mnemonic ) {
		int keycode = this.getKeyCode( mnemonic );
			this.real().setMnemonic( keycode );
		return this.update();
	}

	private MenuEvent runOnOpen = null;
	public Menu onOpen( MenuEvent e ) {
		this.runOnOpen = e.<MenuEvent>setTarget( this );
		return this;
	}

	private MenuEvent runOnClose = null;
	public Menu onClose( MenuEvent e ) {
		this.runOnClose = e.<MenuEvent>setTarget( this );
		return this;
	}

	@Override public void menuCanceled( javax.swing.event.MenuEvent e ) {}
	@Override public void menuDeselected( javax.swing.event.MenuEvent e ) { if( this.runOnClose != null ) this.runOnClose.run(); }
	@Override public void menuSelected( javax.swing.event.MenuEvent e ) { if( this.runOnOpen != null ) this.runOnOpen.run(); }

	@Override protected Menu destroy() {
		this.real().removeMenuListener( this );
		super.destroy();

		return null;
	}
}
