package rin.gl.lib3d;

import org.lwjgl.input.Keyboard;

public class Controllable extends Pickable {
	private boolean controlled = false;
	
	public boolean isControlled() { return this.controlled; }
	public void setControlled( boolean val ) { this.controlled = val; }
	
	public void processInput() {
		if( this.controlled ) {
			boolean changed = false;
			float	step = 0.0f,
					side = 0.0f,
					rise = 0.0f;
			
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
		}
	}
}
