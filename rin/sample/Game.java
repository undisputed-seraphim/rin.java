package rin.sample;

import rin.engine.Engine;
import rin.gl.event.Transition;
import rin.gl.lib3d.ActorList;
import rin.gl.lib3d.Poly;
import rin.gl.lib3d.data.GLRenderThread;
import rin.gl.lib3d.interfaces.Transitionable;
import rin.gl.lib3d.shape.*;
import rin.gl.model.ModelManager;
import rin.gl.model.ModelManager.Format;

public class Game {
	public static void main( String args[] ) {
		//Engine.init( 900, 600 );
		
		//Engine.getScene().addActor( ModelManager.create( Format.OBJ, "cornelia" ) );
		/*Engine.getScene().addActor( ModelManager.create( Format.DAE, "noire_v" ) );
		Engine.getScene().addActor( new Sphere( 5.0f, 50, true ) );
		Engine.getScene().addActor( new Icosahedron( 5.0f, true ) );
		Engine.getScene().addActor( new Tetrahedron( 5.0f, true ) );
		Engine.getScene().addActor( new Cuboid( 10.0f, true ) );
		Engine.getScene().addActor( new Octahedron( 5.0f, true ) );
		Engine.getScene().addActor( new Dodecahedron( 5.0f, true ) );
		Engine.getScene().addActor( new Tile() );
		new Transition( (Transitionable)Engine.getScene().getActor( 1 ) ).start();*/
		//GLRenderThread.add( new Sphere( 5.0f, 50, true ) );
		GLRenderThread.addActor( Format.DAE, "noire_v" );
		//ActorList.get().addEvent();
		//Engine.start();
	}
}