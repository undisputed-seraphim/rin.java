package rin.gl.lib3d.shape;

import rin.engine.Engine.CuboidParams;
import rin.engine.Engine.ShapeParams;
import rin.engine.Engine.SphereParams;
import rin.engine.Engine.TetrahedronParams;
import rin.gl.lib3d.Poly;
import rin.gl.lib3d.properties.Properties;

public class Shape extends Poly {
	private static int items = 0;
	
	public Shape() { this( "Shape-" + Shape.items++, new Properties() ); }
	public Shape( String name ) { super( name, new Properties() ); }
	public Shape( Properties p ) { super( "Shape-" + Shape.items++, p ); }
	public Shape( String name, Properties p ) { super( name, p ); }
	
	public static Shape create( ShapeParams p ) {
		if( p instanceof SphereParams )			return new Sphere( (SphereParams)p );
		if( p instanceof CuboidParams )			return new Cuboid( (CuboidParams)p );
		if( p instanceof TetrahedronParams )	return new Tetrahedron( (TetrahedronParams) p );
		
		return null;
	}
	
	public Shape destroy() {
		super.destroy();
		return null;
	}
}
