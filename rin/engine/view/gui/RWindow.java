package rin.engine.view.gui;

import javax.swing.JFrame;

import rin.engine.meta.RinChainable;

public class RWindow extends RComponent<JFrame, RWindow> {
	
	public RWindow( String id ) {
		super( id, new JFrame() );
		this.swing().setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		this.swing().addMouseListener( this );
	}
	
	@Override
	public JFrame swing() { return (JFrame)this.target; }
	
	@Override
	protected RWindow actual() { return this; }
	
	public RWindow setTitle( String title ) {
		this.swing().setTitle( title );
		return this.update();
	}
	
	@Override
	@RinChainable
	public RWindow update() {
		super.update();
		this.swing().pack();
		
		return this;
	}
	
	@Override
	@RinChainable
	public RWindow add( RComponent<?, ?> component ) {		
		// GUIMenuBars must be added in a special way to GUIWindows
		if( component instanceof RMenuBar ) {
			this.swing().setJMenuBar( ((RMenuBar)component).swing() );
			this.children.put( component.id, component );
			return this.update();
		}
		
		return super.add( component );
	}
	
}
