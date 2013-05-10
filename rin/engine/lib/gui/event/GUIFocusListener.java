package rin.engine.lib.gui.event;

import rin.engine.lib.gui.GUIFactory.GUIEvent;

public interface GUIFocusListener<G> {

	public boolean isFocusListening();
	public G setFocusListening( boolean listen );
	
	public G onFocusGained( GUIEvent<G> e );
	public G onFocusLost( GUIEvent<G> e );
	
}
