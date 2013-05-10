package rin.engine.view.scene;

public class Scene {
	private SceneGraph graph;
	private Camera cam;
	private long start;
	
	public Camera getCamera() {
		return cam;
	}
	
	public void setCamera( Camera c ) {
		cam = c;
	}
	
	private long getDt() {
		long t = System.nanoTime();
		long dt = t - start;
		start = t;
		return dt;
	}
	
	public void render() {
		long dt = getDt();
		for( Node n : graph ) {
			//n.update( dt );
		}
	}
	
}
