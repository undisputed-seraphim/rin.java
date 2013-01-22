package rin.gl.lib3d;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import rin.gl.Texture;
import rin.util.Buffer;

/**
 * Class that contains information needed to render vertexed object
 *  e.g. vertices, normals, texture coordinates, etc.
 *
 */
public class Poly extends Actor {
	/* name of this poly */
	protected String name = "";
	
	/* if poly is ready to be rendered */
	public boolean ready = false;
	
	/* opengl render mode */
	protected int renderMode = GL_TRIANGLES;
	
	/* texture file string and opengl texture number */
	protected String textureFile = "";
	protected int texture = -1;
	protected boolean colored = false;
	
	protected int[] range = new int[]{ 0, 0 };
	//private float[] color = new float[]{ 1.0f, 0.0f, 0.0f, 1.0f };
	
	/* temporary lists for vertices, normals, and texcoords */
	protected ArrayList<Float>	v = new ArrayList<Float>(),
								n = new ArrayList<Float>(),
								t = new ArrayList<Float>();
	
	/* bounding box values */
	protected Float xMin = null, xMax = null,
					yMin = null, yMax = null,
					zMin = null, zMax = null;
	
	/* add bound values to the xmax, ymax, etc if applicable */
	private void addBounds( float x, float y, float z ) {
		if( xMin == null ) xMin = x;
		else if( xMax == null ) xMax = x;
		else {
			if( x > xMax ) xMax = x;
			if( x < xMin ) xMin = x;
		}
		
		if( yMin == null ) yMin = y;
		else if( yMax == null ) yMax = y;
		else {
			if( y > yMax ) yMax = y;
			if( y < yMin ) yMin = y;
		}
		
		if( zMin == null ) zMin = z;
		else if( zMax == null ) zMax = z;
		else {
			if( z > zMax ) zMax = z;
			if( z < zMin ) zMin = z;
		}
	}
	
	/* arrays for the final indices, vertices, normals, and texcoords */
	protected int[] iba;
	protected float[] vba, nba, tba;
	
	/* gl buffer pointers */
	protected int ibo, vbo, nbo, tbo;
	
	public Poly( String name ) {
		this.name = name;
	}

	public String getName() { return this.name; }
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
	
	public void init() { this.init( false ); }
	public void init( boolean textureOnly ) {
		this.ready = false;
		
		if( !this.textureFile.equals( "" ) )
			this.createTexture();
		
		if( textureOnly )
			return;
		
		this.build();
		
		this.ibo = glGenBuffers();
		glBindBuffer( GL_ELEMENT_ARRAY_BUFFER, this.ibo );
		glBufferData( GL_ELEMENT_ARRAY_BUFFER, Buffer.toBuffer( this.iba ), GL_STATIC_DRAW );
		glBindBuffer( GL_ELEMENT_ARRAY_BUFFER, 0 );
		
		if( this.nba.length > 0 ) {
			this.nbo = glGenBuffers();
			glBindBuffer( GL_ARRAY_BUFFER, this.nbo );
			glBufferData( GL_ARRAY_BUFFER, Buffer.toBuffer( this.nba ), GL_STATIC_DRAW );
			glBindBuffer( GL_ARRAY_BUFFER, 0 );
		}
		
		if( this.tba.length > 0 ) {
			this.tbo = glGenBuffers();
			glBindBuffer( GL_ARRAY_BUFFER, this.tbo );
			glBufferData( GL_ARRAY_BUFFER, Buffer.toBuffer( this.tba ), GL_STATIC_DRAW );
			glBindBuffer( GL_ARRAY_BUFFER, 0 );
		}
		
		this.vbo = glGenBuffers();
		glBindBuffer( GL_ARRAY_BUFFER, this.vbo );
		glBufferData( GL_ARRAY_BUFFER, Buffer.toBuffer( this.vba ), GL_STATIC_DRAW );
		glBindBuffer( GL_ARRAY_BUFFER, 0 );
		
		this.ready = true;
	}
	
