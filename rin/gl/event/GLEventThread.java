package rin.gl.event;

import java.util.ArrayList;

public class GLEventThread extends Thread {
	public static ArrayList<GLEvent> queue = new ArrayList<GLEvent>();
	public static int items = 0;
	
	public static boolean destroyRequested = false;
	public static void requestDestroy() { GLEventThread.destroyRequested = true; }
	
	public GLEventThread() { super( "rin.ai | GLEventThread" ); }
	
	public static GLEvent fire( GLEvent e ) {
		synchronized ( GLEventThread.queue ) {
			GLEventThread.queue.add( e );
			GLEventThread.items++;
		}
		return e;
	}
	
	@Override public void run() {
		while( !GLEventThread.destroyRequested ) {
			synchronized( GLEventThread.queue ) {
				for( int i = 0; i < GLEventThread.items; i++ ) {
					((Transition)GLEventThread.queue.get( i )).update();
				}
			}
			
			try {
				Thread.sleep( 1L );
			} catch( InterruptedException e ) {
				System.out.println( "GLEventThread interrupted! Destroying..." );
				GLEventThread.destroyRequested = true;
			}
		}
		this.destroy();
	}
	
	@Override public void destroy() {
		System.out.println( "GLEventThread ended." );
	}
}
