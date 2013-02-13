package rin.gl.lib3d.properties;

import rin.util.math.Mat4;
import rin.util.math.Vec3;

public class Transformation {
	public static final Vec3 defaultPosition = new Vec3( 0.0f, 0.0f, 0.0f );
	public static final Vec3 defaultRotation = new Vec3( 0.0f, 0.0f, 0.0f );
	public static final Vec3 defaultScale = new Vec3( 1.0f, 1.0f, 1.0f );
	
	protected Vec3 position;
	protected Vec3 rotation;
	protected Vec3 scale;
	
	public Transformation() { this( new Vec3( defaultPosition ) ); }
	public Transformation( Vec3 position ) { this( position, new Vec3( defaultRotation ) ); }
	public Transformation( Vec3 position, Vec3 rotation ) { this( position, rotation, new Vec3( defaultScale ) ); }
	public Transformation( Vec3 position, Vec3 rotation, Vec3 scale ) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}
	
	public Vec3 getPosition() { return this.position; }
	public Transformation setPosition( Vec3 position ) { this.position = position; return this; }
	public Transformation setPosition( float x, float y, float z ) { this.position = new Vec3( x, y, z ); return this; }
	
	public Vec3 getRotation() { return this.rotation; }
	public Transformation setRotation( Vec3 rotation ) { this.rotation = rotation; return this; }
	public Transformation setRotation( float x, float y, float z ) { this.rotation = new Vec3( x, y, z ); return this; }
	
	public Vec3 getScale() { return this.scale; }
	public Transformation setScale( Vec3 scale ) { this.scale = scale; return this; }
	public Transformation setScale( float x, float y, float z ) { this.scale = new Vec3( x, y, z ); return this; }
	
	public Mat4 getTranslationMatrix() { return Mat4.translate( new Mat4(), this.position ); }
	public Mat4 getRotationMatrix() { return Mat4.rotate( new Mat4(), this.rotation ); }
	public Mat4 getScaleMatrix() { return Mat4.scale( new Mat4(), this.scale ); }
	
	public Mat4 getTransformationMatrix() {
		return Mat4.multiply( Mat4.multiply( Mat4.multiply( new Mat4(), this.getScaleMatrix() ),
				this.getRotationMatrix() ), this.getTranslationMatrix() );
	}
}