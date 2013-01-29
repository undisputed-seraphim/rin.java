package rin.gl;

import rin.gl.GLEvent.*;

public interface GLEventListener {
	public interface KeyEventListener extends GLEventListener {
		public void processKeyEvent( KeyEvent e );
	}
	
	public interface MouseEventListener extends GLEventListener {
		public void processMouseEvent( MouseEvent e );
	}
}
