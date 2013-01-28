package rin.gl.lib3d;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import rin.gl.Scene;
import rin.gl.TextureManager;
import rin.util.Buffer;

/**
 * Class that contains information needed to render vertexed object
 *  e.g. vertices, normals, texture coordinates, etc.
 *
 */
public class Poly extends Collidable {
	/* if poly is ready to be rendered */
	private boolean ready = false;
	
	/* opengl render mode */
	protected int renderMode = GL_TRIANGLES;
	
	/* texture file string and opengl texture number */
	protected String textureFile = "";
	protected int texture = -1;
	protected float[] color = new float[]{ 1.0f, 0.0f, 0.0f, 1.0f };
	protected int[] range = new int[]{ 0, 0 };
	
	/* temporary lists for vertices, normals, and texcoords */
	protected ArrayList<Float>		v = new ArrayList<Float>(),
									n = new ArrayList<Float>(),
									t = new ArrayList<Float>();
	
	/* gl buffer pointers */
	protected GLBuffer ibuf = null, vbuf = null, nbuf = null, tbuf = null;
	
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
	
	private void getBounds( float[] v ) {
		for( int i = 0; i < v.length; i += 3 )
			this.addBounds( v[i], v[i+1], v[i+2] );
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
	
	public void setTextureFile( String file ) {
		if( this.texture != -1 )
			TextureManager.unload( file );
		
		this.texture = -1;
		this.textureFile = file;
	}
	
	public void createTexture() {
		if( !this.textureFile.equals( "" ) && this.texture == -1 )
			this.texture = TextureManager.load( this.textureFile );
	}
	
	public void applyTexture() {
		if( this.texture != -1 ) {
			glActiveTexture( GL_TEXTURE0 );
			glUniform1i( Scene.getUniform( "useTexture" ), GL_TRUE );
			glBindTexture( GL_TEXTURE_2D, this.texture );
		} else {
			glColor4f( this.color[0], this.color[1], this.color[2], this.color[3] );
			glUniform1i( Scene.getUniform( "useTexture" ), GL_FALSE );
			glDisableVertexAttribArray( Scene.getAttrib( "texture" ) );
		}
	}
	
	public void init() {
		this.ready = false;
		
		this.createTexture();
		
		int[] i = new int[ this.v.size() / 3 ];
		for( int k = 0; k < this.v.size() / 3; k++ )
			i[k] = k;
		
		this.ibuf = new GLBuffer( GL_ELEMENT_ARRAY_BUFFER, i );
		this.vbuf = new GLBuffer( GL_ARRAY_BUFFER, Buffer.toArrayf( this.v ), 3, 0, 0, Scene.getAttrib( "vertex" ) );
		this.nbuf = new GLBuffer( GL_ARRAY_BUFFER, Buffer.toArrayf( this.n ), 3, 0, 0, Scene.getAttrib( "normal" ) );
		this.tbuf = new GLBuffer( GL_ARRAY_BUFFER, Buffer.toArrayf( this.t ), 2, 0, 0, Scene.getAttrib( "texture" ) );
		
		if( this.bound )
			this.createBoundingBox();
		
		this.ready = true;
	}
	
	private boolean buffer() {
		this.vbuf.buffer();
		this.nbuf.buffer();
		this.tbuf.buffer();
		return this.ibuf.buffer();
	}
	
	public void render() { this.render( this.renderMode ); }
	public void render( int renderMode ) {
		if( this.ready ) {
			glUniformMatrix4( Scene.getUniform( "mMatrix"), false, this.matrix.gl() );

			this.applyTexture();
			if( this.buffer() )
					glDrawElements( renderMode, this.v.size() / 3, GL_UNSIGNED_INT, 0 );
		}
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
		
		if( !this.textureFile.equals( "" ) )
			TextureManager.unload( this.textureFile );
		
		return null;
	}
}
