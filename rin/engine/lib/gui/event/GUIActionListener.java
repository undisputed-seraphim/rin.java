package rin.engine.lib.gui.event;

import rin.engine.lib.gui.GUIFactory.GUIEvent;

public interface GUIActionListener<G> {

	public boolean isActionListening();
	public G setActionListening( boolean listen );
	
	public G onAction( GUIEvent<G> e );
	
}
