package rin.sample;

import rin.engine.Engine;
import rin.gl.lib3d.Transformation;
import rin.gl.lib3d.shape.*;
import rin.gl.model.ModelManager;
import rin.gl.model.ModelManager.Format;

public class Game {
	public static void main( String args[] ) {
		Engine.init( 900, 600 );
		
		//Engine.getScene().addActor( ModelManager.create( Format.OBJ, "cornelia" ) );
		Engine.getScene().addActor( new Sphere( 2, 10, 10, true ) );
		Engine.getScene().addActor( ModelManager.create( Format.DAE, "noire_v" ) );
		//Engine.getScene().addActor( new Icosahedron() );
		
		Engine.start();
	}
}