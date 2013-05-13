package rin.gl.lib3d;

public class JointNode extends Node {
	
	public JointNode( String name ) {
		super( name );
	}
	
	@Override
	public void update( double dt ) {
		Animation a = scene.getCurrentAnimation();
		if( a != null ) {
			FrameSet fs = a.findFrames( name );
			if( fs != null ) {
				float[] tdata = fs.getTranslationData();
				if( tdata.length > 0 ) {
					
				}
			}
		}
	}
}
