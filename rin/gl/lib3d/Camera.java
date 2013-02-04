package rin.gl.lib3d;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import rin.gl.event.GLEvent.KeyRepeatEvent;
import rin.gl.lib3d.interfaces.Actor;
import rin.gl.lib3d.interfaces.GL;
import rin.util.Buffer;
import rin.util.math.Mat4;
import rin.util.math.Vec3;

public class Camera extends Actor {
	protected float aspect, fovy;
	protected float znear, zfar;
	
	/* camera's matrices derived from position/rotation vectors, and the camera's perspective */
	protected Mat4	perspective =	new Mat4();
	public Mat4 getPerspectiveMatrix() { return this.perspective; }
	
	public Camera( float fovy, float aspect, float znear, float zfar ) {
		this.aspect = aspect;
		this.znear = znear;
		this.zfar = zfar;
		this.fovy = fovy;
		this.perspective = Mat4.perspective( fovy, aspect, znear, zfar );
		this.setPosition( 0.0f, -1.0f, -11.0f );
		this.setScale( 1.0f, 1.0f, 1.0f );
	}
	
	public void init() {
		this.transform();
		//glUniformMatrix4( Scene.getUniform( "pMatrix" ), false, this.perspective.gl() );
		glUniformMatrix4( GL.getUniform( "vMatrix" ), false, this.getMatrix().gl() );
		this.setControlled( true );
	}
	
	public void attach( Actor a ) {
		/*this.setRotation( a.getRotation() );
		this.setPosition( a.getPosition() );
		if( a instanceof Collidable ) {
			this.move( 0.0f, 0.0f, 10.0f );
		}
		if( a instanceof Controllable ) {
			//this.setControlled( false );
		}*/
	}
	
	public void detach() { this.resetPosition(); this.setControlled( true ); }
	
	public void update() {
		//this.processInput();
		//System.out.println(this.matrix.toString());
		glUniformMatrix4( GL.getUniform( "vMatrix" ), false, this.getMatrix().gl() );
	}
	
	@Override public void transform() {
		super.transform();
		this.setMatrix( Mat4.multiply( this.perspective, this.getMatrix() ) );
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
		
		Vec3 pos = Mat4.unProject( Mouse.getX(), Mouse.getY(), this.getMouseZ(),
				Mat4.flatten( modelView ), Mat4.flatten( this.getMatrix() ), viewport );
		//pos = Vec3.step( pos, this.rotate, 0.00001f );
		//System.out.println( pos.toString() );
		
		//Cube cube = new Cube( 0.2f, pos );
		//cube.init();
		//cube.render();
		
		return pos;
	}
	
	@Override public void processKeyRepeatEvent( KeyRepeatEvent e ) {
		boolean pChanged = false,
				rChanged = false;
		float	step = 0.0f,
				side = 0.0f,
				rise = 0.0f,
				rotx = 0.0f,
				roty = 0.0f;
		
		switch( e.key ) {
		
		//camera forward/backward and strafe
		case Keyboard.KEY_W: pChanged = true; step += 0.05f; break;
		case Keyboard.KEY_S: pChanged = true; step -= 0.05f; break;
		case Keyboard.KEY_A: pChanged = true; side += 0.05f; break;
		case Keyboard.KEY_D: pChanged = true; side -= 0.05f; break;
		
		//camera look and turn
		case Keyboard.KEY_UP: rChanged = true; rotx -= 0.001; break;
		case Keyboard.KEY_DOWN: rChanged = true; rotx += 0.001; break;
		case Keyboard.KEY_LEFT: rChanged = true; roty -= 0.001; break;
		case Keyboard.KEY_RIGHT: rChanged = true; roty += 0.001; break;
		
		//camera ascend/descend
		case Keyboard.KEY_LSHIFT: pChanged = true; rise += 0.05f; break;
		case Keyboard.KEY_SPACE: pChanged = true; rise -= 0.05f; break;
		
		default: break;
		
		}
		
		if( rChanged ) this.spin( rotx, roty, 0.0f );
		if( pChanged ) this.move( step, side, rise );
	}
	
	@Override public Camera destroy() {
		super.destroy();
		return null;
	}
}
