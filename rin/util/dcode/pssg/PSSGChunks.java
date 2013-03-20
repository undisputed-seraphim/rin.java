package rin.util.dcode.pssg;

import java.util.HashMap;

import rin.util.bio.BIOBuffer;
import rin.util.bio.BIOChunks.Chunk;
import static rin.util.bio.BIOTypes.*;
import static rin.util.dcode.pssg.PSSGTypes.*;
import static rin.util.dcode.pssg.PSSGFile.*;

public class PSSGChunks {	
	public static final Chunk PSSGHEADER = new Chunk( "PSSGHEADER" ) {
		@Override public void define( Chunk c ) {
			String magic = c.readString( 4 );
			
			if( !magic.toUpperCase().equals( "PSSG" ) )
				System.out.println( "HUGE ERROR TO BE REPLACED WITH PSSGIncorrectHeaderFormatException" );
			
			PSSG.size = c.readUInt32();
			PSSG.propertyTypes = c.readUInt32();
			PSSG.chunkTypes = c.readUInt32();
		}
	};
	
	public static final Chunk INFOLIST = new Chunk( "INFOLIST" ) {
		@Override public void define( Chunk c ) {
			
			PSSGChunkInfo cinfo = new PSSGChunkInfo();
			cinfo.index = c.readUInt32();
			cinfo.name = c.read( PSSGSTRING );
			cinfo.props = c.readUInt32();
			cinfo.properties = new PSSGPropertyInfo[ (int)cinfo.props ];
			
			for( int i = 0; i < cinfo.props; i++ ) {
				PSSGPropertyInfo pinfo = new PSSGPropertyInfo();
				pinfo.index = c.readUInt32();
				pinfo.name = c.read( PSSGSTRING );
				
				cinfo.properties[i] = pinfo;
				PSSG.propInfoMap.put( (int)pinfo.index, pinfo );
			}
			
			PSSG.chunkInfoMap.put( (int)cinfo.index, cinfo );
		}
	};
	
	public static final Chunk PSSGDATABASE = new Chunk( "PSSGDATABASE" ) {
		@Override public void define( Chunk c ) {
			long index = c.readUInt32();
			if( PSSG.chunkInfoMap.containsKey( (int)index ) )
				if( !PSSG.chunkInfoMap.get( (int)index ).name.toUpperCase().equals( "PSSGDATABASE" ) )
					System.out.println( "MASSIVE ERROR TO BE REPLACED WITH EXCEPTION" );
			//TODO: instead of exceptioning, allow searching for the pssgdatabase node, then exception if it does not exist
			
			c.rewind( 4 );
			c.getParent().addChunk( PSSGCHUNK );
		}
	};
	
	public static final Chunk PSSGCHUNK = new Chunk( "PSSGCHUNK" ) {
		private <T> PSSGProperty<T> create( Type<T> type, long index, long size ) {
			PSSGProperty<T> res = new PSSGProperty<T>();
			res.index = index;
			res.size = size;
			res.type = type;
			res.amount = 1;
			res.data = type.getData( this.getBuffer().actual(), 1 );
			return res;
		}
		
		public PSSGChunk create() {
			return null;
		}
		
		@Override public void define( Chunk c ) {
			PSSGChunk chunk = new PSSGChunk();
			if( PSSG.root == null )
				PSSG.root = chunk;
			
			chunk.index = c.readUInt32();
			PSSGChunkInfo cinfo = PSSG.chunkInfoMap.get( (int)chunk.index );
			chunk.name = cinfo.name;
			chunk.chunksize = c.readUInt32();
			chunk.propsize = c.readUInt32();
			
			long chunkStop = c.position() + chunk.chunksize;
			long propStop = PSSGFile.instance.getBuffer().position() + chunk.propsize;
			
			while( c.position() < propStop ) {
				long index = c.readUInt32();
				long size = c.readUInt32();
				
				PSSGPropertyInfo pinfo = PSSG.propInfoMap.get( (int)index );
				if( pinfo != null ) {
					if( propertyMap.containsKey( pinfo.name ) ) {
						chunk.properties.add( this.create( propertyMap.get( pinfo.name ), index, size ) );
					} else {
						c.advance( (int)size );
					}
				}
			}
			
			/*if( DataOnlyChunks.find( chunk.name ) ) {
				chunk.hasData = true;
				chunk.data = c.readInt8s( chunkStop - c.position() );
			} else {
				while( PSSGFile.instance.getBuffer().position() < chunkStop ) {
					chunk.children.add( PSSGNode.create() );
				}
			}
			
			return node;*/
		}
	};
}
