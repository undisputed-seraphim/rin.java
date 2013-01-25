package rin.gl.lib3d;

import java.util.ArrayList;

import rin.gl.GL;
import rin.util.Buffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * A special extension of {@link Actor} that groups a set of {@link Poly}s together
 *  (usually to create a specific {@link Model}) which can then be rendered to a {@link Scene}.
 *  <p>
 *  A {@code Mesh} has the benefit of being able to use a single interleaved buffer
 *   used to have a single interleaved buffer render multiple {@link Poly}s.
 *  
 *  @see {@link Actor}, {@link Poly}
 */
public class Mesh extends Boundable {
	public static enum VertexFormat {
		VNT,
		VN,
		VT;
	}
	protected VertexFormat vertexFormat = VertexFormat.VNT;
	
	/* Mesh are broken into polys per texture [opengl effeciency] */
	private ArrayList<Poly> polys = new ArrayList<Poly>();
	private int currentPoly = -1;
	
	public void addPoly( String name ) {		
		this.polys.add( new Poly( name ) );
		this.currentPoly = this.polys.size() - 1;
	}
	
	/* add data to a mesh's polys */
	public void addVertex( float x, float y, float z ) { this.addBounds( x, y, z ); this.polys.get( this.currentPoly ).addVertex( x, y, z ); }
	public void addVertices( float[] v, ArrayList<Integer> indices, int offset, int stride, int count ) {
		for( int i = offset; i < indices.size(); i += stride ) {
			//this.addBounds( v[ indices.get( i )*count ], v[ indices.get( i )*count+1 ], v[ indices.get( i )*count+2 ] );
			this.addVertex( v[ indices.get( i )*count ], v[ indices.get( i )*count+1 ], v[ indices.get( i )*count+2 ] );
		}
	}
	public void addNormal( float x, float y, float z ) { this.polys.get( this.currentPoly ).addNormal( x, y, z ); }
	public void addNormals( float[] n, ArrayList<Integer> indices, int offset, int stride, int count ) {
		for( int i = offset; i < indices.size(); i += stride )
			this.addNormal( n[ indices.get( i )*count ], n[ indices.get( i )*count+1 ], n[ indices.get( i )*count+2 ] );
	}
	public void addTexture( float s, float t ) { this.polys.get( this.currentPoly ).addTexture( s, t ); }
	public void addTexcoords( float[] t, ArrayList<Integer> indices, int offset, int stride, int count ) {
		for( int i = offset; i < indices.size(); i += stride )
			this.addTexture( t[ indices.get( i )*count ], t[ indices.get( i )*count+1 ] );
	}
	public void setTexture( String file ) { this.polys.get( this.currentPoly ).setTextureFile( file ); }
	
	public int getVertexCount() { int v = 0; for( Poly p : this.polys ) v += p.getVertices().size(); return v; }
	public int getNormalCount() { int n = 0; for( Poly p : this.polys ) n += p.getVertices().size(); return n; }
	public int getTexcoordCount() { int t = 0; for( Poly p : this.polys ) t += p.getVertices().size(); return t; }
	
	/* final arrays for storing indices, vertex, normal, texture coordinates data from polys */
	private int[] iba;
	private float[] vba, nba, tba, aba;
	
	/* opengl buffers */
	private int ibo = -1, abo = -1;
	
	/* if set, all data will be contained within a single buffer */
	private boolean interleaved = false;
	public Mesh setInterleaved( boolean val ) { this.interleaved = val; return this; }
	
	/* mesh is ready to be rendered */
	private boolean ready = false;
	
	/* initialize the buffer objects for the mesh */
	public void init() {
		this.ready = false;
		
		if( this.interleaved ) {
			this.build();
		} else {
			/* initialize the polys so they can render themselves */
			for( Poly p : this.polys ) {
				p.init();
			}
		}
		
		this.createBBox();
		this.ready = true;
	}
	
