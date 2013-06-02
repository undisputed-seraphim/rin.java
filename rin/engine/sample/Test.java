package rin.engine.sample;

import rin.engine.game.GLGame3D;
import rin.engine.resource.ResourceManager;

public class Test {
	public static void main( String[] args ) {
		GLGame3D game = new GLGame3D() {
			@Override public void run() {
				init();
				
				getView().getScene().addModel( ResourceManager.getPackResource( "neptunia_mk2", "models", "player", "001", "002.ism2" ) );
				
				start();
				destroy();
			}
		};
		game.run();
	}
}
