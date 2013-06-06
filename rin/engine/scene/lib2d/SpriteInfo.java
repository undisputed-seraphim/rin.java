package rin.engine.scene.lib2d;

public class SpriteInfo {

	private String id;
	private int numFrames;
	private int[] tOffsets;
	
	public SpriteInfo( String name, int frames, int ... offsets ) {
		id = name;
		numFrames = frames;
		tOffsets = offsets;
	}
	
}
