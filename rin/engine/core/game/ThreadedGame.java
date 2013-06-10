package rin.engine.core.game;

import rin.engine.core.scene.ThreadedScene;

public interface ThreadedGame extends Game {

	@Override public ThreadedScene getScene();
	
}
