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
	
	public int getFrameCount() { return numFrames; }
	public int[] getOffsets() { return tOffsets; }
	
	public int getOffsetX( int frame ) { return tOffsets[ frame * 2 ]; }
	public int getOffsetY( int frame ) { return tOffsets[ frame * 2 + 1 ]; }
}
