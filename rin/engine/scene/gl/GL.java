package rin.engine.scene.gl;

public class GL {

	private static GLScene scene;
	
	protected static void setScene( GLScene s ) { scene = s; }
	
	public static int getAttribute( String attr ) { return scene.getAttribute( attr ); }
	public static int getUniform( String uni ) { return scene.getUniform( uni ); }
}
