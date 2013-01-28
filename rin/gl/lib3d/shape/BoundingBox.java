package rin.gl.lib3d.shape;

import static org.lwjgl.opengl.GL11.*;
import rin.util.math.Vec3;

public class BoundingBox extends Shape {
	public static int items = 0;
	
	public BoundingBox( float x, float y, float z, float X, float Y, float Z, Vec3 pos ) {
		super( "BoundingBox-" + BoundingBox.items++ );
		super.bound = false;
		super.renderMode = GL_TRIANGLE_STRIP;
		
		this.addVertex( x, Y, z );
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
		this.addVertex( x, Y, z );
		
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
		
		this.position = pos;
		this.init();
	}
	
	public BoundingBox destroy() {
		super.destroy();
		return null;
	}
}
