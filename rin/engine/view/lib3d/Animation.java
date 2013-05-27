package rin.engine.view.lib3d;

import java.util.HashMap;

public class Animation {
	private String name;
	
	protected float start = 0.0f;
	protected float end = 0.0f;
	private double animDt = 0.0;
	
	private HashMap<String, Frame> frames = new HashMap<String, Frame>();
	
	public Animation( String id ) { name = id; }
	public String getName() { return name; }
	
	protected void updateTimes( float s, float e ) {
		start = start > s ? s : start;
		end = end > e ? end : e;
	}
	
	public Frame getFrame( String id ) { return frames.get( id ); }
	public Frame addFrame( JointNode jn ) {
		Frame res = new Frame( jn );
		res.parent = this;
		frames.put( jn.getId(), res );
		return res;
	}
	
	private void updateDt( double dt ) {
		animDt += dt;
		if( animDt > end )
			animDt = animDt - end;
	}
	
	public void update( double dt ) {
		updateDt( dt );
		for( Frame f : frames.values() )
			f.update( animDt );
	}
}
