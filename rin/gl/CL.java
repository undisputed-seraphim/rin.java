package rin.gl;

import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.opencl.CL10;
import org.lwjgl.opencl.CLContext;
import org.lwjgl.opencl.CLDevice;
import org.lwjgl.opencl.CLPlatform;
import org.lwjgl.opengl.Drawable;

public class CL {
	
	private CLContext context;
	public CLContext getContext() { return this.context; }
	
	public CL( Drawable drawable ) {
		List<CLPlatform> platforms = CLPlatform.getPlatforms();
		/*List<CLDevice> devices = platform.getDevices( CL10.CL_DEVICE_TYPE_GPU );
		
		try {
			this.context = CLContext.create( platform, devices, null, drawable, null );
		} catch( LWJGLException e ) {
			System.out.println( "[ERROR] Unable to initialize OpenCL Context." );
		}*/
	}
	
}
