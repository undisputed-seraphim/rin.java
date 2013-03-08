package rin.gl.lib3d.properties;

import rin.util.math.Vec3;

public class Point implements TransitionableProperty<Point> {
	public float x, y, z;
	
	public Point( float x, float y, float z ) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vec3 asVec3() { return new Vec3( this.x, this.y, this.z ); }
	
	@Override public Point copy() { return new Point( this.x, this.y, this.z ); }
	@Override public String toString() { return "Point[ " + this.x + " " + this.y + " " + this.z + " ]"; }
	
	@Override public void doInterpolate( Point from, Point to, float dt ) {
		this.x = from.x * ( 1 - dt ) + to.x * dt;
		this.y = from.y * ( 1 - dt ) + to.y * dt;
		this.z = from.z * ( 1 - dt ) + to.z * dt;
	}
	
}
