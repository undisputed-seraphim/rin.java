package rin.engine.lib.gui;

import java.util.concurrent.Callable;

import javax.swing.JCheckBox;

import rin.engine.lib.gui.GUIFactory.GUIEvent;
import rin.engine.lib.gui.event.GUIChangeListener;

public class RCheckBox  extends RComponent<JCheckBox, RCheckBox> implements GUIChangeListener<RCheckBox>{

	public RCheckBox( String id, String text ) {
		super( id, new JCheckBox() );
		this.canHaveChildren = false;
		
		if( text != null )
			swing().setText( text );
	}
	
	@Override
	public JCheckBox swing() { return (JCheckBox)this.target; }
	
	@Override
	protected RCheckBox actual() { return this; }

	public boolean isChecked() { return swing().isSelected(); }
	public RCheckBox check() {
		final RCheckBox cb = this;
		return SwingDispatcher.invokeLaterAndWait( new Callable<RCheckBox>() {
			@Override public RCheckBox call() {
				if( !isChecked() ) swing().setSelected( true );
				return cb;
			}
		});
	}
	
	// CHANGE EVENTS
	
	private boolean isChangeListening = false;
	
	@Override
	public boolean isChangeListening() { return isChangeListening; }

	@Override
	public RCheckBox setChangeListening( boolean listen ) {
		if( !isChangeListening && listen ) {
			swing().addChangeListener( this );
			isChangeListening = true;
		} else if( isChangeListening && !listen ) {
			swing().removeChangeListener( this );
			isChangeListening = false;
		}
		return this;
	}

	@Override
	public RCheckBox onChange( GUIEvent<RCheckBox> e ) {
		setChangeListening( true );
		runOnChange = e.setTarget( this );
		return this;
	}
	
}
