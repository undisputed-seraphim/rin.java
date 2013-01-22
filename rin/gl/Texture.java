package rin.gl;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

import rin.util.IO;

public class Texture {
	public static int fromFile( String file ) {
		PNGDecoder decoder;
		ByteBuffer buf;
		
		try {
			decoder = new PNGDecoder( IO.file.asInputStream( file ) );
			buf = ByteBuffer.allocateDirect( 4 * decoder.getWidth() * decoder.getHeight() );
			decoder.decode( buf, decoder.getWidth()*4, Format.RGBA );
			buf.rewind();
		} catch( IOException e ) {
			System.out.println( "IOException raised. file = " + file );
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
		
		return texture;
	}
}
