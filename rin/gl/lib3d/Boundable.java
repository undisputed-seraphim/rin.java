package rin.gl.lib3d;

import java.nio.IntBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL41.*;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL41;

import rin.gl.lib3d.shape.Cube;
import rin.util.Buffer;
import rin.util.math.Mat4;
import rin.util.math.Vec3;

public class Boundable extends Actor {
	protected boolean bound = true;
	private boolean ready = false;
	
	/* bounding box values */
	protected Cube bbox = null;
	protected float xMin = Float.MAX_VALUE, yMin = Float.MAX_VALUE, zMin = Float.MAX_VALUE, 
					xMax = Float.MIN_VALUE, yMax = Float.MIN_VALUE, zMax = Float.MIN_VALUE;
	
	/* add bound values to the xmax, ymax, etc if applicable */
	public void addBounds( float x, float y, float z ) {
		xMin = Math.min( xMin, x );
		yMin = Math.min( yMin, y );
		zMin = Math.min( zMin, z );
		
		xMax = Math.max( xMax, x );
		yMax = Math.max( yMax, y );
		zMax = Math.max( zMax, z );
	}
	
	public void createBBox() {
		this.bbox = new Cube( this.xMin, this.yMin, this.zMin, this.xMax, this.yMax, this.zMax, this.position );
		this.bbox.setScene( this.scene );
		this.bbox.init();
		this.ready = true;
	}
	
	public void showBoundingBox() {
		if( this.ready )
			this.bbox.render();
		
		else this.createBBox();
	}
	
	public boolean isMouseOver( Mat4 view, IntBuffer viewport ) {
		/*public void move( float step, float side, float rise ) {
			this.position.x += this.rotate.m[ 8] * step + ( this.rotate.m[0] * side ) + ( this.rotate.m[4] * rise );
			this.position.y += this.rotate.m[ 9] * step + ( this.rotate.m[1] * side ) + ( this.rotate.m[5] * rise );
			this.position.z += this.rotate.m[10] * step + ( this.rotate.m[2] * side ) + ( this.rotate.m[6] * rise );
			this.transform();
		}*/
		
		//float[] test = Mat4.unProject( Mouse.getX(), Mouse.getY(), 0, this.matrix, view, viewport );
		//Cube cube = new Cube( 3, new Vec3( 0.0f, 0.0f, 0.0f ) );
		//System.out.println( Buffer.toString( test ) );
		//cube.setScene( this.scene );
		//cube.init();
		//cube.render();
		return false;
	}
}
