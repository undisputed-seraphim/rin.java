package rin.gl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;


import rin.gl.lib3d.Actor;
import rin.util.Buffer;
import rin.util.math.Mat4;
import rin.util.math.Vec3;

public class Camera extends Actor {
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
		this.init();
	}
	
	public Mat4 getViewMatrix() { return this.matrix; }
	public Mat4 getPerspective() { return this.perspective; }
	
	public void init() {
		this.transform();
		//glUniformMatrix4( Scene.getUniform( "pMatrix" ), false, this.perspective.gl() );
		glUniformMatrix4( Scene.getUniform( "vMatrix" ), false, this.matrix.gl() );
	}
	
	public void update() {
		boolean changed = false;
		float	step = 0.0f,
				side = 0.0f,
				rise = 0.0f;

		if( Keyboard.isKeyDown( Keyboard.KEY_Z ) ) {
			GL.hide();//Scene.requestDestroy();
			return;
		}
		
		if( Keyboard.isKeyDown( Keyboard.KEY_W ) ) {
			changed = true;
			step += 0.05f;
		}
		if( Keyboard.isKeyDown( Keyboard.KEY_S ) ) {
			changed = true;
			step -= 0.05f;
		}
		if( Keyboard.isKeyDown( Keyboard.KEY_A ) ) {
			changed = true;
			side += 0.05f;
		}
		if( Keyboard.isKeyDown( Keyboard.KEY_D ) ) {
			changed = true;
			side -= 0.05f;
		}
		
		if( Keyboard.isKeyDown( Keyboard.KEY_UP ) ) {
			changed = true;
			this.rotation.x -=	0.001;
		}
		if( Keyboard.isKeyDown( Keyboard.KEY_DOWN ) ) {
			changed = true;
			this.rotation.x +=	0.001;
		}
		if( Keyboard.isKeyDown( Keyboard.KEY_LEFT ) ) {
			changed = true;
			this.rotation.y -=	0.001;
		}
		if( Keyboard.isKeyDown( Keyboard.KEY_RIGHT ) ) {
			changed = true;
			this.rotation.y +=	0.001;
		}
		if( Keyboard.isKeyDown( Keyboard.KEY_SPACE ) ) {
			changed = true;
			rise -= 0.05f;
		}
		if( Keyboard.isKeyDown( Keyboard.KEY_LSHIFT ) ) {
			changed = true;
			rise += 0.05f;
		}
		
		if( changed )
			this.move( step, side, rise );
		
		glUniformMatrix4( Scene.getUniform( "vMatrix" ), false, this.matrix.gl() );
	}
	
	public void transform() {
		super.transform();
		this.matrix = Mat4.multiply( this.perspective, this.matrix );
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
