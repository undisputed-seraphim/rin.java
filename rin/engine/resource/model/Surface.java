package rin.engine.resource.model;

import rin.engine.resource.image.ImageContainer;

public class Surface {

	private String surfaceName;
	private Material mat;
	
	private float[] v = new float[0];
	private float[] n = new float[0];
	private float[] t = new float[0];
	
	private int[] i = new int[0];
	
	public Surface( String name ) {
		surfaceName = name;
	}
	
	public String getName() {
		return surfaceName;
	}
	
	public Material getMaterial() {
		return mat;
	}
	
	public Material setMaterial( ImageContainer image ) {
		mat = new Material( image );
		return mat;
	}
	
	public float[] getVertices() {
		return v;
	}
	
	public Surface setVertices( float[] vertices ) {
		v = vertices;
		return this;
	}
	
	public float[] getNormals() {
		return n;
	}
	
	public Surface setNormals( float[] normals ) {
		n = normals;
		return this;
	}
	
	public float[] getTexcoords() {
		return t;
	}
	
	public Surface setTexcoords( float[] texcoords ) {
		t = texcoords;
		return this;
	}
	
	public int[] getIndices() {
		return i;
	}
	
	public Surface setIndices( int[] indices ) {
		i = indices;
		return this;
	}
	
}
