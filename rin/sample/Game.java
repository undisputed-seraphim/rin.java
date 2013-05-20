package rin.sample;

import java.util.ArrayList;

import static rin.engine.Engine.*;
import rin.engine.Engine;
import rin.engine.resource.FormatManager;
import rin.engine.resource.Resource;
import rin.engine.resource.ResourceManager;
import rin.engine.resource.audio.acb.AcbDecoder;
import rin.engine.resource.image.ImageOptions;
import rin.engine.resource.image.tid.TidDecoder;
import rin.engine.util.ArrayUtils;
import rin.engine.lib.gui.GUIManager;
import rin.gl.GL;
import rin.gl.event.Transition;
import rin.gl.lib3d.Actor;
import rin.gl.lib3d.Poly;
import rin.gl.lib3d.interfaces.Transitionable;
import rin.gl.lib3d.shape.*;
import rin.gl.model.ModelManager;
import rin.system.LoaderEvent;
import rin.util.RinUtils;
import static rin.system.RInput.*;
import static rin.engine.lib.gui.GUIFactory.*;

public class Game {
	public static void main( String[] args ) {
		//getView().init();
		
		//ResourceManager.getDecoder( PSSGDecoder.class ).decode( ResourceManager.getPackResource( "meruru", "models", "meruru", "meruru.pssg" ) );
		//States.STATE_GAME.push();
		//new ModelISM2( .getURL().getPath() );
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
		//new GMODecoder( ResourceManager.getPackResource( "dissidia", "models", "test", "test.gmo" ) );
		/*init( 900, 600 );
		getScene().addModel( ResourceManager.getPackResource( "neptunia_mk2", "test.ism2" ) );
		start();*/
		//new AcbDecoder( ResourceManager.getPackResource( "neptunia_v", "models", "001", "sounds", "sound001.acb" ) );
		//FormatManager.extractAll( ResourceManager.getPackDirectory( "neptunia_v", "models", "001", "sfx" ), "acb" );
		/*for( Resource r : ResourceManager.getPackDirectory( "neptunia_mk2", "special" ).getResourcesByExtension( ".tid" ) ) {
			FormatManager.decodeImage( r, new ImageOptions().saveAs( "png" ).setDeleteOnSave( true ) );
		}*/
		
		init( 900, 600 );
		GL.addModel( ResourceManager.getPackResource( "neptunia_mk2", "models", "player", "006", "002.ism2" ) );
		//GL.addModel( ResourceManager.getPackResource( "meruru", "models", "meruru", "meruru.pssg" ) );
		start();
		
		//FormatManager.decodeModel( ResourceManager.getPackResource( "meruru", "models", "meruru", "meruru.pssg" ) );
		//FormatManager.decodeModel( ResourceManager.getPackResource( "neptunia_v", "models", "001", "001.ism2" ) );
		//FormatManager.decodeImage( ResourceManager.getPackResource( "neptunia_mk2", "special", "2001.tid" ), new ImageOptions().saveAs( "png" ) );
		//new CL3Extractor( ResourceManager.getPackDirectory( "neptunia_v", "models", "001", "motion" ) );
		//getScene().addModel( ModelFormat.PSSG, ResourceManager.getPackResource( "meruru", "models", "meruru", "meruru.pssg" ) );
		//start();
		/*getScene().addModel( new ModelParams( ModelFormat.OBJ, "hyrulefield" ) ).onLoad( new LoaderEvent<Actor>() {
			public void handle() {
				System.out.println( this.target + "yes" );
			}
		});*/
		
		//new CL3Extractor( ResourceManager.getPackDirectory( "rin" ) );
		
		//new BRRESDecoder( ResourceManager.getPackResource( "rin", "test.brres" ) );
		//new ACBDecoder( ResourceManager.getPackResource( "rin", "001.acb" ) );
		//new ISM2Decoder( ResourceManager.getPackResource( "rin", "001.ism2" ) );
		
		
		//System.out.println( ResourceManager.getPackResource( "rin", "003.ism2" ).getDirectory().getResource( "003.ism2" ).getPath() );
		//new CL3Decoder( ResourceManager.getPackResource( "rin", "001.cl3" ) );
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