package rin.engine.scene.nodes;

import rin.util.math.Quat4;
import rin.util.math.Vec3;

public class Frame {

	protected Animation parent;
	protected JointNode target;
	
	protected float[] tTime;
	protected float[][] tData;
	
	protected float[] sTime;
	protected float[][] sData;
	
	protected float[] rxTime;
	protected float[][] rxData;
	
	protected float[] ryTime;
	protected float[][] ryData;
	
	protected float[] rzTime;
	protected float[][] rzData;
	
	public Quat4 cRotation = new Quat4( 0, 0, 0, 1 );
	public Vec3 cTranslation = new Vec3( 0, 0, 0 );
	
	public Frame( JointNode jn ) { target = jn; }
	
	public Quat4 getCurrentRotation() { return cRotation; }
	public Vec3 getCurrentTranslation() { return cTranslation; }
	
	public void setTranslateData( float[] time, float[][] data ) {
		tTime = time;
		tData = data;
	}
	
	public void setRotateXData( float[] time, float[][] data ) {
		rxTime = time;
		rxData = data;
	}
	
	public void setRotateYData( float[] time, float[][] data ) {
		ryTime = time;
		ryData = data;
	}
	
	public void setRotateZData( float[] time, float[][] data ) {
		rzTime = time;
		rzData = data;
	}
	
	private float t = 0.0f;
	private int i = 0;
	
	private int tIndex = 0;
	private float tEnd = 0.0f;
	private void calculateT( double dt ) {
		if( tTime != null ) {
			tEnd = parent.end;
			if( tTime[tIndex] > dt )
				tIndex = 0;
			
			for( i = tIndex; i < tTime.length; i++ ) {
				if( dt < tTime[i] ) {
					tEnd = tTime[i];
					break;
				} else tIndex = i;
			}
			
			t = ((float)dt - tTime[tIndex]) / (tEnd - tTime[tIndex]);
			cTranslation.redefine( interp( tData[tIndex][0], tData[tIndex][3], t ), interp( tData[tIndex][4], tData[tIndex][7], t ),
					interp( tData[tIndex][8], tData[tIndex][11], t ) );
		}
	}
	
	private int rxIndex = 0;
	private float rxAng = 0.0f;
	private float rxEnd = 0.0f;
	private void calculateRx( double dt ) {
		if( rxTime != null ) {
			rxEnd = parent.end;
			if( rxTime[rxIndex] > dt )
				rxIndex = 0;
			
			for( i = rxIndex; i < rxTime.length; i++ ) {
				if( dt < rxTime[i] ) {
					rxEnd = rxTime[i];
					break;
				} else rxIndex = i;
			}
			
			/* rxIndex should always be the proper index by this point */
			t = ((float)dt - rxTime[rxIndex]) / (rxEnd - rxTime[rxIndex]);
			rxAng = interp( rxData[rxIndex][0], rxData[rxIndex][3], t );
			cRotation.applyOrientationDeg( Vec3.X_AXIS, rxAng );
		}
	}
	
	private int ryIndex = 0;
	private float ryEnd = 0.0f;
	private float ryAng = 0.0f;
	private void calculateRy( double dt ) {
		if( ryTime != null ) {
			ryEnd = parent.end;
			if( ryTime[ryIndex] > dt )
				ryIndex = 0;
			
			for( i = ryIndex; i < ryTime.length; i++ ) {
				if( dt < ryTime[i] ) {
					ryEnd = ryTime[i];
					break;
				} else ryIndex = i;
			}
			
			/* ryIndex should always be the proper index by this point */
			t = ((float)dt - ryTime[ryIndex]) / (ryEnd - ryTime[ryIndex]);
			ryAng = interp( ryData[ryIndex][0], ryData[ryIndex][3], t );
			cRotation.applyOrientationDeg( Vec3.Y_AXIS, ryAng );
		}
	}
	
	private int rzIndex = 0;
	private float rzEnd = 0.0f;
	private float rzAng = 0.0f;
	private void calculateRz( double dt ) {
		if( rzTime != null ) {
			rzEnd = parent.end;
			if( rzTime[rzIndex] > dt )
				rzIndex = 0;
			
			for( i = rzIndex; i < rzTime.length; i++ ) {
				if( dt < rzTime[i] ) {
					rzEnd = rzTime[i];
					break;
				} else rzIndex = i;
			}
			
			/* rzIndex should always be the proper index by this point */
			t = ((float)dt - rzTime[rzIndex]) / (rzEnd - rzTime[rzIndex]);
			rzAng = interp( rzData[rzIndex][0], rzData[rzIndex][3], t );
			cRotation.applyOrientationDeg( Vec3.Z_AXIS, rzAng );
		}
	}
	
	private float interp( float x, float y, float t ) {
		return x * (1-t) + y * t;
	}
	
	public void update( double dt ) {
		calculateT( dt );
		cRotation.redefine( target.orient );
		calculateRz( dt );
		calculateRy( dt );
		calculateRx( dt );
	}
	
}
