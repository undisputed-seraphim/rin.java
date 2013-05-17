package rin.engine.view.lib3d;

import rin.util.math.Mat4;
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
	
	public Quat4 cRotation = new Quat4();
	public Vec3 cTranslation = new Vec3();
	
	public Frame( JointNode jn ) { target = jn; }
	
	public Quat4 getCurrentRotation() { return cRotation; }
	public Vec3 getCurrentTranslation() { return cTranslation; }
	
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
		System.out.println( "START " + parent.start );
		System.out.println( "END " + parent.end );
		if( tTime != null ) {
			tIndex = 0;
			for( int i = 0; i < tTime.length; i++ ) {
				if( dt < tTime[i] ) {
					break;
				} else tIndex = i;
			}
			System.out.println( "TIME: " + tTime[tIndex] + " " + tIndex + " " + dt + " " + tTime.length );
			if( tTime.length > tIndex+1 ) {
				float t = tTime[tIndex+1] - tTime[tIndex];
				t = (float)dt / t;
				Mat4 m1 = new Mat4( tData[tIndex][0],tData[tIndex][1],tData[tIndex][2],tData[tIndex][3],tData[tIndex][4],tData[tIndex][5],tData[tIndex][6],tData[tIndex][7],tData[tIndex][8],tData[tIndex][9],tData[tIndex][10],tData[tIndex][11],0,0,0,1 );
				Mat4 m2 = new Mat4( tData[tIndex+1][0],tData[tIndex+1][1],tData[tIndex+1][2],tData[tIndex+1][3],tData[tIndex+1][4],tData[tIndex+1][5],tData[tIndex+1][6],tData[tIndex+1][7],tData[tIndex+1][8],tData[tIndex+1][9],tData[tIndex+1][10],tData[tIndex+1][11],0,0,0,1 );
				Vec3 v1 = new Vec3( m1.m[3], m1.m[7], m1.m[11] );
				Vec3 v2 = new Vec3( m2.m[3], m2.m[7], m2.m[11] );
				cTranslation = Vec3.lerp( v1, v2, t );
			}
		}
		
		cRotation = new Quat4();
		if( rxTime != null ) {
			rxIndex = 0;
			for( int i = 0; i < rxTime.length; i++ ) {
				if( dt < rxTime[i] ) {
					break;
				} else rxIndex = i;
			}
			System.out.println( "ROTATEX: " + rxTime[rxIndex] + " " + rxIndex + " " + dt + " " + rxTime.length );
			if( rxTime.length > rxIndex+1 ) {
				float t = rxTime[rxIndex+1] - rxTime[rxIndex];
				t = (float)dt / t;
				Quat4 q1 = new Quat4( rxData[rxIndex][0], rxData[rxIndex][1], rxData[rxIndex][2], rxData[rxIndex][3] );
				Quat4 q2 = new Quat4( rxData[rxIndex+1][0], rxData[rxIndex+1][1], rxData[rxIndex+1][2], rxData[rxIndex+1][3] );
				cRotation = Quat4.multiply( cRotation, Quat4.slerp( q1, q2, t ) );
			}
		}
		
		if( ryTime != null ) {
			ryIndex = 0;
			for( int i = 0; i < ryTime.length; i++ ) {
				if( dt < ryTime[i] ) {
					break;
				} else ryIndex = i;
			}
			System.out.println( "ROTATEY: " + ryTime[ryIndex] + " " + ryIndex + " " + dt + " " + ryTime.length );
			if( ryTime.length > ryIndex+1 ) {
				float t = ryTime[ryIndex+1] - ryTime[ryIndex];
				t = (float)dt / t;
				Quat4 q1 = new Quat4( ryData[ryIndex][0], ryData[ryIndex][1], ryData[ryIndex][2], ryData[ryIndex][3] );
				Quat4 q2 = new Quat4( ryData[ryIndex+1][0], ryData[ryIndex+1][1], ryData[ryIndex+1][2], ryData[ryIndex+1][3] );
				cRotation = Quat4.multiply( cRotation, Quat4.slerp( q1, q2, t ) );
			}
		}
		
		if( rzTime != null ) {
			rzIndex = 0;
			for( int i = 0; i < rzTime.length; i++ ) {
				if( dt < rzTime[i] ) {
					break;
				} else rzIndex = i;
			}
			System.out.println( "ROTATEZ: " + rzTime[rzIndex] + " " + rzIndex + " " + dt + " " + rzTime.length );
			if( rzTime.length > rzIndex+1 ) {
				float t = rzTime[rzIndex+1] - rzTime[rzIndex];
				t = (float)dt / t;
				Quat4 q1 = new Quat4( rzData[rzIndex][0], rzData[rzIndex][1], rzData[rzIndex][2], rzData[rzIndex][3] );
				Quat4 q2 = new Quat4( rzData[rzIndex+1][0], rzData[rzIndex+1][1], rzData[rzIndex+1][2], rzData[rzIndex+1][3] );
				cRotation = Quat4.multiply( cRotation, Quat4.slerp( q1, q2, t ) );
			}
		}
	}
	
	public void update( double dt ) {
		updateIndices( dt );
		
		/*cRotation = new Quat4();
		if( rxTime != null )
			cRotation = Quat4.multiply( cRotation, new Quat4( rxData[0][0], rxData[0][1], rxData[0][2], rxData[0][3] ) );
		
		if( ryTime != null )
			cRotation = Quat4.multiply( cRotation, new Quat4( ryData[0][0], ryData[0][1], ryData[0][2], ryData[0][3] ) );
		
		if( rzTime != null )
			cRotation = Quat4.multiply( cRotation, new Quat4( rzData[0][0], rzData[0][1], rzData[0][2], rzData[0][3] ) );*/
	}
}
