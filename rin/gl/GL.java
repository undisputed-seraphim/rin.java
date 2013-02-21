package rin.gl;

import java.util.concurrent.ConcurrentLinkedQueue;

import rin.engine.Engine;
import rin.gl.lib3d.data.GLRenderThread;
import rin.gl.lib3d.data.GLSource;

public class GL extends Thread {	
	public static boolean destroyRequested = false;
	public static void requestDestroy() { GL.destroyRequested = true; }
	
	public static ConcurrentLinkedQueue<GLSource> sources = new ConcurrentLinkedQueue<GLSource>();
	public GL() {}
	
	public static int getAttrib( String attr ) { return Engine.getScene().getAttrib( attr ); }
	public static int getUniform( String attr ) { return Engine.getScene().getUniform( attr ); }
	
	public void destroy() {
		System.out.println( "Thread gone" );
	}
}