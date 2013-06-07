package rin.engine.sample.metroid.sprites;

import rin.engine.resource.ResourceManager;
import rin.engine.scene.lib2d.SpriteMap;

public class Samus {

	public static SpriteMap SPRITE_MAP = new SpriteMap() {
		@Override public void define() {
			setImage( ResourceManager.getPackResource( "super_metroid", "sprites", "player", "samus", "power", "power_suit.png" ) );
			setMapSize( 78, 43 );
			setSpriteSize( 26, 43 );
			
			addDefaults( this );
		}
	};
	
	private static void addDefaults( SpriteMap sm ) {
		sm.addSprite( "right_idle", 3, 0, 0, 1, 0, 2, 0 );
	}
	
}
