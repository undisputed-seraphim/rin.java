package rin.engine.core.game;

import rin.engine.core.scene.Scene;
import rin.engine.core.view.View;

public class BaseGame implements Game {

	protected View view;
	protected Scene scene;
	
	@Override
	public View getView() { return view; }

	@Override
	public void setView(View v) {
	}

	@Override
	public Scene getScene() { return scene; }

	@Override
	public void setScene(Scene s) {
	}

	@Override
	public void init() {}

	@Override
	public void start() {
	}
	
	@Override
	public void destroy() {}

	@Override
	public void run() {
		init();
		start();
		destroy();
	}

}
