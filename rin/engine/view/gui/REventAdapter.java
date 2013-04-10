package rin.engine.view.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import rin.engine.view.gui.GUIFactory.GUIEvent;

public class REventAdapter<G> implements MouseListener {
	
	protected GUIEvent<G> runOnClick;
	protected GUIEvent<G> runOnMouseIn;
	protected GUIEvent<G> runOnMouseOut;
	protected GUIEvent<G> runOnMouseUp;
	protected GUIEvent<G> runOnMouseDown;
	
	@Override
	public void mouseClicked( MouseEvent e ) {
		if( this.runOnClick != null )
			this.runOnClick.run();
	}
	
	@Override
	public void mouseEntered( MouseEvent e ) {
		if( this.runOnMouseIn != null )
			this.runOnMouseIn.run();
	}
	
	@Override
	public void mouseExited( MouseEvent e ) {
		if( this.runOnMouseOut != null )
			this.runOnMouseOut.run();
	}
	
	@Override
	public void mouseReleased( MouseEvent e ) {
		if( this.runOnMouseUp != null )
			this.runOnMouseUp.run();
	}
	
	@Override
	public void mousePressed( MouseEvent e ) {
		if( this.runOnMouseDown != null )
			this.runOnMouseDown.run();
	}
}
