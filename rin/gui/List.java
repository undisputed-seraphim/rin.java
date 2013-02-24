package rin.gui;

import java.util.ArrayList;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import rin.gui.GUIFactory.ListEvent;
import rin.gui.GUIFactory.ListItemEvent;

public class List extends GUIComponent<List, ListEvent> implements ListSelectionListener {
	private static int items = 0;

	private JList list = null;
	private ScrollPane pane = null;
	private int[] selectedIndices = new int[0];
	private boolean dynamic = false;

	public List() { this( "List-" + List.items++ ); }
	public List( String id ) {
		this.id = id;
		this.list = new JList();
		this.list.setFont( GUIFactory.DEFAULT_FONT );
		
		this.pane = new ScrollPane();
		this.pane.showVerticalScrollAsNeeded();
		this.pane.showHorizontalScrollNever();
		((JScrollPane)this.pane.target).getViewport().setView( this.list );
		this.target = this.pane.target;
		
		this.onWindowLoad( new GUIFactory.OnLoadEvent() {
			@Override public void run() {
				this.component.toList().selectedIndices = ((JList)this.target).getSelectedIndices();
				((JList)this.target).addListSelectionListener( this.component.toList() );
			}
		}.setTargets( this.list, this ) );
	}

	private JList real() { return (JList)this.list; }
	
	@Override public List setSize( int width, int height ) { this.pane.setSize( width, height ); return this.update(); }
	public List setDynamic( boolean dynamic ) { this.dynamic = dynamic; return this; }
	
	public List clearSelection() { this.real().clearSelection(); this.selectedIndices = new int[0]; return this.update(); }
	public List selectListItemAt( int index ) { this.real().setSelectedIndex( index ); return this.update(); }
	public String getListItemAt( int index ) { return this.children.get( index ).target.toString(); }
	public int[] getRecentlySelected() {
		int[] a = this.real().getSelectedIndices();
		int[] b = this.selectedIndices;
		ArrayList<Integer> res = new ArrayList<Integer>();
		for( int i = 0; i < a.length; i++ )
			if( !inArrayi( b, a[i] ) )
				res.add( a[i] );
		
		int[] result = new int[res.size()];
		for( int i = 0; i < res.size(); i++ )
			result[i] = res.get( i );
		return result;
	}
	
	public int[] getRecentlyDeselected() {
		int[] a = this.real().getSelectedIndices();
		int[] b = this.selectedIndices;
		ArrayList<Integer> res = new ArrayList<Integer>();
		
		for( int i = 0; i < b.length; i++ )
			if( !inArrayi( a, b[i] ) )
				res.add( b[i] );
		
		int[] result = new int[res.size()];
		for( int i = 0; i < res.size(); i++ )
			result[i] = res.get( i );
		return result;
	}
	
	private boolean inArrayi( int[] a, int i ) {
		for( int j = 0; j < a.length; j++ )
			if( a[j] == i )
				return true;
		return false;
	}
	
	@Override public List add( GUIComponent<?, ?> component ) {
		if( !(component instanceof ListItem) ) {
			System.out.println( "[ERROR] Only ListItems may be added to Lists." );
			return this;
		}
		
		this.clearSelection();
		component.parent = this;
		this.children.add( component );
		
		ArrayList<String> data = new ArrayList<String>();
		for( GUIComponent<?, ?> g : this.children )
			data.add( g.target.toString() );
		this.real().setListData( data.toArray() );
		
		this.selectedIndices = this.real().getSelectedIndices();
		return this.update();
	}
	
	private ListEvent runOnChange = null;
	public List onChange( ListEvent e ) {
		this.runOnChange = e.<ListEvent>setTarget( this );
		return this;
	}
	
	@Override public void valueChanged( ListSelectionEvent e ) {
		if( !e.getValueIsAdjusting() || this.dynamic ) {
			ListItemEvent event;
			int[] current = this.real().getSelectedIndices();
			int[] on = this.getRecentlySelected();
			int[] off = this.getRecentlyDeselected();
			
			for( int i : on ) {
				event = ((ListItem)this.children.get( i )).runOnSelect;
				if( event != null ) {
					event.selected = current;
					event.recentlySelected = on;
					event.recentlyDeselected = off;
					event.text = this.getListItemAt( i );
					event.run();
				}
			}
			
			for( int i : off ) {
				event = ((ListItem)this.children.get( i )).runOnDeselect;
				if( event != null ) {
					event.selected = current;
					event.recentlySelected = on;
					event.recentlyDeselected = off;
					event.text = this.getListItemAt( i );
					event.run();
				}
			}
			
			if( this.runOnChange != null ) {
				this.runOnChange.selected = current;
				this.runOnChange.recentlySelected = on;
				this.runOnChange.recentlyDeselected = off;
				this.runOnChange.run();
			}
			
			this.selectedIndices = this.real().getSelectedIndices();
		}
	}
	
	@Override protected List destroy() {
		this.real().removeListSelectionListener( this );
		super.destroy();
		
		this.pane = this.pane.destroy();
		this.list = null;
		
		return null;
	}
}
