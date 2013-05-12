package rin.engine.resource.image.png;

import static rin.engine.resource.image.png.PngSpec.*;

import java.nio.ByteBuffer;

import rin.engine.resource.Resource;
import rin.engine.resource.image.ImageContainer;
import rin.engine.resource.image.ImageEncoder;
import rin.engine.resource.image.ImageOptions;
import rin.engine.util.binary.BinaryWriter;

import java.util.zip.CRC32;

public class PngEncoder extends BinaryWriter implements ImageEncoder {

	@Override
	public String getExtensionName() { return "png"; }

	@Override
	public PngOptions getDefaultOptions() { return new PngOptions(); }
	
	@Override
	public boolean encode( ImageContainer from, Resource to, ImageOptions options ) {
		load( to );
		CRC32 crc = new CRC32();

		PngOptions opts = (PngOptions)options;
		
		// write the png magic
		writeInt8( MAGIC );
		
		// get the color format this image will be using
		int colorFormat = T_COLOR_TRUECOLOR;
		switch( from.getFormat() ) {
		case G:
			colorFormat = T_COLOR_GREYSCALE;
			break;
		case GA:
			colorFormat = T_COLOR_GREYSCALE_ALPHA;
			break;
		case BGR:
		case RGB:
			colorFormat = T_COLOR_TRUECOLOR;
			break;
		case ABGR:
		case ARGB:
		case RGBA:
		case BGRA:
			colorFormat = T_COLOR_TRUECOLOR_ALPHA;
			break;
		default: System.err.println( "PixelFormat " + from.getFormat() + " not yet implemented." ); break;
		}
		System.out.println( from.getFormat() + " " + colorFormat );
		if( opts.isIndexed() ) colorFormat = T_COLOR_INDEXED;
		byte interlaceFormat = T_INTERLACE_NONE;
		if( opts.isInterlaced() ) interlaceFormat = T_INTERLACE_ADAM7;
		int w = from.getWidth();
		int h = from.getHeight();
		
		// IHDR
		crc.reset();
		ByteBuffer ihdr = ByteBuffer.allocate( 25 );
		
		ihdr.putInt( 13 );					// chunk data length
		ihdr.putInt( C_IHDR );				// chunk type magic
		ihdr.putInt( w );					// width
		ihdr.putInt( h );					// height
		ihdr.put( (byte)8 );				// bit depth (image containers are always 8 bit depth)
		ihdr.put( (byte)colorFormat );		// color type
		ihdr.put( (byte)0 );				// compression	(always 0, default)
		ihdr.put( (byte)0 );				// filter	(always 0, just because)
		ihdr.put( (byte)interlaceFormat );	// interlace
		crc.update( ihdr.array(), 4, 17 );
		ihdr.putInt( (int)crc.getValue() );	// crc
		writeBuffer( ihdr );
		
		// if this image has alpha, create the alpha table
		if( opts.isIndexed() ) {
			if( from.hasAlpha() ) {
				//TODO: get the alpha from the data, filter it, and create tRNS table
			}
		}
		
		// filter and compress the image data
		byte[] idata = filterAllAndCompress( T_FILTER_NONE, from.getFormat(), colorFormat, w, h, from.getData() );

		//IDAT
		crc.reset();
		ByteBuffer idat = ByteBuffer.allocate( 12 + idata.length );
		
		idat.putInt( idata.length );
		idat.putInt( C_IDAT );
		idat.put( idata );
		crc.update( idat.array(), 4, idat.capacity() - 8 );
		writeBuffer( idat );
		
		//IEND
		crc.reset();
		ByteBuffer iend = ByteBuffer.allocate( 12 );
		
		iend.putInt( 0 );
		iend.putInt( C_IEND );
		crc.update( iend.array(), 4, iend.capacity() - 8 );
		iend.putInt( (int)crc.getValue() );
		writeBuffer( iend );
		
		close();
		return true;
	}

}
