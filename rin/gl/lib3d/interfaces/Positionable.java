package rin.gl.lib3d.interfaces;

import rin.gl.lib3d.Actor;
import rin.gl.lib3d.properties.Position;
import rin.gl.lib3d.properties.Scale;
import rin.util.math.Mat4;
import rin.util.math.Vec3;

public interface Positionable extends Comparable<Positionable> {
	
	public Mat4 getMatrix();
	public void setMatrix( Mat4 m );
	
	/** Obtain the x, y, z coordinates of this Positionable.
	 * @return {@link Vec3} describing item's position in world coordinates. */
	public Position getPosition();
	public Mat4 getPositionMatrix();
	public void resetPosition();
	public void setPosition( float x, float y, float z );
	
	public Vec3 getRotation();
	public Mat4 getRotationMatrix();
	public void resetRotation();
	public void setRotation( float x, float y, float z );
	
	public Scale getScale();
	public Mat4 getScaleMatrix();
	public void resetScale();
	public void setScale( float x, float y, float z );
	
	public void spin( float xaxis, float yaxis, float zaxis );
	public void move( float step, float side, float rise );
	public void transform();
	
	public Actor destroy();
	
	public int compareTo( Positionable p );
	
}
