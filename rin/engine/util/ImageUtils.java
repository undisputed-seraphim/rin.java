package rin.engine.util;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;

import rin.util.bio.BinaryBuffer;

public class ImageUtils {

	private static class ImagePanel extends JFrame {
		
		private short[] data;
		private int width;
		private int height;
		
		public ImagePanel( ArrayList<short[][][]> data, int width, int height ) {
			//this.data = data;
			this.height = height;
			this.width = width;
			
			short[] real = new short[width*height*3];
			int w4 = width / 4;
			int row = 0;
			for( int i = 0; i < data.size(); i++ ) {
				short[][][] cur = data.get( i );
				
				if( i % w4 == 0 && i != 0 )
					row++;
				
				for( int j = 0; j < 4; j++ ) {
					for( int k = 0; k < 4; k++ ) {
						real[(i*4)+(j*4)+(k)] = cur[j][k][0];
						real[(i*4)+(j*4)+(k)+1] = cur[j][k][1];
						real[(i*4)+(j*4)+(k)+2] = cur[j][k][2];
					}
				}
			}
			this.data = real;
			this.setSize( width, height );
			this.setVisible( true );
			this.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		}
		
		@Override
		public void paint( Graphics g ) {
			int count = 0;
			for( int i = 0; i < width; i++ ) {
				for( int j = 0; j < height; j++ ) {
					g.setColor( new Color( data[count++], data[count++], data[count++] ) );
					g.drawRect( i, j, 1, 1 );
				}
			}
			/*for( int i = 0; i < height / 4; i++ ) {
				int subcount = 0;
				for( int j = 0; j < 4; j++ ) {
					for( int k = 0; k < width / 4; k++ ) {
						short[][][] cur = data.get( i + count + k );
						short[] col = cur[j][k%4];
						g.setColor( new Color( col[0], col[1], col[2] ) );
						g.drawRect( k * 4, i * 4 + j, 1, 1 );
						
						if( j == 3 )
							subcount++;
					}
				}
				count += subcount;
			}*/
			/*for( int i = 0; i < width / 4; i++ ) {
				for( int j = 0; j < height / 4; j++ ) {
					short[][][] cur = data.get( count++ );
					for( int b1 = 0; b1 < 4; b1++ ) {
						for( int b2 = 0; b2 < 4; b2++ ) {
							short[] col = cur[b1][b2];
							g.setColor( new Color( col[0], col[1], col[2] ) );
							g.drawRect( i * 4, j * 4, 1, 1 );
						}
					}
					//short[] col = data.get( count++ )[0][0];
				}
			}*/
		}
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
	
	public static short[] decode( PixelFormat format, int width, int height, byte[] data ) {
		if( format == null )
			return null;
		
		ArrayList<short[][][]> blocks = new ArrayList<short[][][]>();
		BinaryBuffer buf = new BinaryBuffer( data );
		//short[] img = new short[width*height*3];
		int stop = height * width * 3 / 6;
		buf.setLittleEndian();
		
		int count = 0;
		while( buf.position() < stop ) {
			int[] colors = buf.readUInt16( 2 );
			
			short[] color0 = dxt1_RGB565ToRGB888( colors[0] );
			short[] color1 = dxt1_RGB565ToRGB888( colors[1] );
			
			//byte[][] codes = buf.readBits( 4 );
			
			byte[][] codes = new byte[][] {
					dxt1_getCodes( buf.readBits() ),
					dxt1_getCodes( buf.readBits() ),
					dxt1_getCodes( buf.readBits() ),
					dxt1_getCodes( buf.readBits() )
			};
			
			int subcount = 0;
			short[][][] block = new short[4][4][3];
			for( int i = 3; i >= 0; i-- ) {
				for( int j = 0; j < codes[i].length; j++ ) {
					switch( codes[i][j] ) {
					
					case 0:
						//img[(count*16) + (subcount*j)] = color0[0];
						//img[(count*16) + (subcount*j)+1] = color0[1];
						//img[(count*16) + (subcount*j)+2] = color0[2];
						block[i][3-j][0] = color0[0];
						block[i][3-j][1] = color0[1];
						block[i][3-j][2] = color0[2];
						break;
						
					case 1:
						//img[(count*16) + (subcount*j)] = color1[0];
						//img[(count*16) + (subcount*j)+1] = color1[1];
						//img[(count*16) + (subcount*j)+2] = color1[2];
						block[i][3-j][0] = color1[0];
						block[i][3-j][1] = color1[1];
						block[i][3-j][2] = color1[2];
						break;
						
					case 2:
						break;
						
					case 3:
						break;
						//524288+131072+32768
						//688128
					}
				}
				subcount++;
			}
			blocks.add( block );
			count++;
			//System.out.println( "codes 0: " + codes0[0] + " " + codes0[1] + " " + codes0[2] + " " + codes0[3] );
		}
		new ImagePanel( blocks, width, height );
		System.out.println( "blocks: " + blocks.size() + " width: " + width + " height: " + height );
		
		return null;
	}
	
}
