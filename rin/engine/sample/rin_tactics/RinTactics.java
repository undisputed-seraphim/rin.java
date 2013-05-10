package rin.engine.sample.rin_tactics;

import rin.engine.Engine;
import rin.engine.game.Game;
import rin.engine.lib.gl.GLView;

public class RinTactics extends Game<GLView> {
	
	@Override
	public String getName() { return "Rin Tactics"; }

	@Override
	public GLView getViewInstance() { return new GLView(); }
	
	@Override
	public void init() {
		System.out.println( "initializing stuffs." );
		getView().setSize( 900, 600 );
	}
	
	@Override
	public void start() {
		System.out.println( "starting game." );
		while( !getView().isClosed() ) {
			getView().update();
		}
	}

	@Override
	public void destroy() {
		System.out.println( "destroying game." );
	}

	public static void main( String[] args ) {
		// pass a new game to the engine
		Engine.run( new RinTactics() );
	}
}
