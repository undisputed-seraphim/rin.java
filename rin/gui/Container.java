package rin.gui;

import javax.swing.JPanel;

import rin.gui.GUIFactory.ContainerEvent;

public class Container extends GUIComponent<Container, ContainerEvent> {
	private static int items = 0;

	private static final int DEFAULT_SIZE = GUIFactory.GroupLayout.DEFAULT_SIZE;
	private static final int PREFERRED_SIZE = GUIFactory.GroupLayout.PREFERRED_SIZE;
	
	private GUIFactory.GroupLayout.ParallelGroup pgroupH;
	private GUIFactory.GroupLayout.SequentialGroup pgroupV;
	
	private GUIFactory.Alignment halign = GUIFactory.Alignment.CENTER;
	
	public Container() { this( "Container-" + Container.items++ ); }
	public Container( String id ) {
		this.id = id;
		this.target = new JPanel();
		
		GUIFactory.GroupLayout layout = new GUIFactory.GroupLayout( this.target );
		this.pgroupH = layout.createParallelGroup( this.getGroupAlignment( this.halign ) );
		this.pgroupV = layout.createSequentialGroup();
		layout.setHorizontalGroup( this.pgroupH );
		layout.setVerticalGroup( this.pgroupV );
		this.target.setLayout( layout );
		this.onWindowLoad( new GUIFactory.OnLoadEvent() {
			public void run() {
				((JPanel)this.target).addFocusListener( this.component.toContainer() );
			}
		}.setTargets( this.target, this ) );
	}
	
	@Override public Container add( GUIComponent<?, ?> component ) {
		this.pgroupH.addComponent( component.target, PREFERRED_SIZE, DEFAULT_SIZE, DEFAULT_SIZE );
		this.pgroupV.addComponent( component.target, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE );
		
		component.parent = this;
		this.children.add( component );
		component.show();
		
		return this.update();
	}
	
	public Container setAlignment( GUIFactory.Alignment alignment ) {
		this.halign = alignment;
		//TODO: items removed from a container would not be removed from their respective groups...? add realign > update
		GUIFactory.GroupLayout layout = (GUIFactory.GroupLayout)this.target.getLayout();
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
	
	public Container onBlur( ContainerEvent e ) {
		this.runOnFocusLost = e.<ContainerEvent>setTarget( this );
		return this;
	}
	
	@Override protected Container destroy() {
		this.target.removeFocusListener( this );
		super.destroy();
		
		this.pgroupH = null;
		this.pgroupV = null;
		this.halign = null;
		
		return null;
	}
}
