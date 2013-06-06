package rin.engine.sample;

import rin.engine.game.GLGame2D;
import rin.engine.game.GLGame3D;
import rin.engine.resource.ResourceManager;

public class Test {
	public static void main( String[] args ) {
		/*GLGame3D game = new GLGame3D() {
			@Override public void run() {
				init();
				
				getScene().addModel( ResourceManager.getPackResource( "neptunia_mk2", "models", "player", "006", "002.ism2" ) );
				
				start();
				destroy();
			}
		};
		game.run();*/
		GLGame2D metroid = new GLGame2D() {
			public void run() {
				init();
				
				getScene().addSprite( 1, ResourceManager.getPackResource( "super_metroid", "sprites", "player", "samus", "power", "right_3.png" ) );
				start();
				destroy();
			}
		};
		metroid.run();
	}
}
