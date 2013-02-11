package rin.gl;

import rin.engine.Engine;

public class GL {
	public static int getAttrib( String attr ) { return Engine.getScene().getAttrib( attr ); }
	public static int getUniform( String attr ) { return Engine.getScene().getUniform( attr ); }
}