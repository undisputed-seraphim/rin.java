package rin.gl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.input.Mouse;

import rin.gl.lib3d.Actor;
import rin.gl.lib3d.Collidable;
import rin.gl.lib3d.Controllable;
import rin.util.Buffer;
import rin.util.math.Mat4;
import rin.util.math.Vec3;

public class Camera extends Controllable {
	protected float aspect;
	protected float znear;
	protected float zfar;
	protected float fovy;
	
	/* camera's matrices derived from position/rotation vectors, and the camera's perspective */
	protected Mat4	perspective =	new Mat4();
	
	public Camera( float fovy, float aspect, float znear, float zfar ) {
		this.aspect = aspect;
		this.znear = znear;
		this.zfar = zfar;
		this.fovy = fovy;
		this.perspective = Mat4.perspective( fovy, aspect, znear, zfar );
		this.setPosition( 0.0f, -1.0f, -11.0f );
		this.setScale( 1.0f, 1.0f, 1.0f );
	}
	
	public Mat4 getViewMatrix() { return this.matrix; }
	public Mat4 getPerspective() { return this.perspective; }
	
	public void init() {
		this.transform();
		//glUniformMatrix4( Scene.getUniform( "pMatrix" ), false, this.perspective.gl() );
		glUniformMatrix4( GL.getUniform( "vMatrix" ), false, this.matrix.gl() );
		this.setControlled( true );
	}
	
	public void attach( Actor a ) {
		this.setRotation( a.getRotation() );
		this.setPosition( a.getPosition() );
		if( a instanceof Collidable ) {
			this.move( 0.0f, 0.0f, 10.0f );
		}
		if( a instanceof Controllable ) {
			//this.setControlled( false );
		}
	}
	
	public void detach() { this.resetPosition(); this.setControlled( true ); }
	
	public void update() {
		this.processInput();
		//System.out.println(this.matrix.toString());
		glUniformMatrix4( GL.getUniform( "vMatrix" ), false, this.matrix.gl() );
	}
	
	public void transform() {
		super.transform();
		this.matrix = Mat4.multiply( this.perspective, this.matrix );
	}
	
	public float[] getMouseRGB() {
		IntBuffer buf = Buffer.toBuffer( new int[3] );
		glReadPixels( Mouse.getX(), Mouse.getY(), 1, 1, GL_RGB, GL_UNSIGNED_BYTE, buf );
		return new float[]{ buf.get( 0 ), buf.get( 1 ), buf.get( 2 ) };
	}
	
	public float getMouseZ() {
		FloatBuffer buf = Buffer.toBuffer( new float[1] );
		glReadPixels( Mouse.getX(), Mouse.getY(), 1, 1, GL_DEPTH_COMPONENT, GL_FLOAT, buf );
		return buf.get( 0 );
	}
	
	public Vec3 getMousePosition( Mat4 modelView ) {
		IntBuffer viewport = Buffer.toBuffer( new int[16] );
		glGetInteger( GL_VIEWPORT, viewport );
		
		Vec3 pos = Mat4.unProject( Mouse.getX(), Mouse.getY(), this.getMouseZ(), Mat4.flatten( modelView ), Mat4.flatten( this.matrix ), viewport );
		//pos = Vec3.step( pos, this.rotate, 0.00001f );
		//System.out.println( pos.toString() );
		
		//Cube cube = new Cube( 0.2f, pos );
		//cube.init();
		//cube.render();
		
		return pos;
	}
	
	public Camera destroy() {
		super.destroy();
		return null;
	}
}
