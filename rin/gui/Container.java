package rin.gui;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JPanel;

public class Container extends GUIComponent<Container>{
	private static int items = 0;

	private ParallelGroup pgroupH;
	private SequentialGroup pgroupV;
	private Runnable runOnFocus = null;
	
	public Container() { this( "Container-" + Container.items++, GUIManager.Alignment.LEFT ); }
	public Container( String id, GUIManager.Alignment alignment ) {
		this.id = id;
		this.target = new JPanel();
		
		GroupLayout layout = new GUIManager.GUIGroupLayout( this.target );
		this.pgroupH = layout.createParallelGroup( this.getAlignmentConstant( alignment ) );
		this.pgroupV = layout.createSequentialGroup();
		layout.setHorizontalGroup( layout.createParallelGroup( this.getAlignmentConstant( alignment ) )
				.addGroup( this.pgroupH ) );
		layout.setVerticalGroup( layout.createSequentialGroup()
				.addGroup( this.pgroupV ) );
		
		this.target.setLayout( layout );
	}
	
	private GroupLayout.Alignment getAlignmentConstant( GUIManager.Alignment alignment ) {
		switch( alignment ) {
		
		case LEFT: return GroupLayout.Alignment.LEADING;
		case CENTER: return GroupLayout.Alignment.CENTER;
		case RIGHT: return GroupLayout.Alignment.TRAILING;
		
		}
		return GroupLayout.Alignment.LEADING;
	}
	
	@Override public Container add( GUIComponent<?> component ) {
		pgroupH.addComponent( component.target, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE );
		pgroupV.addComponent( component.target, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE );
		
		this.children.add( component );
		component.show();
		return this.update();
	}

	public Container onFocus( Runnable r ) {
		this.runOnFocus = r;
		return this;
	}
	
	@Override public void focused() {
		if( runOnFocus != null )
			runOnFocus.run();
	}
	
	@Override public Container destroy() {
		super.destroy();
		
		this.pgroupH = null;
		this.pgroupV = null;
		this.runOnFocus = null;
		
		return null;
	}
}
