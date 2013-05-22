package rin.gl.lib3d;

import java.util.ArrayList;

import rin.engine.system.ident.AbstractNode;
import rin.util.math.Mat4;
import rin.util.math.Quat4;
import rin.util.math.Vec3;

public class Node extends AbstractNode<Node> {
	
	protected ModelScene scene;
	
	protected Mat4 wMat = new Mat4();
	protected Mat4 base = new Mat4();
	protected Mat4 transform = new Mat4();
	protected Mat4 scale = new Mat4();
	protected Mat4 rotate = new Mat4();
	
	public Node( String id ) { super( id ); }
	
	@Override
	protected Node actual() { return this; }
	
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
	
	@Override
	public Node add( Node n ) {
		n.scene = scene;
		return super.add( n );
	}
	
	public Node find( String name ) {
		for( Node n : children )
			if( n.getId().equals( name ) )
				return n;
		return null;
	}
	
	public void update() {
		transform = new Mat4( base );
		if( parent != null ) transform = Mat4.multiply( parent.transform, transform );
		
		//transform = Mat4.multiply( scene.getMatrix(), transform );
	}
	
	public void update( double dt ) {
		update();
	}
	
	public void render() {}
}
