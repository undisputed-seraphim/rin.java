package rin.gl;

import java.lang.reflect.Type;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.SharedDrawable;

public class GLWorkerThread extends Thread {

	private boolean destroyRequested = false;
	private SharedDrawable drawable;
	
	public static ConcurrentLinkedQueue<Runnable> sources = new ConcurrentLinkedQueue<Runnable>();
	
	public GLWorkerThread() {

		try {
			this.drawable = new SharedDrawable( Display.getDrawable() );
		} catch( LWJGLException e ) {
			System.out.println( "[FATAL ERROR] Could not Share opengl context with Worker Thread." );
			System.exit( 0 );
		}
		
		this.start();
	}
	
	@Override
	public void run() {
		
		try {
			this.drawable.makeCurrent();
		} catch( LWJGLException e ) {
			System.out.println( "[FATAL ERROR] Unable to make GL context current!" );
			System.exit( 0 );
		}
		
		Runnable tmp;
		while( !this.destroyRequested ) {
			while( (tmp = GLWorkerThread.sources.poll() ) != null )
				tmp.run();
		}
		
	}
	
	public void requestDestroy() { this.destroyRequested = true; }
	
}
