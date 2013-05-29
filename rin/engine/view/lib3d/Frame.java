package rin.engine.view.lib3d;

import java.util.HashMap;

import rin.util.math.Mat4;
import rin.util.math.Quat4;
import rin.util.math.Vec3;

public class Frame {

	protected Animation parent;
	protected JointNode target;
	
	protected float[] tTime;
	protected float[][] tData;
	protected HashMap<Float, float[]> btData = new HashMap<Float, float[]>();
	
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
	private Mat4 cTransform = new Mat4();
	
	public Frame( JointNode jn ) { target = jn; }
	
	public Quat4 getCurrentRotation() { return cRotation; }
	public Vec3 getCurrentTranslation() { return cTranslation; }
	public Mat4 getCurrentTransform() { return cTransform; }
	
	public void setTranslateData( float[] time, float[][] data ) {
		tTime = time;
		tData = data;
		parent.updateTimes( time[0], time[ time.length - 1 ] );
	}
	
	public void setRotateXData( float[] time, float[][] data ) {
		rxTime = time;
		rxData = data;
		parent.updateTimes( time[0], time[ time.length - 1 ] );
	}
	
	public void setRotateYData( float[] time, float[][] data ) {
		ryTime = time;
		ryData = data;
		parent.updateTimes( time[0], time[ time.length - 1 ] );
	}
	
	public void setRotateZData( float[] time, float[][] data ) {
		rzTime = time;
		rzData = data;
		parent.updateTimes( time[0], time[ time.length - 1 ] );
	}
	
	private int tIndex = -1;
	private int rxIndex = -1;
	private int ryIndex = -1;
	private int rzIndex = -1;
	private void updateIndices( double dt ) {
		//System.out.println( "START FRAME " + target.getId() );
		
		int offset = 0;
		if( tTime != null ) {
			tIndex = 0;
			for( int i = 0; i < tTime.length; i++ ) {
				if( dt < tTime[i] ) {
					break;
				} else tIndex = i;
			}

			if( tTime.length > tIndex+1 ) {
				float t = tTime[tIndex+1] - tTime[tIndex];
				t = (float)dt / t;
				
				if( t <= 0.25f ) offset = 0;
				else if( t <= 0.5f ) offset = 1;
				else if( t <= 0.75f ) offset = 2;
				else offset = 3;
				
				cTranslation.redefine( tData[tIndex][0+offset], tData[tIndex][4+offset], tData[tIndex][8+offset] );
				
				/*System.out.println( "\tTranslation:" );
				System.out.println( "\t\tIndices: " + tIndex + " [" + tTime[tIndex] +"] - " + (tIndex+1) + " [" + tTime[tIndex+1] + "]" );
				System.out.println( "\t\tOffset: " + offset );
				System.out.println( "\t\tTime: " + dt + " / " + t );*/
			} else {
				System.err.println( "SEX t" );
			}
		}
		
		cRotation.identity();
		if( rzTime != null ) {
			rzIndex = 0;
			for( int i = 0; i < rzTime.length; i++ ) {
				if( dt < rzTime[i] ) {
					break;
				} else rzIndex = i;
			}
			if( rzTime.length > rzIndex+1 ) {
				float t = rzTime[rzIndex+1] - rzTime[rzIndex];
				t = ((float)dt - rzTime[rzIndex]) / t;
				
				if( t <= 0.25f ) offset = 0;
				else if( t <= 0.5f ) offset = 1;
				else if( t <= 0.75f ) offset = 2;
				else offset = 3;
				
				//cRotation = Mat4.multiply( cRotation, Quat4.create( Vec3.Z_AXIS, rzData[rzIndex][offset] ).toMat4() );
				cRotation.applyOrientationDeg( Vec3.Z_AXIS, rzData[rzIndex][offset] );
				
				/*System.out.println( "\tRotationZ:" );
				System.out.println( "\t\tIndices: " + rzIndex + " [" + rzTime[rzIndex] +"] - " + (rzIndex+1) + " [" + rzTime[rzIndex+1] + "]" );
				System.out.println( "\t\tOffset: " + offset );
				System.out.println( "\t\tTime: " + dt + " / " + t );*/
			} else {
				System.err.println( "SEX rz" );
			}
		}
		
		if( ryTime != null ) {
			ryIndex = 0;
			for( int i = 0; i < ryTime.length; i++ ) {
				if( dt < ryTime[i] ) {
					break;
				} else ryIndex = i;
			}
			if( ryTime.length > ryIndex+1 ) {
				float t = ryTime[ryIndex+1] - ryTime[ryIndex];
				t = ((float)dt - ryTime[ryIndex]) / t;
				
				if( t <= 0.25f ) offset = 0;
				else if( t <= 0.5f ) offset = 1;
				else if( t <= 0.75f ) offset = 2;
				else offset = 3;
				
				//cRotation = Mat4.multiply( cRotation, Quat4.create( Vec3.Y_AXIS, ryData[ryIndex][offset] ).toMat4() );
				cRotation.applyOrientationDeg( Vec3.Y_AXIS, ryData[ryIndex][offset] );
				
				/*System.out.println( "\tRotationY:" );
				System.out.println( "\t\tIndices: " + ryIndex + " [" + ryTime[ryIndex] +"] - " + (ryIndex+1) + " [" + ryTime[ryIndex+1] + "]" );
				System.out.println( "\t\tOffset: " + offset );
				System.out.println( "\t\tTime: " + dt + " / " + t );*/
			} else {
				System.err.println( "SEX ry" );
			}
		}
		
		if( rxTime != null ) {
			rxIndex = 0;
			for( int i = 0; i < rxTime.length; i++ ) {
				if( dt < rxTime[i] ) {
					break;
				} else rxIndex = i;
			}
			if( rxTime.length > rxIndex+1 ) {
				float t = rxTime[rxIndex+1] - rxTime[rxIndex];
				t = ((float)dt - rxTime[rxIndex]) / t;
				
				if( t <= 0.25f ) offset = 0;
				else if( t <= 0.5f ) offset = 1;
				else if( t <= 0.75f ) offset = 2;
				else offset = 3;
				
				//cRotation = Mat4.multiply( cRotation, Quat4.create( Vec3.X_AXIS, rxData[rxIndex][offset] ).toMat4() );
				cRotation.applyOrientationDeg( Vec3.X_AXIS, rxData[rxIndex][offset] );
				
				/*System.out.println( "\tRotationX:" );
				System.out.println( "\t\tIndices: " + rxIndex + " [" + rxTime[rxIndex] +"] - " + (rxIndex+1) + " [" + rxTime[rxIndex+1] + "]" );
				System.out.println( "\t\tOffset: " + offset );
				System.out.println( "\t\tTime: " + dt + " / " + t );*/
			} else {
				System.err.println( "SEX rx" );
			}
		}
	}
	
	public void update( double dt ) {
		updateIndices( dt );
	}
}
