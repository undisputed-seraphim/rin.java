package rin.engine.view.scene;

import rin.engine.resource.Resource;
import rin.engine.view.View;

public class Scene {
	private View view;
	private SceneGraph graph;
	private Camera cam;
	private long start;
	
	public Scene( View v ) {
		view = v;
		graph = new SceneGraph( this );
	}
	
	public View getView() {
		return view;
	}
	
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
	
	public void update() {
		long dt = getDt();
		for( Node n : graph )
			n.update( dt );
	}
	
	public void addModel( Resource resource ) {
		
	}
	
}
