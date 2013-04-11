package rin.engine.view.gui;

import javax.swing.JButton;

import rin.engine.meta.RinChainable;
import rin.engine.view.gui.GUIFactory.GUIEvent;
import rin.engine.view.gui.event.GUIActionListener;

public class RButton extends RComponent<JButton, RButton> implements GUIActionListener<RButton> {
	
	public RButton( String id, String text ) {
		super( id, new JButton( text ) );
		this.canHaveChildren = false;
	}
	
	@Override
	public JButton swing() { return (JButton)this.target; }
	
	@Override
	protected RButton actual() { return this; }
	
	// ACTION EVENTS
	
	private boolean isActionListening = false;
	
	@Override
	@RinChainable
	public RButton setActionListening( boolean listen ) {
		if( !this.isActionListening && listen ) {
			this.swing().addActionListener( this );
			this.isActionListening = true;
		} else if( this.isActionListening && !listen ) {
			this.swing().removeActionListener( this );
			this.isActionListening = false;
		}
		
		return this;
	}

	@Override
	@RinChainable
	public RButton onAction( GUIEvent<RButton> e ) {
		this.setActionListening( true );
		this.runOnAction = e.setTarget( this );
		return this;
	}

}
