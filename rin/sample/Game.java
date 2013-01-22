package rin.sample;

import rin.engine.Engine;

public class Game {
	private Engine rin;
	
	public Game() {
		this.rin = new Engine();
		this.rin.debug();
		
		/* setup all the engine options here, e.g. load data into scene, characters, etc */
		this.rin.addCharacter( "purplesister_mk2" );
		//this.rin.addCharacter( "blackheart_v" );
	}
	
	/* run the engine */
	public void start() { this.rin.run(); }
	
	public void menu() {
		/* go to the main menu */
		/*try {
			Method test = Game.class.getMethod( "start", String.class );
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}*/
	}

	/* where it all begins... */
	public static void main( String[] args ) {
		Game game = new Game();
		game.start();
		
		/* do things after the game has closed */
	}
}