package rin.gl;

import org.lwjgl.input.Keyboard;

import rin.gl.GLEvent.*;

public class Input {
	public static void process() {
		if( Keyboard.isKeyDown( Keyboard.KEY_W ) ) GLEvent.fire( new KeyEvent( Keyboard.KEY_W ) );
		if( Keyboard.isKeyDown( Keyboard.KEY_S ) ) GLEvent.fire( new KeyEvent( Keyboard.KEY_S ) );
		if( Keyboard.isKeyDown( Keyboard.KEY_A ) ) GLEvent.fire( new KeyEvent( Keyboard.KEY_A ) );
		if( Keyboard.isKeyDown( Keyboard.KEY_D ) ) GLEvent.fire( new KeyEvent( Keyboard.KEY_D ) );
	}
}
