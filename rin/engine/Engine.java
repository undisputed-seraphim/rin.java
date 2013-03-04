package rin.engine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static rin.gui.GUIFactory.*;

import rin.gl.Scene;
import rin.gl.event.GLEventThread;
import rin.gl.lib3d.ActorList;
import rin.gui.*;
import rin.util.Buffer;

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
		//Thread.currentThread().setName( "rin.ai | Render Thread" );
		try {
			Display.setDisplayMode( new DisplayMode( width, height ) );
			Display.create();
			Display.setVSyncEnabled( true );
			Engine.scene = new Scene( width, height );
			if( Engine.scene.isReady() ) {
				Engine.scene.getCamera().init();
				Engine.started = true;

				//Engine.createDebugWindow();
			}
		} catch( LWJGLException e ) {
			System.out.println( "lwjgl instance failed to display [" + width + "x" + height + "]" );
		}
	}

	public static void createDebugWindow() {
		new GUI() {
			@Override public void build() {
				createWindow( "#window1" )
						.setTitle( "rin.ai | Debug" )
						.setLocation( 20, 20 )
						.onWindowLoad( new OnLoadEvent() {
							@Override public void run() {
								System.out.println( "THIS WINDOW LOADED" );
							}
						})
						.addMenu( createMenuBar()
								.add( createMenu( "Menu 1" )
										.onOpen( new MenuEvent() {
											@Override public void run() {
												System.out.println( "menu open success" );
											}
										})
										.onWindowLoad( new OnLoadEvent() {
											@Override public void run() {
												System.out.println( "haha" );
											}
										})
										.add( createMenuItem( "One" ) )
										.add( createMenu( "Two" )
												.add( createMenuItem( "Two One" )
														.setShortcut( ModifierKey.ALT, "A" )
														.onSelect( new MenuItemEvent() {
															@Override public void run() {
																System.out.println( "shortcut worked" );
															}
														}))
												.addSeparator()
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
												.add( 1, createCheckBox( "asdf" ).setLabel( "test" ) )
												.add( 1, createContainer()
														.add( createCheckBox() )
														.add( createCheckBox() ) )
												.add( 2, createTextField().onEnter(new TextFieldEvent() {
													@Override public void run() {
														getCheckBox( "asdf" ).check();
													}
												}))
												
												.add( 2, createButton() )
										)
								)
								.add( createCheckBox() )
								.add( createTabbedPane()
										.addTab( "Overall", createContainer()
												.onFocus( new ContainerEvent() {
													@Override public void run() {
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
								.add( createScrollPane()
										.scrollToY( 100 )
										.setAlignment( Alignment.CENTER )
										.add( createCheckBox() )
										.add( createCheckBox() )
										.add( createCheckBox() )
										.add( createComboBox()
												.onSelect( new ComboBoxEvent() {
													@Override public void run() {
														System.out.println( "an option was selected " + this.currentIndex + " " + this.previousIndex + " " + this.value );
													}
												})
												.add( createComboItem( "Option One" ).onSelect( new ComboItemEvent() {
													@Override public void run() {
														System.out.println( "Option One selected." );
													}
												}) )
												.add( createComboItem( "Option Two" ).select() )
										)
										.add( createList()
												.add( createListItem( "Oneeeee" )
														.onSelect( new ListItemEvent() {
															@Override public void run() {
																System.out.println( Buffer.toString( this.selected ) );
															}
														})
														.onDeselect( new ListItemEvent() {
															@Override public void run() {
																System.out.println( " deselected " + this.text );
															}
														}) )
												.add( createListItem( "Twoooo" ) )
												.add( createListItem( "Threee" ) )
												.add( createListItem( "Threee" ) )
												.add( createListItem( "Threee" ) )
												.add( createListItem( "Threee" ) )
												.add( createListItem( "Threee" ).select() )
												.add( createListItem( "Threee" ) )
												.add( createListItem( "Threee" ) )
												.add( createListItem( "Threee" ) )
												.add( createListItem( "Threee" ) )
										)
										.add( createSlider() )
								)
						).show();
			}
		};
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
		//GLEventThread.requestDestroy();
		//ActorList.requestDestroy();

		if( Engine.scene != null )
			Engine.scene = Engine.scene.destroy();
		
		//GUIManager.destroy();
		//Input.requestDestroy();
		
		Display.destroy();
		System.exit( 0 );
	}
}