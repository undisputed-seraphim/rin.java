package rin.sample;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import rin.gl.GL;
import rin.system.GameState;

public class GSGL extends GameState {
	private static GL gl = null;
	public static GL getGL() { return GSGL.gl; }
	
	public GSGL( String name ) { super( name ); }
	
	@Override public void onEnter() {
		
	}
	
	@Override public void main() {
		
	}
	
	@Override public void onExit() {
		
	}
	
}
