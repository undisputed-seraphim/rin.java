package rin.util.dcode.ttf;

import rin.util.bio.BIOChunks.Chunk;
import rin.util.bio.BIOFile;

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
	
	public long mapCharacter( char c ) {
		int encodings = this.getUShort( "cmap_numTables" );
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
								System.out.println( "add the delta of " + idDelta[j] );
								int index = ((idDelta[j] + code) % 65536);
								return offsets[index];
							}
						}
						break;
					}
				}
			}
		}
		return 0;
	}
	
	public void getGlyphData( char key, long offset ) {
		TableRecord glyf = this.getRecord( "glyf" );
		this.getBuffer().position( (int)(glyf.offset + offset) );
		this.addChunk( TTFChunks.GLYF.copy( ""+key ) );
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
		long amap = this.mapCharacter( 'A' );
		this.getGlyphData( 'A', amap );
		this.previewChunks();
	}
	
	@Override public void write() {}
}
