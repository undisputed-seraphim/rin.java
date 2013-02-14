package rin.gl.lib3d.shape;

import rin.gl.lib3d.Poly;
import rin.gl.lib3d.properties.Properties;

public class Shape extends Poly {
	private static int items = 0;
	
	public Shape() { this( "Shape-" + Shape.items++, new Properties() ); }
	public Shape( String name ) { super( name, new Properties() ); }
	public Shape( Properties p ) { super( "Shape-" + Shape.items++, p ); }
	public Shape( String name, Properties p ) { super( name, p ); }
	
	public Shape destroy() {
		super.destroy();
		return null;
	}
}
