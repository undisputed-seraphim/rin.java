package rin.gui;

import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JPanel;

import rin.gui.GUIManager.Alignment;

public class Columns extends GUIComponent<Columns> {
	private static int items = 0;
	
	private static final int DEFAULT_SIZE = GroupLayout.DEFAULT_SIZE;
	private static final int PREFERRED_SIZE = GroupLayout.PREFERRED_SIZE;
	
	private int cols = 0;
	private SequentialGroup sgroup;
	ArrayList<ParallelGroup> pgroups = new ArrayList<ParallelGroup>();
	private ParallelGroup pgroup;
	ArrayList<SequentialGroup> sgroups = new ArrayList<SequentialGroup>();
	
	public Columns( int cols ) { this( "Columns-" + Columns.items++, cols, Alignment.LEFT, Alignment.CENTER ); }
	public Columns( int cols, Alignment halign ) { this( "Columns-" + Columns.items++, cols, halign, Alignment.CENTER ); }
	public Columns( int cols, Alignment halign, Alignment valign ) { this( "Columns-" + Columns.items++, cols, halign, valign ); }
	public Columns( String id, int cols ) { this( "Columns-" + Columns.items++, cols, Alignment.LEFT, Alignment.CENTER ); }
	public Columns( String id, int cols, Alignment halign ) { this( "Columns-" + Columns.items++, cols, halign, Alignment.CENTER ); }
	public Columns( String id, int cols, Alignment halign, Alignment valign ) {
		this.id = id;
		this.cols = cols;
		this.target = new JPanel();
		
		GroupLayout layout = new GUIManager.GUIGroupLayout( this.target );
		this.pgroup = layout.createParallelGroup( this.getAlignmentConstant( valign ) );
		this.sgroup = layout.createSequentialGroup();
		layout.setHorizontalGroup( this.sgroup );
		layout.setVerticalGroup( this.pgroup );

		for( int i = 0; i < cols; i++ ) {
			this.sgroups.add( layout.createSequentialGroup() );
			this.pgroups.add( layout.createParallelGroup( this.getAlignmentConstant( halign ) ) );
			this.pgroup.addGroup( this.sgroups.get( this.sgroups.size() - 1 ) );
			this.sgroup.addGroup( this.pgroups.get( this.pgroups.size() - 1 ) );
		}
		
		this.target.setLayout( layout );
	}
	
	private GroupLayout.Alignment getAlignmentConstant(Alignment alignment ) {
		switch( alignment ) {
		
		case LEFT: return GroupLayout.Alignment.LEADING;
		case CENTER: return GroupLayout.Alignment.CENTER;
		case RIGHT: return GroupLayout.Alignment.TRAILING;
		
		}
		return GroupLayout.Alignment.LEADING;
	}
	
	public Columns add( GUIComponent<?> component ) {
		if( this.cols > 0 )
			this.add( 1, component );
		return this;
	}
	
	public Columns add( int column, GUIComponent<?> component ) {
		if( column <= this.cols && column > 0 ) {
			this.sgroups.get( column - 1 ).addComponent( component.target, PREFERRED_SIZE, DEFAULT_SIZE, DEFAULT_SIZE );
			this.pgroups.get( column - 1 ).addComponent( component.target, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE );
			this.children.add( component );
			component.show();
			this.target.validate();
			this.target.repaint();
		}
		return this;
	}

	@Override public Columns destroy() {
		super.destroy();
		
		this.pgroup = null;
		this.pgroups.clear();
		this.sgroup = null;
		this.sgroups.clear();
		
		return null;
	}
}
