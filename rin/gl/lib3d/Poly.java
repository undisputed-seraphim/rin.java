package rin.gl.lib3d;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL15.*;

import rin.gl.GL;
import rin.gl.event.GLEvent;
import rin.gl.TextureManager;
import rin.util.Buffer;

/**
 * Class that contains information needed to render vertexed object
 *  e.g. vertices, normals, texture coordinates, etc.
 *
 */
public class Poly extends Pickable {	
	/* texture file string and opengl texture number */
	protected int[] range = new int[]{ 0, 0 };
	
	public Poly( String name ) {
		super.setName( name );
	}
	
	public Poly( String name, float[] v, float[] n, float[] t, String texture ) {
		super.setName( name );
		this.setVertices( v );
		this.setNormals( n );
		this.setTexcoords( t );
		this.setTextureFile( texture );
	}
	
	private Poly ready( boolean val ) { this.ready = val; return this; }
	
	public ArrayList<Float> getVertices() { return this.v; }
	public void addVertex( float x, float y, float z ) { this.addBounds( x, y, z ); this.v.add( x ); this.v.add( y ); this.v.add( z ); }
	public Poly setVertices( float[] arr ) {
		this.v = Buffer.toArrayList( arr );
		this.getBounds( arr );
		
		return this.ready( false );
	}
	
	public ArrayList<Float> getNormals() { return this.n; }
	public void addNormal( float x, float y, float z ) { this.n.add( x ); this.n.add( y ); this.n.add( z ); }
	public Poly setNormals( float[] arr ) {
		this.n = Buffer.toArrayList( arr );
		
		return this.ready( false );
	}
	
	public ArrayList<Float> getTexcoords() { return this.t; }
	public void addTexcoord( float s, float t ) { this.t.add( s ); this.t.add( t ); }
	public Poly setTexcoords( float[] arr ) {
		this.t = Buffer.toArrayList( arr );
		
		return this.ready( false );
	}
	
	public void init() {
		this.ready = false;
		
		this.createTexture();
		
		int[] i = new int[ this.v.size() / 3 ];
		for( int k = 0; k < this.v.size() / 3; k++ )
			i[k] = k;
		
		this.ibuf = new GLBuffer( GL_ELEMENT_ARRAY_BUFFER, i );
		this.vbuf = new GLBuffer( GL_ARRAY_BUFFER, Buffer.toArrayf( this.v ), 3, 0, 0, GL.getAttrib( "vertex" ) );
		this.nbuf = new GLBuffer( GL_ARRAY_BUFFER, Buffer.toArrayf( this.n ), 3, 0, 0, GL.getAttrib( "normal" ) );
		this.tbuf = new GLBuffer( GL_ARRAY_BUFFER, Buffer.toArrayf( this.t ), 2, 0, 0, GL.getAttrib( "texture" ) );
		
		if( this.bound )
			this.createBoundingBox();
		
		this.ready = true;
	}
	
	public Poly destroy() {
		this.ready = false;
		
		this.vbuf = this.vbuf != null ? this.vbuf.destroy() : null;
		this.nbuf = this.nbuf != null ? this.nbuf.destroy() : null;
		this.tbuf = this.ibuf != null ? this.tbuf.destroy() : null;
		this.ibuf = this.ibuf != null ? this.ibuf.destroy() : null;
		
		this.v.clear();
		this.n.clear();
		this.t.clear();
		
		this.bbox = this.bbox != null ? this.bbox.destroy() : null;
		
		if( this.isPickListening() )
			GLEvent.removePickEventListener( this );
		
		if( !this.textureFile.equals( "" ) )
			TextureManager.unload( this.textureFile );
		
		return null;
	}
}
