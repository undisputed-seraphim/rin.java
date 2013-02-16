package rin.gl;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import rin.engine.Engine;
import rin.gl.lib3d.data.GLBuffer;
import rin.util.Buffer;

public class GL extends Thread {
	public static FutureTask<?> future = null;
	
	public static boolean destroyRequested = false;
	public static void requestDestroy() { GL.destroyRequested = true; }
	
	public GL() {}
	
	public static int getAttrib( String attr ) { return Engine.getScene().getAttrib( attr ); }
	public static int getUniform( String attr ) { return Engine.getScene().getUniform( attr ); }
	
	public static <T> T createBuffer( Class<T> cls, final int target, final int[] indices ) {
		GL.future = new FutureTask<GLBuffer>( new Callable<GLBuffer>() {
			public GLBuffer call() {
				GLBuffer res = new GLBuffer( target, indices );
				System.out.println( Buffer.toString( res.readi() ) );
				return res;
			}
		});
		T res = null;
		try {
			res = cls.cast( future.get() );
		} catch (InterruptedException e) {
			System.out.println( "GL#createBuffer caught an InterruptedException" );
		} catch (ExecutionException e) {
			System.out.println( "GL#createBuffer caught an ExecutionException" );
			e.printStackTrace();
		}
		GL.future = null;
		return res;
	}
	
	public void run() {
		System.out.println( "glthread started" );
		
		try {
			Display.setDisplayMode( new DisplayMode( 900, 600 ) );
			Display.create();
			Display.setVSyncEnabled( true );
		} catch( LWJGLException e ) {
			System.out.println( "lwjgl instance failed to display []" );
			GL.destroyRequested = true;
		}
		
		while( !GL.destroyRequested ) {
			if( GL.future != null ) {
				System.out.println( "here" );
				GL.future.run();
				System.out.println( "did it finish yet?" );
			}
			try {
				Thread.sleep( 3L );
			} catch( InterruptedException e ) {
				System.out.println( "GL Thread interrupted. Stopping..." );
				GL.destroyRequested = true;
			}
		}
		
		this.destroy();
	}
	
	public void destroy() {
		System.out.println( "Thread gone" );
	}
}