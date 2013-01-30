package rin.gl.lib3d;

import rin.gl.Scene;
import rin.util.math.*;

//TODO: add overflow checking for radians/rotation
public class Actor {
	public static final float PIOVER180 = (float)( java.lang.Math.PI / 180 );
	/* this actors unique id */
	private float[] uniqueColor = new float[3];
	protected int id = -1;
	protected String name = "No Name";
	
	public Actor() {
		this.uniqueColor = Scene.getNextColor();
	}
	
	public int getId() { return this.id; }
	public Actor setId( int id ) { this.id = id; return this; }
	public Actor setName( String name ) { this.name = name; return this; }
	public String getName() { return this.name; }
	public float[] getUniqueColor() { return this.uniqueColor; }
	
	/* matrices depicting the location in 3d space of this positionable */
	protected Mat4	scaled =	new Mat4(),
					rotate =	new Mat4(),
					translate = new Mat4(),
					matrix =	new Mat4();
	
	/* vectors for the current location, rotation, and scale of the positionable */
	protected Vec3	position =	new Vec3(),
					rotation =	new Vec3(),
					scale =		new Vec3( 1.0f, 1.0f, 1.0f );
	
	/* quaternions used for handling rotation on each axis */
	protected Quat4 rotateX =	new Quat4(),
					rotateY = 	new Quat4(),
					rotateZ =	new Quat4();
	
	/* get information from this positionable */
	public Vec3 getPosition() { return this.position; }
	public Vec3 getRotation() { return this.rotation; }
	public Vec3 getScale() { return this.scale; }
	public Mat4 getMatrix() { return this.matrix; }
	
	/* move positionable to a specific vector */
	public void setPosition( Vec3 v ) { this.setPosition( v.x, v.y, v.z ); }
	public void setPosition( float x, float y, float z ) {
		this.position.redefine( x, y, z );
		this.transform();
	}
	
	/* set positionables position */
	private void updatePosition() {
		this.translate = Mat4.translate( new Mat4(), this.position );
	}
	
	/* rotate positionable to given degrees per axis */
	public void setRotation( Vec3 v ) { this.setRotation( v.x, v.y, v.z ); }
	public void setRotation( float x, float y, float z ) {
		this.rotation.redefine( x * PIOVER180, y * PIOVER180, z * PIOVER180 );
		this.transform();
	}
	
	/* obtain rotation quaternions and compute final rotation matrix based on rotation vector */
	private void updateRotation() {
		Quat4	rotateX = Quat4.create( Vec3.X_AXIS, this.rotation.x ),
				rotateY = Quat4.create( Vec3.Y_AXIS, this.rotation.y ),
				rotateZ = Quat4.create( Vec3.Z_AXIS, this.rotation.z );
		this.rotate = Quat4.multiply( Quat4.multiply( rotateX, rotateY ), rotateZ ).toMat4();
	}
	
	public void setScale( Vec3 v ) { this.setScale( v.x, v.y, v.z ); }
	public void setScale( float x, float y, float z ) {
		this.scale.redefine( x, y, z );		
		this.updateScale();
	}
	
	private void updateScale() {
		
	}
	
	/* apply rotation and position vectors then compute positionables location matrix */
	public void transform() {
		this.updateRotation();
		this.updatePosition();
		this.updateScale();
		this.matrix = Mat4.multiply( Mat4.multiply( new Mat4(), this.rotate ), this.translate );
	}
	
	/* move the positionable a set amount toward/away, left/right, and up/down */
	public void move( float step, float side, float rise ) {
		this.position.x += this.rotate.m[ 8] * step + ( this.rotate.m[0] * side ) + ( this.rotate.m[4] * rise );
		this.position.y += this.rotate.m[ 9] * step + ( this.rotate.m[1] * side ) + ( this.rotate.m[5] * rise );
		this.position.z += this.rotate.m[10] * step + ( this.rotate.m[2] * side ) + ( this.rotate.m[6] * rise );
		this.transform();
	}
	
	public void resetPosition() {
		this.position = new Vec3( 0.0f, 0.0f, 0.0f );
		this.rotation = new Vec3( 0.0f, 0.0f, 0.0f );
		this.transform();
	}
	
	/* determine if actor is within given distance from given point */
	public boolean withinRange( float d, Vec3 pos ) {
		return Vec3.distance( pos, this.position ) <= d;
	}
	
	public boolean isMesh() { return this instanceof Mesh; }
	public Mesh toMesh() { return ( Mesh )this; }
	
	public boolean isPoly() { return this instanceof Poly; }
	public Poly toPoly() { return ( Poly )this; }
	
	public Pickable toPickable() { return ( Pickable )this; }
	
	public boolean isRenderable() { return this instanceof Renderable; }
	public Renderable toRenderable() { return ( Renderable )this; }
	
	public Actor destroy() {
		if( this.isMesh() )
			this.toMesh().destroy();
		
		if( this.isPoly() )
			this.toPoly().destroy();
		
		return null;
	}
}
