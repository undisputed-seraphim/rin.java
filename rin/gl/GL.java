package rin.gl;

import rin.gl.Scene;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.*;

public class GL {
	private static boolean ready = false,
						running = false,
						paused = false;
	
	private static Scene scene = null;
	private static boolean destroyRequested = false;
	private static Runnable onDestroy = null;
	
	private static int width = 900, height = 600;
	
	/* constructors */
	public static void create() { GL.create( 900, 600 ); }
	public static void create( int both ) { GL.create( both, both ); }
	public static void create( int width, int height ) {
		/* initialize scene */
		GL.width = width;
		GL.height = height;
		/* attempt to invoke the display's opengl context, then initialize scene */
		try {
			Display.setDisplayMode( new DisplayMode( width, height ) );
			Display.create();
			GL.running = true;
			GL.scene = new Scene( GL.width, GL.height );
		} catch( LWJGLException e ) {
			System.out.println( "lwjgl instance failed to display [" + GL.width + "x" + GL.height + "]" );
		}
	}
	
	/* getters */
	public static int getWidth() { return GL.width; }
	public static int getHeight() { return GL.height; }
	public static int getAttrib( String attr ) { return GL.scene.getAttrib( attr ); }
	public static int getUniform( String attr ) { return GL.scene.getUniform( attr ); }
	public static Scene getScene() { return GL.scene; }
	public static float getSceneDt() { return GL.scene.getDt(); }
	
	public static boolean isReady() { return GL.ready; }
	public static boolean isPaused() { return GL.paused; }
	public static boolean isRunning() { if( !GL.running ) return false; return !Display.isCloseRequested(); }
	
	/* initialize lwjgl wrapper object gl */
	public static void show() {
		GL.scene.show();
		GL.ready = true;
	}

	/* update this modules Display object */
	public static void update() {
		if( GL.destroyRequested ) {
			GL.destroy();
			return;
		} else if( Keyboard.isKeyDown( Keyboard.KEY_ESCAPE ) ) {
			GL.requestDestroy( new Runnable() {
				public void run() {
					System.out.println( "flawless exit." );
				}
			});
		}
		
		if( !GL.paused && GL.running ) {			
			if( GL.scene.isReady() ) {
				GL.scene.update();
				Display.sync( 60 );
				Display.update();
			}
		}
	}
	
	/* pause / unpause the opengl context */
	public static void pause() { GL.paused = true; }
	public static void unpause() { GL.paused = false; }

	/* hide the actual 3d display window */
	public static void forceDestroy() { GL.destroy(); }
	public static void requestDestroy() { GL.requestDestroy( null ); }
	public static void requestDestroy( Runnable onDestroy ) {
		GL.destroyRequested = true;
		GL.onDestroy = onDestroy;
	}
	
	private static void destroy() {
		GL.destroyRequested = false;
		GL.ready = false;
		GL.running = false;
		GL.paused = false;
		
		GL.scene = GL.scene != null ? GL.scene.destroy() : null;
		Display.destroy();
		
		if( GL.onDestroy != null )
			GL.onDestroy.run();
		
		GL.onDestroy = null;
	}
}