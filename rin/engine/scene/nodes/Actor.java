package rin.engine.scene.nodes;

import rin.util.math.Mat4;
import rin.util.math.Quat4;
import rin.util.math.Vec3;
import rin.engine.view.gl.GLEvent;
import rin.engine.view.gl.GLEvent.*;
import rin.engine.view.gl.GLEventListener.KeyEventListener;
import rin.engine.view.gl.GLEventListener.MouseEventListener;

public class Actor extends AbstractSceneNode implements KeyEventListener, MouseEventListener {

	private boolean changed = true;
	
	private float[] position = new float[3];
	private float[] rotation = new float[3];
	private float[] scale = new float[] { 1.0f, 1.0f, 1.0f };
	
	private Mat4 transform = new Mat4();
	private Mat4 translate = new Mat4();
	private Mat4 rotate = new Mat4();
	private Mat4 scaled = new Mat4();
	private Quat4 rot = new Quat4();
	
	public Actor( String id ) { super( id ); }
	
	public Mat4 getTransform() { return transform; }
	
	private void updatePosition() {
		translate.translateTo( position[0], position[1], position[2] );
	}
	
	public void setPosition( float tx, float ty, float tz ) {
		position[0] = tx;
		position[1] = ty;
		position[2] = tz;
		updatePosition();
		changed = true;
	}

	private void updateRotation() {
		rot.identity();
		rot.applyOrientationDeg( Vec3.X_AXIS, rotation[0] );
		rot.applyOrientationDeg( Vec3.Y_AXIS, rotation[1] );
		rot.applyOrientationDeg( Vec3.Z_AXIS, rotation[2] );
		rot.intoMat4( rotate );
	}
	
	public void setRotation( float rx, float ry, float rz ) {
		rotation[0] = rx;
		rotation[1] = ry;
		rotation[2] = rz;
		updateRotation();
		changed = true;
	}
	
	private void updateScale() {
		scaled.scaleTo( scale[0], scale[1], scale[2] );
	}
	
	public void setScale( float sx, float sy, float sz ) {
		scale[0] = sx;
		scale[1] = sy;
		scale[2] = sz;
		updateScale();
		changed = true;
	}
	
	public void spin( float xaxis, float yaxis, float zaxis ) {
		rotation[0] += xaxis;
		rotation[1] += yaxis;
		rotation[2] += zaxis;
		updateRotation();
		changed = true;
	}
	
	public void move( float step, float side, float rise ) {
		position[0] += rotate.m[ 8] * step + rotate.m[0] * side + rotate.m[4] * rise;
		position[1] += rotate.m[ 9] * step + rotate.m[1] * side + rotate.m[5] * rise;
		position[2] += rotate.m[10] * step + rotate.m[2] * side + rotate.m[6] * rise;
		updatePosition();
		changed = true;
	}
	
	private void updateTransform() {
		transform.redefine( scaled );
		transform.multiply( rotate );
		transform.multiply( translate );
		changed = false;
	}
	
	public void update() {
		if( changed ) updateTransform();
	}
	
	@Override
	public void process( double dt ) { update(); }
	
	@Override
	public void destroy() {
		setKeyboardControlled( false );
		setMouseControlled( false );
	}
	
	/* ------------ controllable implementation ------------- */
	
	private boolean keyboardListening = false;	
	private boolean keyboardControlled = false;
	
	public boolean isKeyboardControlled() { return keyboardControlled; }
	public void setKeyboardControlled( boolean val ) {
		keyboardControlled = val;
		if( val && !keyboardListening ) {
			GLEvent.addKeyEventListener( this );
			keyboardListening = true;
		} else if( keyboardListening ) {
			GLEvent.removeKeyEventListener( this );
			keyboardListening = false;
		}
	}
	
	private boolean mouseListening = false;
	private boolean mouseControlled = false;
	
	public boolean isMouseControlled() { return mouseControlled; }
	public void setMouseControlled( boolean val ) {
		mouseControlled = val;
		if( val && !mouseListening ) {
			GLEvent.addMouseEventListener( this );
			mouseListening = true;
		} else if( mouseListening ) {
			GLEvent.removeMouseEventListener( this );
			mouseListening = false;
		}
	}
	
	@Override public void processMouseMoveEvent( MouseMoveEvent e ) {}
	@Override public void processMouseDownEvent( MouseDownEvent e ) {}
	@Override public void processMouseUpEvent( MouseUpEvent e ) {}
	@Override public void processMouseRepeatEvent( MouseRepeatEvent e ) {}
	@Override public void processMouseWheelEvent( MouseWheelEvent e ) {}
	
	@Override public void processKeyDownEvent( KeyDownEvent e ) {}
	@Override public void processKeyUpEvent( KeyUpEvent e ) {}
	@Override public void processKeyRepeatEvent( KeyRepeatEvent e ) {}
	
}