	public void build() {
		this.vba = Buffer.toArray( this.v );
		this.tba = Buffer.toArray( this.t );
		this.nba = Buffer.toArray( this.n );
		this.iba = new int[ this.vba.length / 3 ];
		
		int v = 0;
		for( int i = 0; i < this.vba.length; i++ ) {
			if( i % 3 == 0 )
				this.iba[v] = v++;
		}
	}
	
	public void buffer() {
		glBindBuffer( GL_ARRAY_BUFFER, this.vbo );
		glVertexAttribPointer( this.scene.getAttrib( "vertex" ), 3, GL_FLOAT, false, 0, 0 );
		glEnableVertexAttribArray( this.scene.getAttrib( "vertex" ) );
		
		if( this.nba.length > 0 ) {
			glBindBuffer( GL_ARRAY_BUFFER, this.nbo );
			glVertexAttribPointer( this.scene.getAttrib( "normal" ), 3, GL_FLOAT, false, 0, 0 );
			glEnableVertexAttribArray( this.scene.getAttrib( "normal" ) );
		} else glDisableVertexAttribArray( this.scene.getAttrib( "normal" ) );
		
		if( this.tba.length > 0 && !this.colored ) {
			glBindBuffer( GL_ARRAY_BUFFER, this.tbo );
			glVertexAttribPointer( this.scene.getAttrib( "texture" ), 2, GL_FLOAT, false, 0, 0 );
			glEnableVertexAttribArray( this.scene.getAttrib( "texture" ) );
		} else glDisableVertexAttribArray( this.scene.getAttrib( "texture" ) );
	}
	
	public void render() {
		if( this.ready ) {
			this.buffer();
			glUniformMatrix4( this.scene.getUniform( "mvMatrix"), false, this.matrix.gl() );
			/* bind the indices buffer */
			glBindBuffer( GL_ELEMENT_ARRAY_BUFFER, this.ibo );

			if( this.id != -1 )
				glPushName( this.id );

			/* if poly contains a texture */
			if( this.texture != -1 ) {
				glActiveTexture( GL_TEXTURE0 );
				glUniform1i( this.scene.getUniform( "useTexture" ), GL_TRUE );
				glBindTexture( GL_TEXTURE_2D, this.texture );
			}

			/* if not, disable texture and set a color */
			else {
				glUniform1i( this.scene.getUniform( "useTexture" ), GL_FALSE );
				glDisableVertexAttribArray( this.scene.getAttrib( "texture" ) );
			}

			glDrawElements( this.renderMode, this.vba.length / 3, GL_UNSIGNED_INT, 0 );

			if( this.id != -1 )
				glPopName();
		}
	}
	
	public void showBoundingBox() {
		float	x = this.xMin, X = this.xMax,
				y = this.yMin, Y = this.yMax,
				z = this.zMin, Z = this.zMax;
		
		System.out.println( this.name + " selected!" );
		glUniform1i( this.scene.getUniform( "useTexture" ), GL_FALSE );
		glColor4f( 1.0f, 0.0f, 0.0f, 0.3f );
		
		/* bounding box */
		glBegin( GL_LINE_STRIP );
		glVertex3f( x, y, z );
		glVertex3f( x, Y, z );
		glVertex3f( x, Y, Z );
		glVertex3f( x, y, Z );
		glVertex3f( x, y, z );
		
		glVertex3f( X, y, z );
		glVertex3f( X, Y, z );
		glVertex3f( X, Y, Z );
		glVertex3f( X, y, Z );
		glVertex3f( X, y, z );
		glVertex3f( X, y, Z );
		glVertex3f( x, y, Z );
		
		glVertex3f( x, Y, Z );
		glVertex3f( X, Y, Z );
		glVertex3f( X, Y, z );
		glVertex3f( x, Y, z );
		glEnd();
	}
}
