package rin.gl.lib3d;

import rin.gl.GLEvent.PickEvent;
import rin.gl.GLEvent;
import rin.gl.GLEventListener;
import rin.util.math.Vec3;

public class Pickable extends Collidable implements GLEventListener.PickEventListener {
	public static final float MARGIN_ERROR = 0.0007f;
	protected boolean pickable = false;
	public boolean isMouseOver = false;
	
	public boolean isPickable() { return this.pickable; }
	public void setPickable( boolean val ) { this.pickable = val; }
	
	public boolean withinBoundingBox( Vec3 pos ) {
		System.out.println( this.yMin );
		if( this.bbox != null )
			if( this.xMin <= pos.x + MARGIN_ERROR && this.xMax >= pos.x - MARGIN_ERROR )
				if( this.yMin <= pos.y + MARGIN_ERROR && this.yMax >= pos.y - MARGIN_ERROR )
					if( this.zMin <= pos.z + MARGIN_ERROR && this.zMax >= pos.z - MARGIN_ERROR ) {
						this.isMouseOver = true;
						return true;
					}
		this.isMouseOver = false;
		return false;
	}
	
	public void clicked() {
		System.out.println( "Clicked on: " + this.name );
	}
	
	public void listenForPicking() {
		if( this.bbox == null )
			this.createBoundingBox();
		
		float[] tmp = this.getUniqueColor();
		GLEvent.addPickEventListener( this, "FloatArray[ "+(float)((int)(tmp[0] * 255) & 0xff)+" "+
				(float)((int)(tmp[1] * 255) & 0xff)+" "+(float)((int)(tmp[2] * 255) & 0xff)+" ]" );
	}
	
	@Override
	public void processPickEvent(PickEvent e) {
		System.out.println( "mouse hovering over " + this.getName() );
	}
}