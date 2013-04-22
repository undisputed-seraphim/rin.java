package rin.engine.game.entity.animated;

import java.util.HashMap;

import rin.engine.math.Matrix4x4;

public class Bone {

	private String id;
	private Bone parent;
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
	
	public Bone getParent() {
		return this.parent;
	}
	
	public Matrix4x4 getTransformation() {
		return pose;
	}
	
	public void setTransformation( Matrix4x4 m ) {
		pose = m;
	}
	
	public HashMap<Integer, Bone> getChildren() {
		return children;
	}
	
	
	
	public Bone addBone( String name ) {
		children.put( children.size(), new Bone( name ) );
		children.get( children.size() - 1 ).parent = this;
		return children.get( children.size() - 1 );
	}
	
	public Bone addBone( String name, float[] mat ) {
		children.put( children.size(), new Bone( name, new Matrix4x4( mat ) ) );
		children.get( children.size() - 1 ).parent = this;
		return children.get( children.size() - 1 );
	}
	
	public void print( String tab ) {
		System.out.println( toString() );
	}
	
	@Override
	public String toString() {
		return toString( "" );
	}
	
	private String toString( String tab ) {
		String res = tab + "Bone: " + id + " " + children.size() + "\n";
		for( Integer i : children.keySet() )
			res += children.get( i ).toString( tab + "   " );
		return res;
	}
	
}
