package rin.engine.view.lib3d;

import java.util.HashMap;

public class Animation {
	private String name;
	private HashMap<String, Frame> frames = new HashMap<String, Frame>();
	
	public Animation( String id ) { name = id; }
	public String getName() { return name; }
	
	public Frame getFrame( String id ) { return frames.get( id ); }
	public Frame addFrame( JointNode jn ) {
		Frame res = new Frame( jn );
		frames.put( jn.getId(), res );
		return res;
	}
}
