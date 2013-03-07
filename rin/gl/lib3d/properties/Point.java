package rin.gl.lib3d.properties;

import rin.util.math.Vec3;

public class Point<T extends Point<?>> implements TransitionableProperty<T> {
	public float x, y, z;
	
	public Point( float x, float y, float z ) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vec3 asVec3() { return new Vec3( this.x, this.y, this.z ); }
	
	@Override public T copy() { return null; }
	@Override public void update( T from, T to, float dt ) {
		System.out.println( "here" );
		this.x = from.x * ( 1 - dt ) + to.x * dt;
		this.y = from.y * ( 1 - dt ) + to.y * dt;
		this.z = from.z * ( 1 - dt ) + to.z * dt;
		
		this.x = from.x < to.x ? to.x < this.x ? to.x :
				 this.x < to.x ? to.x : this.x : this.x;
		this.y = from.y < to.y ? to.y < this.y ? to.y :
			 	 this.y < to.y ? to.y : this.y : this.y;
		this.z = from.z < to.z ? to.z < this.z ? to.z :
			 	 this.z < to.z ? to.z : this.z : this.z;
	}
	
}
