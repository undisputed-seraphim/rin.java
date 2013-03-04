package rin.sample;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import rin.gl.GL;
import rin.gl.Scene;
import rin.gl.model.ModelManager;
import rin.engine.Engine;
import rin.system.GameState;
import static rin.gui.GUIFactory.*;

public class States {
	public static GameState TITLE = new GameState( "title" ) {

		@Override public void onEnter() {
			System.out.println( "Entering Title State" );
			this.setLooped( false );
		}
		
		@Override public void main() {
			/* show title screen logo thingy */
			new GUI() {
				public void build() {
					createWindow()
							.add( createContainer()
							.onClick( new ContainerEvent() {
								@Override public void run() {
									System.out.println( "window clicked" );
								}
							})
							.add( createLabel()
									.setImage( Engine.IMG_DIR + "logo.png" )
									.setSize( 200, 50 ) )
							.add( createButton()
									.setText( "Proceed" )
									.onClick( new ButtonEvent() {
										@Override public void run() {
											this.target.destroyWindow();
											States.TITLE.pop();
										}
									})
							)
					)
					.show();
				}
			};
		}
		
		@Override public void onExit() {
			System.out.println( "Exiting Title State" );
			States.MENU.push();
		}
		
	};
	
	public static GameState MENU = new GameState( "mainmenu" ) {

		@Override public void onEnter() {
			System.out.println( "Entering Menu State" );
			this.setLooped( false );
		}
		
		@Override public void main() {
			new GUI() {
				@Override public void build() {
					createWindow()
							.add( createContainer()
									.add( createButton()
											.setText( "300 x 300" )
											.onClick( new ButtonEvent() {
												@Override public void run() {
													States.GAME.setWidth( 300 );
													States.GAME.setHeight( 300 );
													this.target.destroyWindow();
													States.MENU.pop();
												}
											})
									)
									.add( createButton()
											.setText( "900 x 600" )
											.onClick( new ButtonEvent() {
												@Override public void run() {
													States.GAME.setWidth( 900 );
													States.GAME.setHeight( 600 );
													this.target.destroyWindow();
													States.MENU.pop();
												}
											})
									)
					)
					.show();
				}
			};
		}
		
		@Override public void onExit() {
			System.out.println( "Exiting Menu State" );
			States.GAME.push();
		}
		
	};
	
	public static GSGame GAME = new GSGame( "game" );
	public static class GSGame extends GameState {
		private Scene scene = null;
		public Scene getScene() { return this.scene; }
		
		private int width;
		public int getWidth() { return this.width; }
		public void setWidth( int width ) { this.width = width; }
		
		private int height;
		public int getHeight() { return this.height; }
		public void setHeight( int height ) { this.height = height; }

		public GSGame( String name ) { super( name ); }
		
		@Override public void onEnter() {
			System.out.println( "Entering Game State" );
			
			/* start the gl render thread */
			GL.init( States.GAME.getWidth(), States.GAME.getHeight() );
			GL.addModel( ModelManager.Format.DAE, "noire_v" );
			
			/* start the scene update thread */
			
		}
		
		@Override public void main() {
			//System.out.println( "in the game loop" );
		}
		
		@Override public void onExit() {
			System.out.println( "Exiting Game State" );
		}
		
	}
}
