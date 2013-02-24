package rin.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;

import rin.gui.GUIFactory.ComboBoxEvent;
import rin.gui.GUIFactory.ComboItemEvent;

//TODO: add support for dynamically removing items
public class ComboBox extends GUIComponent<ComboBox, ComboBoxEvent> implements ItemListener {
	private static int items = 0;
	private int selectedIndex = 0;
	
	public ComboBox() { this( "ComboBox-" + ComboBox.items++ ); }
	public ComboBox( String id ) {
		this.id = id;
		this.target = new JComboBox();
		this.target.setFont( GUIFactory.DEFAULT_FONT );
		
		this.onWindowLoad( new GUIFactory.OnLoadEvent() {
			@Override public void run() {
				this.component.toComboBox().selectedIndex = this.component.toComboBox().getSelectedIndex();
				((JComboBox)this.target).addItemListener( this.component.toComboBox() );
				((JComboBox)this.target).addActionListener( this.component.toComboBox() );
			}
		}.setTargets( this.target, this ) );
	}
	
	private JComboBox real() { return (JComboBox)this.target; }
	public int getSelectedIndex() { return this.real().getSelectedIndex(); }
	@Override public ComboBox update() { this.selectedIndex = this.getSelectedIndex(); return super.update(); }
	
	@Override public ComboBox add( GUIComponent<?, ?> component ) {
		if( !(component instanceof ComboItem) ) {
			System.out.println( "[ERROR] Only ComboItems may be added to a ComboBox." );
			return this;
		}
		
		super.add( component );
		this.real().addItem( component.target );
		return this.update();
	}
	
	private ComboBoxEvent runOnSelect = null;
	public ComboBox onSelect( ComboBoxEvent e ) {
		this.runOnSelect = e.<ComboBoxEvent>setTarget( this );
		return this;
	}
	
	@Override public void actionPerformed( ActionEvent e ) {
		if( this.runOnSelect != null ) {
			this.runOnSelect.previousIndex = this.selectedIndex;
			this.runOnSelect.currentIndex = this.real().getSelectedIndex();
			this.runOnSelect.value = this.real().getItemAt( this.real().getSelectedIndex() ).toString();
			this.runOnSelect.run();
		}
		this.selectedIndex = this.real().getSelectedIndex();
	}
	
	@Override public void itemStateChanged( ItemEvent e ) {
		int index = this.real().getSelectedIndex();
		ComboItemEvent event;
		
		if( e.getStateChange() == 1 )
			event = ((ComboItem)this.children.get( index )).runOnSelect;
		else
			event = ((ComboItem)this.children.get( this.selectedIndex )).runOnDeSelect;
		
		if( event != null ) {
			event.previousIndex = this.selectedIndex;
			event.currentIndex = index;
			this.runOnSelect.value = this.real().getItemAt( index ).toString();
			event.run();
		}
	}
	
	@Override protected ComboBox destroy() {
		this.real().removeItemListener( this );
		this.real().removeActionListener( this );
		super.destroy();
		
		return null;
	}
}
