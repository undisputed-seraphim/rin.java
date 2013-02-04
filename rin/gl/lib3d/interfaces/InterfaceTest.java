package rin.gl.lib3d.interfaces;

import rin.gl.model.Model;

public class InterfaceTest {
	public static void main( String args[] ) {
		Engine.init( 900, 600 );
		
		Model.create( Model.Format.DAE, Engine.MODEL_DIR + "noire_v" + Engine.LS + "noire_v" + ".dae" );
		
		Engine.start();
	}
}
