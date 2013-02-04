package rin.gl.lib3d;

import org.lwjgl.input.Mouse;

import rin.gl.event.GLEvent.*;
import rin.gl.event.GLEvent;
import rin.gl.event.GLEventListener.*;
import static org.lwjgl.opengl.GL11.*;
import rin.util.math.Vec3;

public class Pickable extends Collidable implements PickEventListener {
	public static final float MARGIN_ERROR = 0.0007f;
	public boolean isMouseOver = false;
	
	private boolean pickListening = false;
	public boolean isPickListening() { return this.pickListening; }
	
	protected boolean picking = false;
	public boolean isPicking() { return this.picking; }
	public void setPicking( boolean val ) { this.picking = val; }
	
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
	
	public void listenForPicking() {
		if( this.bbox == null )
			this.createBoundingBox();
		
		this.pickListening = true;
		float[] tmp = this.getUniqueColor();
		//GLEvent.addPickEventListener( this, "FloatArray[ "+(float)((int)(tmp[0] * 255) & 0xff)+" "+
			//	(float)((int)(tmp[1] * 255) & 0xff)+" "+(float)((int)(tmp[2] * 255) & 0xff)+" ]" );
	}
	
	@Override
	public void processPickEvent( PickEvent e ) {
		this.showBoundingBox( GL_LINE_STRIP );
		if( Mouse.isButtonDown( 0 ) );
			//this.clicked( new MouseEvent( 0, GLEvent.STATE_DOWN, Mouse.getX(), Mouse.getY() ) );
	}
	
	public void clicked( MouseEvent e ) {
		System.out.println( "Clicked on: " + this.name + " at x:" + e.x + " y:" + e.y );
	}
}
