package rin.engine.util;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import rin.engine.resource.image.PixelFormat;

public class ImageUtils {

	private static class ImagePanel extends JFrame {
		private static final long serialVersionUID = 1L;
		
		public ImagePanel( int w, int h, PixelFormat pf, short[] d ) {
			d = convert( pf, PixelFormat.RGBA, d );
			
			BufferedImage bi = new BufferedImage( w, h, BufferedImage.TYPE_INT_ARGB );
			WritableRaster wr = (WritableRaster)bi.getData();
			
			int[] pixels = new int[d.length];
			for( int i = 0; i < d.length; i++ ) pixels[i] = d[i];
			
			wr.setPixels( 0, 0, w, h, pixels );
			bi.setData( wr );

			getContentPane().add( new JLabel( new ImageIcon( bi ) ) );
			setVisible( true );
			pack();
			setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		}
	}
	
	public static void test( int width, int height, PixelFormat pf, short[] rawData ) {
		new ImagePanel( width, height, pf, rawData );
	}
	
	public static short[] convert( PixelFormat src, PixelFormat dest, short[] data ) {
		//System.out.println( "converting pixels from " + src.toString() + " to " + dest.toString() );
		if( src == dest )
			return data;
		
		//TODO: ensure the length matches up with src format, throw exceptions, etc
		
		int srcStride = src.getStride();
		int destStride = dest.getStride();
		short[] res = new short[data.length / srcStride * destStride];
		
		for( int i = 0; i < data.length / srcStride; i++ ) {
			res[i*destStride+dest.r()] = data[i*srcStride+src.r()];
			if( dest.g() != -1 ) {
				if( src.g() != -1 ) {
					res[i*destStride+dest.g()] = data[i*srcStride+src.g()];
				} else res[i*destStride+dest.g()] = data[i*srcStride+src.r()];
			}
			
			if( dest.b() != -1 ) {
				if( src.b() != -1 ) {
					res[i*destStride+dest.b()] = data[i*srcStride+src.b()];
				} else res[i*destStride+dest.b()] = data[i*srcStride+src.r()];
			}
			
			if( dest.a() != -1 ) {
				if( src.a() != -1 ) {
					res[i*destStride+dest.a()] = data[i*srcStride+src.a()];
				} else res[i*destStride+dest.a()] = 255;
			}
		}
		return res;
	}

}
