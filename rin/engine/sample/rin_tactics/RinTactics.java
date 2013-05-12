package rin.engine.sample.rin_tactics;

import rin.engine.Engine;
import rin.engine.game.Game;
import rin.engine.resource.ResourceManager;

public class RinTactics extends Game {
	@Override
	public void init() {
		System.out.println( "initializing stuffs." );
		getView().setSize( 900, 600 );
		getView().setTitle( "Rin Tactics" );
		getScene().addModel( ResourceManager.getPackResource( "neptunia_v", "models", "001", "002.ism2" ) );
	}
	
	@Override
	public void start() {
		System.out.println( "starting game." );
		while( !getView().isClosed() ) {
			getView().update();
			getScene().update();
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
