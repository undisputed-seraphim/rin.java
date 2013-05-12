package rin.engine.resource.image.png;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;

import rin.engine.resource.image.PixelFormat;
import rin.engine.util.ImageUtils;

public class PngSpec {

	public static final byte[] MAGIC = new byte[] { (byte)137, 80, 78, 71, 13, 10, 26, 10 };
	
	public static final int C_IHDR = 0x49484452;
	public static final int C_IDAT = 0x49444154;
	public static final int C_IEND = 0x49454E44;
	
		public static final int T_COLOR_GREYSCALE = 0;
		public static final int T_COLOR_TRUECOLOR = 2;
		public static final int T_COLOR_INDEXED = 3;
		public static final int T_COLOR_GREYSCALE_ALPHA = 4;
		public static final int T_COLOR_TRUECOLOR_ALPHA = 6;
	
		public static final int T_COMPRESSION_DEFAULT = 0;
	
		public static final int T_FILTER_DEFAULT = 0;
		public static final byte T_FILTER_NONE = 0;
	
		public static final byte T_INTERLACE_NONE = 0;
		public static final byte T_INTERLACE_ADAM7 = 1;
	
	public static byte[] filterAllAndCompress( byte filter, PixelFormat pformat, int colorFormat, int w, int h, short[] data ) {
		PixelFormat format = PixelFormat.RGBA;
		switch( colorFormat ) {
		case T_COLOR_TRUECOLOR:
			data = ImageUtils.convert( pformat, PixelFormat.RGB, data );
			format = PixelFormat.RGB;
			break;
		case T_COLOR_TRUECOLOR_ALPHA:
			data = ImageUtils.convert( pformat, PixelFormat.RGBA, data );
			format = PixelFormat.RGBA;
			break;
		}
		int stride = format.getStride();
		byte[] res = new byte[ (w+1) * h * stride ];
		
		switch( filter ) {
		case T_FILTER_NONE: default:
			for( int i = 0; i < h; i++ ) {
				res[i*(w*stride+1)] = filter;
				for( int j = 0; j < w*stride; j++ )
					res[i*(w*stride+1)+j+1] = (byte)data[i*w*stride+j];
			}
			break;
		}
		
		Deflater deflater = new Deflater();
		deflater.setInput( res );
		ByteArrayOutputStream os = new ByteArrayOutputStream( res.length );
		deflater.finish();
		
		byte[] buffer = new byte[1024];
		while( !deflater.finished() ) {
			int count = deflater.deflate( buffer );
			os.write( buffer, 0, count );
		}
		
		byte[] ret = os.toByteArray();
		deflater.end();
		try {
			os.close();
		} catch( IOException ex ) {
			System.out.println( "PngSpec#filterAllAndCompress(byte,int,PixelFormat,int,int,short[]): IOException" );
		}
		
		return ret;
	}
	
}
