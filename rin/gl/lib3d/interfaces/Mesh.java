package rin.gl.lib3d.interfaces;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.glDrawRangeElements;
import static org.lwjgl.opengl.GL20.*;

import rin.gl.lib3d.interfaces.GL;
import rin.gl.TextureManager;

public class Mesh extends Poly {
	private int textures = 0;
	private ArrayList<Integer> texture = new ArrayList<Integer>();
	private ArrayList<int[]> range = new ArrayList<int[]>();
	private int current = -1;
	
	public void addTexture( String textureFile ) { this.textures++; this.texture.add( TextureManager.load( textureFile ) ); }
	public void bindTexture() {
		if( this.isUsingUnique() ) {
			glUniform1i( GL.getUniform( "useColor" ), GL_TRUE );
			float[] color = this.getUniqueColor();
			glUniform4f( GL.getUniform( "color" ), color[0], color[1], color[2], 1.0f );
		} else if( this.isColored() || this.current == -1 ) {
			glUniform1i( GL.getUniform( "useColor" ), GL_TRUE );
			float[] color = this.getColor();
			glUniform4f( GL.getUniform( "color" ), color[0], color[1], color[2], color[3] );
		} else {
			glUniform1i( GL.getUniform( "useColor" ), GL_FALSE );
			if( this.texture.size() >= this.current + 1 )
				TextureManager.enable( this.texture.get( this.current ) );
		
			else
				TextureManager.disable();
		}
	}
	
	public void addRange( int start, int end ) { this.range.add( new int[] { start, end } ); }
	public void addTextureRange( String textureFile, int start, int end ) {		
		this.addTexture( textureFile );
		this.addRange( start, end );
	}
	
	public void render() {
		if( this.isReady() && this.isVisible() ) {
			if( this.buffer() ) {
				glUniformMatrix4( GL.getUniform( "mMatrix"), false, this.getMatrix().gl() );
				if( this.textures > 0 ) {
					int c = 0;
					for( int[] i : this.range ) {
						this.current = c++;
						this.bindTexture();
						glDrawRangeElements( this.getRenderMode(), 0, i[1], i[1] - i[0], GL_UNSIGNED_INT, i[0] * 4 );
					}
					this.current = -1;
				}
			}
		}
	}
}