	/* combine all poly information into one single array */
	public void build() {
		for( Poly p : this.polys )
			p.init( true );
		
		this.vba = new float[ this.getVertexCount() ];
		this.nba = new float[ this.getNormalCount() ];
		this.tba = new float[ this.getTexcoordCount() ];
		this.iba = new int[ this.vba.length / 3 ];
		
		if( this.interleaved )
			this.aba = new float[ this.vba.length + this.nba.length + this.tba.length ];
		
		int v = 0, n = 0, t = 0, i = 0, a = 0;
		for( Poly p : this.polys ) {
			p.range[0] = i;
			for( float f : p.getVertices() ) {
				this.vba[v++] = f;
				if( v % 3 == 0 )
					this.iba[i] = i++;
			}
			
			for( float f : p.getNormals() ) {
				this.nba[n++] = f;
			}
			
			for( float f : p.getTexcoords() ) {
				this.tba[t++] = f;
			}
			
			p.range[1] = i;
		}
		
		if( this.interleaved ) {
			/* pack the vertices into an interleaved float array, vao */
			for( i = 0; i < this.vba.length / 3; i++ ) {
				this.aba[a++] = this.vba[i*3];
				this.aba[a++] = this.vba[i*3+1];
				this.aba[a++] = this.vba[i*3+2];
				this.aba[a++] = this.nba[i*3];
				this.aba[a++] = this.nba[i*3+1];
				this.aba[a++] = this.nba[i*3+2];
				this.aba[a++] = this.tba[i*2];
				this.aba[a++] = this.tba[i*2+1];
				//this.addBounds( this.vba[i*3], this.vba[i*3+1], this.vba[i*3+2] );
			}
		}
		
		this.ibo = glGenBuffers();
		glBindBuffer( GL_ELEMENT_ARRAY_BUFFER, this.ibo );
		glBufferData( GL_ELEMENT_ARRAY_BUFFER, Buffer.toBuffer( this.iba ), GL_STATIC_DRAW );
		glBindBuffer( GL_ELEMENT_ARRAY_BUFFER, 0 );
		
		this.abo = glGenBuffers();
		glBindBuffer( GL_ARRAY_BUFFER, this.abo );
		glBufferData( GL_ARRAY_BUFFER, Buffer.toBuffer( this.aba ), GL_STATIC_DRAW );
		glBindBuffer( GL_ARRAY_BUFFER, 0 );
	}
	
	public void buffer() {
		int stride = (3 + 3 + 2) * 4; //vertex, normal, texture
		int offset = 0;
			
		glBindBuffer( GL_ARRAY_BUFFER, this.abo );
		glVertexAttribPointer( GL.scene.getAttrib( "vertex" ), 3, GL_FLOAT, false, stride, offset );
		glEnableVertexAttribArray( GL.scene.getAttrib( "vertex" ) );

		if( this.vertexFormat == VertexFormat.VN || this.vertexFormat == VertexFormat.VNT ) {
			glVertexAttribPointer( GL.scene.getAttrib( "normal" ), 3, GL_FLOAT, false, stride, 3 * 4 );
			glEnableVertexAttribArray( GL.scene.getAttrib( "normal" ) );
		} else glDisableVertexAttribArray( GL.scene.getAttrib( "normal" ) );

		if( this.vertexFormat == VertexFormat.VT || this.vertexFormat == VertexFormat.VNT ) {
			glVertexAttribPointer( GL.scene.getAttrib( "texture" ), 2, GL_FLOAT, false, stride, (3 + 3) * 4 );
			glEnableVertexAttribArray( GL.scene.getAttrib( "texture" ) );
		} else glDisableVertexAttribArray( GL.scene.getAttrib( "texture" ) );
	}
	
	public void render() {
		if( this.ready ) {
			if( !this.interleaved )
				for( Poly p : this.polys )
					p.render();
			else {
				this.buffer();
				glUniformMatrix4( GL.scene.getUniform( "mMatrix"), false, this.matrix.gl() );
				glBindBuffer( GL_ELEMENT_ARRAY_BUFFER, this.ibo );
				
				if( this.id != -1 )
					glPushName( this.id );
				
				/* for each different poly ( different texture ) */
				for( Poly p : this.polys ) {
					int[] cur = p.range;
					
					/* if poly contains a texture */
					if( p.texture != -1 ) {
						glActiveTexture( GL_TEXTURE0 );
						glUniform1i( GL.scene.getUniform( "useTexture" ), GL_TRUE );
						glBindTexture( GL_TEXTURE_2D, p.texture );
					}
					
					/* if not, disable texture and set a color */
					else {
						glUniform1i( GL.scene.getUniform( "useTexture" ), GL_FALSE );
						glDisableVertexAttribArray( GL.scene.getAttrib( "texture" ) );
					}
					
					glDrawRangeElements( GL_TRIANGLES, 0, cur[1], cur[1] - cur[0], GL_UNSIGNED_INT, cur[0] * 4 );
				}

				if( this.id != -1 )
					glPopName();
			}
		}
	}
}
