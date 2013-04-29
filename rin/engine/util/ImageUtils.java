package rin.engine.util;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;

import rin.util.bio.BinaryBuffer;

public class ImageUtils {

	private static class ImagePanel extends JFrame {
		private static final long serialVersionUID = 1L;
		
		short[] data;
		private int width;
		private int height;
		private int stride;
		
		public ImagePanel( short[] d, int w, int h, int s ) {
			data = d;
			width = w;
			height = h;
			stride = s;

			setSize( width + 40, height + 65 );
			setVisible( true );
			setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		}
		
		@Override
		public void paint( Graphics g ) {
			int col = 19;
			int row = 45;
			for( int i = 0; i < data.length; i+=stride ) {
				if( (i / stride) % width == 0 && i != 0 ) {
					col = 19;
					row++;
				}
				
				short[] c = new short[] {0,0,0,1};
				for( int j = 0; j < stride; j++ ) {
					c[j] = data[i+j];
				}
				
				g.setColor( new Color( c[0], c[1], c[2], c[3] ) );
				g.drawRect( col++, row, 1, 1 );
			}
		}
	}
	
	public static void test( int width, int height, int stride, short[] rawData ) {
		new ImagePanel( rawData, width, height, stride );
	}
	
	public static final int test = 0x11;
	public static enum PixelFormat {
		DXT1;
		
		public static PixelFormat fromString( String format ) {
			for( PixelFormat p : PixelFormat.values() )
				if( p.toString().equals( format.toUpperCase() ) )
					return p;
			return null;
		}
	}
	
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
	
	//TODO: update to properly return single dimension raw rgb array
	public static short[] dxt1_decode( int width, int height, byte[] pixelData ) {		
		ArrayList<short[][][]> blocks = new ArrayList<short[][][]>();
		BinaryBuffer buf = new BinaryBuffer( pixelData );
		buf.setLittleEndian();
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

			short[][][] block = new short[4][4][3];
			for( int i = 3; i >= 0; i-- ) {
				for( int j = 0; j < codes[i].length; j++ ) {
					switch( codes[i][j] ) {
					
					case 0:
						block[i][3-j] = color0;
						break;
						
					case 1:
						block[i][3-j] = color1;
						break;
						
					case 2:
						if( colors[0] > colors[1] )
							block[i][3-j] = dxt1_code2Greater( color0, color1 );
						else
							block[i][3-j] = dxt1_code2Less( color0, color1 );
						break;
						
					case 3:
						if( colors[0] > colors[1] )
							block[i][3-j] = dxt1_code3Greater( color0, color1 );
						else
							block[i][3-j] = new short[] { 0, 0, 0 };
						break;
						
					default:
						System.err.println( "DXT1 ERROR" );
						break;
						
					}
				}
			}
			blocks.add( block );
		}
		System.out.println( "blocks: " + blocks.size() + " width: " + width + " height: " + height );
		
		return null;
	}
	
	
	
	public static class TGA {}
}
