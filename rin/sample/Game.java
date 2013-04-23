package rin.sample;

import java.util.ArrayList;

import static rin.engine.Engine.*;
import rin.engine.Engine;
import rin.engine.resource.ResourceManager;
import rin.engine.resource.formats.ism2.ISM2Decoder;
import rin.engine.resource.formats.pssg.PSSGDecoder;
import rin.engine.view.gui.GUIManager;
import rin.gl.event.Transition;
import rin.gl.lib3d.Actor;
import rin.gl.lib3d.Poly;
import rin.gl.lib3d.interfaces.Transitionable;
import rin.gl.lib3d.shape.*;
import rin.gl.model.ModelManager;
import rin.system.LoaderEvent;
import rin.util.RinUtils;
import static rin.system.RInput.*;
import static rin.engine.view.gui.GUIFactory.*;

public class Game {
	public static void main( String args[] ) {
		
		//ResourceManager.getDecoder( PSSGDecoder.class ).decode( ResourceManager.getPackResource( "meruru", "models", "meruru", "meruru.pssg" ) );
		//States.STATE_GAME.push();
		new ISM2Decoder( ResourceManager.getPackResource( "rin", "002.ism2" ) );
		//trackInput();

		/*createDesktop( "yup" )
		.setSize( 300, 300 )
		.setMouseListening( true )
		.onWindowClosing( new WindowEvent() {
			public void run() {
				Engine.requestDestroy();
			}
		})
		.add( createMenuBar( "mb" ).add( createMenu( "one" ) .add( createMenuItem( "first" ) ) ) )
		.add( createToolBar().add( createButton( "button 1" ) ) )
		.add( createDesktopWindow().add( createButton( "b1", "hi" ).onClick( new ButtonEvent() {
			@Override public void run() {
				Engine.requestDestroy();
			}
		}) ).show() )
		.show();*/
		
		//Engine.start( new MyGame() );
		//getPanel( "p1" ).add( getButton( "b1" ) ).destroy();
		
		//getWindow( "yup" ).destroy();
		//GUIManager.print();
		//System.out.println( ResourceManager.getPackResource( "meruru", "models", "meruru", "meruru.pssg" ) );
		//init( 900, 600 );
		
		/*getScene().addModel( new ModelParams( ModelFormat.OBJ, "hyrulefield" ) ).onLoad( new LoaderEvent<Actor>() {
			public void handle() {
				System.out.println( this.target + "yes" );
			}
		});*/
		
		/*getScene().addModel( new ModelParams( ModelFormat.PSSG, "meruru", "meruru/models" ) ).onLoad( new LoaderEvent<Actor>() {
			public void handle() {
				System.out.println( this.target.getName() );
			}
		});*/
		//getScene().addModel( new ModelParams( ModelFormat.DAE, "noire_v" ) );
		
		//start();
		
		//Engine.init( 900, 600 );
		/*ActorList.init();
		GLRenderThread.init( 900, 600 );
		
		GLRenderThread.addActor( Format.DAE, "noire_v" );*/
		//Engine.getScene().addActor( ModelManager.create( Format.OBJ, "cornelia" ) );
		/*Engine.getScene().addActor( ModelManager.create( Format.DAE, "noire_v" ) );
		Engine.getScene().addActor( new Sphere( 5.0f, 50, true ) );
		Engine.getScene().addActor( new Icosahedron( 5.0f, true ) );
		Engine.getScene().addActor( new Tetrahedron( 5.0f, true ) );
		Engine.getScene().addActor( new Cuboid( 10.0f, true ) );
		Engine.getScene().addActor( new Octahedron( 5.0f, true ) );
		Engine.getScene().addActor( new Dodecahedron( 5.0f, true ) );*/
		//Engine.getScene().addActor( new Tile() );
		//new Transition( (Transitionable)Engine.getScene().getActor( 1 ) ).start();
		//GLRenderThread.add( new Sphere( 5.0f, 50, true ) );
		//GLRenderThread.addActor( Format.DAE, "noire_v" );
		//Engine.start();
	}
}