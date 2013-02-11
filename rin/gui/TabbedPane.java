package rin.gui;

import java.awt.event.KeyEvent;

import javax.swing.JTabbedPane;

public class TabbedPane extends GUIComponent {
	private int tabCount = 0;
	
	public TabbedPane() {
		this.target = new JTabbedPane();
	}
	
	public TabbedPane addTab( String title, GUIComponent component ) { return this.addTab( title, component, '\0' ); }
	public TabbedPane addTab( String title, GUIComponent component, char mnemonic ) {
		this.children.add( component );
		component.show();
		((JTabbedPane)this.target).insertTab( title, null, component.target, null, this.tabCount );
		if( mnemonic != '\0' ) {
			try {
				try {
					((JTabbedPane)this.target).setMnemonicAt( this.tabCount, KeyEvent.class.getField( "VK_" + mnemonic ).getInt( null ) );
				} catch( IllegalArgumentException e ) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch( IllegalAccessException e ) {
					System.out.println( "unacceptable mnemonic requested." );
				}
			} catch( SecurityException e ) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch( NoSuchFieldException e ) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.tabCount++;
		this.target.validate();
		this.target.repaint();
		return this;
	}
}