package rin.engine.game.entity.animated;

import java.util.TreeMap;

import rin.engine.math.Matrix4x4;

public class Skeleton {

	private TreeMap<Integer, Bone> bones = new TreeMap<Integer, Bone>();
	
	public Skeleton() {}
	
	public TreeMap<Integer, Bone> getBoneMap() { return bones; }
	
	public Bone getRootBone() {
		return bones.get( 0 );
	}
	
	public Bone getBone( String name ) {
		for( Integer i : bones.keySet() )
			if( bones.get( i ).getName().toUpperCase().equals( name.toUpperCase() ) )
				return bones.get( i );
		return null;
	}
	
	public int indexOf( String name ) {
		for( Integer i : bones.keySet() )
			if( bones.get( i ).getName().toUpperCase().equals( name.toUpperCase() ) )
				return i;
		return -1;
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
	
	public Skeleton clone() {
		Skeleton res = new Skeleton();
		for( Integer i : bones.keySet() )
			res.addBone( new Bone( bones.get( i ) ) );
		
		return res;
	}
	
	public void print() {
		System.out.println( toString() );
	}
	
	@Override
	public String toString() {
		return bones.get( 0 ).toString();
	}
	
}
