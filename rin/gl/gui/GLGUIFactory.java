package rin.gl.gui;

import rin.gl.GL;

public class GLGUIFactory {
	
	public GLPane getPane( String name ) { return (GLPane)GL.get().getGUI().getComponent( name ); }
	public GLPane createPane( String id ) {
		return new GLPane( id );
	}
	
	public static class GLGUIEvent implements Runnable {
		@Override public void run() {}
	}

}
