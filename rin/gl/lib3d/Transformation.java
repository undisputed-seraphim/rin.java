package rin.gl.lib3d;

import rin.util.math.Mat4;
import rin.util.math.Vec3;

public class Transformation {
	public static final Vec3 defaultPosition = new Vec3( 0.0f, 0.0f, 0.0f );
	public static final Vec3 defaultRotation = new Vec3( 0.0f, 0.0f, 0.0f );
	public static final Vec3 defaultScale = new Vec3( 1.0f, 1.0f, 1.0f );
	
	private Vec3 position;
	private Vec3 rotation;
	private Vec3 scale;
	
	public Transformation() { this( new Vec3( defaultPosition ) ); }
	public Transformation( Vec3 position ) { this( position, new Vec3( defaultRotation ) ); }
	public Transformation( Vec3 position, Vec3 rotation ) { this( position, rotation, new Vec3( defaultScale ) ); }
	public Transformation( Vec3 position, Vec3 rotation, Vec3 scale ) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}
	
	public Vec3 getPosition() { return this.position; }
	public Vec3 getRotation() { return this.rotation; }
	public Vec3 getScale() { return this.scale; }
	
	public Mat4 getTranslationMatrix() { return Mat4.translate( new Mat4(), this.position ); }
	public Mat4 getRotationMatrix() { return Mat4.rotate( new Mat4(), this.rotation ); }
	public Mat4 getScaleMatrix() { return Mat4.scale( new Mat4(), this.scale ); }
	
	public Mat4 getTransformationMatrix() {
		return Mat4.multiply( Mat4.multiply( Mat4.multiply( new Mat4(), this.getScaleMatrix() ),
				this.getRotationMatrix() ), this.getTranslationMatrix() );
	}
}
