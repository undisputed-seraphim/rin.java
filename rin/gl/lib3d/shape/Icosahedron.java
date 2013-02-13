package rin.gl.lib3d.shape;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.GL_LINES;

import rin.gl.lib3d.properties.Properties;
import rin.util.Buffer;
import rin.util.math.Vec3;

public class Icosahedron extends Shape {
	private static int items = 0;
	
	public Icosahedron() { this( 0.5f, new Properties(), false ); }
	public Icosahedron( boolean wire ) { this( 0.5f, new Properties(), wire ); }
	public Icosahedron( Properties p ) { this( 0.5f, p, false ); }
	public Icosahedron( Properties p, boolean wire ) { this( 0.5f, p, wire ); }
	public Icosahedron( float radius ) { this( radius, new Properties(), false ); }
	public Icosahedron( float radius, boolean wire ) { this( radius, new Properties(), wire ); }
	public Icosahedron( float radius, Properties p ) { this( radius, p, false ); }
	public Icosahedron( float radius, Properties p, boolean wire ) {
		super( "Icosahedron-" + Icosahedron.items++ );

		if( wire )
			this.setRenderMode( GL_LINES );
		
		ArrayList<Float> v = new ArrayList<Float>();
		ArrayList<Float> n = new ArrayList<Float>();

		//System.out.println( Math.sin( ( 360 ) ) / 20 + " " + 2 / Math.sin( 360 ) );
		float X = 0.525731112119133606f * radius, Z = 0.850650808352039932f * radius;
		
		float[][] verts = new float[][] {
				{-X, 0.0f, Z}, {X, 0.0f, Z}, {-X, 0.0f, -Z}, {X, 0.0f, -Z},
				{0.0f, Z, X}, {0.0f, Z, -X}, {0.0f, -Z, X}, {0.0f, -Z, -X},
				{Z, X, 0.0f}, {-Z, X, 0.0f}, {Z, -X, 0.0f}, {-Z, -X, 0.0f}
		};
		
		int[][] indices = new int[][] {
				{0,4,1}, {0,9,4}, {9,5,4},{4,5,8}, {4,8,1},
				{8,10,1}, {8,3,10}, {5,3,8}, {5,2,3}, {2,7,3},
				{7,10,3}, {7,6,10}, {7,11,6}, {11,0,6}, {0,1,6},
				{6,1,10}, {9,0,11}, {9,11,2}, {9,2,5}, {7,2,11}
		};
		
		for( int i = 0; i < 20; i++ ) {
			// current point
			Vec3 v1 = new Vec3( verts[indices[i][0]][0], verts[indices[i][0]][1], verts[indices[i][0]][2] );
			Vec3 v2 = new Vec3( verts[indices[i][1]][0], verts[indices[i][1]][1], verts[indices[i][1]][2] );
			Vec3 v3 = new Vec3( verts[indices[i][2]][0], verts[indices[i][2]][1], verts[indices[i][2]][2] );
			
			if( !wire ) {
				//calculate normal
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
		
		this.setPosition( p.getPosition() );
		this.setRotation( p.getRotation() );
		this.setScale( p.getScale() );
		this.build( Buffer.toArrayf( v ), Buffer.toArrayf( n ), new float[0], new float[0] );
		
		v.clear();
		n.clear();
	}
	
	public Icosahedron destroy() {
		super.destroy();
		return null;
	}
}
