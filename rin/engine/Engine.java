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
				.addMenu( createMenuBar()
						.add( createMenu( "Menu 1" )
								.onOpen( new MenuEvent() {
									public void run() {
										System.out.println( "menu open success" );
									}
								})
								.add( createMenuItem( "One" ) )
								.add( createMenu( "Two" )
										.add( createMenuItem( "Two One" )
												.setShortcut( ModifierKey.ALT, "A" )
												.onSelect( new MenuItemEvent() {
													public void run() {
														System.out.println( "shortcut worked" );
													}
												}))
										.add( createMenuItem( "Two Two" ) )
								)
								.add( createMenuItem( "Three" ) )
						)
						.add( createMenu( "Menu 2" ) )
						.addHorizontalSeparator()
						.add( createMenu( "Menu 2" ) )
						.addHorizontalSeparator()
						.add( createMenu( "Menu 3" ) )
				)
				.add( createContainer()
						.setAlignment( Alignment.CENTER )
						.add( createCheckBox().setLabel( "test" ) )
						.add( createPanel()
								.setAlignment( Alignment.CENTER )
								.add( createColumns( 2 )
										.add( 1, createCheckBox().setLabel( "test" ) )
										.add( 1, createContainer()
												.add( createCheckBox() )
												.add( createCheckBox() ) )
										.add( 2, createTextField().onEnter(new TextFieldEvent() {
											public void run() {
												System.out.println( this.value );
											}
										}))
										
										.add( 2, createButton().onClick( new ButtonEvent() {
											public void run() {
												GUIManager.getScrollPane( "asdf" ).showHorizontalScrollNever();
											}
										}) )
								)
						)
						.add( createCheckBox() )
						.add( createTabbedPane()
								.onTabChanged( new TabbedPaneEvent() {
									public void run() {
										System.out.println( this.source );
									}
								} )
								.addTab( "Overall", createContainer()
										.onFocus( new ContainerEvent() {
											public void run() {
												System.out.println( "FOCUSED OVERALL" );
											}
										})
										.add( createPanel()
												.setAlignment( Alignment.CENTER )
												.add( createPair()
														.setLeftItem( createCheckBox() )
														.setRightItem( createCheckBox() )
												)
										)
								)
								.addTab( "Actors", createContainer() )
								.addTab( "Misc", createContainer() )
						)
						.add( createScrollPane( "asdf" )
								.setAlignment( Alignment.CENTER )
								.add( createCheckBox() )
								.add( createCheckBox() )
								.add( createCheckBox() )
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
		
		SwingUtilities.invokeLater( GUIManager.DESTROY );
		//Input.requestDestroy();
		
		Display.destroy();
	}
}