package rin.gl.lib3d.shape;

import rin.gl.lib3d.interfaces.Poly;

public class Shape extends Poly {
	public static int shapes = 0;
	
	public Shape() { super( "Shape-" + Shape.shapes ); }
	public Shape( String name ) { super( name ); }
	
	public Shape destroy() {
		super.destroy();
		return null;
	}
}
