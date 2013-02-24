package rin.gui;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import rin.gui.GUIFactory.ComboItemEvent;

public class ComboItem extends GUIComponent<ComboItem, ComboItemEvent> {
	private static int items = 0;
	
	public ComboItem() { this( "ComboItem-" + ComboItem.items++, "N/A" ); }
	public ComboItem( String id, final String text ) {
		this.id = id;
		this.canHaveChildren = false;
		this.target = new JLabel() {
			private String value = text;
			private static final long serialVersionUID = 1L;
			@Override public String toString() { return this.value; }
		};
	}
	
	private JComboBox real() { return (JComboBox)this.parent.target; }
	
	public ComboItem select() {
		if( this.parent == null ) {
			new DoLater<ComboItem, ComboItem>( this, null ) {
				@Override public void run() { this.t1.select(); }
			};
		} else {
			this.real().setSelectedIndex( this.parent.children.indexOf( this ) );
		}
		return this;
	}
	
	protected ComboItemEvent runOnSelect = null;
	public ComboItem onSelect( ComboItemEvent e ) {
		this.runOnSelect = e.<ComboItemEvent>setTarget( this );
		return this;
	}
	
	protected ComboItemEvent runOnDeSelect = null;
	public ComboItem onDeSelect( ComboItemEvent e ) {
		this.runOnDeSelect = e.<ComboItemEvent>setTarget( this );
		return this;
	}
	
	@Override protected ComboItem destroy() {
		super.destroy();
		
		return null;
	}
}
