package rin.engine.scene.nodes;

import static org.lwjgl.opengl.GL20.glUniformMatrix4;

import org.lwjgl.input.Keyboard;

import rin.engine.view.gl.GL;
import rin.engine.view.View;
import rin.engine.view.gl.GLEvent.KeyRepeatEvent;
import rin.util.math.Mat4;

public class Camera extends Actor {

	protected float _fovy = 45.0f;
	protected float _aspect = View.DEFAULT_WIDTH / View.DEFAULT_HEIGHT;
	protected float _znear = 0.1f;
	protected float _zfar = 15.0f;
	
	private Mat4 perspective = new Mat4();
	
	public Camera() { super( "CAMERA" ); updatePerspective(); }
	public Camera( float fovy, float aspect, float znear, float zfar ) {
		super( "CAMERA" );
		_fovy = fovy;
		_aspect = aspect;
		_znear = znear;
		_zfar = zfar;
		updatePerspective();
	}
	
	public void setFovy( float fovy ) {
		_fovy = fovy;
		updatePerspective();
	}
	
	public void setAspect( float aspect ) {
		_aspect = aspect;
		updatePerspective();
	}
	
	public void setZnear( float znear ) {
		_znear = znear;
		updatePerspective();
	}
	
	public void setZfar( float zfar ) {
		_zfar = zfar;
		updatePerspective();
	}
	
	private void updatePerspective() {
		perspective.redefine( Mat4.perspective( _fovy, _aspect, _znear, _zfar ) );
		glUniformMatrix4( GL.getUniform( "pMatrix" ), false, perspective.gl() );
	}
	
	@Override
	public void process( double dt ) {
		super.process( dt );
		//TODO: if attached to a node, apply that node's matrix to this camera
		glUniformMatrix4( GL.getUniform( "vMatrix" ), false, getTransform().gl() );
	}
	
	@Override
	public void processKeyRepeatEvent( KeyRepeatEvent e ) {
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
		case Keyboard.KEY_UP: rChanged = true; rotx -= 1.5f; break;
		case Keyboard.KEY_DOWN: rChanged = true; rotx += 1.5f; break;
		case Keyboard.KEY_LEFT: rChanged = true; roty -= 1.5f; break;
		case Keyboard.KEY_RIGHT: rChanged = true; roty += 1.5f; break;
		
		//camera ascend/descend
		case Keyboard.KEY_LSHIFT: pChanged = true; rise += 0.05f; break;
		case Keyboard.KEY_SPACE: pChanged = true; rise -= 0.05f; break;
		
		default: break;
		
		}
		
		if( rChanged ) spin( rotx, roty, 0.0f );
		if( pChanged ) move( step, side, rise );
	}
	
}
