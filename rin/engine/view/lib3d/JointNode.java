package rin.engine.view.lib3d;

import static org.lwjgl.opengl.GL20.*;
import rin.gl.GL;
import rin.util.math.Mat4;
import rin.util.math.Quat4;
import rin.util.math.Vec3;

public class JointNode extends SceneNode<JointNode> {
	
	protected Mat4 joint = new Mat4();
	protected Mat4 world = new Mat4();
	protected Mat4 inverse = new Mat4();
	protected Mat4 absoluteInverse = new Mat4();
	protected Mat4 skin = new Mat4();
	protected Mat4 transform = new Mat4();
	protected Mat4 poseRelative = new Mat4();
	protected Mat4 poseAbsolute = new Mat4();
	
	protected Vec3 trans = new Vec3( 0, 0, 0 );
	protected float orientX = 0.0f;
	protected float orientY = 0.0f;
	protected float orientZ = 0.0f;
	
	protected Mat4 translate = new Mat4();
	protected Quat4 gOrient = new Quat4();
	protected Quat4 orient = new Quat4( 0, 0, 0, 1 );
	protected Mat4 rotate = new Mat4();
	
	public JointNode( String id ) { super( id ); }
	@Override public JointNode actual() { return this; }
	
	public void setJointMatrix( float[] m ) { joint = new Mat4( m ); }
	public void setJointTranslation( float[] t ) { tLocal = Vec3.add( tLocal, new Vec3( t[0], t[1], t[2] ) ); }
	//public void setJointRotateX( float[] r ) { orient = Mat4.multiply( orient, Quat4.create( new Vec3( r[0], r[1], r[2] ), r[3] * Quat4.PIOVER180 ).toMat4() ); }
	//public void setJointRotateY( float[] r ) { orient = Mat4.multiply( orient, Quat4.create( new Vec3( r[0], r[1], r[2] ), r[3] * Quat4.PIOVER180 ).toMat4() ); }
	//public void setJointRotateZ( float[] r ) { orient = Mat4.multiply( orient, Quat4.create( new Vec3( r[0], r[1], r[2] ), r[3] * Quat4.PIOVER180 ).toMat4() ); }
	public void setJointRotateX( float[] r ) { orientX = r[3]; }//orient.applyOrientationDeg( Vec3.X_AXIS, r[3] ); }// = Quat4.multiply( orient, Quat4.create( Vec3.X_AXIS , r[3] ) ); }
	public void setJointRotateY( float[] r ) { orientY = r[3]; }//orient.applyOrientationDeg( Vec3.Y_AXIS, r[3] ); }// = Quat4.multiply( orient, Quat4.create( Vec3.Y_AXIS , r[3] ) ); }
	public void setJointRotateZ( float[] r ) { orientZ = r[3]; }//orient.applyOrientationDeg( Vec3.Z_AXIS, r[3] ); }// = Quat4.multiply( orient, Quat4.create( Vec3.Z_AXIS , r[3] ) ); }
	public void setInverseBindMatrix( float[] m ) { inverse = new Mat4( m ); }
	
	protected Quat4 rLocal = new Quat4( 0, 0, 0, 1 );
	protected Quat4 rGlobal = new Quat4( 0, 0, 0, 1 );
	protected Vec3 tLocal = new Vec3( 0, 0, 0 );
	protected Vec3 tGlobal = new Vec3( 0, 0, 0 );
	
	protected Quat4 rBaseGlobal = new Quat4( 0, 0, 0, 1 );
	protected Vec3 tBaseGlobal = new Vec3( 0, 0, 0 );
	
	public void applyBone( int index ) {
		/*Mat4 inv = new Mat4( inverse );
		if( parent != null && parent != tree.getRoot() ) {
			//inv = Mat4.multiply( parent.inverse, inverse );
		}
		world = Mat4.multiply( skin, inverse );
		Quat4 q = world.toQuat4();
		glUniform4f( GL.getUniform( "quats["+index+"]" ), q.x, q.y, q.z, q.w );
		glUniform3f( GL.getUniform( "trans["+index+"]" ), world.m[3], world.m[7], world.m[11] );*/
		//world = Mat4.multiply( Mat4.translate( new Mat4(), tGlobal ), rGlobal.toMat4() );
		world = Mat4.multiply( poseAbsolute, inverse );
		Quat4 q = world.toQuat4();
		glUniform4f( GL.getUniform( "quats["+index+"]" ), q.x, q.y, q.z, q.w );
		glUniform3f( GL.getUniform( "trans["+index+"]" ), world.m[3], world.m[7], world.m[11] );
	}
	
