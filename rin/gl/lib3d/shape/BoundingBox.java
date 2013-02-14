package rin.gl.lib3d.shape;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.GL_LINE_STRIP;

import rin.gl.lib3d.properties.Properties;
import rin.util.Buffer;

public class BoundingBox extends Shape {
	private static int items = 0;
	
	public BoundingBox( float x, float y, float z, float X, float Y, float Z, Properties p ) {
		super( "BoundingBox-" + BoundingBox.items++, p );
		
		this.setBound( false );
		this.setColored( true );
		this.setRenderMode( GL_LINE_STRIP );
		
		ArrayList<Float> v = new ArrayList<Float>();
		
		v.add( x ); v.add( y ); v.add( Z );
		v.add( X ); v.add( y ); v.add( Z );
		v.add( X ); v.add( Y ); v.add( Z );
		v.add( x ); v.add( Y ); v.add( Z );
		v.add( x ); v.add( y ); v.add( Z );
		
		v.add( x ); v.add( y ); v.add( z );
		v.add( X ); v.add( y ); v.add( z );
		v.add( X ); v.add( Y ); v.add( z );
		v.add( x ); v.add( Y ); v.add( z );
		v.add( x ); v.add( y ); v.add( z );
		
		v.add( x ); v.add( Y ); v.add( z );
		v.add( x ); v.add( Y ); v.add( Z );
		
		v.add( X ); v.add( Y ); v.add( Z );
		v.add( X ); v.add( Y ); v.add( z );
		
		v.add( X ); v.add( y ); v.add( z );
		v.add( X ); v.add( y ); v.add( Z );
		
		this.build( Buffer.toArrayf( v ), new float[0], new float[0], new float[0] );
	}
	
	public BoundingBox destroy() {
		super.destroy();
		return null;
	}
}
