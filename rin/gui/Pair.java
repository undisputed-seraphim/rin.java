package rin.gui;

import javax.swing.JPanel;

import rin.gui.GUIFactory.PairEvent;

public class Pair extends GUIComponent<Pair, PairEvent> {
	private static int items = 0;
	
	private Panel left;
	private Panel right;
	
	public Pair() { this( "Pair-" + Pair.items++ ); }
	public Pair( String id ) {
		this.id = id;
		this.canHaveChildren = false;
		this.target = new JPanel( new GUIFactory.GridLayout( 1, 2 ) );
		
		this.left = new Panel( this.id + "_left" );
		this.left.parent = this;
		this.target.add( this.left.target );
		
		this.right = new Panel( this.id + "_right" );
		this.right.parent = this;
		this.target.add( this.right.target );
	}
	
	public Pair setLeftItemAlignment( GUIFactory.Alignment alignment ) { this.left.setAlignment( alignment ); return this; }
	public Pair setLeftItem( GUIComponent<?, ?> component ) {
		this.left.removeAll();
		component.addTo( this.left );

		return this.update();
	}
	
	public Pair setRightItemAlignment( GUIFactory.Alignment alignment ) { this.right.setAlignment( alignment ); return this; }
	public Pair setRightItem( GUIComponent<?, ?> component ) {
		this.right.removeAll();
		component.addTo( this.right );

		return this.update();
	}
	
	@Override protected Pair destroy() {
		this.left.destroy();
		this.right.destroy();
		
		super.destroy();
		
		return null;
	}
}
