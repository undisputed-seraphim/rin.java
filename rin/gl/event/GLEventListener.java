package rin.gl.event;

import rin.gl.event.GLEvent.*;

public interface GLEventListener {
	
	
	public interface KeyEventListener extends GLEventListener {
		public void processKeyDownEvent( KeyDownEvent e );
		public void processKeyUpEvent( KeyUpEvent e );
		public void processKeyRepeatEvent( KeyRepeatEvent e );
	}
	
	
	public interface MouseEventListener extends GLEventListener {
		public void processMouseMoveEvent( MouseMoveEvent e );
		public void processMouseDownEvent( MouseDownEvent e );
		public void processMouseUpEvent( MouseUpEvent e );
		public void processMouseRepeatEvent( MouseRepeatEvent e );
		public void processMouseWheelEvent( MouseWheelEvent e );
	}
	
	
	public interface PickEventListener extends GLEventListener {
		/** Perform actions when a PickEvent is fired.
		 * @param e PickEvent detailing mouse coordinates
		 */
		public void processPickEvent( PickEvent e );
	}
	
	
}
