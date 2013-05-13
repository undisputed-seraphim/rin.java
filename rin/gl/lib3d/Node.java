package rin.gl.lib3d;

import java.util.ArrayList;

import rin.util.math.Mat4;
import rin.util.math.Quat4;
import rin.util.math.Vec3;

public class Node {
	protected ModelScene scene;
	protected String name;

	protected Node parent;
	protected ArrayList<Node> children = new ArrayList<Node>();
	
	protected Mat4 wMat = new Mat4();
	protected Mat4 base = new Mat4();
	protected Mat4 transform = new Mat4();
	protected Mat4 scale = new Mat4();
	protected Mat4 rotate = new Mat4();
	
	public void setBaseMatrix( float[] m ) {
		base = new Mat4( m );
	}
	
	public void setScale( float x, float y, float z ) {
		scale = Mat4.scale( new Mat4(), new Vec3( x, y, z ) );
	}
	
	public void setRotation( int x, int y, int z ) {
		Quat4	rotateX = Quat4.create( Vec3.X_AXIS, x * Quat4.PIOVER180 ),
				rotateY = Quat4.create( Vec3.Y_AXIS, y * Quat4.PIOVER180 ),
				rotateZ = Quat4.create( Vec3.Z_AXIS, z * Quat4.PIOVER180 );
		rotate = Quat4.multiply( Quat4.multiply( rotateX, rotateY ), rotateZ ).toMat4();
	}
	
	public Node( String n ) { name = n; }
	
	public ArrayList<Node> getChildren() { return children; }
	public String getName() { return name; }
	
	public Node add( Node n ) {
		n.scene = scene;
		n.parent = this;
		children.add( n );
		scene.update();
		return children.get( children.size() - 1 );
	}
	
	public void remove( Node n ) {
		children.remove( n );
		scene.update();
	}
	
	public Node find( String name ) {
		for( Node n : children )
			if( n.getName().equalsIgnoreCase( name ) )
				return n;
		return null;
	}
	
	public void update() {
		if( parent != null ) transform = Mat4.multiply( parent.base, base );
		else transform = base;
	}
	
	public void update( double dt ) {
		update();
	}
	
	public void render() {}
}
