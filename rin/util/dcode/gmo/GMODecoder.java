package rin.util.dcode.gmo;

import rin.util.bio.BIOBuffer;
import rin.util.bio.BinaryDecoder;
import static rin.util.bio.BinaryTypes.*;

public class GMODecoder extends BinaryDecoder {
	
	public final String FILE_START = "0x0002";
	
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
			int end = position() + headerSize - 8;
			while( position() < end ) {
				System.out.println( readHex16() );
			}
		}
		
	}
}
