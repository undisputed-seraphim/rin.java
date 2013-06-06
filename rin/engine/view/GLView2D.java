package rin.engine.view;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import rin.engine.scene.GLScene2D;
import rin.engine.view.gl.GL;

public class GLView2D extends ViewAdapter {

	private boolean created = false;
	private double dt = 0.0;
	private double start = System.nanoTime() * 1e-9;
	
	@Override public GLScene2D getScene() { return (GLScene2D)scene; }
	
	@Override
	public void init() {
		System.out.println( "GLView2D#init()" );
		initGL();
		setScene( new GLScene2D() );
	}
	
	private void initGL() {
		try {
			Display.setDisplayMode( new DisplayMode( width, height ) );
			Display.setVSyncEnabled( true );
			Display.create();
			created = true;
			GL.initDefaults2D();
			setSize( width, height );
		} catch( LWJGLException ex ) {
			System.err.println( "GLView2D#startGL(): LWJGLException raised." );
			ex.printStackTrace();
		}
	}
	
	@Override
	public void setSize( int w, int h ) {
		width = w;
		height = h;

		try {
			Display.setDisplayMode( new DisplayMode( width, height ) );
		} catch( LWJGLException ex ) {
			System.err.println( "GLView2D#setSize(int,int): LWJGLException raised." );
			ex.printStackTrace();
		}
		
		if( created ) GL.setSize( width, height );
	}
	
	private void updateDt() {
		dt = System.nanoTime() * 1e-9 - start;
		start += dt;
	}
	
	@Override
	public void show() {
		if( !created ) {
			System.err.println( "Cannot GLView2D#show() before GLView2D#init()!" );
			return;
		}
		
		while( !Display.isCloseRequested() ) {
			updateDt();
			
			if( !Keyboard.isKeyDown( Keyboard.KEY_P ) ) {
				glClear( GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT );
				
				Input.process();
				getScene().process( dt );
				
				Display.sync( 60 );
				Display.update();
			} else {
				Keyboard.poll();
			}
		}
	}
	
	@Override
	public void destroy() {
		stopGL();
	}
	
	private void stopGL() {
		System.out.println( "GLView2D#destroy()" );
		if( scene != null ) scene.destroy();
		GL.destroy();
		Display.destroy();
	}
	
	public void setTitle( String title ) { Display.setTitle( title ); }
	
}
