package rin.engine.resource.model;

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
	
	public float[] getVertices() {
		return v;
	}
	
	public Surface setVertices( float[] vertices ) {
		v = vertices;
		return this;
	}
	
}
