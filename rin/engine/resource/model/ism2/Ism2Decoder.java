package rin.engine.resource.model.ism2;

import static rin.engine.resource.model.ism2.Ism2Spec.*;

import java.util.TreeMap;

import rin.engine.resource.Resource;
import rin.engine.resource.model.ModelContainer;
import rin.engine.resource.model.ModelDecoder;
import rin.engine.resource.model.ModelOptions;
import rin.engine.util.ArrayUtils;
import rin.util.bio.BaseBinaryReader;

public class Ism2Decoder extends BaseBinaryReader implements ModelDecoder {
	
	private int chunkCount;
	private TreeMap<Integer, Integer> chunkOffsets = new TreeMap<Integer, Integer>();
	private String[] stringMap;
	
	private void header() {
		boolean valid = true;		
		for( int i = 0; i < MAGIC.length; i++ )
			valid &= MAGIC[i] == readChar();
		advance( 12 );
		
		valid &= readInt32() == length();
		chunkCount = readInt32();
		advance( 8 );
		
		if( !valid ) exitWithError( "Not a valid ISM2 file." );
		debug.writeLine( "ISM2 file: size " + length() + ", chunkCount " + chunkCount );
	}
	
	private void chunkList() {
		for( int i = 0; i < chunkCount; i++ ) {
			int id = readInt32();
			int offset = readInt32();
			chunkOffsets.put( offset, id );
		}
		debug.writeLine( ArrayUtils.asString( chunkOffsets ) );
		
		for( int i : chunkOffsets.keySet() )
			processChunk( i );
	}
	
	private void getStrings( int offset, int hsize ) {
		//System.out.println( "strings at " + offset + " " + hsize );
		int[] offsets = getOffsets( readInt32() );
		
		stringMap = new String[offsets.length];
		for( int i = 0; i < offsets.length; i++ ) {
			position( offsets[i] );
			stringMap[i] = readString();
		}
		debug.writeLine( "Strings: " + ArrayUtils.asString( stringMap ) );
	}
	
	private void getTextureList( int offset, int hsize ) {
		System.out.println( "c80 at " + offset + " " + hsize );
	}
	
	private void getC52( int offset, int hsize ) {
		System.out.println( "c52 at " + offset + " " + hsize );
		int count = readInt32();
		advance( 20 );
		
		int[] offsets = getOffsets( count );
		debug.writeLine( offsets.length + " bones." );
		debug.writeLine( "c52 offsets: " + ArrayUtils.asString( offsets ) );
		for( int i : offsets )
			processChunk( i );
	}
	
	private void getC80( int offset, int hsize ) {
		System.out.println( "c80 at " + offset + " " + hsize );
		int count = readInt32();
		debug.writeLine( stringMap[ readInt32() ] );
		readInt32( 4 ); //TODO: unknown
		
		int[] offsets = getOffsets( count );
		debug.writeLine( "c80 offsets: " + ArrayUtils.asString( offsets ) );
		for( int i : offsets )
			processChunk( i );
	}
	
	private void getC15( int offset, int hsize ) {
		System.out.println( "c80 at " + offset + " " + hsize );
		readInt32(); //TODO: unknown
		debug.writeLine( "b1: " + stringMap[ readInt32() ] );
		readInt32( 6 ); //TODO: unknown
		debug.writeLine( "b2: " + stringMap[ readInt32() ] );
		readInt32( 5 ); //TODO: unknown
		
		position( offset + hsize );
		processChunk( position() );
		
		System.out.println( position() + " " + length() );
	}
	
	private void getSkinning( int offset, int hsize ) {
		System.out.println( "skinning at " + offset + " " + hsize );
		int count = readInt32();
		readInt32(); //TODO: unknown
		int type = readInt32();
		int stride = readInt32();
		readInt32(); //TODO: unknown
		readInt32(); //TODO: unknown
		debug.writeLine( "count: " + count + " " + (count % stride == 0 ) + " " + (count/stride) );
		switch( type ) {
		
		case 5:
			debug.writeLine( "shorts [" + stride + "]: " + ArrayUtils.asString( readInt16( count ) ) );
			break;
			
		case 18:
			for( int i = 0; i < count; i += stride ) {
				debug.writeLine( "time: " + (readInt16()) );
				debug.write( "Values: " );
				for( int j = 0; j < stride - 1; j++ )
					debug.write( (readInt16() / 65535.0f) + " " );
				debug.writeLine();
			}
			//debug.writeLine( "floats [" + stride + "]: " + ArrayUtils.asString( readFloat16( count ) ) );
			break;
			
		default:
			exitWithError( "UNKOWN SKINNING TYPE " + type );
			break;
			
		}
	}
	
	private void processChunk( int offset ) {
		position( offset );
		int type = readInt32();
		int hsize = readInt32();
		
		if( !(chunkOffsets.containsKey( offset )) )
			debug.writeLine( "Unexpected chunk type " + type + " at offset " + offset );
		
		switch( type ) {
		
		case C_STRINGS: getStrings( offset, hsize ); break;
		case C_TEXTURE_LIST: getTextureList( offset, hsize ); break;
		case C_SKINNING: getSkinning( offset, hsize ); break;
		case C_52: getC52( offset, hsize ); break;
		case C_80: getC80( offset, hsize ); break;
		case C_15: getC15( offset, hsize ); break;
			
		default:
			System.out.println( "unknown chunk type: " + type + " [" + String.format( "0x%02x", type ) + "] at " + offset );
		}
	}
	
	private Resource debug;
	
	@Override
	public String getExtensionName() { return "ism2"; }
	
	@Override
	public ModelContainer decode( Resource resource, ModelOptions options ) {
		load( resource );
		debug = resource.getDirectory().createResource( resource.getBaseName() + ".debug", true );
		debug.openStream();
		
		header();
		chunkList();
		
		// construct model container
		ModelContainer mc = new ModelContainer();
		
		debug.closeStream();
		return mc;
	}
	
	private String readString() {
		char c;
		String res = "";
		while( (c = readChar()) != '\0' )
			res += c;
		return res;
	}
	
	private int[] getOffsets( int amount ) {
		int[] res = new int[amount];
		for( int i = 0; i < amount; i++ )
			res[i] = readInt32();
		return res;
	}

}
