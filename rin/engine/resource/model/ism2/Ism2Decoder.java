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
	
	private boolean isAnimation = false;
	private String name;
	private Ism2Animation cAnimation;
	private Ism2KeyFrame cFrame;
	private Ism2TransformData cTransform;
	
	private void header() {
		boolean valid = true;		
		for( int i = 0; i < MAGIC.length; i++ )
			valid &= MAGIC[i] == readChar();
		advance( 12 );
		
		valid &= readInt32() == length();
		chunkCount = readInt32();
		advance( 8 );
		
		if( !valid ) exitWithError( "Not a valid ISM2 file." );
		//debug.writeLine( "ISM2 file: size " + length() + ", chunkCount " + chunkCount );
	}
	
	private void chunkList() {
		for( int i = 0; i < chunkCount; i++ ) {
			int id = readInt32();
			int offset = readInt32();
			chunkOffsets.put( offset, id );
		}
		//debug.writeLine( ArrayUtils.asString( chunkOffsets ) );
		
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
		//debug.writeLine( "Strings: " + ArrayUtils.asString( stringMap ) );
	}
	
	private void getTextureList( int offset, int hsize ) {
		System.out.println( "c80 at " + offset + " " + hsize );
	}
	
	private void getAnimation( int offset, int hsize ) {
		//System.out.println( "c52 at " + offset + " " + hsize );
		isAnimation = true;
		cAnimation = new Ism2Animation( name );
		
		int count = readInt32();
		advance( 20 );
		
		int[] offsets = getOffsets( count );
		for( int i : offsets )
			processChunk( i );
	}
	
	private void getAnimationFrame( int offset, int hsize ) {
		//System.out.println( "c80 at " + offset + " " + hsize );
		int count = readInt32();
		cFrame = cAnimation.addFrame( stringMap[readInt32()] );
		readInt32( 4 ); //TODO: unknown
		
		int[] offsets = getOffsets( count );
		//debug.writeLine( "c80 offsets: " + ArrayUtils.asString( offsets ) );
		for( int i : offsets )
			processChunk( i );
	}
	
	private void getFrameTransform( int offset, int hsize ) {
		//System.out.println( "c80 at " + offset + " " + hsize );
		readInt32(); //TODO: unknown
		String name1 = stringMap[ readInt32() ];
		readInt32( 6 ); //TODO: unknown
		String name2 = stringMap[ readInt32() ];
		readInt32( 5 ); //TODO: unknown
		cTransform = cFrame.addTransform( name1, name2 );
		
		position( offset + hsize );
		processChunk( position() );
		
		//System.out.println( position() + " " + length() );
	}
	
	private void getTransformData( int offset, int hsize ) {
		//System.out.println( "skinning at " + offset + " " + hsize );
		cTransform.count = readInt32();
		readInt32(); //TODO: unknown
		cTransform.type = readInt32();
		cTransform.stride = readInt32();
		readInt32(); //TODO: unknown
		readInt32(); //TODO: unknown
		int count = cTransform.count / cTransform.stride;
		switch( cTransform.type ) {
		
		case 5:
			//debug.writeLine( "shorts [" + stride + "]: " + ArrayUtils.asString( readInt16( count ) ) );
			readInt16( cTransform.count );
			System.err.println( "WHAT DOES THIS EVEN DO?" );
			break;
			
		case 18:
			cTransform.time = new short[count];
			cTransform.data = new float[count][cTransform.stride-1];
			for( int i = 0; i < count; i++ ) {
				cTransform.time[i] = readInt16();
				for( int j = 0; j < cTransform.stride - 1; j++ )
					cTransform.data[i][j] = (readInt16() / 65535.0f);
			}
			break;
			
		default:
			exitWithError( "UNKOWN transform TYPE " + cTransform.type );
			break;
			
		}
	}
	
	private void processChunk( int offset ) {
		position( offset );
		int type = readInt32();
		int hsize = readInt32();
		
		/*if( !(chunkOffsets.containsKey( offset )) )
			debug.writeLine( "Unexpected chunk type " + type + " at offset " + offset );*/
		
		switch( type ) {
		
		case C_STRINGS: getStrings( offset, hsize ); break;
		case C_TEXTURE_LIST: getTextureList( offset, hsize ); break;
		case C_TRANSFORM_DATA: getTransformData( offset, hsize ); break;
		
		case C_ANIMATION: getAnimation( offset, hsize ); break;
		case C_ANIMATION_FRAME: getAnimationFrame( offset, hsize ); break;
		case C_FRAME_TRANSFORM: getFrameTransform( offset, hsize ); break;
			
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
		name = resource.getBaseName();
		debug = resource.getDirectory().createResource( resource.getBaseName() + ".debug", true );
		debug.openStream();
		
		header();
		chunkList();

		// construct model container
		ModelContainer mc = new ModelContainer();
		
		// check for animations
		//TODO: add this check to the options object for ism2
		if( !isAnimation ) {
			System.out.println( "SEARCHING FOR ANIMATIONS" );
			//TODO: add 'animation directory' to options
		} else cAnimation.print();
		
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
