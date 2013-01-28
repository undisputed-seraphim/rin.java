package rin.gl.lib3d;

import rin.gl.lib3d.shape.BoundingBox;

public class Boundable extends Actor {
	protected boolean bound = true;
	private boolean ready = false;
	
	/* bounding box values */
	protected BoundingBox bbox = null;
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
	
	public void createBoundingBox() {
		this.bbox = new BoundingBox( this.xMin, this.yMin, this.zMin, this.xMax, this.yMax, this.zMax, this.position );
		this.ready = true;
	}
	
	public void showBoundingBox() { this.showBoundingBox( this.bbox.renderMode ); }
	public void showBoundingBox( int renderMode ) {
		if( this.ready )
			this.bbox.render( renderMode );
		else this.createBoundingBox();
	}
}
