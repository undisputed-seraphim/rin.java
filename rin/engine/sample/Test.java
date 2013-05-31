package rin.engine.sample;

import rin.engine.game.GLGame3D;

public class Test {
	public static void main( String[] args ) {
		GLGame3D game = new GLGame3D();
		game.init();
		game.start();
		System.out.println( "Test#main(String): out of game.start()" );
		game.destroy();
	}
}
