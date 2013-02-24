package rin.gui;

import java.util.ArrayList;
import javax.swing.JPanel;

import rin.gui.GUIFactory.ColumnsEvent;

public class Columns extends GUIComponent<Columns, ColumnsEvent> {
	private static int items = 0;
	
	private static final int DEFAULT_SIZE = GUIFactory.GroupLayout.DEFAULT_SIZE;
	private static final int PREFERRED_SIZE = GUIFactory.GroupLayout.PREFERRED_SIZE;
	private static final GUIFactory.Alignment defaultH = GUIFactory.Alignment.LEFT;
	private static final GUIFactory.Alignment defaultV = GUIFactory.Alignment.CENTER;
	
	private int cols = 0;
	private boolean padding = true;
	private GUIFactory.Alignment[] halign;
	private GUIFactory.Alignment valign;
	
	private ArrayList< ArrayList<GUIComponent<?, ?>>> children;
	private GUIFactory.GroupLayout.SequentialGroup hgroup;
	private GUIFactory.GroupLayout.SequentialGroup vgroup;
	
	public Columns( int cols ) { this( "Columns-" + Columns.items++, cols ); }
	public Columns( String id, int cols ) {
		this.id = id;
		this.cols = cols;
		this.target = new JPanel();
		this.target.setLayout( new GUIFactory.GroupLayout( this.target ) );
		this.halign = new GUIFactory.Alignment[cols];
		this.valign = Columns.defaultV;
		this.children = new ArrayList< ArrayList<GUIComponent<?, ?>>>();

		for( int i = 0; i < cols; i++ ) {
			this.children.add( new ArrayList<GUIComponent<?, ?>>() );
			this.halign[i] = Columns.defaultH;
		}
	}
	
	public Columns setPadding( boolean pad ) { this.padding = pad; return this.realign(); }
	
	public Columns setAlignmentX( int col, GUIFactory.Alignment alignment ) { this.halign[col-1] = alignment; return this.realign(); }
	public Columns setAlignmentX( GUIFactory.Alignment alignment ) {
		for( int i = 0; i < cols; i++ )
			this.halign[i] = alignment;
		
		return this.realign();
	}
	
	public Columns setAlignmentY( GUIFactory.Alignment alignment ) {
		this.valign = alignment;
		
		return this.realign();
	}
	
	public Columns realign() {
		GUIFactory.GroupLayout layout = (GUIFactory.GroupLayout)this.target.getLayout();
		this.hgroup = layout.createSequentialGroup();
		this.vgroup = layout.createSequentialGroup();
		if( this.padding ) {
			layout.setAutoCreateContainerGaps( true );
			layout.setAutoCreateGaps( true );
		}

		int size = this.children.size() > 0 ? this.children.get( 0 ).size() : 0;
		for( int i = 0; i < cols; i++ )
			size = Math.max( size, this.children.get( i ).size() );
		
		GUIFactory.GroupLayout.ParallelGroup htmp;
		for( int i = 0; i < this.cols; i++ ) {
			htmp = layout.createParallelGroup( this.getGroupAlignment( this.halign[i] ) );
			for( int j = 0; j < size; j++ )
				if( j < this.children.get( i ).size() )
					htmp.addComponent( this.children.get( i ).get( j ).target, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE );
			
			this.hgroup.addGroup( htmp );
		}
		
		GUIFactory.GroupLayout.ParallelGroup vtmp;
		for( int i = 0; i < size; i++ ) {
			vtmp = layout.createParallelGroup( this.getGroupAlignment( this.valign ) );
			for( int j = 0; j < this.cols; j++ )
				if( i < this.children.get( j ).size() )
					vtmp.addComponent( this.children.get( j ).get( i ).target, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE );
			
			this.vgroup.addGroup( vtmp );
		}
		
		layout.setHorizontalGroup( this.hgroup);
		layout.setVerticalGroup( this.vgroup );
		this.target.setLayout( layout );
		
		return this.update();
	}
	
	@Override public Columns add( GUIComponent<?, ?> component ) {
		if( this.cols > 0 )
			return this.add( 1, component );
		return this;
	}
	
	public Columns add( int column, GUIComponent<?, ?> component ) {
		if( column <= this.cols && column > 0 ) {
			this.children.get( column - 1 ).add( component );
			component.show();
		}
		return this.realign();
	}

	@Override public Columns removeAll() {
		for( ArrayList<GUIComponent<?, ?>> childs : this.children )
			for( GUIComponent<?, ?> g : childs )
				g = g.destroy();
		
		this.children.clear();
		return this;
	}
	
	@Override public Columns destroy() {
		super.destroy();
		this.removeAll();
		
		this.hgroup = null;
		this.vgroup = null;
		
		return null;
	}
}
