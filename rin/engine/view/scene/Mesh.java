package rin.engine.view.scene;

import static rin.engine.util.binary.BinaryConstants.*;

import java.util.ArrayList;

public class Mesh extends Actor {
	public static final byte BIT_VERTEX = b00000001;
	public static final byte BIT_NORMAL = b00000010;
	public static final byte BIT_TEXCOORD = b00000100;
	public static final byte BIT_COLOR = b00001000;

	private int flags = 0;
	
	private float[] v;
	private float[] n;
	private float[] t;
	private float[] c;
	
	private float[] b;
	private float[] w;
	
	private ArrayList<Surface> surfaces = new ArrayList<Surface>();
	
	public float[] getVertices() {
		return v;
	}
	
	public void setVertices( float[] vertices ) {
		flags |= BIT_VERTEX;
		v = vertices;
	}
	
	public float[] getNormals() {
		return n;
	}
	
	public void setNormals( float[] normals ) {
		flags |= BIT_NORMAL;
		n = normals;
	}
	
	public float[] getTexcoords() {
		return t;
	}
	
	public void setTexcoords( float[] texcoords ) {
		flags |= BIT_TEXCOORD;
		t = texcoords;
	}
}
