package rin.engine.lib.gui.event;

import rin.engine.lib.gui.GUIFactory.GUIEvent;

public interface GUIChangeListener<G> {
	
	public boolean isChangeListening();
	public G setChangeListening( boolean listen );
	
	public G onChange( GUIEvent<G> e );
	
}
