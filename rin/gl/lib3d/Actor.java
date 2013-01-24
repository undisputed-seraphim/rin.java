package rin.gl.lib3d;

import rin.gl.Scene;
import rin.util.math.*;

public class Actor {
	/* scene this actor belongs to */
	protected Scene	scene;
	
	/* this actors unique id */
	protected int id = -1;
	protected String name = "No Name";
	
	public int id() {
		return this.id;
	}
	
	public void setId( int id ) { this.id = id; }
	public void setName( String name ) { this.name = name; }
	public String getName() { return this.name; }
	public void setScene( Scene scene ) { this.scene = scene; }
	
	/* matrices depicting the location in 3d space of this positionable */
	protected Mat4	rotate =	new Mat4(),
					translate = new Mat4(),
					matrix =	new Mat4();
	
	/* vectors for the current location, rotation, and scale of the positionable */
	protected Vec3	position =	new Vec3(),
					rotation =	new Vec3();
	
	/* if this positionable controls a uniform variable, set the uniform upon transforms */
	protected boolean hasUniform = false;
	protected String uniform = "";
	
	/* get information from this positionable */
	public Vec3 getPosition() {
		return this.position;
	}
	
	public Mat4 getMatrix() {
		return this.matrix;
	}
	
	/* move positionable to a specific vector */
	public void position( Vec3 v ) {
		this.position = v;
		this.position();
	}
	
	/* move positionable to a specific x, y, z coordinate */
	public void position( float x, float y, float z ) {
		this.position = new Vec3( x, y, z );
		this.position();
	}
	
	/* set positionables position */
	public void position() {
		this.translate = Mat4.translate( new Mat4(), this.position );
	}
	
	/* rotate positionable to given degrees per axis */
	public void rotation( Vec3 v ) {
		float helper = (float)( java.lang.Math.PI / 180 );
		this.rotation.x = v.x * helper;
		this.rotation.y = v.y * helper;
		this.rotation.z = v.z * helper;
		this.rotation();
	}
	
	/* obtain rotation quaternions and compute final rotation matrix based on rotation vector */
	public void rotation() {
		Quat4	rotateX = Quat4.create( new Vec3( 1.0f, 0.0f, 0.0f ), this.rotation.x ),
				rotateY = Quat4.create( new Vec3( 0.0f, 1.0f, 0.0f ), this.rotation.y ),
				rotateZ = Quat4.create( new Vec3( 0.0f, 0.0f, 1.0f ), this.rotation.z );
		this.rotate = Quat4.Mat4( Quat4.multiply( Quat4.multiply( rotateX, rotateY ), rotateZ ) );
	}
	
	/* apply rotation and position vectors then compute positionables location matrix */
	public void transform() {
		this.rotation();
		this.position();
		this.matrix = Mat4.multiply( Mat4.multiply( new Mat4(), this.rotate ), this.translate );
		
		if( this.hasUniform ) {
			
		}
	}
	
	/* move the positionable a set amount toward/away, left/right, and up/down */
	public void move( float step, float side, float rise ) {
		this.position.x += this.rotate.m[ 8] * step + ( this.rotate.m[0] * side ) + ( this.rotate.m[4] * rise );
		this.position.y += this.rotate.m[ 9] * step + ( this.rotate.m[1] * side ) + ( this.rotate.m[5] * rise );
		this.position.z += this.rotate.m[10] * step + ( this.rotate.m[2] * side ) + ( this.rotate.m[6] * rise );
		this.transform();
	}
}
