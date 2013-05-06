package rin.engine.resource.image.dds;

import java.util.ArrayList;

import rin.engine.resource.image.ImageContainer;
import rin.engine.resource.image.PixelFormat;
import rin.util.bio.BinaryBuffer;

public class DdsUtils {
	
	private static short[] dxt1_RGB565ToRGB888( int c ) {
		int r = ((c & 0xf800) >> 11);
		r = (r << 3) | (r >> 2);
		int g = ((c & 0x07e0) >> 5);
		g = (g << 2) | (g >> 4);
		int b = ((c & 0x001f));
		b = (b << 3) | (b >> 2);
		
		return new short[] { (short)r, (short)g, (short)b };
	}
	
	private static short[] dxt1_code2Greater( short[] color0, short[] color1 ) {
		short[] res = new short[3];
		
		res[0] = (short)((2 * color0[0] + color1[0] ) / 3);
		res[1] = (short)((2 * color0[1] + color1[1] ) / 3);
		res[2] = (short)((2 * color0[2] + color1[2] ) / 3);
		
		return res;
	}
	
	private static short[] dxt1_code2Less( short[] color0, short[] color1 ) {
		short[] res = new short[3];
		
		res[0] = (short)((color0[0] + color1[0]) / 2);
		res[1] = (short)((color0[1] + color1[1]) / 2);
		res[2] = (short)((color0[2] + color1[2]) / 2);
		
		return res;
	}
	
	private static short[] dxt1_code3Greater( short[] color0, short[] color1 ) {
		short[] res = new short[3];
		
		res[0] = (short)((color0[0] + 2*color1[0]) / 3);
		res[1] = (short)((color0[1] + 2*color1[1]) / 3);
		res[2] = (short)((color0[2] + 2*color1[2]) / 3);
		
		return res;
	}
	
	private static byte dxt1_getCode( byte a, byte b ) {
		if( a == 1 && b == 1 )
			return 3;
		if( a == 1 && b == 0 )
			return 2;
		if( a == 0 && b == 1 )
			return 1;
		return 0;
	}
	
	private static byte[] dxt1_getCodes( byte[] bits ) {
		byte[] res = new byte[4];
		for( int i = 0; i < 4; i++ )
			res[i] = dxt1_getCode( bits[i*2], bits[i*2+1] );
		return res;
	}
	
	public static ImageContainer fromRawDXT1( int width, int height, byte[] data ) {
		//ArrayList<short[][][]> blocks = new ArrayList<short[][][]>();		
		BinaryBuffer buf = new BinaryBuffer( data );
		buf.setLittleEndian();
		
		short[] pixels = new short[ width * height * 4 ];
		short[] color = new short[3];
		
		int w4 = (int)Math.ceil( width / 4.0 );
		int blocks = 0;
		int row = 0;
		int col = 0;
		
		int stop = height * width * 3 / 6;
		while( buf.position() < stop ) {
			int[] colors = buf.readUInt16( 2 );
			
			short[] color0 = dxt1_RGB565ToRGB888( colors[0] );
			short[] color1 = dxt1_RGB565ToRGB888( colors[1] );
			
			byte[][] codes = new byte[][] {
					dxt1_getCodes( buf.readBits() ),
					dxt1_getCodes( buf.readBits() ),
					dxt1_getCodes( buf.readBits() ),
					dxt1_getCodes( buf.readBits() )
			};

			//short[][][] block = new short[4][4][3];
			for( int i = 3; i >= 0; i-- ) {
				for( int j = 0; j < codes[i].length; j++ ) {
					int cur = (((row + i) * w4 * 4) + (col * 4 + 3 - j)) * 4;
					switch( codes[i][j] ) {
					
					case 0:
						//block[i][3-j] = color0;
						color[0] = color0[0];
						color[1] = color0[1];
						color[2] = color0[2];
						break;
						
					case 1:
						//block[i][3-j] = color1;
						color[0] = color1[0];
						color[1] = color1[1];
						color[2] = color1[2];
						break;
						
					case 2:
						if( colors[0] > colors[1] )
							color = dxt1_code2Greater( color0, color1 );
							//block[i][3-j] = dxt1_code2Greater( color0, color1 );
						else
							color = dxt1_code2Less( color0, color1 );
							//block[i][3-j] = dxt1_code2Less( color0, color1 );
						break;
						
					case 3:
						if( colors[0] > colors[1] )
							color = dxt1_code3Greater( color0, color1 );
							//block[i][3-j] = dxt1_code3Greater( color0, color1 );
						else {
							color[0] = 0;
							color[1] = 0;
							color[2] = 0;
						}
							//block[i][3-j] = new short[] { 0, 0, 0 };
						break;
						
					default:
						System.err.println( "DXT1 ERROR" );
						break;
					}
					pixels[cur] = color[0];
					pixels[cur+1] = color[1];
					pixels[cur+2] = color[2];
					pixels[cur+3] = 255;
				}
			}
			col++;
			blocks++;
			if( blocks % w4 == 0 ) {
				row += 4;
				col = 0;
			}
			//blocks.add( block );
		}
		ImageContainer img = new ImageContainer( width, height, pixels, PixelFormat.RGBA );
		return img;
	}
	
}
