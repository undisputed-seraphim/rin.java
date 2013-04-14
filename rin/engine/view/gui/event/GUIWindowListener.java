package rin.engine.view.gui.event;

import rin.engine.view.gui.GUIFactory.GUIEvent;

public interface GUIWindowListener<G> {
	
	public boolean isWindowListening();
	public G setWindowListening( boolean listen );
	
	public G onWindowClosing( GUIEvent<G> e );

}
