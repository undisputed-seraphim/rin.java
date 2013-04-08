package rin.util.dcode.gmo;

import rin.util.bio.BIOBuffer;
import rin.util.bio.BinaryDecoder;
import static rin.util.bio.BinaryTypes.*;

public class GMODecoder extends BinaryDecoder {
	
	public final String FILE_START			= "0x0002";
	public final String SCALE_AND_BIAS		= "0x8015";
		public final int SCALE_AND_BIAS_V	= 384;
		public final int SCALE_AND_BIAS_UV	= 3;
	public final String BONE_INFO			= "0x0004";
	
	public GMODecoder( String file ) { super( file ); }
	
	public boolean ensureGMO() {
		return (	readHex8().equals( "0x4f" )
				&&	readHex8().equals( "0x4d" )
				&&	readHex8().equals( "0x47" )
				&&	readHex8().equals( "0x2e" )
				&&	readHex8().equals( "0x30" )
				&&	readHex8().equals( "0x30" )
				&&	readHex8().equals( "0x2e" )
				&&	readHex8().equals( "0x31" )
				&&	readHex8().equals( "0x50" )
				&&	readHex8().equals( "0x53" )
				&&	readHex8().equals( "0x50" )
				&&	readHex8().equals( "0x00" )
				&&	readHex8().equals( "0x00" )
				&&	readHex8().equals( "0x00" )
				&&	readHex8().equals( "0x00" )
				&&	readHex8().equals( "0x00" ) );
	}
	
	@Override
	public void read() {
		/* gmo files are little endian */
		setLittleEndian();
		
		/* check file header for GMO magic (consuming first 16 bytes) */
		if( !this.ensureGMO() ) {
			System.out.println( "File is not a GMO file." );
			return;
		}
		
		/* first specialized chunk is the File Start chunk... */
		if( readHex16().equals( FILE_START ) ) {
			short fsHeaderSize = readInt16();
			int fsSize = readInt32();
			
			advance( fsHeaderSize - 8 );
			
			String name = readHex16();
			short headerSize = readInt16();
			int chunkSize = readInt32();

			System.out.println( name + " " + headerSize + " " + chunkSize );
			advance( headerSize - 8 );
			
			for( int i = 0; i < 30; i++ ) {
				name = readHex16();
				headerSize = readInt16();
				chunkSize = readInt32();
				int end = position() - 8 + chunkSize;
				
				if( name.equals( SCALE_AND_BIAS ) ) {
					int type = readInt32();
					if( type == SCALE_AND_BIAS_V ) {
						System.out.println( readFloat32() + " " + readFloat32() + " " + readFloat32() + " " + readFloat32() + " " + readFloat32() + " " + readFloat32() );
					} else if( type == SCALE_AND_BIAS_UV ) {
						System.out.println( readFloat32() + " " + readFloat32() + " " + readFloat32() + " " + readFloat32() );
					} else {
						System.out.println( "Unknown SCALE_AND_BIAS type: " + type );
					}
				} else if( name.equals( BONE_INFO ) ) {
					System.out.println( readHex16() );
				} else {
					System.out.println( "Unknown chunk type: " + name + " [" + chunkSize +"]" );
				}
				
				position( end );
				System.out.println( "ended at: " + position() );
			}
		}
		
	}
}
