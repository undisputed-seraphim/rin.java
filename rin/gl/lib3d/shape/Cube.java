package rin.gl.lib3d.shape;

import static org.lwjgl.opengl.GL11.*;

import rin.util.math.Vec3;

public class Cube extends Shape {
	public static int cubes = 0;

	public Cube( float w, Vec3 pos ) {
		this(	pos.x - ( w / 2 ), pos.y + ( w / 2 ), pos.z - ( w / 2 ),
				pos.x + ( w / 2 ), pos.y - ( w / 2 ), pos.z + ( w / 2 ), pos );
	}
	public Cube( float x, float y, float z, float X, float Y, float Z, Vec3 pos ) {
		super( "Cube-" + Cube.cubes++ );
		super.renderMode = GL_TRIANGLE_STRIP;
		
		// left
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
		
		//this.position = pos;
	}
}
