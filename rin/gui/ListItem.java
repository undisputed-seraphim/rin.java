package rin.gui;

import javax.swing.JLabel;

import rin.gui.GUIFactory.ListItemEvent;

public class ListItem extends GUIComponent<ListItem, ListItemEvent> {
	private static int items = 0;
	
	public ListItem() { this( "ListItem-" + ListItem.items++, "N/A" ); }
	public ListItem( String id, final String text ) {
		this.id = id;
		this.canHaveChildren = false;
		this.target = new JLabel() {
			private String value = text;
			private static final long serialVersionUID = 1L;
			@Override public String toString() { return this.value; }
		};
	}

	public ListItem select() {
		this.onWindowLoad( new GUIFactory.OnLoadEvent() {
			@Override public void run() {
				((List)this.component.parent).selectListItemAt( this.component.parent.children.indexOf( this.component ) );
			}
		}.setTargets( this.target, this ) );
		return this;
	}
	
	protected ListItemEvent runOnSelect = null;
	public ListItem onSelect( ListItemEvent e ) {
		this.runOnSelect = e.<ListItemEvent>setTarget( this );
		return this;
	}
	
	protected ListItemEvent runOnDeselect = null;
	public ListItem onDeselect( ListItemEvent e ) {
		this.runOnDeselect = e.<ListItemEvent>setTarget( this );
		return this;
	}
	
	@Override protected ListItem destroy() {
		super.destroy();
		
		return null;
	}
}
