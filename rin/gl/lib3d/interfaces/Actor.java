package rin.gl.lib3d.interfaces;

import rin.gl.Scene;
import rin.util.math.Mat4;
import rin.util.math.Quat4;
import rin.util.math.Vec3;

public class Actor implements Positionable, Controllable {

	/** Name describing Actor */
	private String name = "No Name";
	
	/** Unique color in the format of [ r, g, b ]. Used for Picking. */
	private float[] uniqueColor = new float[3];
	
	public Actor() { this( "No Name Actor" ); }
	public Actor( String name ) { this( name, new Vec3( 0.0f, 0.0f, 0.0f ) ); }
	public Actor( String name, Vec3 position ) { this( name, position, new Vec3( 0.0f, 0.0f, 0.0f ) ); }
	public Actor( String name, Vec3 position, Vec3 rotation ) { this( name, position, rotation, new Vec3( 1.0f, 1.0f, 1.0f ) ); }
	public Actor( String name, Vec3 position, Vec3 rotation, Vec3 scale ) {
		this.name = name;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		this.uniqueColor = Scene.getNextColor();
	}
	
	public String getName() { return this.name; }
	public float[] getUniqueColor() { return this.uniqueColor; }
	
	/* -------------- positionable implementation ------------------ */
	private Vec3 position =	new Vec3(), rotation =	new Vec3(), scale = new Vec3();
	private Mat4 translate = new Mat4(), rotate = new Mat4(), scaled = new Mat4(), matrix = new Mat4();
	
	@Override public Mat4 getMatrix() { return this.matrix; }
	
	@Override public Vec3 getPosition() { return this.position; }
	@Override public Mat4 getPositionMatrix() { return this.translate; }
	@Override public void resetPosition() { this.setPosition( 0.0f, 0.0f, 0.0f ); }
	@Override public void setPosition( Vec3 p ) { this.setPosition( p.x, p.y, p.z ); }
	@Override public void setPosition( float x, float y, float z ) {
		this.position.redefine( x, y, z );
		this.transform();
	}
	
	private void updatePosition() { this.translate = Mat4.translate( new Mat4(), this.position ); }

	@Override public Vec3 getRotation() { return this.rotation; }
	@Override public Mat4 getRotationMatrix() { return this.rotate; }
	@Override public void resetRotation() { this.setRotation( 0.0f, 0.0f, 0.0f ); }
	@Override public void setRotation( Vec3 r ) { this.setRotation( r.x, r.y, r.z ); }
	@Override public void setRotation( float x, float y, float z ) {
		this.rotation.redefine( x * Quat4.PIOVER180, y * Quat4.PIOVER180, z * Quat4.PIOVER180 );
		this.transform();
	}
	
	private void updateRotation() {
		Quat4	rotateX = Quat4.create( Vec3.X_AXIS, this.rotation.x ),
				rotateY = Quat4.create( Vec3.Y_AXIS, this.rotation.y ),
				rotateZ = Quat4.create( Vec3.Z_AXIS, this.rotation.z );
		this.rotate = Quat4.multiply( Quat4.multiply( rotateX, rotateY ), rotateZ ).toMat4();
	}

	@Override public Vec3 getScale() { return this.scale; }
	@Override public Mat4 getScaleMatrix() { return this.scaled; }
	@Override public void resetScale() { this.setScale( 1.0f, 1.0f, 1.0f ); }
	@Override public void setScale( Vec3 s ) { this.setScale( s.x, s.y, s.z ); }
	@Override public void setScale( float x, float y, float z ) {
		this.scale.redefine( x, y, z );
		this.transform();
	}
	
	private void updateScale() { this.scaled = Mat4.scale( new Mat4(), this.scale ); }
	
	@Override public void move( float step, float side, float rise ) {
		this.position.x += this.rotate.m[ 8] * step + ( this.rotate.m[0] * side ) + ( this.rotate.m[4] * rise );
		this.position.y += this.rotate.m[ 9] * step + ( this.rotate.m[1] * side ) + ( this.rotate.m[5] * rise );
		this.position.z += this.rotate.m[10] * step + ( this.rotate.m[2] * side ) + ( this.rotate.m[6] * rise );
		this.transform();
	}
	
	private void transform() {
		this.updatePosition();
		this.updateRotation();
		this.updateScale();
		this.matrix = Mat4.multiply( Mat4.multiply( new Mat4(), this.rotate ), this.translate );
	}

	@Override public Actor destroy() {
		return null;
	}
}
