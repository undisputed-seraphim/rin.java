package rin.gl.lib3d.shape;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.GL_LINES;

import rin.engine.Engine.TetrahedronParams;
import rin.util.Buffer;
import rin.util.math.Vec3;

public class Tetrahedron extends Shape {
	private static int items = 0;

	public Tetrahedron( TetrahedronParams p ) {
		super( "Tetrahedron-" + Tetrahedron.items++, p.properties );
		float radius = p.radius;
		boolean wire = p.wire;
		
		this.setColored( true );
		if( wire )
			this.setRenderMode( GL_LINES );
		
		ArrayList<Float> v = new ArrayList<Float>();
		ArrayList<Float> n = new ArrayList<Float>();
		
		float phi = -19.471220333f;
		float alpha = (float)Math.PI * phi / 180.0f;
		float increment = (float)Math.PI * 120.0f / 180.0f;
		
		float verts[][] = new float[4][3];
		
		verts[0][0] = 0.0f;
		verts[0][1] = 0.0f;
		verts[0][2] = radius;
		for( int i = 1; i < 4; i++ ) {
			verts[i][0] = radius * (float)Math.cos( (i - 1) * increment ) * (float)Math.cos( alpha );
			verts[i][1] = radius * (float)Math.sin( (i - 1) * increment ) * (float)Math.cos( alpha );
			verts[i][2] = radius * (float)Math.sin( alpha );
		}
		
		int indices[][] = new int[][] {
				{ 0, 1, 2 },
				{ 0, 2, 3 },
				{ 0, 3, 1 },
				{ 1, 2, 3 }	
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
	
	public Tetrahedron destroy() {
		super.destroy();
		return null;
	}
}
