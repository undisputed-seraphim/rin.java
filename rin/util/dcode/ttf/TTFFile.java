package rin.util.dcode.ttf;

import static rin.util.dcode.ttf.TTFTypes.FLAG;
import static rin.util.dcode.ttf.TTFTypes.FWORD;
import static rin.util.dcode.ttf.TTFTypes.SHORT;
import static rin.util.dcode.ttf.TTFTypes.UBYTE;
import static rin.util.dcode.ttf.TTFTypes.USHORT;
import rin.util.bio.BIOChunks.Chunk;
import rin.util.bio.BIOFile;
import rin.util.bio.BIOParts.Part;

public class TTFFile extends BIOFile {
	public static class TableRecord {
		public String id;
		public long checksum;
		public long offset;
		public long length;
		
		public TableRecord( String id, long checksum, long offset, long length ) {
			this.id = id;
			this.checksum = checksum;
			this.offset = offset;
			this.length = length;
		}
	}
	
	public TTFFile( String file ) { super( file ); }
	
	private TableRecord[] records;
	public TableRecord getRecord( String id ) {
		for( TableRecord r : this.records )
			if( r.id.equals( id ) )
				return r;
		return null;
	}
	
	public void loadChunks( String ... ids ) {
		for( String id : ids ) {
			TableRecord tmp;
			if( (tmp = this.getRecord( id )) != null ) {
				Chunk tmpc = this.getChunk( TTFChunks.class, id );
				if( tmpc != null ) {
					this.getBuffer().position( (int)tmp.offset );
					this.addChunk( tmpc );
				}
			}
		}
	}
	
	public long[] mapCharacter( char c ) {
		int encodings = this.getUShort( "cmap_numTables" );
		long[] res = new long[] { 0, 0 };
		for( int i = 0; i < encodings; i++ ) {
			int format = this.getUShort( "cmap_"+i+"_format" );
			int platform = this.getUShort( "cmap_"+i+"_platform" );
			int encoding = this.getUShort( "cmap_"+i+"_encoding" );
			
			//for now, use microsoft platform 3, encoding 1, format 4
			if( platform == 3 && encoding == 1 && format == 4 ) {
				Integer[] endCount = this.getUShorts( "cmap_"+i+"_endCount" );
				Integer[] startCount = this.getUShorts( "cmap_"+i+"_startCount" );
				Integer[] idDelta = this.getUShorts( "cmap_"+i+"_idDelta" );
				Integer[] idRangeOffsets = this.getUShorts( "cmap_"+i+"_idRangeOffsets" );
				
				Long[] offsets = this.getUInts( "loca_offsets" );
				
				int code = (int)c;
				//System.out.println( endCount.length + " " + startCount.length + " " + idDelta.length + " " + idRangeOffsets.length );
				for( int j = 0; j < endCount.length; j++ ) {
					if( endCount[j] >= code ) {
						System.out.println( "first known: " + startCount[j] + " " + endCount[j] );
						if( startCount[j] <= code ) {
							if( idRangeOffsets[j] != 0 ) {
								//TODO: this needs to be fixed; it used memory address of idRangeOffsets in calculation!
								System.out.println( "CRAP, SHOULD NOT BE HERE YET" );
								int index = idRangeOffsets[j] / 2 + (code - startCount[j]) + idRangeOffsets[j];
								if( index != 0 )
									index = ((idDelta[j] + index) % 65536);
								System.out.println( index );
							} else {
								int index = ((idDelta[j] + code) % 65536);
								System.out.println( "index of " + c + " = " + index );
								res[0] = offsets[index];
								res[1] = offsets[index+1];
							}
						}
						break;
					}
				}
			}
		}
		return res;
	}
	
	private int getMax( Integer[] endpoints ) {
		int res = endpoints[0];
		for( Integer i : endpoints ) {
			if( i > res )
				res = i;
		}
		return res;
	}
	
	public void getGlyphData( char key ) {
		long[] map = this.mapCharacter( key );
		TableRecord glyf = this.getRecord( "glyf" );
		this.getBuffer().position( (int)(glyf.offset + map[0] ) );
		long end = (((int)(glyf.offset + map[0]))+ (map[1]-map[0]));
		//this.addChunk( TTFChunks.GLYF.copy( ""+key ) );
		
		Chunk c = new Chunk() {
			@Override public void define( Chunk c ) {}
		};
		c.setParent( this );
		c.addPart( SHORT, 1, "glyf_" + c.id + "_contours", true );
		c.addPart( FWORD, 1, "glyf_" + c.id + "_xMin", true );
		c.addPart( FWORD, 1, "glyf_" + c.id + "_yMin", true );
		c.addPart( FWORD, 1, "glyf_" + c.id + "_xMax", true );
		c.addPart( FWORD, 1, "glyf_" + c.id + "_yMax", true );
		
		short contours = c.getShort( "glyf_" + c.id + "_contours" );
		if( contours > 0 ) {
			c.addPart( USHORT, contours, "glyf_"+c.id+"_endPoints", true );
			c.addPart( USHORT, 1, "glyf_"+c.id+"_instructionLength", true );
			int ins = c.getUShort( "glyf_"+c.id+"_instructionLength" );
			c.addPart( UBYTE, ins, "glyf_"+c.id+"_instructions", true );
			
			int max = this.getMax( c.getUShorts( "glyf_"+c.id+"_endPoints" ) ) - 1;
			
			for( int i = 1; i < max; i++ ) {
				c.addPart( FLAG, 1, "glyf_"+c.id+"_flags"+i, true );
			}
			
			for( int i = 1; i < max; i++ ) {
				String flags = c.getString( "glyf_"+c.id+"_flags"+i );
				boolean xShort = flags.charAt( 6 ) == '1' ? true : false;
				if( xShort ) {
					c.addPart( UBYTE, 1, "glyf_"+c.id+"_x"+i, true );
				} else {
					c.addPart( USHORT, 1, "glyf_"+c.id+"_x"+i, true );
				}
				
			}
			
			for( int i = 1; i < max; i++ ) {
				String flags = c.getString( "glyf_"+c.id+"_flags"+i );
				boolean yShort = flags.charAt( 5 ) == '1' ? true : false;
				if( yShort ) {
					c.addPart( UBYTE, 1, "glyf_"+c.id+"_y"+i, true );
				} else {
					c.addPart( USHORT, 1, "glyf_"+c.id+"_y"+i, true );
				}
				
			}
		}
		System.out.println( this.getBuffer().position() + " " + end );
	}
	
	@Override public void read() {
		this.addChunk( TTFChunks.HEADER, true );
		
		this.records = new TableRecord[ this.getUShort( "numTables" ) ];
		
		for( int i = 0; i < this.getUShort( "numTables" ); i++ ) {
			this.addChunk( TTFChunks.TABLE_RECORD.copy( "table_" + i ), true );
			this.records[i] = new TableRecord(
					this.getString( "table_" + i + "_tag" ),
					this.getUInt( "table_" + i + "_checkSum" ),
					this.getUInt( "table_" + i + "_offset" ),
					this.getUInt( "table_" + i + "_length" )
			);
		}
		
		this.loadChunks( "head", "maxp", "cmap", "loca" );
		this.getGlyphData( 'A' );
		this.previewChunks();
	}
	
	@Override public void write() {}
}
