package rin.engine.game;

import static org.lwjgl.opengl.GL11.glViewport;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import rin.engine.scene.gl.GLScene3D;

public class GLGame3D implements Game {

	private boolean running = false;
	private GLScene3D scene;
	
	private int w = 900;
	private int h = 600;
	
	public GLGame3D() { this( "" ); }
	public GLGame3D( String name ) {
		Display.setTitle( name );
	}
	
	@Override
	public void init() {
		System.out.println( "GLGame3D#init()" );
		
		try {
			setSize( w, h );
			Display.setVSyncEnabled( true );
			Display.create();
			
			scene = new GLScene3D();
			scene.init();
			glViewport( 0, 0, w, h );
		} catch( LWJGLException ex ) {
			System.err.println( "GLGame3D#init(): LWJGLException raised." );
			ex.printStackTrace();
		}
	}

	public void setWidth( int width ) { w = width; setSize( w, h ); }
	public void setHeight( int height ) { h = height; setSize( w, h ); }
	
	@Override
	public void setSize( int width, int height ) {
		w = width;
		h = height;
		try {
			Display.setDisplayMode( new DisplayMode( w, h ) );
		} catch( LWJGLException ex ) {
			System.err.println( "GLGame3D#setSize(int,int): LWJGLException raised." );
			ex.printStackTrace();
		}
	}
	
	@Override
	public GLScene3D getScene() { return scene; }
	
	@Override
	public void start() {
		running = true;
		while( !Display.isCloseRequested() && running ) {
			scene.process();
			Display.sync( 60 );
			Display.update();
		}
		if( running ) stop();
	}
	
	@Override
	public void stop() {
		System.out.println( "GLGame3D#stop()" );
		running = false;
		scene.destroy();
		Display.destroy();
	}

	@Override
	public void destroy() {
		System.out.println( "GLGame3D#destroy()" );
	}

	@Override
	public void run() {
		init();
		start();
		System.out.println( "GLGame3D#run(): out of start method." );
		destroy();
	}
	
}
