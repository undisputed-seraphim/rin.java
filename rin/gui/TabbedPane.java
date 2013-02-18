package rin.gui;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;

public class TabbedPane extends GUIComponent<TabbedPane> {
	private static int items = 0;
	
	protected ArrayList<String> tabs = new ArrayList<String>();
	private int tabCount = 0;
	
	public TabbedPane() { this( "TabbedPane-" + TabbedPane.items++ ); }
	public TabbedPane( String id ) {
		this.id = id;
		this.target = new JTabbedPane();
		this.real().addChangeListener( this );
	}
	
	private JTabbedPane real() { return (JTabbedPane)this.target; }
	
	@Override public TabbedPane add( GUIComponent<?> component ) {
		if( (component instanceof Panel || component instanceof Container || component instanceof TabbedPane ) ) {
			System.out.println( "[WARNING] Use addTab() instead." );
			return this.addTab( "No Title", '\0', component );
		}
		System.out.println( "[ERROR] Only Tab capable items ( Container, Panel, TabbedPane ) may be added to a TabbedPane." );
		return this;
	}
	
	public TabbedPane addTab( String title, GUIComponent<?> component ) { return this.addTab( title, '\0', component ); }
	public TabbedPane addTab( String title, char mnemonic, GUIComponent<?> component ) {
		if( !(component instanceof Panel || component instanceof Container || component instanceof TabbedPane ) ) {
			System.out.println( "[Error] Tabs must be of type Panel, Container, or TabbedPane." );
			return this;
		}
		this.children.add( component );
		this.tabs.add( component.id );
		component.show();
		this.real().insertTab( title, null, component.target, null, this.tabCount );
		if( mnemonic != '\0' ) {
			try {
				try {
					this.real().setMnemonicAt( this.tabCount, KeyEvent.class.getField( "VK_" + mnemonic ).getInt( null ) );
				} catch( IllegalArgumentException e ) {
					System.out.println( "unacceptable mnemonic requested." );
				} catch( IllegalAccessException e ) {
					System.out.println( "unacceptable mnemonic requested." );
				}
			} catch( SecurityException e ) {
				System.out.println( "unacceptable mnemonic requested." );
			} catch( NoSuchFieldException e ) {
				System.out.println( "unacceptable mnemonic requested." );
			}
		}
		this.tabCount++;
		return this.update();
	}
	
	@Override public void stateChanged( ChangeEvent e ) {
		int pos = this.real().getSelectedIndex();
		if( pos != -1 )
			GUIManager.get( this.tabs.get( pos ) ).focused();
	}
	
	@Override public TabbedPane destroy() {
		super.destroy();
		
		this.tabs.clear();
		
		return null;
	}
}
