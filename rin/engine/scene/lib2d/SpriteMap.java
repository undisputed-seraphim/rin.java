package rin.engine.scene.lib2d;

import java.util.HashMap;

import rin.engine.resource.Resource;

public class SpriteMap {

	protected Resource image;
	protected HashMap<String, SpriteInfo> map = new HashMap<String, SpriteInfo>();
	private int swidth = 0;
	private int sheight = 0;
	private int mapw = 0;
	private int maph = 0;
	
	public SpriteMap() { define(); }
	public void define() {}
	
	public void setImage( Resource res ) { image = res; }
	public Resource getImage() { return image; }
	
	public int getSpriteWidth() { return swidth; }
	public int getSpriteHeight() { return sheight; }
	public void setSpriteSize( int width, int height ) {
		swidth = width;
		sheight = height;
	}
	
	public int getMapWidth() { return mapw; }
	public int getMapHeight() { return maph; }
	public void setMapSize( int w, int h ) {
		mapw = w;
		maph = h;
	}
	
	public SpriteInfo getSpriteInfo( String id ) {
		return map.get( id );
	}
	
	public SpriteInfo addSprite( String id, int frames, int ... offsets ) {
		SpriteInfo info = new SpriteInfo( id, frames, offsets );
		map.put( id, info );
		return info;
	}
	
}
