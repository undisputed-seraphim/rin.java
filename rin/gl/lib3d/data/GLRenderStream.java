package rin.gl.lib3d.data;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class GLRenderStream extends Thread {
	private LinkedBlockingQueue<GLStream> streams = new LinkedBlockingQueue<GLStream>();
	public static boolean ready = false;
	
	public GLRenderStream() {
		super( "rin.ai | Render Thread" );
		GLRenderStream.ready = true;
	}
	
	public void run() {
		while( GLRenderStream.ready ) {
			if( this.streams.size() > 0 ) {
				System.out.println( "in thread" );
			}
		}
	}
}
