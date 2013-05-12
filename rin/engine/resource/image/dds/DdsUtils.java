package rin.engine.resource.image.dds;

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
		BinaryBuffer buf = new BinaryBuffer( data );
		buf.setLittleEndian();
		
		short[] pixels = new short[ width * height * 3 ];
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

			for( int i = 3; i >= 0; i-- ) {
				for( int j = 0; j < codes[i].length; j++ ) {
					int cur = (((row + i) * w4 * 4) + (col * 4 + 3 - j)) * 3;
					switch( codes[i][j] ) {
					case 0:
						color[0] = color0[0];
						color[1] = color0[1];
						color[2] = color0[2];
						break;
					case 1:
						color[0] = color1[0];
						color[1] = color1[1];
						color[2] = color1[2];
						break;
					case 2:
						if( colors[0] > colors[1] ) color = dxt1_code2Greater( color0, color1 );
						else color = dxt1_code2Less( color0, color1 );
						break;
					case 3:
						if( colors[0] > colors[1] ) color = dxt1_code3Greater( color0, color1 );
						else {
							color[0] = 0;
							color[1] = 0;
							color[2] = 0;
						}
						break;
					default:
						System.err.println( "DXT1 ERROR" );
						break;
					}
					pixels[cur] = color[0];
					pixels[cur+1] = color[1];
					pixels[cur+2] = color[2];
				}
			}
			col++;
			blocks++;
			if( blocks % w4 == 0 ) {
				row += 4;
				col = 0;
			}
		}
		ImageContainer img = new ImageContainer( width, height, pixels, PixelFormat.RGB );
		return img;
	}
	
	private static byte getBit( long l, int b ) {
		return ((l & (1 << b)) != 0) ? (byte)1 : (byte)0;
	}
	
	private static short dxt3_getAlphaFromBits( long l, int x, int y ) {
		int b = 4*(4*y+x);
		String b1 = getBit(l,b+3)+""+getBit(l,b+2)+""+getBit(l,b+1)+""+getBit(l,b);//l[b+3]+""+l[b+2]+""+l[b+1]+""+l[b+0];
		//System.out.println( "x:y" + x + ":" + y + " " + (b+3) + " " + (b+2) + " " + (b+1) + " " + b );
		return (short)(Integer.parseInt( b1, 2 ) * 255 / 15 );
	}
	
	//0 1 2 3   4 5 6 7    8 9 10 11    12 13 14 15
	private static long dxt3_getAlpha( byte[] a ) {
		/*byte[] res = new byte[64];
		for( int i = 0; i < a.length; i++ ) {
			for( int j = 0; j < a[i].length; j++ )
				res[i*8+j] = a[i][j];
		}
		return res;*/
		return a[0] + 256 * ( a[1] + 256 * ( a[2] + 256 * ( a[3] + 256 * ( a[4] +
                256 * ( a[5] + 256 * ( a[6] + 256 * a[7] ) ) ) ) ) );
		/*float[] res = new float[16];
		for( int i = 0; i < bits.length; i++ ) {
			String b1 = bits[i][0]+""+bits[i][1]+""+bits[i][2]+""+bits[i][3];
			String b2 = bits[i][4]+""+bits[i][5]+""+bits[i][6]+""+bits[i][7];
			res[i*2] = Integer.parseInt( b1, 2 ) / 15.0f;
			res[i*2+1] = Integer.parseInt( b2, 2 ) / 15.0f;
		}
		return res;*/
	}
	
	public static ImageContainer fromRawDXT3( int width, int height, byte[] data ) {
		BinaryBuffer buf = new BinaryBuffer( data );
		buf.setLittleEndian();
		
		short[] pixels = new short[ width * height * 4 ];
		short[] color = new short[3];
		
		int w4 = (int)Math.ceil( width / 4.0 );
		int blocks = 0;
		int row = 0;
		int col = 0;
		
		int stop = height * width;
		while( buf.position() < stop ) {
			//buf.setBigEndian();
			long alpha = dxt3_getAlpha( buf.readInt8( 8 ) );
			//buf.setLittleEndian();
			
			int[] colors = buf.readUInt16( 2 );
			
			short[] color0 = dxt1_RGB565ToRGB888( colors[0] );
			short[] color1 = dxt1_RGB565ToRGB888( colors[1] );
			
			byte[][] codes = new byte[][] {
					dxt1_getCodes( buf.readBits() ),
					dxt1_getCodes( buf.readBits() ),
					dxt1_getCodes( buf.readBits() ),
					dxt1_getCodes( buf.readBits() )
			};

			for( int i = 3; i >= 0; i-- ) {
				for( int j = 0; j < codes[i].length; j++ ) {
					int cur = (((row + i) * w4 * 4) + (col * 4 + 3 - j)) * 4;
					switch( codes[i][j] ) {
					case 0:
						color[0] = color0[0];
						color[1] = color0[1];
						color[2] = color0[2];
						break;
					case 1:
						color[0] = color1[0];
						color[1] = color1[1];
						color[2] = color1[2];
						break;
					case 2:
						//if( colors[0] > colors[1] ) color = dxt1_code2Greater( color0, color1 );
						//else color = dxt1_code2Less( color0, color1 );
						color = dxt1_code2Greater( color0, color1 );
						break;
					case 3:
						color = dxt1_code3Greater( color0, color1 );
						break;
					default:
						System.err.println( "DXT3 ERROR" );
						break;
					}
					pixels[cur] = color[0];
					pixels[cur+1] = color[1];
					pixels[cur+2] = color[2];
					pixels[cur+3] = dxt3_getAlphaFromBits( alpha, i, j );
				}
			}
			col++;
			blocks++;
			if( blocks % w4 == 0 ) {
				row += 4;
				col = 0;
			}
		}
		ImageContainer img = new ImageContainer( width, height, pixels, PixelFormat.RGBA );
		return img;
	}
	
}
