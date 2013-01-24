package rin.gl.lib3d;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import rin.gl.Texture;
import rin.gl.lib3d.shape.Cube;
import rin.util.Buffer;

/**
 * Class that contains information needed to render vertexed object
 *  e.g. vertices, normals, texture coordinates, etc.
 *
 */
public class Poly extends Boundable {
	/* name of this poly */
	protected String name = "";
	
	/* if poly is ready to be rendered */
	protected boolean ready = false;
	
	/* opengl render mode */
	protected int renderMode = GL_TRIANGLES;
	
	/* texture file string and opengl texture number */
	protected String textureFile = "";
	protected int texture = -1;
	protected boolean colored = false;
	
	protected int[] range = new int[]{ 0, 0 };
	protected float[] color = new float[]{ 1.0f, 0.0f, 0.0f, 1.0f };
	
	/* temporary lists for vertices, normals, and texcoords */
	protected ArrayList<Float>	v = new ArrayList<Float>(),
								n = new ArrayList<Float>(),
								t = new ArrayList<Float>();
	
	/* arrays for the final indices, vertices, normals, and texcoords */
	protected int[] iba = new int[0];
	protected float[] vba = new float[0], nba = new float[0], tba = new float[0];
	
	/* gl buffer pointers */
	protected GLBuffer ibuf, vbuf, nbuf, tbuf;
	
	public Poly( String name ) { this.name = name; }
	
	//public float[] getColor() { return this.color; }
	
	public ArrayList<Float> getVertices() { return this.v; }
	public ArrayList<Float> getNormals() { return this.n; }
	public ArrayList<Float> getTexcoords() { return this.t; }
	
	public Poly addVertex( float x, float y, float z ) {
		this.v.add( x ); this.v.add( y ); this.v.add( z );
		this.addBounds( x, y, z);
		return this;
	}
	public Poly addNormal( float x, float y, float z ) { this.n.add( x ); this.n.add( y ); this.n.add( z ); return this; }
	public Poly addTexture( float s, float t ) { this.t.add( s ); this.t.add( t ); return this; }
	
	public void setTexture( String file ) { this.textureFile = file; }
	public void createTexture() { this.texture = Texture.fromFile( this.textureFile ); }
	public void applyTexture() {
		if( this.texture != -1 ) {
			glActiveTexture( GL_TEXTURE0 );
			glUniform1i( this.scene.getUniform( "useTexture" ), GL_TRUE );
			glBindTexture( GL_TEXTURE_2D, this.texture );
		} else {
			glColor4f( this.color[0], this.color[1], this.color[2], this.color[3] );
			glUniform1i( this.scene.getUniform( "useTexture" ), GL_FALSE );
			glDisableVertexAttribArray( this.scene.getAttrib( "texture" ) );
		}
	}
	
	public void init() { this.init( false ); }
	public void init( boolean textureOnly ) {
		this.ready = false;
		
		if( !this.textureFile.equals( "" ) )
			this.createTexture();
		
		if( textureOnly )
			return;
		
		this.build();
		
		this.ibuf = new GLBuffer( GL_ELEMENT_ARRAY_BUFFER, this.iba );
		
		this.nbuf = new GLBuffer( GL_ARRAY_BUFFER, this.nba )
			.setAttribute( this.scene.getAttrib("normal" ) )
			.setSSO( 3, 0, 0 );
		
		this.tbuf = new GLBuffer( GL_ARRAY_BUFFER, this.tba )
			.setAttribute( this.scene.getAttrib("texture" ) )
			.setSSO( 2, 0, 0 );
		
		this.vbuf = new GLBuffer( GL_ARRAY_BUFFER, this.vba )
			.setAttribute( this.scene.getAttrib("vertex" ) )
			.setSSO( 3, 0, 0 );
		
		if( this.bound ) {
			this.bbox = new Cube( this.xMin, this.yMin, this.zMin, this.xMax, this.yMax, this.zMax, this.position );
			this.bbox.setScene( this.scene );
			this.bbox.init();
		}
		
		this.ready = true;
	}
	
	public void build() {
		this.vba = Buffer.toArray( this.v );
		this.tba = Buffer.toArray( this.t );
		this.nba = Buffer.toArray( this.n );
		this.iba = new int[ this.vba.length / 3 ];
		
		int v = 0;
		for( int i = 0; i < this.vba.length / 3; i++ ) {
			this.iba[v] = v++;
		}
	}
	
	public void buffer() {
		this.vbuf.buffer();
		this.nbuf.buffer();
		this.tbuf.buffer();
		this.ibuf.buffer();
	}
	
	public void render() { this.render( this.renderMode ); }
	public void render( int renderMode ) {
		if( this.ready ) {
			glUniformMatrix4( this.scene.getUniform( "mvMatrix"), false, this.matrix.gl() );

			//if( this.id != -1 )
				//glPushName( this.id );
			
			this.applyTexture();
			this.buffer();
			glDrawElements( renderMode, this.vba.length / 3, GL_UNSIGNED_INT, 0 );

			//if( this.id != -1 )
				//glPopName();
		}
	}
}
