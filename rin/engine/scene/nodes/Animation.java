package rin.engine.scene.nodes;

import java.util.HashMap;

public class Animation {
	private String name;
	
	protected float start = 0.0f;
	protected float end = 0.0f;
	private double animDt = 0.0;
	
	private HashMap<String, Frame> frames = new HashMap<String, Frame>();
	
	public Animation( String id ) { name = id; }
	public String getName() { return name; }
	
	public void setTimes( float s, float e ) {
		start = s;
		end = e;
		animDt = start;
	}
	
	public Frame getFrame( String id ) { return frames.get( id ); }
	public Frame addFrame( JointNode jn ) {
		Frame res = new Frame( jn );
		res.parent = this;
		frames.put( jn.getId(), res );
		return res;
	}
	
	public Animation restart() {
		animDt = start;
		return this;
	}
	
	private void updateDt( double dt ) {
		animDt += dt;
		if( animDt > end )
			animDt = (animDt - end) + start;
	}
	
	public void update( double dt ) {
		updateDt( dt );
		for( Frame f : frames.values() )
			f.update( animDt );
	}
}
