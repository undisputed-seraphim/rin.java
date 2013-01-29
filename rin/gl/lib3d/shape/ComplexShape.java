package rin.gl.lib3d.shape;

import rin.gl.lib3d.Mesh;

public class ComplexShape extends Mesh {
	public static int complexShapes = 0;
	
	public ComplexShape() { super( "ComplexShape-" + ComplexShape.complexShapes ); }
	public ComplexShape( String name ) { super( name ); }
	
	public ComplexShape destroy() {
		super.destroy();
		return null;
	}
}
