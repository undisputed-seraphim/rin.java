package rin.gl.lib3d.interfaces;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import rin.gl.GL;
import rin.gl.lib3d.GLBuffer;
import rin.gl.lib3d.GLInterleavedBuffer;
import rin.gl.lib3d.shape.BoundingBox;
import rin.util.Buffer;
import rin.util.math.Vec3;

public class Poly extends Actor implements Renderable, Boundable {

	public Poly() { this( "No Name Poly" ); }
	public Poly( String name ) { this( name, new Vec3( 0.0f, 0.0f, 0.0f ) ); }
	public Poly( String name, Vec3 position ) { this( name, position, new Vec3( 0.0f, 0.0f, 0.0f ) ); }
	public Poly( String name, Vec3 position, Vec3 rotation ) { this( name, position, rotation, new Vec3( 1.0f, 1.0f, 1.0f ) ); }
	public Poly( String name, Vec3 position, Vec3 rotation, Vec3 scale ) {
		super( name, position, rotation, scale );
	}
	
	/* ---------------- renderable implementation ----------------- */
	private boolean ready = false;
	@Override public boolean isReady() { return this.ready; }
	
	private boolean interleaved = false;
	private int renderMode = GL_TRIANGLES;
	@Override public int getRenderMode() { return this.renderMode; }
	
	private GLInterleavedBuffer abuf = null;
	private GLBuffer ibuf = null, vbuf = null, nbuf = null, tbuf = null;
	private ArrayList<Float> v = null, n = null, t = null;
	
	@Override public void setVertices( float[] vertices ) {
		this.ready = false;
		this.v = Buffer.toArrayList( vertices );
		this.setBounds( vertices );
	}
	
	@Override public void setNormals( float[] normals ) {
		this.ready = false;
		this.n = Buffer.toArrayList( normals );
	}
	
	@Override public void setTexcoords( float[] texcoords ) {
		this.ready = false;
		this.t = Buffer.toArrayList( texcoords );
	}
	
	@Override public void init() {
		this.ready = false;
		
		//this.createTexture();
		
		int[] i = new int[ this.v.size() / 3 ];
		for( int k = 0; k < this.v.size() / 3; k++ )
			i[k] = k;
		
		this.ibuf = new GLBuffer( GL_ELEMENT_ARRAY_BUFFER, i );
		this.vbuf = new GLBuffer( GL_ARRAY_BUFFER, Buffer.toArrayf( this.v ), 3, 0, 0, GL.getAttrib( "vertex" ) );
		this.nbuf = new GLBuffer( GL_ARRAY_BUFFER, Buffer.toArrayf( this.n ), 3, 0, 0, GL.getAttrib( "normal" ) );
		this.tbuf = new GLBuffer( GL_ARRAY_BUFFER, Buffer.toArrayf( this.t ), 2, 0, 0, GL.getAttrib( "texture" ) );
		
		//if( this.bound )
			//this.createBoundingBox();
		
		this.ready = true;
	}
	
	@Override public boolean buffer() {
		return false;
	}
	
	@Override public void render() {
		
	}

	/* ------------------ boundable implementation ------------------ */
	private boolean bound = true;
	private BoundingBox bbox = null;
	private float	xMin = Float.POSITIVE_INFINITY, yMin = Float.POSITIVE_INFINITY, zMin = Float.POSITIVE_INFINITY,
					xMax = Float.NEGATIVE_INFINITY, yMax = Float.NEGATIVE_INFINITY, zMax = Float.NEGATIVE_INFINITY;
	
	@Override public void setBounds( float[] vertices ) {
		this.xMin = Float.POSITIVE_INFINITY; this.yMin = Float.POSITIVE_INFINITY; this.zMin = Float.POSITIVE_INFINITY;
		this.xMax = Float.NEGATIVE_INFINITY; this.yMax = Float.NEGATIVE_INFINITY; this.zMax = Float.NEGATIVE_INFINITY;
		for( int i = 0; i < vertices.length; i += 3 )
			this.addBounds( vertices[i], vertices[i+1], vertices[i+2] );
	}
	
	private void addBounds( float x, float y, float z ) {
		xMin = Math.min( xMin, x );
		yMin = Math.min( yMin, y );
		zMin = Math.min( zMin, z );
		
		xMax = Math.max( xMax, x );
		yMax = Math.max( yMax, y );
		zMax = Math.max( zMax, z );
	}
	
	@Override public void createBoundingBox() {
		this.bbox = new BoundingBox( this.xMin, this.yMin, this.zMin, this.xMax, this.yMax, this.zMax, this.getPosition() );
		this.ready = true;
	}
	
	@Override public void showBoundingBox() { this.showBoundingBox( this.bbox.getRenderMode(), false ); }
	public void showBoundingBox( int renderMode ) { this.showBoundingBox( renderMode, false ); }
	public void showBoundingBox( int renderMode, boolean unique ) {
		//if( this.bbox != null && this.bbox.isReady() ) {
			if( unique ) {
				//this.bbox.setColor( this.getUniqueColor() );
				//this.bbox.setColored( true );
				this.bbox.render( renderMode );
			} else {
				this.bbox.render( renderMode );
			}
		//}
		//else this.createBoundingBox();
	}
	
}
