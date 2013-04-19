package rin.engine.game.entity.animated;

import java.util.HashMap;

import rin.engine.math.Matrix4x4;

public class Skeleton {

	private HashMap<Integer, Bone> bones = new HashMap<Integer, Bone>();
	
	public Skeleton() {}
	
	public HashMap<Integer, Bone> getBoneMap() { return bones; }
	
	public Bone getRootBone() {
		return bones.get( 0 );
	}
	
	public Bone getBone( String name ) {
		for( Integer i : bones.keySet() )
			if( bones.get( i ).getName().toUpperCase().equals( name.toUpperCase() ) )
				return bones.get( i );
		return null;
	}
	
	public void addBone( Bone bone ) {
		bones.put( bones.size(), bone );
	}
	
	public Bone addBone( String name ) {
		bones.put( bones.size(), new Bone( name ) );
		return bones.get( bones.size() - 1 );
	}
	
	public Bone addBone( String name, float[] mat ) {
		bones.put( bones.size(), new Bone( name, new Matrix4x4( mat ) ) );
		return bones.get( bones.size() - 1 );
	}
	
	public void print() {
		bones.get( 0 ).print( "" );
	}
}
