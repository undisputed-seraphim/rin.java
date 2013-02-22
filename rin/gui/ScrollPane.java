package rin.gui;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JScrollPane;

import rin.gui.GUIManager.ScrollPaneEvent;

public class ScrollPane extends GUIComponent<ScrollPane, ScrollPaneEvent> implements AdjustmentListener {
	private static int items = 0;
	
	private Container pane;
	private int currentX = 0;
	private int currentY = 0;
	
	public ScrollPane() { this( "ScrollPane-" + ScrollPane.items++ ); }
	public ScrollPane( String id ) {
		this.id = id;
		this.pane = new Container( this.id + "_root" );
		this.pane.show();
		this.target = new JScrollPane( this.pane.target );
		this.showHorizontalScrollAlways();
		this.showVerticalScrollAlways();
		
		this.real().getHorizontalScrollBar().addAdjustmentListener( this );
		this.real().getVerticalScrollBar().addAdjustmentListener( this );
	}
	
	private JScrollPane real() { return (JScrollPane)this.target; }
	
	public ScrollPane setSize( int width, int height ) {
		this.pane.setSize( width, height );
		return this.update();
	}
	
	public ScrollPane setAlignment( GUIManager.Alignment alignment ) {
		this.pane.setAlignment( alignment );
		return this.update();
	}
	
	@Override public ScrollPane add( GUIComponent<?, ?> component ) {
		component.addTo( this.pane );
		return this.update();
	}
	
	private ScrollPane setH( int opt ) { this.real().setHorizontalScrollBarPolicy( opt ); return this.update(); }
	public ScrollPane showHorizontalScrollAsNeeded() { return this.setH( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED ); }	
	public ScrollPane showHorizontalScrollAlways() { return this.setH( JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS ); }
	public ScrollPane showHorizontalScrollNever() { return this.setH( JScrollPane.HORIZONTAL_SCROLLBAR_NEVER ); }
	
	private ScrollPane setV( int opt ) { this.real().setVerticalScrollBarPolicy( opt ); return this.update(); }
	public ScrollPane showVerticalScrollAsNeeded() { return this.setV( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED ); }
	public ScrollPane showVerticalScrollAlways() { return this.setV( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS ); }
	public ScrollPane showVerticalScrollNever() { return this.setV( JScrollPane.VERTICAL_SCROLLBAR_NEVER ); }
	
	private ScrollPaneEvent runOnVerticalScroll = null;
	public ScrollPane onVerticalScroll( ScrollPaneEvent e ) {
		this.runOnVerticalScroll = e.<ScrollPaneEvent>setTarget( this );
		return this;
	}
	
	private ScrollPaneEvent runOnHorizontalScroll = null;
	public ScrollPane onHorizontalScroll( ScrollPaneEvent e ) {
		this.runOnHorizontalScroll = e.<ScrollPaneEvent>setTarget( this );
		return this;
	}
	
	@Override public void adjustmentValueChanged( AdjustmentEvent e ) {
		if( !e.getValueIsAdjusting() ) {
			if( e.getAdjustable().equals( this.real().getVerticalScrollBar() ) ) {
				if( e.getValue() != this.currentY ) {
					if( this.runOnVerticalScroll != null ) {
						this.runOnVerticalScroll.delta = e.getValue() - this.currentY;
						this.runOnVerticalScroll.position = e.getValue();
						this.runOnVerticalScroll.execute( e );
					}
					this.currentY = e.getValue();
				}
			} else {
				if( e.getValue() != this.currentX ) {
					if( this.runOnHorizontalScroll != null ) {
						this.runOnHorizontalScroll.delta = e.getValue() - this.currentX;
						this.runOnHorizontalScroll.position = e.getValue();
						this.runOnHorizontalScroll.execute( e );
					}
					this.currentX = e.getValue();
				}
			}
		}
	}
	
	@Override public ScrollPane destroy() {
		if( this.target != null ) {
			this.real().getHorizontalScrollBar().removeAdjustmentListener( this );
			this.real().getVerticalScrollBar().removeAdjustmentListener( this );
		}
		super.destroy();
		
		this.pane = null;
		
		return null;
	}
}
