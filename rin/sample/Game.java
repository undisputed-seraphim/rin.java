package rin.sample;

import rin.engine.Engine;
import rin.gl.lib3d.shape.Grid;
import rin.util.math.Vec3;

public class Game {
	private Engine rin;
	
	public Game() {
		this.rin = new Engine();
		/* setup all the engine options here, e.g. load data into scene, characters, etc */
		this.rin.addCharacter( "noire_v" );
		this.rin.addCharacter( "purplesister_mk2" );
		this.rin.addCharacter( "blackheart_v" );
		this.rin.addComplexShape( new Grid( 10, 10, 0.5f,new Vec3( 0.0f, 0.0f, 0.0f ), Grid.Y_AXIS, 0.1f ) );
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