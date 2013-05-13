package rin.gl.lib3d;

import java.util.HashMap;

public class Animation {

	private String name;
	private float start = 0;
	private float end = 0;
	private double currentDt = 0;
	private HashMap<String, FrameSet> bones = new HashMap<String, FrameSet>();
	
	public Animation( String id ) {
		name = id;
	}
	
	public String getName() { return name; }
	
	public void setTimes( float s, float e ) {
		start = s;
		end = e;
	}
	
	public FrameSet getFrames( String bone ) {
		if( bones.get( bone ) == null )
			bones.put( bone, new FrameSet() );
		return bones.get( bone );
	}
	
	public FrameSet findFrames( String bone ) {
		return bones.get( bone );
	}
	
	public void update( double dt ) {
		currentDt += (dt);
		if( currentDt > end )
			currentDt = start + (currentDt-end);
	}
}
