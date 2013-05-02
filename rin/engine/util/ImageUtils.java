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
				
				short[] c = new short[] {0,0,0,255};
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
}
