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
	private int btIndex = 0;
	private int btNext = 0;
	
	protected float[] sTime;
	protected float[][] sData;
	
	protected float[] rxTime;
	protected float[][] rxData;
	
	protected float[] ryTime;
	protected float[][] ryData;
	
	protected float[] rzTime;
	protected float[][] rzData;
	
	public Mat4 cRotation = new Mat4();
	public Vec3 cTranslation = new Vec3( 0, 0, 0 );
	private Mat4 cTransform = new Mat4();
	
	public Frame( JointNode jn ) { target = jn; }
	
	public Mat4 getCurrentRotation() { return cRotation; }
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
		//System.out.println( "START " + parent.start );
		//System.out.println( "END " + parent.end );
		Vec3 trans = new Vec3( 0, 0, 0 );
		if( tTime != null ) {
			tIndex = 0;
			for( int i = 0; i < tTime.length; i++ ) {
				if( dt < tTime[i] ) {
					break;
				} else tIndex = i;
			}
			//System.out.println( "TIME: " + tTime[tIndex] + " " + tIndex + " " + dt + " " + tTime.length );
			if( tTime.length > tIndex+1 ) {
				float t = tTime[tIndex+1] - tTime[tIndex];
				t = (float)dt / t;
				//Mat4 m1 = new Mat4( tData[tIndex][0],tData[tIndex][1],tData[tIndex][2],tData[tIndex][3],tData[tIndex][4],tData[tIndex][5],tData[tIndex][6],tData[tIndex][7],tData[tIndex][8],tData[tIndex][9],tData[tIndex][10],tData[tIndex][11],0,0,0,1 );
				//Mat4 m2 = new Mat4( tData[tIndex+1][0],tData[tIndex+1][1],tData[tIndex+1][2],tData[tIndex+1][3],tData[tIndex+1][4],tData[tIndex+1][5],tData[tIndex+1][6],tData[tIndex+1][7],tData[tIndex+1][8],tData[tIndex+1][9],tData[tIndex+1][10],tData[tIndex+1][11],0,0,0,1 );
				cTranslation = Vec3.lerp( new Vec3( tData[tIndex][0],tData[tIndex][4],tData[tIndex][8] ), new Vec3(tData[tIndex+1][0],tData[tIndex+1][4],tData[tIndex+1][8]), t );
				//cTranslation = new Vec3( tData[0][0], tData[0][1], tData[0][2] );
				//cTranslation = Mat4.lerp( m1, m2, t )
				System.out.println( "translate: " + tIndex );
			}
		}
		
		cRotation = new Mat4();
		if( rxTime != null ) {
			rxIndex = 0;
			for( int i = 0; i < rxTime.length; i++ ) {
				if( dt < rxTime[i] ) {
					break;
				} else rxIndex = i;
			}
			//System.out.println( "ROTATEX: " + rxTime[rxIndex] + " " + rxIndex + " " + dt + " " + rxTime.length );
			if( rxTime.length > rxIndex+1 ) {
				float t = rxTime[rxIndex+1] - rxTime[rxIndex];
				t = ((float)dt - rxTime[rxIndex]) / t;
				//Quat4 q1 = new Quat4( rxData[rxIndex][0], rxData[rxIndex][1], rxData[rxIndex][2], rxData[rxIndex][3] );
				//Quat4 q2 = new Quat4( rxData[rxIndex+1][0], rxData[rxIndex+1][1], rxData[rxIndex+1][2], rxData[rxIndex+1][3] );
				Quat4 q1 = Quat4.create( Vec3.X_AXIS, rxData[rxIndex][0] * Quat4.PIOVER180 );
				Quat4 q2 = Quat4.create( Vec3.X_AXIS, rxData[rxIndex+1][0] * Quat4.PIOVER180 );
				cRotation = Mat4.multiply( cRotation, Quat4.slerp( q1, q2, t ).toMat4() );
				//cRotation = Mat4.multiply( cRotation, Quat4.create( Vec3.X_AXIS, rxData[0][0] * Quat4.PIOVER180 ).toMat4() );
				System.out.println( "rx time: " + rxTime[rxIndex] + " " + rxTime[rxIndex+1] + " " + dt + " " + rxTime.length + " " + t + " " + rxIndex );
			}
		}
		
		if( ryTime != null ) {
			ryIndex = 0;
			for( int i = 0; i < ryTime.length; i++ ) {
				if( dt < ryTime[i] ) {
					break;
				} else ryIndex = i;
			}
			//System.out.println( "ROTATEY: " + ryTime[ryIndex] + " " + ryIndex + " " + dt + " " + ryTime.length );
			if( ryTime.length > ryIndex+1 ) {
				float t = ryTime[ryIndex+1] - ryTime[ryIndex];
				t = ((float)dt - ryTime[ryIndex]) / t;
				Quat4 q1 = Quat4.create( Vec3.Y_AXIS, ryData[ryIndex][0] * Quat4.PIOVER180 );
				Quat4 q2 = Quat4.create( Vec3.Y_AXIS, ryData[ryIndex+1][0] * Quat4.PIOVER180 );
				cRotation = Mat4.multiply( cRotation, Quat4.slerp( q1, q2, t ).toMat4() );
				//cRotation = Mat4.multiply( cRotation, Quat4.create( Vec3.Y_AXIS, ryData[0][0] * Quat4.PIOVER180 ).toMat4() );
				System.out.println( "ry time: " + ryTime[ryIndex] + " " + ryTime[ryIndex+1] + " " + dt + " " + ryTime.length + " " + t + " " + ryIndex );
			}
		}
		
		if( rzTime != null ) {
			rzIndex = 0;
			for( int i = 0; i < rzTime.length; i++ ) {
				if( dt < rzTime[i] ) {
					break;
				} else rzIndex = i;
			}
			//System.out.println( "ROTATEZ: " + rzTime[rzIndex] + " " + rzIndex + " " + dt + " " + rzTime.length );
			if( rzTime.length > rzIndex+1 ) {
				float t = rzTime[rzIndex+1] - rzTime[rzIndex];
				t = ((float)dt - rzTime[rzIndex]) / t;
				Quat4 q1 = Quat4.create( Vec3.Z_AXIS, rzData[rzIndex][0] * Quat4.PIOVER180 );
				Quat4 q2 = Quat4.create( Vec3.Z_AXIS, rzData[rzIndex+1][0] * Quat4.PIOVER180 );
				cRotation = Mat4.multiply( cRotation, Quat4.slerp( q1, q2, t ).toMat4() );
				//cRotation = Mat4.multiply( cRotation, Quat4.create( Vec3.Z_AXIS, rzData[0][0] * Quat4.PIOVER180 ).toMat4() );
				System.out.println( "rz time: " + rzTime[rzIndex] + " " + rzTime[rzIndex+1] + " " + dt + " " + rzTime.length + " " + t + " " + rxIndex );
			}
		}
		
		cTransform = Mat4.multiply( Mat4.translate( new Mat4(), cTranslation ), cRotation );
		//cTransform.m[3] += trans.x;
		//cTransform.m[7] += trans.y;
		//cTransform.m[11] += trans.z;
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
