package rin.gui;

import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import rin.gui.GUIFactory.MenuItemEvent;

public class MenuItem extends GUIComponent<MenuItem, MenuItemEvent>{
	private static int items = 0;
	
	public MenuItem() { this( "Menu-" + MenuItem.items++, "N/A", "\0" ); }
	public MenuItem( String id, String text, String mnemonic ) {
		this.id = id;
		this.canHaveChildren = false;
		this.target = new JMenuItem( text );
		this.real().setFont( GUIFactory.DEFAULT_FONT );
		this.setShortcutLetter( mnemonic );
		this.onWindowLoad( new GUIFactory.OnLoadEvent() {
			public void run() {
				((JMenuItem)this.target).addActionListener( this.component.toMenuItem() );
			}
		}.setTargets( this.target, this ) );
	}
	
	private JMenuItem real() { return (JMenuItem)this.target; }
	
	public MenuItem setShortcut( GUIFactory.ModifierKey modifier, String key ) {
		int keycode = this.getKeyCode( key );
		if( keycode != -1 ) {
			this.real().setAccelerator( KeyStroke.getKeyStroke( keycode, this.getModifier( modifier ) ) );
		}
		return this.update();
	}
	
	public MenuItem setShortcutLetter( String mnemonic ) {
		int keycode = this.getKeyCode( mnemonic );
		if( keycode != -1 )
			this.real().setMnemonic( keycode );
		return this.update();
	}
	
	public MenuItem onSelect( MenuItemEvent e ) {
		this.runOnAction = e.<MenuItemEvent>setTarget( this );
		return this;
	}
	
	@Override protected MenuItem destroy() {
		this.real().removeActionListener( this );
		super.destroy();
		
		return null;
	}
}
