package rin.engine.view.gui.event;

import rin.engine.view.gui.GUIFactory.GUIEvent;

public interface GUIWindowFocusListener<G> {

	public boolean isWindowFocusListening();
	public G setWindowFocusListening( boolean listen );
	
	public G onWindowFocusGained( GUIEvent<G> e );
	public G onWindowFocusLost( GUIEvent<G> e );
	
}
