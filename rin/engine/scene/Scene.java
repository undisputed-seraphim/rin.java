package rin.engine.scene;

import rin.engine.scene.nodes.Camera;

public interface Scene {

	public Camera getCamera();
	
	public void init();
	public void process( double dt );
	public void destroy();
		
}
