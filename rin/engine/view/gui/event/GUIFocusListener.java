package rin.engine.view.gui.event;

import rin.engine.view.gui.GUIFactory.GUIEvent;

public interface GUIFocusListener<G> {

	public G setFocusListening( boolean listen );
	
	public G onFocusGained( GUIEvent<G> e );
	public G onFocusLost( GUIEvent<G> e );
	
}
