package rin.engine.lib.gui;

import javax.swing.JButton;

import rin.engine.meta.RinChainable;
import rin.engine.lib.gui.GUIFactory.GUIEvent;
import rin.engine.lib.gui.event.GUIActionListener;

public class RButton extends RComponent<JButton, RButton> implements GUIActionListener<RButton> {
	
	public RButton( String id, String text ) {
		super( id, new JButton() );
		this.canHaveChildren = false;
		
		if( text != null )
			this.setText( text );
	}
	
	@Override
	public JButton swing() { return (JButton)this.target; }
	
	@Override
	protected RButton actual() { return this; }
	
	public String getText() { return this.swing().getText(); }
	
	@RinChainable
	public RButton setText( String text ) {
		this.swing().setText( text );
		return this.update();
	}
	
	// ACTION EVENTS
	
	private boolean isActionListening = false;
	
	@Override
	public boolean isActionListening() { return this.isActionListening; }
	
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
