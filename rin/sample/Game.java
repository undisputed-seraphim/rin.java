package rin.sample;

import java.util.ArrayList;

import static rin.engine.Engine.*;
import rin.engine.Engine;
import rin.engine.resource.FormatManager;
import rin.engine.resource.ResourceManager;
import rin.engine.resource.formats.acb.ACBDecoder;
import rin.engine.resource.formats.brres.BRRESDecoder;
import rin.engine.resource.formats.cl3.CL3Decoder;
import rin.engine.resource.formats.cl3.CL3Extractor;
import rin.engine.resource.formats.gmo.GMODecoder;
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
		getScene().addModel( ResourceManager.getPackResource( "dissidia", "models", "test.gmo" ) );
		start();*/
		
		FormatManager.decodeModel( ResourceManager.getPackResource( "rin", "test.brres" ) );
		//getScene().addModel( ModelFormat.PSSG, ResourceManager.getPackResource( "meruru", "models", "meruru", "meruru.pssg" ) );
		/*getScene().addModel( new ModelParams( ModelFormat.OBJ, "hyrulefield" ) ).onLoad( new LoaderEvent<Actor>() {
			public void handle() {
				System.out.println( this.target + "yes" );
			}
		});*/
		
		//new CL3Extractor( ResourceManager.getPackDirectory( "rin" ) );
		
		//new BRRESDecoder( ResourceManager.getPackResource( "rin", "test.brres" ) );
		//new ACBDecoder( ResourceManager.getPackResource( "rin", "001.acb" ) );
		//new ISM2Decoder( ResourceManager.getPackResource( "rin", "001.ism2" ) );
		/*init( 900, 600 );
		getScene().addModel( ModelFormat.ISM2, ResourceManager.getPackResource( "rin", "002.ism2" ) );
		start();*/
		
		
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