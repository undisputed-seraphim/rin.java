package rin.engine.lib.gl;

import java.awt.Canvas;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class GLCanvas {
	private JFrame window;
	private Canvas canvas;
	
	private boolean closeRequested = false;
	
	public GLCanvas() {
		window = new JFrame();
		window.addWindowListener( new WindowAdapter() {
			@Override public void windowClosing( WindowEvent e ) {
				closeRequested = true;
			}
		});
		window.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
		
		canvas = new Canvas() {
			private static final long serialVersionUID = 1L;
			@Override public final void addNotify() {
				super.addNotify();
				startGL();
			}
			@Override public final void removeNotify() {
				stopGL();
				super.removeNotify();
			}
		};
		window.add( canvas );
		
		try {
			Display.setParent( canvas );
		} catch( LWJGLException ex ) {
			System.err.println( "GLCanvas(): LWJGLException raised." );
			ex.printStackTrace();
		}
	}
	
	public void setTitle( String title ) {
		window.setTitle( title );
	}
	
	public void startGL() {
		try {
			Display.create();
		} catch( LWJGLException ex ) {
			System.err.println( "GLCanvas#startGL(): LWJGLException raised." );
			ex.printStackTrace();
		}
	}
	
	public void stopGL() {
		Display.destroy();
	}
	
	public void show() {
		window.setVisible( true );
		window.revalidate();
	}
	
	public void hide() {
		window.setVisible( false );
		window.revalidate();
	}
	
	public void update() {
		Display.sync( 60 );
		Display.update();
	}
	
	public void close() {
		closeRequested = true;
		stopGL();
		window.setVisible( false );
		window.dispose();
	}
	
	public void resize( int width, int height ) {
		window.setSize( width, height );
		try {
			Display.setDisplayMode( new DisplayMode( width, height ) );
		} catch( LWJGLException ex ) {
			System.err.println( "GLCanvas#resize(int,int): LWJGLException raised." );
			ex.printStackTrace();
		}
		window.revalidate();
	}
	
	public boolean isClosed() {
		if( closeRequested )
			close();
		return closeRequested;
	}
}
