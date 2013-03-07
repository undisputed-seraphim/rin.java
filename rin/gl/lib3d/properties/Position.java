package rin.gl.lib3d.properties;

public class Position extends Point<Position> {
	
	public Position() { super( 0.0f, 0.0f, 0.0f ); }
	public Position( float x, float y, float z ) { super( x, y, z ); }

	@Override public String toString() { return "Position[ " + this.x + " " + this.y + " " + this.z + " ]"; }
	@Override public Position copy() { return new Position( this.x, this.y, this.z ); }
}
