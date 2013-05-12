package rin.engine.resource.model;

import java.util.ArrayList;

import rin.engine.resource.ResourceContainer;
import rin.gl.lib3d.ModelScene;
import rin.gl.lib3d.Node;

public class ModelContainer extends ResourceContainer {

	private ModelScene scene;
	
	public ModelScene getScene() {
		return scene;
	}
	
	public ModelScene startScene( Node root ) {
		scene = new ModelScene( root );
		return scene;
	}
	
	private ArrayList<Surface> surfaces = new ArrayList<Surface>();
	
	private float[] v = new float[0];
	private float[] n = new float[0];
	private float[] t = new float[0];
	
	public float[] getVertices() {
		return v;
	}
	
	public void setVertices( float[] vertices ) {
		v = vertices;
	}
	
	public float[] getNormals() {
		return n;
	}
	
	public void setNormals( float[] normals ) {
		n = normals;
	}
	
	public float[] getTexcoords() {
		return t;
	}
	
	public void setTexcoords( float[] texcoords ) {
		t = texcoords;
	}
	
	public ArrayList<Surface> getSurfaces() {
		return surfaces;
	}
	
	public Surface addSurface( String name ) {
		surfaces.add( new Surface( name ) );
		return surfaces.get( surfaces.size() - 1 );
	}
}
