package rin.gl;

import rin.gl.Scene;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;

public class GL {
	public static String GL_DIR = "C:\\Users\\johall\\Desktop\\Horo\\rin.java\\rin\\inc";
	//public static String GL_DIR = "/Users/Musashi/Desktop/Horo/rin.java/rin/inc/";
	private boolean ready = false,
					running = false,
					paused = false;
	private int width,
				height;
	
	private Scene scene;
	
	/* constructors */
	public GL() { this( 900, 600 ); }
	public GL( int both ) { this( both, both ); }
	public GL( int width, int height ) { this.init( width,  height ); }
	
	/* getters */
	public int getWidth() { return this.width; }
	public int getHeight() { return this.height; }
	public Scene getScene() { return this.scene; }
	public boolean ready() { return this.ready; }
	public boolean paused() { return this.paused; }
	public boolean running() { if( !this.running ) return false; return !Display.isCloseRequested(); }
	
	/* initialize lwjgl wrapper object gl */
	public void init( int width, int height ) {
		this.width = width;
		this.height = height;
		
		this.scene = new Scene();
		this.ready = true;
	}

	/* update this modules Display object */
	public void update() {
		if( !this.paused && this.running ) {
			if( this.scene.ready() ) {
				this.scene.update();
				Display.sync( 60 );
				Display.update();
			}
		}
	}
	
	/* pause / unpause the opengl context */
	public void pause() { if( !this.paused ) this.paused = true; }
	public void unpause() { if( this.paused ) this.paused = false; }
	
	/* show the actual 3d Display window */
	public void show() { this.show( this.width, this.height ); }
	public void show( int both ) { this.show( both, both ); }
	public void show( int width, int height ) {
		this.width = width;
		this.height = height;
		

		/* attempt to invoke the display's opengl context, then initialize scene */
		try {
			Display.setDisplayMode( new DisplayMode( width, height ) );
			Display.create();
			this.scene.init( this.width, this.height );
			this.running = true;
		} catch (LWJGLException e) {
			System.out.println( "lwjgl instance failed to display [" + this.width + "x" + this.height + "]" );
		}
	}
	
	/* hide the actual 3d display window */
	public void hide() { Display.destroy(); }
	
	public String debug() {
		String str = "Debug of [GL object] '" + this + "' {\n" +
				"\t\t" + "ready: " + ( this.ready ? "true" : "false" ) + "\n" +
				"\t\t" + "paused: " + ( this.paused ? "true" : "false" ) + "\n" +
				"\t}";
		return str;
	}
}