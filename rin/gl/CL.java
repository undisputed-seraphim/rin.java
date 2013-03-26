package rin.gl;

import java.nio.IntBuffer;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.opencl.CLCommandQueue;
import org.lwjgl.opencl.CLContext;
import org.lwjgl.opencl.CLDevice;
import org.lwjgl.opencl.CLPlatform;
import org.lwjgl.opencl.Util;
import org.lwjgl.opengl.Drawable;

import rin.util.Buffer;
import static org.lwjgl.opencl.CL10.*;

public class CL {
	
	private CLContext context;
	public CLContext getContext() { return this.context; }
	
	CLCommandQueue queue;
	
	public CL( Drawable drawable ) {
		IntBuffer error = Buffer.toBuffer( new int[1] );
		
		try {
			org.lwjgl.opencl.CL.create();
			CLPlatform platform = CLPlatform.getPlatforms().get( 0 );
			List<CLDevice> devices = platform.getDevices( CL_DEVICE_TYPE_GPU );
			
			this.context = CLContext.create( platform, devices, null, drawable, error );
			this.queue = clCreateCommandQueue(context, devices.get(0), CL_QUEUE_PROFILING_ENABLE, error);
		} catch( LWJGLException e ) {
			System.out.println( "[ERROR] Unable to initialize OpenCL Context: " + error.get( 0 ) );
			Util.checkCLError( error.get( 0 ) );
			return;
		}
	}
	
}
