package rin.gui;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;

import rin.gui.GUIManager.TabbedPaneEvent;

public class TabbedPane extends GUIComponent<TabbedPane, TabbedPaneEvent> {
	private static int items = 0;
	
	protected ArrayList<String> tabs = new ArrayList<String>();
	private int tabCount = 0;
	private int current = 0;
	
	public TabbedPane() { this( "TabbedPane-" + TabbedPane.items++ ); }
	public TabbedPane( String id ) {
		this.id = id;
		this.target = new JTabbedPane();
		this.real().addChangeListener( this );
	}
	
	private JTabbedPane real() { return (JTabbedPane)this.target; }
	
	public Container getCurrentTab() {
		if( this.children.size() > 0 && this.current >= 0 )
			if( this.children.get( this.current ).target != null )
				return (Container)this.children.get( this.current );
		return null;
	}
	
	@Override public TabbedPane add( GUIComponent<?, ?> component ) {
		if( component instanceof Container )
			return this.addTab( "No Title", '\0', (Container)component );
		
		System.out.println( "[ERROR] Only Containers may serve as Tabs." );
		return this;
	}
	
	public TabbedPane addTab( String title, Container container ) { return this.addTab( title, '\0', container ); }
	public TabbedPane addTab( String title, char mnemonic, Container container ) {
		this.children.add( container );
		this.tabs.add( container.id );
		container.show();
		this.real().insertTab( title, null, container.target, null, this.tabCount );
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
	
	public TabbedPane onTabChange( TabbedPaneEvent e ) {
		this.runOnStateChanged = e.<TabbedPaneEvent>setTarget( this );
		return this;
	}
	
	@Override public void stateChanged( ChangeEvent e ) {
		/*if( this.runOnTabChange != null ) {
			this.runOnTabChange.previous = current;
			this.runOnTabChange.current = Math.max( this.real().getSelectedIndex(), 0 );
			this.current = Math.max( this.real().getSelectedIndex(), 0 );
			this.runOnTabChange.run();
		}
		if( this.getCurrentTab() != null )
			this.getCurrentTab().focus();*/
	}
	
	@Override public TabbedPane destroy() {
		super.destroy();
		
		this.tabs.clear();
		
		return null;
	}
}
