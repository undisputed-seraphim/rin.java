package rin.engine.util;

import java.nio.ByteBuffer;
import java.util.TreeMap;

import rin.util.bio.BinaryReader;

public class PSSGExtractor extends BinaryReader {

	private final int C_PROPERTY_MAP = 33;
	private final int C_46 = 46;				//something to do with textures
	private final int C_121 = 121;				//something to do with complex properties of light
	private final int C_97 = 97;
	private final int C_98 = 98;
	private final int C_3 = 3;					//skeleton / node graph
	private final int C_50 = 50;
	private final int C_11 = 11;
	
	private final int T_12 = 12;			// ARRAYS! ... i think. next int32 after this is array size
	private final int T_45 = 45;			//something to do with textures
	private final int T_89 = 89;
	private final int T_70 = 70;
	private final int T_91 = 91;
	private final int T_92 = 92;
	private final int T_93 = 93;
	private final int T_94 = 94;
	private final int T_95 = 95;
	private final int T_120 = 120;
	private final int T_20 = 20;
	private final int T_5 = 5;
	
	private ByteBuffer data;
	private TreeMap<Integer, Integer> chunkMap = new TreeMap<Integer, Integer>();
	private String[] propMap;
	private String[] texMap;
	
	@Override
	public ByteBuffer getBuffer() { return this.data; }
	
	public PSSGExtractor( String file ) {
		this.data = ByteBuffer.wrap( FileUtils.asByteArray( file ) );
		this.decode();
	}
	
	public static void main( String[] args ) {
		new PSSGExtractor( "/Users/Musashi/Desktop/Horo/rin.java/packs/rin/003.ism2" );
	}
	
	private void header() {
		if( readChar() == 'I' && readChar() == 'S' && readChar() == 'M' && readChar() == '2' ) {
			System.out.println( "file length: " + length() );
			
			//TODO: unsure
			System.out.println( "[after 'ISM2'] UNSURE 3 int32: " + ArrayUtils.asString( readInt32( 3 ) ) );
			
			if( readInt32() != length() ) {
				System.err.println( "File length in header does not match actual file length." );
				System.exit( 0 );
			}
			
			// number of chunk indices
			int chunkNum = readInt32();
			
			//TODO: unsure
			System.out.println( "[after chunkNum] UNSURE 2 int32: " + ArrayUtils.asString( readInt32( 2 ) ) );
			
			// chunk map indices
			for( int i = 0; i < chunkNum; i++ ) {
				int id = readInt32();
				int offset = readInt32();
				chunkMap.put( offset, id );
			}
			
			System.out.println( "chunkmap: " + ArrayUtils.asString( chunkMap ) );
			
			System.out.println( position() );
		} else {
			System.err.println( "Not an IMS2 file, or invalid header." );
			System.exit( 0 );
		}
	}
	
	private void readChunks() {
		for( Integer i : chunkMap.keySet() ) {
			position( i );
			readChunk( chunkMap.get( i ) );
		}
	}
	
