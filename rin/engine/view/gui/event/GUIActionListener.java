package rin.engine.view.gui.event;

import rin.engine.view.gui.GUIFactory.GUIEvent;

public interface GUIActionListener<G> {

	public G setActionListening( boolean listen );
	
	public G onAction( GUIEvent<G> e );
	
}
