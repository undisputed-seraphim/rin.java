package rin.gui;

import javax.swing.JPanel;

import rin.gui.GUIManager.ContainerEvent;

public class Container extends GUIComponent<Container, ContainerEvent> {
	private static int items = 0;

	private static final int DEFAULT_SIZE = GUIManager.GUIGroupLayout.DEFAULT_SIZE;
	private static final int PREFERRED_SIZE = GUIManager.GUIGroupLayout.PREFERRED_SIZE;
	
	private GUIManager.GUIGroupLayout.ParallelGroup pgroupH;
	private GUIManager.GUIGroupLayout.SequentialGroup pgroupV;
	
	private GUIManager.Alignment halign = GUIManager.Alignment.CENTER;
	
	public Container() { this( "Container-" + Container.items++ ); }
	public Container( String id ) {
		this.id = id;
		this.target = new JPanel();
		
		GUIManager.GUIGroupLayout layout = new GUIManager.GUIGroupLayout( this.target );
		this.pgroupH = layout.createParallelGroup( this.getGroupAlignment( this.halign ) );
		this.pgroupV = layout.createSequentialGroup();
		this.target.addFocusListener( this );
		
		layout.setHorizontalGroup( this.pgroupH );
		layout.setVerticalGroup( this.pgroupV );
		this.target.setLayout( layout );
	}
	
	@Override public Container add( GUIComponent<?, ?> component ) {
		this.pgroupH.addComponent( component.target, PREFERRED_SIZE, DEFAULT_SIZE, DEFAULT_SIZE );
		this.pgroupV.addComponent( component.target, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE );
		
		this.children.add( component );
		component.show();
		
		return this.update();
	}
	
	public Container setAlignment( GUIManager.Alignment alignment ) {
		this.halign = alignment;
		
		GUIManager.GUIGroupLayout layout = (GUIManager.GUIGroupLayout)this.target.getLayout();
		this.pgroupH = layout.createParallelGroup( this.getGroupAlignment( this.halign ) );
		this.pgroupV = layout.createSequentialGroup();
		
		for( GUIComponent<?, ?> g : this.children ) {
			this.pgroupH.addComponent( g.target, PREFERRED_SIZE, DEFAULT_SIZE, DEFAULT_SIZE );
			this.pgroupV.addComponent( g.target, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE );
		}
		
		layout.setHorizontalGroup( this.pgroupH );
		layout.setVerticalGroup( this.pgroupV );
		this.target.setLayout( layout );
		
		return this.update();
	}
	
	public Container onFocus( ContainerEvent e ) {
		this.runOnFocusGained = e.<ContainerEvent>setTarget( this );
		return this;
	}
	
	@Override public Container destroy() {
		super.destroy();
		
		this.pgroupH = null;
		this.pgroupV = null;
		
		return null;
	}
}
