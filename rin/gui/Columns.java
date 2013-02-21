package rin.gui;

import java.util.ArrayList;
import javax.swing.JPanel;

import rin.gui.GUIManager.ColumnsEvent;

public class Columns extends GUIComponent<Columns, ColumnsEvent> {
	private static int items = 0;
	
	private static final int DEFAULT_SIZE = GUIManager.GUIGroupLayout.DEFAULT_SIZE;
	private static final int PREFERRED_SIZE = GUIManager.GUIGroupLayout.PREFERRED_SIZE;
	private static final GUIManager.Alignment defaultH = GUIManager.Alignment.LEFT;
	private static final GUIManager.Alignment defaultV = GUIManager.Alignment.CENTER;
	
	private int cols = 0;
	private boolean padding = true;
	private GUIManager.Alignment[] halign;
	private GUIManager.Alignment valign;
	
	private ArrayList< ArrayList<GUIComponent<?, ?>>> children;
	private GUIManager.GUIGroupLayout.SequentialGroup hgroup;
	private GUIManager.GUIGroupLayout.SequentialGroup vgroup;
	
	public Columns( int cols ) { this( "Columns-" + Columns.items++, cols ); }
	public Columns( String id, int cols ) {
		this.id = id;
		this.cols = cols;
		this.target = new JPanel();
		this.target.setLayout( new GUIManager.GUIGroupLayout( this.target ) );
		this.halign = new GUIManager.Alignment[cols];
		this.valign = Columns.defaultV;
		this.children = new ArrayList< ArrayList<GUIComponent<?>>>();

		for( int i = 0; i < cols; i++ ) {
			this.children.add( new ArrayList<GUIComponent<?>>() );
			this.halign[i] = Columns.defaultH;
		}
	}
	
	public Columns setPadding( boolean pad ) { this.padding = pad; return this.realign(); }
	
	public Columns setAlignmentX( int col, GUIManager.Alignment alignment ) { this.halign[col-1] = alignment; return this.realign(); }
	public Columns setAlignmentX( GUIManager.Alignment alignment ) {
		for( int i = 0; i < cols; i++ )
			this.halign[i] = alignment;
		
		return this.realign();
	}
	
	public Columns setAlignmentY( GUIManager.Alignment alignment ) {
		this.valign = alignment;
		
		return this.realign();
	}
	
	public Columns realign() {
		GUIManager.GUIGroupLayout layout = (GUIManager.GUIGroupLayout)this.target.getLayout();
		this.hgroup = layout.createSequentialGroup();
		this.vgroup = layout.createSequentialGroup();
		if( this.padding ) {
			layout.setAutoCreateContainerGaps( true );
			layout.setAutoCreateGaps( true );
		}

		int size = this.children.size() > 0 ? this.children.get( 0 ).size() : 0;
		for( int i = 0; i < cols; i++ )
			size = Math.max( size, this.children.get( i ).size() );
		
		GUIManager.GUIGroupLayout.ParallelGroup htmp;
		for( int i = 0; i < this.cols; i++ ) {
			htmp = layout.createParallelGroup( this.getGroupAlignment( this.halign[i] ) );
			for( int j = 0; j < size; j++ )
				if( j < this.children.get( i ).size() )
					htmp.addComponent( this.children.get( i ).get( j ).target, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE );
			
			this.hgroup.addGroup( htmp );
		}
		
		GUIManager.GUIGroupLayout.ParallelGroup vtmp;
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
	
	@Override public Columns add( GUIComponent<?> component ) {
		if( this.cols > 0 )
			return this.add( 1, component );
		return this;
	}
	
	public Columns add( int column, GUIComponent<?> component ) {
		if( column <= this.cols && column > 0 ) {
			this.children.get( column - 1 ).add( component );
			component.show();
		}
		return this.realign();
	}

	@Override public Columns removeAll() {
		for( ArrayList<GUIComponent<?>> childs : this.children )
			for( GUIComponent<?> g : childs )
				if( g.target != null )
					g = g.destroy();
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
