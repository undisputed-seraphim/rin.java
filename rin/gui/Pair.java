package rin.gui;

import javax.swing.JPanel;

public class Pair extends GUIComponent<Pair> {
	private static int items = 0;
	
	private Panel left;
	private Panel right;
	
	public Pair() { this( "Pair-" + Pair.items++ ); }
	public Pair( String id ) {
		this.id = id;
		this.canHaveChildren = false;
		this.target = new JPanel( new GUIManager.GUIGridLayout( 1, 2 ) );
		
		this.left = new Panel( this.id + "_left" );
		this.target.add( this.left.target );
		
		this.right = new Panel( this.id + "_right" );
		this.target.add( this.right.target );
	}
	
	public Pair setLeftItemAlignment( GUIManager.Alignment alignment ) { this.left.setAlignment( alignment ); return this; }
	public Pair setLeftItem( GUIComponent<?> component ) {
		this.left.removeAll();
		this.left.add( component );
		component.show();

		return this.update();
	}
	
	public Pair setRightItemAlignment( GUIManager.Alignment alignment ) { this.right.setAlignment( alignment ); return this; }
	public Pair setRightItem( GUIComponent<?> component ) {
		this.right.removeAll();
		this.right.add( component );
		component.show();

		return this.update();
	}
	
	@Override public Pair destroy() {
		super.destroy();
		
		this.left.destroy();
		this.right.destroy();
		
		return null;
	}
}