	private void readChunk( int chunkType ) {
		int type = readInt32();
		if( type == chunkType ) {
			System.out.println( "Starting chunk " + chunkType + " at " + (position() - 4) + "..." );
			switch( type ) {
			
			case C_PROPERTY_MAP:
				type = readInt32();
				if( type == 12 ) {
					int propCount = readInt32();
					int[] propIndex = new int[propCount];
					for( int i = 0; i < propCount; i++ )
						propIndex[i] = readInt32();
					
					propMap = new String[propCount];
					for( int i = 0; i < propCount; i++ ) {
						position( propIndex[i] );
						propMap[i] = "";
						while( !readHex8().equals( "0x00" ) ) {
							rewind( 1 );
							propMap[i] += readChar();
						}
						rewind( 1 );
					}
					
					//System.out.println( "propMap: " + ArrayUtils.asString( propMap ) );
				}
				break;
				
			case C_46:
				type = readInt32();
				if( type == 12 ) {
					int count = readInt32();
					int[] tmp = new int[count];
					for( int i = 0; i < count; i++ )
						tmp[i] = readInt32();
					
					texMap = new String[count];
					for( int i = 0; i < tmp.length; i++ ) {
						position( tmp[i] );
						if( readInt32() == T_45 ) {
							readInt32();							// hair_c
							readInt32();							// hair_c
							readInt32();							// file:F:/ps3_/etc/hair_c.dds
							texMap[i] = propMap[ readInt32() ];		// hair_c.dds
							
							//TODO: unsure
							//System.out.println( "[in texMap] UNSURE 3 int32 " + ArrayUtils.asString( readInt32( 3 ) ) );
							advance( 4 * 3 );
						}
					}
					
					//System.out.println( "texMap: " + ArrayUtils.asString( texMap ) );
				}
				break;
				
			case C_121:
				/*printInt32( 6 );
				System.out.println( propMap[ readInt32() ] );
				System.out.println( propMap[ readInt32() ] );
				System.out.println( propMap[ readInt32() ] );
				printInt32( 8 );
				System.out.println( position() );
				System.out.println( propMap[ readInt32() ] );
				System.out.println( propMap[ readInt32() ] );
				printInt32( 2 );
				printFloat32( 3 );
				System.out.println( propMap[ readInt32() ] );
				System.out.println( propMap[ readInt32() ] );
				printInt32( 3 );
				System.out.println( propMap[ readInt32() ] );
				System.out.println( propMap[ readInt32() ] );
				printInt32( 3 );
				System.out.println( propMap[ readInt32() ] );
				System.out.println( propMap[ readInt32() ] );
				printInt32( 3 );
				System.out.println( propMap[ readInt32() ] );
				System.out.println( propMap[ readInt32() ] );
				printInt32( 3 );
				System.out.println( propMap[ readInt32() ] );
				System.out.println( propMap[ readInt32() ] );
				printInt32( 3 );
				System.out.println( propMap[ readInt32() ] );
				System.out.println( propMap[ readInt32() ] );
				printInt32( 3 );
				System.out.println( propMap[ readInt32() ] );
				System.out.println( propMap[ readInt32() ] );
				printInt32( 3 );*/
				break;
				
			case C_97:
				/*printInt32( 5 );
				System.out.println( propMap[ readInt32() ] );
				printInt32( 20 );*/
				break;
				
			case C_98:
				/*printInt32( 5 );
				System.out.println( propMap[ readInt32() ] );
				printInt32( 20 );*/
				break;
				
			//case C_3:
			case 0:
				//TODO: unsure what this 20 means, its on others as well
				printInt32( 1 );
				
				int num = readInt32();
				int[] tmp = new int[num];
				System.out.println( propMap[ readInt32() ] );
				System.out.println( propMap[ readInt32() ] );
				
				for( int i = 0; i < num; i++ )
					tmp[i] = readInt32();

				for( int i = 0; i < num; i++ ) {
					position( tmp[i] );
					System.out.println( "start: " + position() );
					
					//TODO: unsure; find out what 4/5 means
					type = readInt32();  // type 4/5, not sure what it means yet
					
					if( readInt32() != 64 )
						System.out.println( "[WARNING] NOT A JOINT!" );
					
					int count = readInt32();
					System.out.println( propMap[ readInt32() ] );
					System.out.println( propMap[ readInt32() ] );
					
					printInt32(); // property index for the bone representing this node, if one exists
					
					//TODO: unsure; this is always 0, no clue what its for
					readInt32();
					
					printInt32(); // file offset location of this joints parent
					
					//TODO: unsure
					System.out.println( "UNSURE 8 int32: " + ArrayUtils.asString( readInt32( 8 ) ) );
					
					int[] tmp2 = new int[count];
					for( int j = 0; j < count; j++ )
						tmp2[j] = readInt32();
					
					for( int j = 0; j < tmp2.length; j++ ) {
						position( tmp2[j] );
						int ttype = readInt32();
						switch( ttype ) {
						
						case T_91:
							advance( 4 );
							int ttnum = readInt32();
							int[] ttmp3 = new int[ttnum];
							for( int k = 0; k < ttnum; k++ )
								ttmp3[k] = readInt32();
							
							for( int k = 0; k < ttmp3.length; k++ ) {
								position( ttmp3[k] );
								System.out.println( "T_91: " + ttmp3[k] );
								System.out.println( propMap[ readInt32() ] );
								printInt32( 4 );
							}
							break;
							
						case T_92:
							System.out.println( "TTYPE " + ttype );
							printInt32( 10 );
							break;
							
						case T_93:
						case T_94:
						case T_95:
							advance( 4 );
							int tnum = readInt32();
							int[] tmp3 = new int[tnum];
							for( int k = 0; k < tnum; k++ )
								tmp3[k] = readInt32();
							
							for( int k = 0; k < tmp3.length; k++ ) {
								position( tmp3[k] );
								int tttype = readInt32();
								switch( tttype ) {
								
								case T_93:
								case T_94:
								case T_95:
								case T_20:
									advance( 4 );
									System.out.println( tttype + ": " + ArrayUtils.asString( readFloat32( 3 ) ) );
									break;
									
								case T_5:
									System.out.println( "TTTYPE FIVE: " + tmp3[k]);
									printInt32( 1 );
									System.out.println( position() );
									break;
									
								default:
									System.out.println( "[UNKNOWN] TTTYPE " + tttype );
									break;
								}
							}
							
							System.out.println( ArrayUtils.asString( tmp3 ) );
							break;
							
						default:
							System.out.println( "[UNKNOWN] ttype " + ttype );
							break;
						}
					}
					
					System.out.println( "end: " + position() );
				}
				
				break;
			
			case C_50:
				/*printInt32( 16 );
				printFloat32( 16 );
				printInt32( 16 );*/
				break;
				
			case C_11:
				 //TODO: number of meshes, perhaps?
				printInt32( 3 );
				
				//TODO: unsure
				System.out.println( "[per mesh] UNKNOWN 2 int32 " + ArrayUtils.asString( readInt32( 2 ) ) );
				
				int tnum = readInt32();
				int[] tmp2 = new int[tnum];
				
				// name of mesh
				System.out.println( propMap[ readInt32() ] );
				System.out.println( propMap[ readInt32() ] );
				
				//TODO: unsure
				System.out.println( "[per mesh] UNKNOWN 3 int32 " + ArrayUtils.asString( readInt32( 3 ) ) );
				for( int i = 0; i < tnum; i++ )
					tmp2[i] = readInt32();
				
				for( int i = 0; i < tnum; i++ ) {
					position( tmp2[i] );
					int tttype = readInt32();
					System.out.println( "TTTYPE " + tttype + " " + tmp2[i] );
					switch( tttype ) {
					
					case T_89:
						//TODO: unsure
						System.out.println( "[per T_89] UNKNOWN 1 int32 " + ArrayUtils.asString( readInt32( 1 ) ) );
						
						int nnum = readInt32();
						int[] tmp4 = new int[nnum];
						
						//TODO: unsure
						System.out.println( "[per T_89] UNKNOWN 1 int32 " + ArrayUtils.asString( readInt32( 1 ) ) );
						
						int important = readInt32();
						
						//TODO: unsure
						System.out.println( "[per T_89] UNKNOWN 2 int32 " + ArrayUtils.asString( readInt32( 2 ) ) );
						
						for( int j = 0; j < nnum; j++ )
							tmp4[j] = readInt32();
						
						int impo = 0;
						for( int j = 0; j < nnum; j++ ) {
							position( tmp4[j] );
							System.out.println( "HERE " + " " + tmp4[j] );
							printInt32( 5 );
							impo = readInt32();
						}
						System.out.println( "POS " + position() + " " + impo );
						position( impo );
						printFloat32( important );
						break;
						
					default:
						break;
						
					}
					
					System.out.println( "finished at " + position() );
				}
				break;
				
			default:
				break;
				
			}
			
			System.out.println( "\tFinished at " + position() );
		} else {
			System.err.println( "Unexpected chunk type " + type  );
		}
	}
	
	private void decode() {
		header();
		
		if( chunkMap.size() > 0 )
			readChunks();
	}
	
}
