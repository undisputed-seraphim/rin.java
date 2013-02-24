package rin.gui;

import java.util.ArrayList;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;

import rin.gui.GUIFactory.TabbedPaneEvent;

public class TabbedPane extends GUIComponent<TabbedPane, TabbedPaneEvent> {
	private static int items = 0;
	
	protected ArrayList<String> tabs = new ArrayList<String>();
	private int tabCount = 0;
	private int currentIndex = 0;
	
	public TabbedPane() { this( "TabbedPane-" + TabbedPane.items++ ); }
	public TabbedPane( String id ) {
		this.id = id;
		this.target = new JTabbedPane();
		this.real().setFont( GUIFactory.DEFAULT_FONT );
		
		this.onWindowLoad( new GUIFactory.OnLoadEvent() {
			public void run() {
				((JTabbedPane)this.target).addChangeListener( this.component.toTabbedPane() );
			}
		}.setTargets( this.target, this ) );
	}
	
	private JTabbedPane real() { return (JTabbedPane)this.target; }
	
	public Container getCurrentTab() {
		if( this.children.size() > 0 && this.currentIndex >= 0 )
			if( this.children.get( this.currentIndex ).target != null )
				return (Container)this.children.get( this.currentIndex );
		return null;
	}
	
	public String getTitle( int index ) {
		if( this.tabs.size() >= index && index > 0 && index - 1 < this.real().getTabCount() )
			return this.real().getTitleAt( index - 1 );
		return null;
	}
	
	@Override public TabbedPane add( GUIComponent<?, ?> component ) {
		if( component instanceof Container )
			return this.addTab( "No Title", '\0', (Container)component );
		
		System.out.println( "[ERROR] Only Containers may serve as Tabs." );
		return this;
	}
	
	//TODO: add proper remove logic so that tabs are removed with real().removeTabAt(...)
	public TabbedPane addTab( String title, Container container ) { return this.addTab( title, '\0', container ); }
	public TabbedPane addTab( String title, char mnemonic, Container container ) {
		container.parent = this;
		this.children.add( container );
		this.tabs.add( container.id );
		container.show();
		this.real().insertTab( title, null, container.target, null, this.tabCount );
		
		int keycode = this.getKeyCode( mnemonic + "" );
		if( keycode != -1 )
			this.real().setMnemonicAt( this.tabCount, keycode );
		this.tabCount++;
		
		return this.update();
	}
	
	public TabbedPane onTabChanged( TabbedPaneEvent e ) {
		this.runOnStateChanged = e.<TabbedPaneEvent>setTarget( this );
		return this;
	}
	
	@Override public void stateChanged( ChangeEvent e ) {
		if( this.runOnStateChanged != null && this.getCurrentTab() != null ) {
			this.runOnStateChanged.previousIndex = this.currentIndex;
			this.runOnStateChanged.currentIndex = Math.max( this.real().getSelectedIndex(), 0 );
			this.currentIndex = Math.max( this.real().getSelectedIndex(), 0 );
			
			this.runOnStateChanged.tab = this.getCurrentTab();
			this.runOnStateChanged.title = this.getTitle( this.currentIndex + 1 );

			this.getCurrentTab().focus();
			this.runOnStateChanged.run();
		}
	}
	
	@Override protected TabbedPane destroy() {
		this.real().removeChangeListener( this );
		super.destroy();
		
		this.tabs.clear();
		
		return null;
	}
}
