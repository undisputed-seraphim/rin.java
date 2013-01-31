package rin.gl.lib3d;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;

import java.util.ArrayList;

import rin.gl.GL;
import rin.gl.TextureManager;
import rin.gl.lib3d.shape.BoundingBox;

public class Renderable extends Actor {
	protected boolean ready = false;
	
	/* opengl render mode */
	protected int renderMode = GL_TRIANGLES;
	public int getRenderMode() { return this.renderMode; }
	public void setRenderMode( int renderMode ) { this.renderMode = renderMode; }
	
	/* temporary lists for vertices, normals, and texcoords */
	protected ArrayList<Float>		v = new ArrayList<Float>(),
									n = new ArrayList<Float>(),
									t = new ArrayList<Float>();
	
	protected String textureFile = "";
	protected int texture = -1;
	
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
		if( this.texture != -1 && !this.colored ) {
			glActiveTexture( GL_TEXTURE0 );
			glUniform1i( GL.getUniform( "useTexture" ), GL_TRUE );
			glBindTexture( GL_TEXTURE_2D, this.texture );
		} else {
			glDisable( GL_TEXTURE_2D );
			glUniform1i( GL.getUniform( "useTexture" ), GL_FALSE );
			glUniform4f( GL.getUniform( "color" ), this.color[0], this.color[1], this.color[2], this.color[3] );
			glDisableVertexAttribArray( GL.getAttrib( "texture" ) );
		}
	}
	
	/* to render shapes with solid rgba colors */
	protected boolean colored = false;
	protected float[] color = new float[]{ 1.0f, 0.0f, 0.0f, 1.0f };
	protected void setColored( boolean val ) { this.colored = val; }
	protected void setColor( float[] c ) { this.color = new float[]{ c[0], c[1], c[2], 1.0f }; }
	
	/* gl buffer pointers */
	protected GLBuffer ibuf = null, vbuf = null, nbuf = null, tbuf = null;
	
	/* bounding box values */
	protected boolean bound = true;
	protected BoundingBox bbox = null;
	protected float xMin = Float.POSITIVE_INFINITY, yMin = Float.POSITIVE_INFINITY, zMin = Float.POSITIVE_INFINITY,
					xMax = Float.NEGATIVE_INFINITY, yMax = Float.NEGATIVE_INFINITY, zMax = Float.NEGATIVE_INFINITY;
	
	public void getBounds( float[] v ) {
		this.xMin = Float.POSITIVE_INFINITY; this.yMin = Float.POSITIVE_INFINITY; this.zMin = Float.POSITIVE_INFINITY;
		this.xMax = Float.NEGATIVE_INFINITY; this.yMax = Float.NEGATIVE_INFINITY; this.zMax = Float.NEGATIVE_INFINITY;
		for( int i = 0; i < v.length; i += 3 )
			this.addBounds( v[i], v[i+1], v[i+2] );
	}
	
	public void addBounds( float x, float y, float z ) {
		xMin = Math.min( xMin, x );
		yMin = Math.min( yMin, y );
		zMin = Math.min( zMin, z );
		
		xMax = Math.max( xMax, x );
		yMax = Math.max( yMax, y );
		zMax = Math.max( zMax, z );
	}
	
	public void createBoundingBox() {
		this.bbox = new BoundingBox( this.xMin, this.yMin, this.zMin, this.xMax, this.yMax, this.zMax, this.position );
		this.ready = true;
	}
	
	public void showBoundingBox() { this.showBoundingBox( this.bbox.renderMode, false ); }
	public void showBoundingBox( int renderMode ) { this.showBoundingBox( renderMode, false ); }
	public void showBoundingBox( int renderMode, boolean unique ) {
		if( this.ready ) {
			if( unique ) {
				this.bbox.setColor( this.getUniqueColor() );
				this.bbox.setColored( true );
				this.bbox.render( renderMode );
			} else {
				this.bbox.render( renderMode );
			}
		}
		else this.createBoundingBox();
	}
	
	public boolean buffer() {
		this.vbuf.buffer();
		this.nbuf.buffer();
		this.tbuf.buffer();
		return this.ibuf.buffer();
	}
	
	public void render() { this.render( this.renderMode ); }
	public void render( int renderMode ) { this.render( renderMode, false ); }
	public void render( int renderMode, boolean unique ) { this.render( renderMode, unique, false ); }
	public void render( int renderMode, boolean unique, boolean override ) {
		if( this.ready ) {
			glUniformMatrix4( GL.getUniform( "mMatrix"), false, this.matrix.gl() );

			if( unique ) {
				this.setColored( true );
				if( !override )
					this.setColor( this.getUniqueColor() );
			} else this.setColored( false );
			this.applyTexture();
			if( this.buffer() )
				glDrawElements( renderMode, this.v.size() / 3, GL_UNSIGNED_INT, 0 );
		}
	}
}
