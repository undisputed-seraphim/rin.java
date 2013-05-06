package rin.engine.resource.audio.acb;

import rin.engine.resource.Resource;
import rin.engine.util.binary.BinaryReader;

public class AcbDecoder extends BinaryReader {

	public static final int STORAGE_MASK = 0xF0;
	public static final int STORAGE_CONSTANT = 0x30;
	
	public static final int COLUMN_TYPE = 0x0F;
	public static final int COLUMN_TYPE_DATA = 0x0b;
	public static final int COLUMN_TYPE_STRING = 0x0a;
	// 0x09 double?
	public static final int COLUMN_TYPE_FLOAT = 0x08;
	// 0x07 signed 8byte?
	public static final int COLUMN_TYPE_8BYTE = 0x06;
	public static final int COLUMN_TYPE_4BYTE2 = 0x05;
	public static final int COLUMN_TYPE_4BYTE = 0x04;
	public static final int COLUMN_TYPE_2BYTE2 = 0x03;
	public static final int COLUMN_TYPE_2BYTE = 0x02;
	public static final int COLUMN_TYPE_1BYTE2 = 0x01;
	public static final int COLUMN_TYPE_1BYTE = 0x00;
	
	public AcbDecoder( Resource resource ) {
		load( resource );
		System.out.println( "ACB file, length " + length() );
		
		int start = 0;
		int end = 0;
		while( position() < length() ) {
			if( readChar() == 'H' )
				if( readChar() == 'C' )
					if( readChar() == 'A' ) {
						rewind( 3 );
						start = position();
						while( position() < length() ) {
							if( readChar() == '@' )
								if( readChar() == 'U' )
									if( readChar() == 'T' )
										if( readChar() == 'F' ) {
											rewind( 4 );
											end = position();
											break;
										}
						}
						if( end == 0 ) end = length();
						break;
					}
		}
		
		if( start != 0 && end != 0 ) {
			Resource aud = resource.getDirectory().createResource( resource.getBaseName() + ".bin", true );
			position( start );
			aud.writeBytes( readInt8( end - start ) );
			System.out.println( "attempted to write .bin file!" );
			resource.delete();
		} else System.out.println( "hca file not found within acb." );
		/*readChar( 4 );
		int size = readInt32();
		int row_offset = readInt32();
		int table_offset = readInt32();
		int data_offset = readInt32();
		int table_name_string = readInt32();
		int columns = readInt16();
		int row_width = readInt16();
		int rows = readInt32();
		
		int string_table_size = data_offset - table_offset;
		System.out.println( "data offset: " + data_offset + " " + table_offset + " " + string_table_size );
		System.out.println( position() );
		
		for( int i = 0; i < columns; i++ ) {
			int type = readInt8();
			readInt32();
			switch( type & COLUMN_TYPE ) {
			
			case COLUMN_TYPE_8BYTE:
			case COLUMN_TYPE_DATA:
				System.out.println( "8byte / data" );
				printInt32( 2 );
				break;
				
			case COLUMN_TYPE_STRING:
			case COLUMN_TYPE_FLOAT:
			case COLUMN_TYPE_4BYTE2:
			case COLUMN_TYPE_4BYTE:
				System.out.println( "string / float / 4byte2 / 4byte" );
				printInt32();
				break;
				
			case COLUMN_TYPE_2BYTE2:
			case COLUMN_TYPE_2BYTE:
				System.out.println( "2byte2 / 2byte" );
				printInt16();
				break;
				
			case COLUMN_TYPE_1BYTE2:
			case COLUMN_TYPE_1BYTE:
				System.out.println( "1byte2 / 1byte" );
				printInt8();
				break;
				
			default:
				System.out.println( "unknown column type " + (type & COLUMN_TYPE) );
				break;
			}
		}
		
		position( table_offset + 8 );
		printChar( string_table_size );*/
	}
}