	public void finish() {
		if( parent != null && parent != tree.getRoot() ) {
			rBaseGlobal = Quat4.multiply( Quat4.multiply( parent.rBaseGlobal, orient ), rLocal );
			tBaseGlobal = Vec3.add( parent.tBaseGlobal, Vec3.rotate( tLocal, parent.rBaseGlobal ) );
		} else {
			rBaseGlobal = Quat4.multiply( orient, rLocal );
			tBaseGlobal = new Vec3( tLocal );
		}
		orient.applyOrientationDeg( Vec3.Z_AXIS, orientZ );
		orient.applyOrientationDeg( Vec3.Y_AXIS, orientY );
		orient.applyOrientationDeg( Vec3.X_AXIS, orientX );
		//poseRelative = Mat4.multiply( Mat4.translate( new Mat4(), tLocal ), orient.toMat4() );
		//if( parent != null && parent != tree.getRoot() )
			//poseRelative = Mat4.multiply( parent.poseRelative, poseRelative );
		//Mat4 rot = Mat4.flatten( rBaseGlobal.toMat4() );
		//Vec3 tmp = Vec3.rotate( tBaseGlobal, rot.toQuat4() );
		//absoluteInverse = Mat4.multiply( Mat4.translate( new Mat4(), Vec3.inverse( tmp ) ), rot );
	}
	
	public void update( double dt ) {
		Animation cAnimation = ((Skeleton)tree).getCurrentAnimation();
		
		if( cAnimation != null ) {
			Frame cFrame = cAnimation.getFrame( getId() );
			if( cFrame != null ) {
				//tGlobal = cFrame.getCurrentTranslation();
				//rGlobal = Quat4.multiply( parent.rGlobal, cFrame.getCurrentRotation().toQuat4() );
				//joint = Mat4.multiply( Mat4.translate( new Mat4(), cFrame.getCurrentTranslation() ), cFrame.getCurrentRotation() );
				tLocal = cFrame.getCurrentTranslation();
				rLocal = cFrame.getCurrentRotation().toQuat4();
				//joint = Mat4.multiply( joint, cFrame.getCurrentRotation() );
				//rLocal = cFrame.getCurrentRotation().toQuat4();
				//skin = cFrame.getCurrentTransform();
				/*Vec3 tmp = Mat4.getPos( Mat4.multiply( Mat4.translate( new Mat4(), cFrame.getCurrentTranslation() ), orient ) );
				skin.m[3] = tmp.x;
				skin.m[7] = tmp.y;
				skin.m[11] = tmp.z;*/
				//skin = Mat4.multiply( skin, cFrame.getCurrentRotation() );
				//skin = Mat4.multiply( skin, Mat4.translate( new Mat4(), cFrame.getCurrentTranslation() ) );
				//skin = Mat4.multiply( skin, cFrame.getCurrentRotation() );
				
				//Vec3 tmp = cFrame.getCurrentTranslation();
				//skin.m[3] += tmp.x;
				//skin.m[7] += tmp.y;
				//skin.m[11] += tmp.z;
			}
		}
		
		/*if( parent != null && parent != tree.getRoot() ) {
			poseAbsolute = Mat4.multiply( parent.poseAbsolute, joint );
		} else {
			poseAbsolute = joint;
		}
		poseAbsolute = Mat4.multiply( poseAbsolute, orient.toMat4() );*/
		
		if( parent != null && parent != tree.getRoot() ) {
			//rGlobal = Quat4.multiply( Quat4.multiply( parent.rGlobal, orient ), rLocal );
			rGlobal = Quat4.multiply( Quat4.multiply( parent.rGlobal, orient ), rLocal );
			tGlobal = Vec3.add( parent.tGlobal, Vec3.rotate( tLocal, parent.rGlobal ) );
		} else {
			rGlobal = Quat4.multiply( orient, rLocal );
			tGlobal.redefine( tLocal );
		}
		rGlobal.intoMat4( rotate );
		translate.identity().translate( tGlobal );
		Mat4.multiplyInto( translate, rotate, poseAbsolute );
		//if( parent != null && parent != tree.getRoot() )
			//skin = Mat4.multiply( parent.skin, skin );
	}
}
