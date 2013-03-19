package rin.util.dcode.ttf;

import static rin.util.dcode.ttf.TTFTypes.*;
import rin.util.bio.BIOChunks.Chunk;;

public class TTFChunks {
	/*public static final Chunk HEADER = new Chunk( "header" ) {
		@Override public void define( Chunk c ) {
			c.addPart( FIXED, 1, "version" );
			c.addPart( USHORT, 1, "numTables" );
			c.addPart( USHORT, 1, "searchRange" );
			c.addPart( USHORT, 1, "entrySelector" );
			c.addPart( USHORT, 1, "rangeShift" );
		}
	};
	
	public static final Chunk TABLE_RECORD = new Chunk() {
		@Override public void define( Chunk c ) {
			c.addPart( TAG, 1, c.id + "_tag" );
			c.addPart( ULONG, 1, c.id + "_checkSum" );
			c.addPart( ULONG, 1, c.id + "_offset" );
			c.addPart( ULONG, 1, c.id + "_length" );
		}
	};
	
	public static final Chunk HEAD = new Chunk( "head" ) {
		@Override public void define( Chunk c ) {
			this.getParent().getBuffer().advance( 4+4+4+4+2 );
			c.addPart( USHORT, 1, "head_unitsPerEm", true );
			c.addPart( LONGDATETIME, 1, "head_created", true );
			c.addPart( LONGDATETIME, 1, "head_modified", true );
			this.getParent().getBuffer().advance( 2+2+2+2+2+2+2 );
			c.addPart( SHORT, 1, "head_indexToLocFormat", true );
			c.addPart( SHORT, 1, "head_glyphDataFormat", true );
		}
	};
	
	public static final Chunk MAXP = new Chunk( "maxp" ) {
		@Override public void define( Chunk c ) {
			c.addPart( FIXED, 1, "maxp_version", true );
			c.addPart( USHORT, 1, "maxp_numGlyphs", true );
		}
	};
	
	public static final Chunk LOCA = new Chunk( "loca" ) {
		@Override public void define( Chunk c ) {
			int longorshort = c.getParent().get( SHORT, "head_indexToLocFormat" );
			int numGlyphs = c.getParent().get( USHORT, "maxp_numGlyphs" );
			switch( longorshort ) {
			
			//short
			case 0:
				c.addPart( USHORT, numGlyphs + 1, "loca_offsets", true );
				break;
				
			//long
			case 1:
				c.addPart( ULONG, numGlyphs + 1, "loca_offsets", true );
				break;
				
			}
		}
	};
	
	public static final Chunk DSIG = new Chunk( "dsig" ) {
		@Override public void define( Chunk c ) {
			int start = this.getParent().getBuffer().position();
			
			c.addPart( ULONG, 1, "dsig_version", true );
			c.addPart( USHORT, 1, "dsig_numSigs", true );
			c.addPart( USHORT, 1, "dsig_flags", true );
			
			for( int i = 0; i < c.getParent().get( USHORT, "dsig_numSigs" ); i++ ) {
				c.addPart( ULONG, 1, "dsig_"+i+"_format", true );
				c.addPart( ULONG, 1, "dsig_"+i+"_length", true );
				c.addPart( ULONG, 1, "dsig_"+i+"_offset", true );
				
				int back = this.getParent().getBuffer().position();
				this.getParent().getBuffer().position( (int)(start + this.getParent().get( ULONG, "dsig_"+i+"_offset" ) ) );
				
				c.addPart( USHORT, 1, "dsig_"+i+"_reserve1", true );
				c.addPart( USHORT, 1, "dsig_"+i+"_reserve2", true );
				c.addPart( ULONG, 1, "dsig_"+i+"_siglength", true );
				c.addPart( UBYTE, (int)(long)this.getParent().get( ULONG, "dsig_"+i+"_siglength" ), "dsig_"+i+"_sig", true );
				
				this.getParent().getBuffer().position( back );
			}
		}
	};
	
	public static final Chunk CMAP = new Chunk( "cmap" ) {
		@Override public void define( Chunk c ) {
			int start = this.getParent().getBuffer().position();
			
			c.addPart( USHORT, 1, "cmap_version", true );
			c.addPart( USHORT, 1, "cmap_numTables", true );
			
			for( int i = 0; i < this.getParent().get( USHORT, "cmap_numTables" ); i++ ) {
				c.addPart( USHORT, 1, "cmap_"+i+"_platform", true );
				c.addPart( USHORT, 1, "cmap_"+i+"_encoding", true );
				c.addPart( ULONG, 1, "cmap_"+i+"_offset", true );
				
				int back = this.getParent().getBuffer().position();
				this.getParent().getBuffer().position( (int)(start + this.getParent().get( ULONG, "cmap_"+i+"_offset" )) );
				
				c.addPart( USHORT, 1, "cmap_"+i+"_format", true );
				c.addPart( USHORT, 1, "cmap_"+i+"_length", true );
				c.addPart( USHORT, 1, "cmap_"+i+"_version", true );
				
				switch( this.getParent().get( USHORT, "cmap_"+i+"_format" ) ) {
				
				// apple standard
				case 0:
					c.addPart( UBYTE, 256, "cmap_"+i+"_glyphId", true );
					break;
					
				// microsoft standard
				case 4:
					c.addPart( USHORT, 1, "cmap_"+i+"_segCountX2", true );
					c.addPart( USHORT, 1, "cmap_"+i+"_searchRange", true );
					c.addPart( USHORT, 1, "cmap_"+i+"_entrySelector", true );
					c.addPart( USHORT, 1, "cmap_"+i+"_rangeShift", true );
					
					int segCount = this.get( USHORT, "cmap_"+i+"_segCountX2" ) / 2;
					c.addPart( USHORT, segCount, "cmap_"+i+"_endCount", true );
					c.addPart( USHORT, 1, "cmap_"+i+"_reserved", true );
					c.addPart( USHORT, segCount, "cmap_"+i+"_startCount", true );
					c.addPart( USHORT, segCount, "cmap_"+i+"_idDelta", true );
					c.addPart( USHORT, segCount, "cmap_"+i+"_idRangeOffsets", true );
					break;
				}
				
				this.getParent().getBuffer().position( back );
			}
		}
	};
	
	public static final Chunk GLYF = new Chunk( "glyf" ) {
		private int getMax( Integer[] endpoints ) {
			int res = endpoints[0];
			for( Integer i : endpoints ) {
				if( i > res )
					res = i;
			}
			return res;
		}
		
		@Override public void define( Chunk c ) {
			c.addPart( SHORT, 1, "glyf_" + c.id + "_contours", true );
			c.addPart( FWORD, 1, "glyf_" + c.id + "_xMin", true );
			c.addPart( FWORD, 1, "glyf_" + c.id + "_yMin", true );
			c.addPart( FWORD, 1, "glyf_" + c.id + "_xMax", true );
			c.addPart( FWORD, 1, "glyf_" + c.id + "_yMax", true );
			
			short contours = c.get( SHORT, "glyf_" + c.id + "_contours" );
			if( contours > 0 ) {
				c.addPart( USHORT, contours, "glyf_"+c.id+"_endPoints", true );
				c.addPart( USHORT, 1, "glyf_"+c.id+"_instructionLength", true );
				int ins = c.get( USHORT, "glyf_"+c.id+"_instructionLength" );
				c.addPart( UBYTE, ins, "glyf_"+c.id+"_instructions", true );
				
				int max = this.getMax( c.getArray( USHORT, "glyf_"+c.id+"_endPoints" ) );
				
				for( int i = 1; i < max; i++ ) {
					c.addPart( FLAG, 1, "glyf_"+c.id+"_flags"+i, true );
				}
				
				for( int i = 1; i < max; i++ ) {
					String flags = c.get( FLAG, "glyf_"+c.id+"_flags"+i );
					boolean xShort = flags.charAt( 6 ) == '1' ? true : false;
					boolean yShort = flags.charAt( 5 ) == '1' ? true : false;
					if( xShort ) {
						c.addPart( UBYTE, 1, "glyf_"+c.id+"_x"+i, true );
					} else {
						c.addPart( USHORT, 1, "glyf_"+c.id+"_x"+i, true );
					}
					
				}
			}
		}
	};*/
}
