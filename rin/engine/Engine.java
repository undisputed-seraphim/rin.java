package rin.engine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import rin.gl.Scene;
import rin.gl.Input;
import rin.gui.*;

public class Engine {
	public static final String LS = System.getProperty( "file.separator" );
	public static final String OS = System.getProperty( "os.name" ).toLowerCase();
	public static final String ROOT = OS.indexOf( "mac" ) != -1 ? "" : "C:";
	public static final String USER = OS.indexOf( "mac" ) != -1 ? "Musashi" : "johall";
	public static final String ROOTDIR = ROOT + LS + "Users"+LS+USER+LS+"Desktop"+LS+"Horo"+LS+"rin.java"+LS+"rin"+LS;
	public static final String SHADER_DIR = ROOTDIR+"inc"+LS+"shaders"+LS;
	public static final String MODEL_DIR = ROOTDIR+"inc"+LS+"models"+LS;
	public static final String FONT_DIR = ROOTDIR+"inc"+LS+"fonts"+LS;
	
	public static boolean DEBUG = true;
	
	private static boolean started = false;
	public static boolean isStarted() { return Engine.started; }
	
	private static Scene scene = null;
	public static Scene getScene() { return Engine.scene; }

	public static void init() { Engine.init( 900, 600 ); }
	public static void init( int width, int height ) {
		try {
			Display.setDisplayMode( new DisplayMode( width, height ) );
			Display.create();
			Display.setVSyncEnabled( true );
			Engine.scene = new Scene( width, height );
			if( Engine.scene.isReady() ) {
				Engine.scene.getCamera().init();
				Engine.started = true;
			
				Engine.createDebugWindow();
			}
		} catch( LWJGLException e ) {
			System.out.println( "lwjgl instance failed to display [" + width + "x" + height + "]" );
		}
	}
	
	public static void createDebugWindow() {
		GUIComponent window = GUIManager.createWindow()
				.setTitle( "rin.ai | Debug" )
				.setSize( 250, 500 )
				.setLocation( 20, 20 )
				.setBackgroundColor( 233, 233, 233 )
				.show();
		
		GUIComponent tab1 = GUIManager.createContainer()
				.setBackgroundColor( 70, 70, 70 );
		
		GUIComponent checkBox = GUIManager.createCheckBox()
				.onTrue( new Runnable() {
					public void run() {
						System.out.println( "true selected" );
					}
				})
				.setLabel( "testing" )
				.setLabelPositionH( GUIManager.LEFT )
				.setBackgroundColor( 70, 70, 70 );
		
		GUIComponent div = GUIManager.createDiv()
				.add( checkBox )
				.addTo( tab1 );
		
		GUIComponent check = GUIManager.createCheckBox();
		
		GUIComponent div2 = GUIManager.createContainer()
				.add( check )
				.addTo( tab1 );
		
		GUIComponent tab2 = GUIManager.createPanel()
				.setBackgroundColor( 70, 70, 70 );
		
		GUIManager.createTabbedPane()
				.addTab( "Overall", tab1, 'O' )
				.addTab( "Actors", tab2, 'A' )
				.addTo( window );
	}
	
	public static void start() {
		//new Input( "Rin Input/Event Thread" ).start();
		Engine.loop();
		Engine.destroy();
	}
	
	public static void loop() {
		while( !Display.isCloseRequested() ) {
			Engine.scene.update();
			Display.sync( 60 );
			Display.update();
		}
	}
	
	public static void stop() { Engine.destroy(); }
	
	public static void destroy() {
		if( Engine.scene != null )
			Engine.scene = Engine.scene.destroy();
		
		GUIManager.destroy();
		//Input.requestDestroy();
		
		Display.destroy();
	}
}