package rin.engine.view.gui.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;

import rin.engine.view.gui.RContextMenu;
import rin.engine.view.gui.GUIFactory.GUIEvent;

public class GUIEventAdapter<G> implements MouseListener, ActionListener, WindowFocusListener, FocusListener,
		WindowListener {
	
	// WINDOW FOCUS
	
	protected GUIEvent<G> runOnWindowFocusGained;
	protected GUIEvent<G> runOnWindowFocusLost;
	
	@Override
	public void windowGainedFocus( WindowEvent e ) {
		if( this.runOnWindowFocusGained != null )
			this.runOnWindowFocusGained.run();
	}

	@Override
	public void windowLostFocus( WindowEvent e ) {
		if( this.runOnWindowFocusLost != null )
			this.runOnWindowFocusLost.run();
	}
	
	// WINDOW
	
	protected GUIEvent<G> runOnWindowClosing;
	
	@Override
	public void windowActivated( WindowEvent e ) {}

	@Override
	public void windowClosed( WindowEvent e ) {}

	@Override
	public void windowClosing( WindowEvent e ) {
		if( this.runOnWindowClosing != null )
			this.runOnWindowClosing.run();
	}

	@Override
	public void windowDeactivated( WindowEvent e ) {}

	@Override
	public void windowDeiconified( WindowEvent e ) {}

	@Override
	public void windowIconified( WindowEvent e ) {}

	@Override
	public void windowOpened( WindowEvent e ) {}
	
	// FOCUS
	
	protected GUIEvent<G> runOnFocusGained;
	protected GUIEvent<G> runOnFocusLost;
	
	@Override
	public void focusGained( FocusEvent e ) {
		if( this.runOnFocusGained != null )
			this.runOnFocusGained.run();
	}

	@Override
	public void focusLost( FocusEvent e ) {
		if( this.runOnFocusLost != null )
			this.runOnFocusLost.run();
	}
	
	// MOUSE
	
	protected GUIEvent<G> runOnClick;
	protected GUIEvent<G> runOnMouseIn;
	protected GUIEvent<G> runOnMouseOut;
	protected GUIEvent<G> runOnMouseUp;
	protected GUIEvent<G> runOnMouseDown;
	protected RContextMenu contextMenu;
	
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

	// ACTION
	
	protected GUIEvent<G> runOnAction;
	
	@Override
	public void actionPerformed( ActionEvent e ) {
		if( this.runOnAction != null )
			this.runOnAction.run();
	}
	
}
