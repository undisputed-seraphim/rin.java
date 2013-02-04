package rin.gl.lib3d;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL15.*;

import rin.gl.GL;
import rin.gl.event.GLEvent;
import rin.gl.lib3d.GLInterleavedBuffer.IndexType;
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
	protected int indices = 0;
	protected GLInterleavedBuffer abuf = null;
	
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
	
	public void build( float[] vertices, float[] normals, float[] texcoords, String textureFile ) {
		this.ready = false;
		this.setTextureFile( textureFile );
		this.createTexture();
		
		int[] i = new int[ vertices.length / 3 ];
		for( int k = 0; k < vertices.length / 3; k++ )
			i[k] = k;
		
		this.ibuf = new GLBuffer( GL_ELEMENT_ARRAY_BUFFER, i );
		this.setVertices( vertices );
		this.setNormals( normals );
		this.setTexcoords( texcoords );
		this.vbuf = new GLBuffer( GL_ARRAY_BUFFER, Buffer.toArrayf( this.v ), 3, 0, 0, GL.getAttrib( "vertex" ) );
		this.nbuf = new GLBuffer( GL_ARRAY_BUFFER, Buffer.toArrayf( this.n ), 3, 0, 0, GL.getAttrib( "normal" ) );
		this.tbuf = new GLBuffer( GL_ARRAY_BUFFER, Buffer.toArrayf( this.t ), 2, 0, 0, GL.getAttrib( "texture" ) );
		
		
		/*float[] aba = new float[ vertices.length + ( (vertices.length / 3) * 4) + normals.length + texcoords.length ];
		int[] iba = new int[ vertices.length / 3 ];
		
		for( int i = 0, a = 0; i < vertices.length / 3; i++ ) {
			aba[a++] = vertices[ i*3 ];
			aba[a++] = vertices[ i*3+1 ];
			aba[a++] = vertices[ i*3+2 ];
			
			float[] color = this.getUniqueColor();
			aba[a++] = color[0];
			aba[a++] = color[1];
			aba[a++] = color[2];
			aba[a++] = 1.0f;
			
			if( normals.length > 0 ) {
				aba[a++] = normals[ i*3 ];
				aba[a++] = normals[ i*3+1 ];
				aba[a++] = normals[ i*3+2 ];
			}
			
			if( texcoords.length > 0 ) {
				aba[a++] = texcoords[ i*2 ];
				aba[a++] = texcoords[ i*2+1 ];
			}
			
			iba[i] = i;
		}
		this.indices = iba.length;
		
		this.ibuf = new GLBuffer( GL_ELEMENT_ARRAY_BUFFER, iba );
		this.abuf = new GLInterleavedBuffer( GL_ARRAY_BUFFER, aba )
				.addIndex( IndexType.VERTEX, 3, GL.getAttrib( "vertex" ) );

		if( normals.length > 0 ) this.abuf.addIndex( IndexType.NORMAL, 3, GL.getAttrib( "normal" ) );
		if( texcoords.length > 0 ) this.abuf.addIndex( IndexType.TEXCOORD, 2, GL.getAttrib( "texture" ) );

		this.abuf.build();*/
		this.ready = true;
	}
	
	public Poly destroy() {
		this.ready = false;
		
		this.vbuf = this.vbuf != null ? this.vbuf.destroy() : null;
		this.nbuf = this.nbuf != null ? this.nbuf.destroy() : null;
		this.tbuf = this.tbuf != null ? this.tbuf.destroy() : null;
		this.ibuf = null;//this.ibuf != null ? this.ibuf.destroy() : null;
		
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
