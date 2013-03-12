package rin.gl.lib3d.shape;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.GL_LINES;

import rin.engine.Engine.CuboidParams;
import rin.util.Buffer;
import rin.util.math.Vec3;

public class Cuboid extends Shape {
	private static int items = 0;

	public Cuboid( CuboidParams p ) {
		super( "Cuboid-" + Cuboid.items++, p.properties );
		float width = p.width;
		float height = p.height;
		float depth = p.depth;
		boolean wire = p.wire;
		
		this.setColored( true );
		if( wire )
			this.setRenderMode( GL_LINES );
		
		float x = width / 2.0f;
		float y = height / 2.0f;
		float z = depth / 2.0f;
		
		ArrayList<Float> v = new ArrayList<Float>();
		ArrayList<Float> n = new ArrayList<Float>();
		
		float[][] verts = new float[][]{
				{ -x, y,  z }, { -x, -y,  z }, { x, -y,  z }, { x, y,  z },		// near square
				{ -x, y, -z }, { -x, -y, -z }, { x, -y, -z }, { x, y, -z }		// far square
		};
		
		int[][] indices = new int[][] {
				{ 0, 1, 2 }, { 0, 2, 3 },	// front
				{ 4, 5, 6 }, { 4, 6, 7 },	// back
				{ 4, 5, 1 }, { 4, 1, 0 },	// left
				{ 3, 2, 6 }, { 3, 6, 7 },	// right
				{ 4, 0, 3 }, { 4, 3, 7 },	// top
				{ 1, 5, 6 }, { 1, 6, 2 }	// bottom
		};
		
		for( int i = 0; i < indices.length; i++ ) {
			// current vertex
			Vec3 v1 = new Vec3( verts[indices[i][0]][0], verts[indices[i][0]][1], verts[indices[i][0]][2] );
			Vec3 v2 = new Vec3( verts[indices[i][1]][0], verts[indices[i][1]][1], verts[indices[i][1]][2] );
			Vec3 v3 = new Vec3( verts[indices[i][2]][0], verts[indices[i][2]][1], verts[indices[i][2]][2] );
			
			if( !wire ) {
				// calculate normal
				Vec3 n1 = Vec3.normalize( Vec3.cross( Vec3.subtract( v2, v1 ), Vec3.subtract( v3, v1 ) ) );
				
				v.add( v1.x );		n.add( n1.x );
				v.add( v1.y );		n.add( n1.y );
				v.add( v1.z );		n.add( n1.z );
				
				v.add( v2.x );		n.add( n1.x );
				v.add( v2.y );		n.add( n1.y );
				v.add( v2.z );		n.add( n1.z );
				
				v.add( v3.x );		n.add( n1.x );
				v.add( v3.y );		n.add( n1.y );
				v.add( v3.z );		n.add( n1.z );
			} else {
				v.add( v1.x );
				v.add( v1.y );
				v.add( v1.z );
				
				v.add( v2.x );
				v.add( v2.y );
				v.add( v2.z );
				
				v.add( v2.x );
				v.add( v2.y );
				v.add( v2.z );
				
				v.add( v3.x );
				v.add( v3.y );
				v.add( v3.z );
				
				v.add( v3.x );
				v.add( v3.y );
				v.add( v3.z );
				
				v.add( v1.x );
				v.add( v1.y );
				v.add( v1.z );
			}
		}
		
		this.build( Buffer.toArrayf( v ), Buffer.toArrayf( n ), new float[0], new float[0] );
		
		v.clear();
		n.clear();
	}
	
	public Cuboid destroy() {
		super.destroy();
		return null;
	}
}
