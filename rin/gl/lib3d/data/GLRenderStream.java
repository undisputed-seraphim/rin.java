package rin.gl.lib3d.data;

import java.util.ArrayList;

public class GLRenderStream extends Thread {
	
	private static ArrayList<GLStream> streams = new ArrayList<GLStream>();
	public static boolean ready = false;
	
	public GLRenderStream() {
		super( "rin.ai | Render Thread" );
	}
	
	public void run() {
	}
	
}
