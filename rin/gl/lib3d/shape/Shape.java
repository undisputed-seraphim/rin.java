package rin.gl.lib3d.shape;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import rin.gl.lib3d.GLBuffer;
import rin.gl.lib3d.Poly;

public class Shape extends Poly {
	public static int shapes = 0;
	
	public Shape() { super( "Shape-" + Shape.shapes ); this.bound = false; }
	public Shape( String name ) { super( name ); this.bound = false; }
}
