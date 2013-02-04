package rin.gl.lib3d.shape;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

import rin.util.Buffer;
import rin.util.math.Vec3;

public class BoundingBox extends Shape {
	private static int items = 0;
	
	public BoundingBox( float x, float y, float z, float X, float Y, float Z, Vec3 pos ) {
		super( "BoundingBox-" + BoundingBox.items++ );
		this.setBound( false );
		
		ArrayList<Float> v = new ArrayList<Float>();
		
		v.add( x ); v.add( y ); v.add( Z );
		v.add( X ); v.add( y ); v.add( Z );
		v.add( x ); v.add( Y ); v.add( Z );
		v.add( X ); v.add( Y ); v.add( Z );
		
		v.add( x ); v.add( Y ); v.add( z );
		v.add( X ); v.add( Y ); v.add( z );
		
		
		/*this.addVertex( x, Y, z );
		this.addVertex( x, y, z );
		this.addVertex( x, Y, Z );
		this.addVertex( x, y, Z );
		
		// front
		this.addVertex( X, Y, Z );
		this.addVertex( X, y, Z );
		
		// right
		this.addVertex( X, Y, z );
		this.addVertex( X, y, z );
		
		// back
		this.addVertex( x, Y, z );
		this.addVertex( x, y, z );
		
		// bottom hack
		this.addVertex( x, y, Z );
		this.addVertex( X, y, z );
		this.addVertex( X, y, Z );
		this.addVertex( x, y, Z );
		
		// top hack
		this.addVertex( X, Y, Z );
		this.addVertex( x, Y, Z );
		this.addVertex( X, Y, z );
		this.addVertex( x, Y, z );*/
		
		/*this.addVertex( x, y, z );
		this.addVertex( X, y, z );
		this.addVertex( X, y, Z );
		this.addVertex( x, y, Z );
		this.addVertex( x, y, z );
		
		this.addVertex( x, Y, z );
		this.addVertex( X, Y, z );
		this.addVertex( X, Y, Z );
		this.addVertex( x, Y, Z );
		this.addVertex( x, Y, z );
		
		this.addVertex( X, Y, z );
		this.addVertex( X, y, z );
		this.addVertex( X, y, Z );
		this.addVertex( X, Y, Z );
		this.addVertex( x, Y, Z );
		this.addVertex( x, y, Z );*/
		
		this.setRenderMode( GL_TRIANGLE_STRIP );
		this.setColor( 1.0f, 0.0f, 0.0f, 0.5f );
		this.setColored( true );
		this.build( Buffer.toArrayf( v ), new float[0], new float[0] );
	}
	
	public BoundingBox destroy() {
		super.destroy();
		return null;
	}
}
