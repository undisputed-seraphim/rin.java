package rin.engine;

import javax.swing.SwingUtilities;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static rin.gui.GUIManager.*;

import rin.gl.Scene;
import rin.gl.event.GLEventThread;
import rin.gl.lib3d.ActorList;
import rin.gui.*;

public class Engine {
	public static final String LS = System.getProperty( "file.separator" );
	public static final String OS = System.getProperty( "os.name" ).toLowerCase();
	public static final String ROOT = OS.indexOf( "window" ) != -1 ? "C:" : "";
	public static final String USER = OS.indexOf( "window" ) != -1 ? "johall" : "Musashi";
	public static final String MAINDIR = ROOT + LS + "Users"+LS+USER+LS+"Desktop"+LS+"Horo"+LS+"rin.java"+LS+"rin"+LS;
	public static final String ROOTDIR = OS.indexOf( "linux" ) != -1 ? LS+"media"+LS+"sf_Horo"+LS+"rin.java"+LS+"rin"+LS : MAINDIR;
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
		Thread.currentThread().setName( "rin.ai | Render Thread" );
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
		createWindow()
				.setTitle( "rin.ai | Debug" )
				.setLocation( 20, 20 )
				.setBackgroundColor( 233, 233, 233, 175 )
				.onWindowFocusGained( new WindowEvent() {
					public void run() {
						this.target.setTitle( "booya" + System.nanoTime() );
					}
				})
				.add( createContainer()
						.setAlignment( Alignment.CENTER )
						.add( createCheckBox().setLabel( "test" ).setBackgroundColor( 70, 70, 70 ) )
						.add( createPanel()
								.setBackgroundColor( 70, 70, 70, 175 )
								.setAlignment( Alignment.CENTER )
								.add( createColumns( 2 )
										.add( 1, createCheckBox().setLabel( "test" ) )
										.add( 1, createContainer()
												.add( createCheckBox() )
												.add( createCheckBox() ) )
										.add( 2, createTextField() )
										.add( 2, createButton().onClick( new ButtonEvent() {
											public void run() {
												System.out.println( "button clicked!" );
											}
										}) )
								)
						)
						.add( createCheckBox() )
						.add( createTabbedPane()
								.onTabChange( new TabbedPaneEvent() {
									public void run() {
										System.out.println( this.previous + " " + this.current + " " + this.target.getId() );
									}
								})
								.addTab( "Overall", createContainer().onFocus( new ContainerEvent() {
									public void run() {
										System.out.println( "FOCUSED OVERALL" );
									}
								}) )
								.addTab( "Actors", createContainer() )
								.addTab( "Misc", createContainer() )
						)
				).show();
	}
	
	public static void start() {
		//new Input( "Rin Input/Event Thread" ).start();
		Engine.loop();
		Engine.destroy();
	}
				
	/* You're Fun. */
	
	public static void loop() {
		while( !Display.isCloseRequested() ) {
			Engine.scene.update();
			Display.sync( 60 );
			Display.update();
		}
	}
	
	public static void stop() { Engine.destroy(); }
	
	public static void destroy() {
		GLEventThread.requestDestroy();
		ActorList.requestDestroy();

		if( Engine.scene != null )
			Engine.scene = Engine.scene.destroy();
		
		GUIManager.destroy();
		//Input.requestDestroy();
		
		Display.destroy();
	}
}