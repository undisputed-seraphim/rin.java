package rin.sample;

import rin.engine.resource.Resource;
import rin.engine.resource.ResourceManager;
import rin.engine.resource.image.ImageContainer;
import rin.engine.resource.image.PixelFormat;
import rin.engine.util.binary.BinaryReader;

public class TestDecoder extends BinaryReader {

	public static void main( String[] args ) {
		new TestDecoder( ResourceManager.getPackResource( "rin", "test.bmp" ) );
	}
	
	public TestDecoder( Resource resource ) {
		load( resource );
		setLittleEndian();
		System.out.println( "testing bmp " + length() );

		int size = readUInt16();
		System.out.println( size + " " + position() );
		readUInt16( 2 );
		readInt8();
		printChar( 2 );
		short[] colors = new short[100*100*3];
		for( int i = 0; i < 100; i++ ) {
			for( int j = 0; j < 100; j++ ) {
				colors[i*3+j*3] = readUInt8();
				colors[i*3+j*3+1] = readUInt8();
				colors[i*3+j*3+2] = readUInt8();
			}
		}
		ImageContainer ic = new ImageContainer( 100, 100, colors, PixelFormat.RGB );
		ic.test();
		
	}
}
