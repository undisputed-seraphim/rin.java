package rin.gl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glUniform1i;
import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

import rin.gl.lib3d.interfaces.GL;
import rin.util.Buffer;
import rin.util.IO;

public class TextureManager {
	private static ArrayList<String> names = new ArrayList<String>();
	private static ArrayList<Integer> uses = new ArrayList<Integer>();
	private static ArrayList<Integer> ids = new ArrayList<Integer>();
	private static int currentTextureId = -1;
	private static boolean textureOn = false;
	
	/** Create an opengl Texture resource.
	 * @param file absolute file location of desired image
	 * @return int id of the opengl resource
	 */
	public static int load( String file ) {
		if( TextureManager.names.indexOf( file ) != -1 ) {
			int pos = TextureManager.names.indexOf( file );
			System.out.println( "[NOTIFY] Texture [" + file + "] already loaded at " + TextureManager.ids.get( pos ) + "." );
			TextureManager.uses.set( pos, TextureManager.uses.get( pos ) + 1 );
			return TextureManager.ids.get( pos );
		}
		
		PNGDecoder decoder;
		ByteBuffer buf;
		
		try {
			decoder = new PNGDecoder( IO.file.asInputStream( file ) );
			buf = ByteBuffer.allocateDirect( 4 * decoder.getWidth() * decoder.getHeight() );
			decoder.decode( buf, decoder.getWidth()*4, Format.RGBA );
			buf.rewind();
		} catch( FileNotFoundException e ) {
			System.out.println( "[WARNING] Could not find file [" + file + "]." );
			return -1;
		} catch( IOException e ) {
			System.out.println( "[WARNING] IOException on file [" + file + "]." );
			return -1;
		}
		
		int texture = glGenTextures();
		glBindTexture( GL_TEXTURE_2D, texture );
		glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST );
		glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST );
		glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT );
		glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT );
		glPixelStorei( GL_UNPACK_ALIGNMENT, 4 );
		glTexImage2D( GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buf );
		
		if( texture != -1 ) {
			System.out.println( "[LOADED] Texture [" + file + "] loaded at " + texture + "." );
			TextureManager.names.add( file );
			TextureManager.ids.add( texture );
			TextureManager.uses.add( 1 );

		}
		
		return texture;
	}
	
	public static void enable( int textureId ) {
		// if texture is valid and the manager is managing it
		if( textureId != -1 && TextureManager.ids.indexOf( textureId ) != -1 ) {
			// if the texture is not already the active texture
			if( TextureManager.currentTextureId != textureId ) {
				if( !textureOn ) {
					glUniform1i( GL.getUniform( "useTexture" ), GL_TRUE );
					glUniform1i( GL.getUniform( "useColor" ), GL_FALSE );
					TextureManager.textureOn = true;
				} else { System.out.println( "[NOTIFY] Texturing was already turned on." ); }
				glBindTexture( GL_TEXTURE_2D, textureId );
				TextureManager.currentTextureId = textureId;
			} else { System.out.println( "[NOTIFY] Texture id " + textureId + " was already the current texture." ); }
		} else {
			System.out.println( "[NOTIFY] Texture not valid." );
			glUniform1i( GL.getUniform( "useTexture" ), GL_FALSE );
			TextureManager.textureOn = false;
			TextureManager.currentTextureId = -1;
		}
	}
	
	public static void disable() {
		if( TextureManager.textureOn ) {
			glUniform1i( GL.getUniform( "useTexture" ), GL_FALSE );
			TextureManager.textureOn = false;
			TextureManager.currentTextureId = -1;
		} else { System.out.println( "[NOTIFY] Texture already turned off." ); }
	}
	
	public static void unload( int textureId ) {
		int pos;
		if( ( pos = TextureManager.ids.indexOf( textureId ) ) != -1 )
			TextureManager.unload( TextureManager.names.get( pos ) );
	}
	
	public static void unload( String file ) {
		if( TextureManager.names.indexOf( file ) == -1 ) {
			System.out.println( "[WARNING] Attempted to unload unknown texture [" + file + "]" );
			return;
		}
		
		int pos = TextureManager.names.indexOf( file );
		
		if( TextureManager.uses.get( pos ) > 1 ) {
			System.out.println( "[NOTIFY] texture [" + file + "] requested, but still in use." );
			TextureManager.uses.set( pos, TextureManager.uses.get( pos ) - 1 );
		}
		
		else {
			System.out.println( "[UNLOAD] Texture [" + file + "] unloaded from " + TextureManager.ids.get( pos ) + "." );
			glDeleteTextures(Buffer.toBuffer( new int[] { TextureManager.ids.get( pos ) } ) );
			TextureManager.names.remove( pos );
			TextureManager.ids.remove( pos );
			TextureManager.uses.remove( pos );
		}
	}
	
	public static void reset() {
		for( int i : TextureManager.ids )
			glDeleteTextures( Buffer.toBuffer( new int[] { i } ) );
		
		TextureManager.names.clear();
		TextureManager.ids.clear();
		TextureManager.uses.clear();
	}
}
