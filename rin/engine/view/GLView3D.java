package rin.engine.view;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.input.Keyboard;

import rin.engine.scene.GLScene3D;
import rin.engine.view.gl.GL;
import rin.engine.view.gl.GLEvent;
import rin.engine.view.gl.GLEvent.KeyDownEvent;
import rin.engine.view.gl.GLEvent.KeyRepeatEvent;
import rin.engine.view.gl.GLEvent.KeyUpEvent;
import rin.engine.view.gl.GLEventListener.KeyEventListener;

public class GLView3D extends ViewAdapter implements KeyEventListener {
	
	private boolean created = false;
	private boolean paused = false;
	
	private double dt = 0.0;
	private double start = 0.0;
	
	@Override public GLScene3D getScene() { return (GLScene3D)scene; }
	
	@Override
	public void init() {
		System.out.println( "GLView3D#init()" );
		initGL();
		setScene( new GLScene3D() );
		setKeyboardControlled( true );
	}
	
	private void initGL() {
		try {
			Display.setDisplayMode( new DisplayMode( width, height ) );
			Display.setVSyncEnabled( true );
			Display.create();
			created = true;
			GL.initDefaults3D();
			setSize( width, height );
		} catch( LWJGLException ex ) {
			System.err.println( "GLView3D#startGL(): LWJGLException raised." );
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
			System.err.println( "GLView3D#setSize(int,int): LWJGLException raised." );
			ex.printStackTrace();
		}
		
		if( created ) GL.setSize( width, height );
	}
	
	private void updateDt() {
		dt = System.nanoTime() * 1e-9 - start;
		start += dt;
	}
	
	private double average = 0.0;
	private int count = 0;
	
	@Override
	public void show() {
		if( !created ) {
			System.err.println( "Cannot GLView3D#show() before GLView3D#init()!" );
			return;
		}
		
		start = System.nanoTime() * 1e-9;
		while( !Display.isCloseRequested() ) {
			updateDt();
			glClear( GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT );
			
			Input.process();
			
			if( !paused ) {
				getScene().process( dt );
			} else {
				getScene().process( 0.0 );
			}
			
			Display.sync( 60 );
			Display.update();
			average += dt;
			count++;
			if( count % 100 == 0 )
				Display.setTitle( "frame " + count + " in " + dt + " seconds (average: " + (average/count) + " sec)" );
		}
	}
	
	@Override
	public void destroy() {
		stopGL();
	}
	
	private void stopGL() {
		System.out.println( "GLView3D#destroy()" );
		if( scene != null ) scene.destroy();
		GL.destroy();
		Display.destroy();
	}
	
	public void setTitle( String title ) { Display.setTitle( title ); }

	private boolean keyboardListening = false;	
	private boolean keyboardControlled = false;
	
	public boolean isKeyboardControlled() { return keyboardControlled; }
	public void setKeyboardControlled( boolean val ) {
		keyboardControlled = val;
		if( val && !keyboardListening ) {
			GLEvent.addKeyEventListener( this );
			keyboardListening = true;
		} else if( keyboardListening ) {
			GLEvent.removeKeyEventListener( this );
			keyboardListening = false;
		}
	}
	
	@Override
	public void processKeyDownEvent( KeyDownEvent e ) {
		switch( e.key ) {
		
		case Keyboard.KEY_P:
			paused = !paused;
			break;
			
		}
	}

	@Override public void processKeyUpEvent( KeyUpEvent e ) {}
	@Override public void processKeyRepeatEvent( KeyRepeatEvent e ) {}
	
}
