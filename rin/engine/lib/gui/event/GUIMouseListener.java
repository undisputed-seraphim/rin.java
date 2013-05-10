package rin.engine.lib.gui.event;

import rin.engine.lib.gui.GUIFactory.GUIEvent;

public interface GUIMouseListener<G> {

	public boolean isMouseListening();
	public G setMouseListening( boolean listen );
	
	public G onClick( GUIEvent<G> e );
	public G onMouseIn( GUIEvent<G> e );
	public G onMouseOut( GUIEvent<G> e );
	public G onMouseUp( GUIEvent<G> e );
	public G onMouseDown( GUIEvent<G> e );
	
}
