package rin.engine.lib.gui.event;

import rin.engine.lib.gui.GUIFactory.GUIEvent;

public interface GUIWindowListener<G> {
	
	public boolean isWindowListening();
	public G setWindowListening( boolean listen );
	
	public G onWindowClosing( GUIEvent<G> e );

}
