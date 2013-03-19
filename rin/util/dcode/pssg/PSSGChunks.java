package rin.util.dcode.pssg;

import static rin.util.dcode.pssg.PSSGFile.*;

import java.util.HashMap;

import rin.util.bio.BIOBuffer;
import rin.util.bio.BIOChunks.Chunk;
import static rin.util.bio.BIOTypes.*;
import static rin.util.dcode.pssg.PSSGTypes.*;

public class PSSGChunks {	
	public static final Chunk PSSGHEADER = new Chunk( "PSSGHEADER" ) {
		@Override public void define( Chunk c ) {
			String magic = c.readString( 4 );
			System.out.println( magic );
			/*c.addPart( CHAR, 4, "header_pssg", true );
			c.addPart( UINT32, "header_chunksize", true );
			c.addPart( UINT32, "header_props", true );
			c.addPart( UINT32, "header_params", true );
			
			PSSG.header = new Header();
			Header head = PSSG.header;
			
			head.pssg = BIOBuffer.asString( c.getArray( CHAR, "header_pssg" ), true );
			head.chunksize = c.get( UINT32,	"header_chunksize" );
			head.props = c.get( UINT32, "header_props" );
			head.params = c.get( UINT32, "header_params" );*/
		}
	};
	
	public static final Chunk PARAM_LIST = new Chunk( "PARAM_LIST" ) {
		@Override public void define( Chunk c ) {
			/*c.addPart( UINT32, c.id+"_index", true );
			c.addPart( UINT32, c.id+"_namelength", true );
			c.addPart( CHAR, c.get( UINT32, c.id+"_namelength" ), c.id+"_name", true );
			c.addPart( UINT32, c.id+"_props", true );
			
			Parameter param = new Parameter();
			param.index = c.get( UINT32, c.id+"_index" );
			param.namelength = c.get( UINT32, c.id+"_namelength" );
			param.name = BIOBuffer.asString( c.getArray( CHAR, c.id+"_name" ), true );
			param.props = c.get( UINT32, c.id+"_props" );
			param.properties = new Property[param.props.intValue()];
			
			for( int i = 0; i < param.props; i++ ) {
				c.addPart( UINT32, c.id+"_prop_"+i+"_index", true );
				c.addPart( UINT32, c.id+"_prop_"+i+"_namelength", true );
				c.addPart( CHAR, c.get( UINT32, c.id+"_prop_"+i+"_namelength" ), c.id+"_prop_"+i+"_name", true );
				
				Property prop = new Property();
				prop.index = c.get( UINT32, c.id+"_prop_"+i+"_index" );
				prop.namelength = c.get( UINT32, c.id+"_prop_"+i+"_namelength" );
				prop.name = BIOBuffer.asString( c.getArray( CHAR, c.id+"_prop_"+i+"_name" ), true );
				PSSG.prop_map.put( prop.index.intValue(), prop );
				
				param.properties[i] = prop;
			}
			
			PSSG.param_map.put( param.index.intValue(), param );*/
		}
	};
	
	public static final Chunk PSSGNODE = new Chunk( "PSSGNODE" ) {
		@Override public void define( Chunk c ) {

		}
	};
}
