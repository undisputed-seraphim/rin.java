package rin.engine.game.entity.animated;

import java.util.HashMap;

import rin.engine.math.Matrix4x4;

public class Bone {

	private String id;
	private Matrix4x4 pose = new Matrix4x4();
	private HashMap<Integer, Bone> children = new HashMap<Integer, Bone>();
	
	
	public Bone( String name ) {
		id = name;
	}
	
	public Bone( Bone bone ) {
		id = bone.getName();
		pose = new Matrix4x4( bone.getTransformation() );
	}
	
	public Bone( String name, Matrix4x4 pose ) {
		this( name );
		
	}
	
	public String getName() {
		return id;
	}
	
	public Matrix4x4 getTransformation() {
		return pose;
	}
	
	public Bone addBone( String name ) {
		children.put( children.size(), new Bone( name ) );
		return children.get( children.size() - 1 );
	}
	
	public HashMap<Integer, Bone> getChildren() { return children; }
	
	public void print( String tab ) {
		System.out.println( tab + "Bone: " + id + " " + children.size() );
		for( Integer i : children.keySet() )
			children.get( i ).print( tab + "   " );
	}
}
