package rin.gl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.util.glu.GLU.gluPickMatrix;

import java.nio.IntBuffer;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import rin.gl.lib3d.Actor;
import rin.gl.lib3d.shape.Cube;
import rin.util.Buffer;
import rin.util.math.Mat4;
import rin.util.math.Vec3;

public class Camera extends Actor {
	/* buffer used for picking results and bool for isPicking */
	private IntBuffer pickingBuffer = Buffer.toBuffer( new int[64] );
	public boolean	picking = false;
	
	protected float aspect;
	protected float znear;
	protected float zfar;
	protected float fovy;
	
	/* camera's matrices derived from position/rotation vectors, and the camera's perspective */
	public Mat4		perspective =	new Mat4();
	
	public Camera( Scene scene, float fovy, float aspect, float znear, float zfar ) {
		this.scene = scene;
		this.aspect = aspect;
		this.znear = znear;
		this.zfar = zfar;
		this.fovy = fovy;
		this.perspective = Mat4.perspective( fovy, aspect, znear, zfar );
		this.position( 0.0f, -1.0f, -11.0f );
		System.out.println( this.position.toString() );
		this.init();
	}
	
	public Mat4 getViewMatrix() { return this.matrix; }
	public Mat4 getPerspective() { return this.perspective; }
	
	public void init() {
		this.transform();
		//glUniformMatrix4( this.scene.getUniform( "pMatrix" ), false, this.perspective.gl() );
		glUniformMatrix4( this.scene.getUniform( "vMatrix" ), false, Mat4.multiply( this.perspective, this.matrix ).gl() );
	}
	
	public void update() {
		boolean changed = false;
		float	step = 0.0f,
				side = 0.0f,
				rise = 0.0f;
		
		if( Mouse.isButtonDown( 0 ) ) {
			this.picking = true;
		} else {
			this.picking = false;
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
		
		glUniformMatrix4( this.scene.getUniform( "vMatrix" ), false, Mat4.multiply( this.perspective, this.matrix ).gl() );
	}
	
	public void getPickingRay( Mat4 mat ) {
		float x = Mouse.getX() * ( 2.0f / this.scene.getWidth() ) - 1.0f;
		float y = Mouse.getY() * ( 2.0f / this.scene.getHeight() ) - 1.0f;
		
		Vec3 fin = Mat4.multVec4( Mat4.inverse( Mat4.multiply( this.perspective, this.matrix ) ), new Vec3( x, y, 1.0f ) );
		fin.x -= this.position.x;
		fin.y -= this.position.y;
		fin.z += this.position.z;
		
		Cube cube = new Cube( 0.2f, fin );
		cube.setScene( this.scene );
		cube.init();
		cube.render();
		
	}
	
	/*public void startPicking() {
		glUniform1i( this.scene.getUniform( "picking" ), GL_TRUE );
		
		glSelectBuffer( this.pickingBuffer );
		glRenderMode( GL_SELECT );

		glMatrixMode( GL_PROJECTION );
		glPushMatrix();
		glLoadIdentity();
		//gluPerspective( 45, 900 / 600, 0.1f, 100f );

		IntBuffer viewport = Buffer.toBuffer( new int[16] );
		glGetInteger( GL_VIEWPORT, viewport );
		
		gluPickMatrix( Mouse.getX(), Mouse.getY(), 3, 3, viewport );
		//mat4 tmp = mat4.pickMatrix( Mouse.getX(), Mouse.getY(), 3, 3, viewport );
		glMatrixMode( GL_MODELVIEW );
		
		glInitNames();
	}*/
	
	/*public IntBuffer stopPicking() {
		glUniform1i( this.scene.getUniform( "picking" ), GL_FALSE );
		glMatrixMode( GL_PROJECTION );
		glPopMatrix();
		glMatrixMode( GL_MODELVIEW );
		glFlush();
		
		int hits = glRenderMode( GL_RENDER );
		if( hits > 0 ) {
			return this.pickingBuffer;
		}
		
		return null;
	}*/

}
