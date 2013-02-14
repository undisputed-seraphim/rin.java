package rin.gl.lib3d.shape;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.GL_LINES;

import rin.gl.lib3d.properties.*;
import rin.util.Buffer;
import rin.util.math.Vec3;

public class Dodecahedron extends Shape {
	private static int items = 0;
	
	public Dodecahedron() { this( 0.5f, new Properties(), false ); }
	public Dodecahedron( boolean wire ) { this( 0.5f, new Properties(), wire ); }
	public Dodecahedron( Properties p ) { this( 0.5f, p, false ); }
	public Dodecahedron( Properties p, boolean wire ) { this( 0.5f, p, wire ); }
	public Dodecahedron( float radius ) { this( radius, new Properties(), false ); }
	public Dodecahedron( float radius, boolean wire ) { this( radius, new Properties(), wire ); }
	public Dodecahedron( float radius, Properties p ) { this( radius, p, false ); }
	public Dodecahedron( float radius, Properties p, boolean wire ) {
		super( "Dodecahedron-" + Dodecahedron.items++, p );
		
		this.setColored( true );
		if( wire )
			this.setRenderMode( GL_LINES );
		
		ArrayList<Float> v = new ArrayList<Float>();
		ArrayList<Float> n = new ArrayList<Float>();
		
		float angleA = 52.62263590f;
		float angleB = 10.81231754f;
		
		float phiA = (float)Math.PI * angleA / 180.0f;
		float phiB = (float)Math.PI * angleB / 180.0f;
		float phiC = (float)Math.PI * (-angleB) / 180.0f;
		float phiD = (float)Math.PI * (-angleA) / 180.0f;
		
		float incrementA = (float)Math.PI * 72.0f / 180.0f;
		float incrementB = incrementA / 2.0f;
		
		float[][] verts = new float[20][3];
		
		float cur = 0.0f;
		for( int i = 0; i < 5; i++ ) {
			verts[i][0] = radius * (float)Math.cos( cur ) * (float)Math.cos( phiA );
			verts[i][1] = radius * (float)Math.sin( cur ) * (float)Math.cos( phiA );
			verts[i][2] = radius * (float)Math.sin( phiA );
			cur += incrementA;
		}
		
		cur = 0.0f;
		for( int i = 5; i < 10; i++ ) {
			verts[i][0] = radius * (float)Math.cos( cur ) * (float)Math.cos( phiB );
			verts[i][1] = radius * (float)Math.sin( cur ) * (float)Math.cos( phiB );
			verts[i][2] = radius * (float)Math.sin( phiB );
			cur += incrementA;
		}
		
		cur = incrementB;
		for( int i = 10; i < 15; i++ ) {
			verts[i][0] = radius * (float)Math.cos( cur ) * (float)Math.cos( phiC );
			verts[i][1] = radius * (float)Math.sin( cur ) * (float)Math.cos( phiC );
			verts[i][2] = radius * (float)Math.sin( phiC );
			cur += incrementA;
		}
		
		cur = incrementB;
		for( int i = 15; i < 20; i++ ) {
			verts[i][0] = radius * (float)Math.cos( cur ) * (float)Math.cos( phiD );
			verts[i][1] = radius * (float)Math.sin( cur ) * (float)Math.cos( phiD );
			verts[i][2] = radius * (float)Math.sin( phiD );
			cur += incrementA;
		}
		
		int[][] indices = new int[][] {
				{  0,  1,  2,  3,  4 },
				{  0,  1,  6, 10,  5 },
				{  1,  2,  7, 11,  6 },
				{  2,  3,  8, 12,  7 },
				{  3,  4,  9, 13,  8 },
				{  4,  0,  5, 14,  9 },
				{ 15, 16, 11,  6, 10 },
				{ 16, 17, 12,  7, 11 },
				{ 17, 18, 13,  8, 12 },
				{ 18, 19, 14,  9, 13 },
				{ 19, 15, 10,  5, 14 },
				{ 15, 16, 17, 18, 19 }
		};
		
		for( int i = 0; i < indices.length; i++ ) {
			float[] center = Dodecahedron.findCenter( verts[indices[i][0]], verts[indices[i][1]], verts[indices[i][2]],
					verts[indices[i][3]], verts[indices[i][4]] );
			Vec3 m = new Vec3( center[0], center[1], center[2] );
			
			for( int j = 0; j < indices[i].length; j++ ) {
				Vec3 v1 = new Vec3( verts[indices[i][j]][0], verts[indices[i][j]][1], verts[indices[i][j]][2] );
				
				int k = j + 1;
				if( k >= indices[i].length )
					k = 0;
				
				Vec3 v2 = new Vec3( verts[indices[i][k]][0], verts[indices[i][k]][1], verts[indices[i][k]][2] );
				if( !wire ) {
					// calculate normal
					Vec3 n1 = Vec3.normalize( Vec3.cross( Vec3.subtract( v2, v1 ), Vec3.subtract( m, v1 ) ) );
					
					v.add( v1.x );		n.add( n1.x );
					v.add( v1.y );		n.add( n1.y );
					v.add( v1.z );		n.add( n1.z );
					
					v.add( v2.x );		n.add( n1.x );
					v.add( v2.y );		n.add( n1.y );
					v.add( v2.z );		n.add( n1.z );
					
					v.add( m.x );		n.add( n1.x );
					v.add( m.y );		n.add( n1.y );
					v.add( m.z );		n.add( n1.z );
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
					
					v.add( m.x );
					v.add( m.y );
					v.add( m.z );
					
					v.add( m.x );
					v.add( m.y );
					v.add( m.z );
					
					v.add( v1.x );
					v.add( v1.y );
					v.add( v1.z );
				}
			}
		}
		
		this.build( Buffer.toArrayf( v ), Buffer.toArrayf( n ), new float[0], new float[0] );
		
		v.clear();
		n.clear();
	}
	
	public static float[] findCenter( float[] v1, float[] v2, float[] v3, float[] v4, float[] v5 ) {
		float x = ( v1[0] + v2[0] + v3[0] + v4[0] + v5[0] ) / 5;
		float y = ( v1[1] + v2[1] + v3[1] + v4[1] + v5[1] ) / 5;
		float z = ( v1[2] + v2[2] + v3[2] + v4[2] + v5[2] ) / 5;
		return new float[] { x, y, z };
	}
	
	public Dodecahedron destroy() {
		super.destroy();
		return null;
	}
}
