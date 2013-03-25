package rin.gl.texture;

import static org.lwjgl.opengl.EXTTextureCompressionS3TC.GL_COMPRESSED_RGBA_S3TC_DXT3_EXT;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_UNPACK_ALIGNMENT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glPixelStorei;
import static org.lwjgl.opengl.GL11.glTexImage2D;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import rin.gl.TextureManager;
import rin.util.Buffer;
import rin.util.RinUtils;
import rin.util.bio.BIOBuffer;

public final class ImportDDS {
    
    public ImportDDS() {}
    
    public static int loadDDS( byte[] data, int format, int width, int height, String name, int mipmaps ) {
    	if( TextureManager.find( name ) != -1 ) {
    		return TextureManager.find( name );
    	}
    	
    	// create the GL Name
        IntBuffer glName = BufferUtils.createIntBuffer(1);
        
        // create the texture
        GL11.glGenTextures(glName);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,glName.get(0));
        
        /* Implement the filtering stuff anywhere you want, this is only here to
           have at least one filter applied on the texture */
        
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_WRAP_S,GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_WRAP_T,GL11.GL_REPEAT);
        glPixelStorei( GL_UNPACK_ALIGNMENT, 4 );
        GL13.glCompressedTexImage2D(GL11.GL_TEXTURE_2D,0,format,width,height,0,Buffer.toBuffer( data ) );
        //glTexImage2D( GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buf );
        //GL11.glTexImage2D(GL_TEXTURE_2D, 0, format, width, height, 0, format, GL_UNSIGNED_BYTE, Buffer.toBuffer( data ) );
        
        int block = 8;
        if( format == GL_COMPRESSED_RGBA_S3TC_DXT3_EXT )
        	block = 16;

        int pos = 0;
        for( int i = 0; i < mipmaps; i++ ) {
        	int w = (width >> i);
        	int h = (height >> i);
        	if( w < 1 ) w = 1;
        	if( h < 1 ) h = 1;
        	int size;
        	
        	size = ((w+3)/4)*((h+3)/4)*block;
        	GL13.glCompressedTexImage2D(GL11.GL_TEXTURE_2D,i,format,w,h,0,Buffer.toBuffer( RinUtils.splice( data, pos, size, new byte[size] ) ) );
        	pos += size;
        }
        
        if( mipmaps > 0 ) {
        	// use the mipmaps
        	GL11.glTexParameteri( GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR );
			GL11.glTexParameteri( GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR );
        } else {
            // Linear Filtering
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        }
        
        TextureManager.add( glName.get( 0 ), name );
        return glName.get( 0 );
    }
}