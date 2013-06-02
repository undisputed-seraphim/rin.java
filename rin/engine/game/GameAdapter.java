package rin.engine.game;

import rin.engine.view.View;

public class GameAdapter implements Game {

	protected View view;
	
	@Override public View getView() { return view; }
	public void setView( View v ) {
		if( view != null )
			view.destroy();
		view = v;
		view.init();
	}

	@Override public void init() {}
	@Override public void start() {}
	@Override public void stop() {}
	@Override public void destroy() {}

	@Override
	public void run() {}
	
}
