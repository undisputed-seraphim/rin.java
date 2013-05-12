package rin.engine.view;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class View {
	public void init() {
		startGL();
	}

	public void startGL() {
		try {
			Display.setDisplayMode( new DisplayMode( 900, 600 ) );
			Display.setVSyncEnabled( true );
			Display.create();
		} catch( LWJGLException ex ) {
			System.err.println( "GLCanvas#startGL(): LWJGLException raised." );
			ex.printStackTrace();
		}
	}
	
	public void stopGL() {
		Display.destroy();
	}
	
	public void setSize( int width, int height ) {
		try {
			Display.setDisplayMode( new DisplayMode( width, height ) );
		} catch( LWJGLException ex ) {
			System.err.println( "GLCanvas#resize(int,int): LWJGLException raised." );
			ex.printStackTrace();
		}
	}
	
	public void update() {
		Display.sync( 60 );
		Display.update();
	}
	
	public boolean isClosed() { return Display.isCloseRequested(); }
	
	public void setTitle( String title ) { Display.setTitle( title ); }

	public void destroy() { stopGL(); }
}
