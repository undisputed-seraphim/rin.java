package rin.engine.scene;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import rin.gl.event.GLEvent;
import rin.gl.event.GLEvent.KeyDownEvent;
import rin.gl.event.GLEvent.KeyRepeatEvent;
import rin.gl.event.GLEvent.KeyUpEvent;
import rin.gl.event.GLEvent.MouseDownEvent;
import rin.gl.event.GLEvent.MouseEvent;
import rin.gl.event.GLEvent.MouseMoveEvent;
import rin.gl.event.GLEvent.MouseRepeatEvent;
import rin.gl.event.GLEvent.MouseUpEvent;
import rin.gl.event.GLEvent.MouseWheelEvent;

public class Input {
	public static void process() {
		while( Keyboard.next() ) {
			
			//key down
			if( Keyboard.getEventKeyState() )
				GLEvent.fire( new KeyDownEvent( Keyboard.getEventKey() ) );
			
			//key up
			else
				GLEvent.fire( new KeyUpEvent( Keyboard.getEventKey() ) );

		}
		
		//key repeat
		if( Keyboard.isKeyDown( Keyboard.KEY_A ) ) GLEvent.fire( new KeyRepeatEvent( Keyboard.KEY_A ) );
		if( Keyboard.isKeyDown( Keyboard.KEY_B ) ) GLEvent.fire( new KeyRepeatEvent( Keyboard.KEY_B ) );
		if( Keyboard.isKeyDown( Keyboard.KEY_C ) ) GLEvent.fire( new KeyRepeatEvent( Keyboard.KEY_C ) );
		if( Keyboard.isKeyDown( Keyboard.KEY_D ) ) GLEvent.fire( new KeyRepeatEvent( Keyboard.KEY_D ) );
		if( Keyboard.isKeyDown( Keyboard.KEY_W ) ) GLEvent.fire( new KeyRepeatEvent( Keyboard.KEY_W ) );
		if( Keyboard.isKeyDown( Keyboard.KEY_S ) ) GLEvent.fire( new KeyRepeatEvent( Keyboard.KEY_S ) );
		
		if( Keyboard.isKeyDown( Keyboard.KEY_UP ) ) GLEvent.fire( new KeyRepeatEvent( Keyboard.KEY_UP ) );
		if( Keyboard.isKeyDown( Keyboard.KEY_DOWN ) ) GLEvent.fire( new KeyRepeatEvent( Keyboard.KEY_DOWN ) );
		if( Keyboard.isKeyDown( Keyboard.KEY_LEFT ) ) GLEvent.fire( new KeyRepeatEvent( Keyboard.KEY_LEFT ) );
		if( Keyboard.isKeyDown( Keyboard.KEY_RIGHT ) ) GLEvent.fire( new KeyRepeatEvent( Keyboard.KEY_RIGHT ) );
		
		if( Keyboard.isKeyDown( Keyboard.KEY_LSHIFT ) ) GLEvent.fire( new KeyRepeatEvent( Keyboard.KEY_LSHIFT ) );
		if( Keyboard.isKeyDown( Keyboard.KEY_SPACE ) ) GLEvent.fire( new KeyRepeatEvent( Keyboard.KEY_SPACE ) );
		
		//TODO: don't allow mouse events to propagate if glwindow is not in front
		//if( Display.isVisible() ) {
			while( Mouse.next() ) {
				
				//mouse move
				if( Mouse.getEventButton() == -1 && Mouse.getEventDWheel() == 0 )
					GLEvent.fire( new MouseMoveEvent( Mouse.getEventButton(), Mouse.getEventX(), Mouse.getEventY(),
							Mouse.getEventDX(), Mouse.getEventDY() ) );
				
				//mouse wheel
				else if( Mouse.getEventDWheel() != 0 )
					GLEvent.fire( new MouseWheelEvent( Mouse.getEventX(), Mouse.getEventY(), Mouse.getEventDWheel() ) );
				
				//mouse down
				else if( Mouse.getEventButtonState() )
					GLEvent.fire( new MouseDownEvent( Mouse.getEventButton(), Mouse.getEventX(), Mouse.getEventY() ) );
				
				//mouse up
				else
					GLEvent.fire( new MouseUpEvent( Mouse.getEventButton(), Mouse.getEventX(), Mouse.getEventY() ) );
				
			}
			
			//mouse repeat
			if( Mouse.isButtonDown( 0 ) ) GLEvent.fire( new MouseRepeatEvent( MouseEvent.BUTTON_LEFT, Mouse.getX(), Mouse.getY() ) );
			if( Mouse.isButtonDown( 1 ) ) GLEvent.fire( new MouseRepeatEvent( MouseEvent.BUTTON_RIGHT, Mouse.getX(), Mouse.getY() ) );
		//}
	}
}
