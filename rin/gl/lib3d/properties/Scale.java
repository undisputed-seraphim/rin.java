package rin.gl.lib3d.properties;

public class Scale extends Point {
	public Scale() { super( 0.0f, 0.0f, 0.0f ); }
	public Scale( Position p ) { super( p.x, p.y, p.z ); }
	public Scale( float x, float y, float z ) { super( x, y, z ); }

	@Override public Scale copy() { return new Scale( this.x, this.y, this.z ); }
	@Override public String toString() { return "Scale[ " + this.x + " " + this.y + " " + this.z + " ]"; }
}
