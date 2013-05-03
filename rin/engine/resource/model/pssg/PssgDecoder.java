package rin.engine.resource.model.pssg;

import static rin.engine.resource.model.pssg.PssgSpec.*;

import java.util.HashMap;

import rin.engine.resource.Resource;
import rin.engine.resource.model.ModelContainer;
import rin.engine.resource.model.ModelDecoder;
import rin.engine.resource.model.ModelOptions;
import rin.engine.util.ArrayUtils;
import rin.util.bio.BaseBinaryReader;

public class PssgDecoder extends BaseBinaryReader implements ModelDecoder {

	private HashMap<Integer, ChunkType> chunkMap = new HashMap<Integer, ChunkType>();
	String[] cStrings;
	private HashMap<Integer, PropertyType> propMap = new HashMap<Integer, PropertyType>();
	String[] pStrings;
	
	private Pssg cPssg = new Pssg();
	private PssgAnimation cAnimation;
	private PssgAnimationDataBlock cAnimationDataBlock;
	private PssgAnimationChannel cAnimationChannel;
	private PssgChannelRef cChannelRef;
	private PssgConstantChannel cConstantChannel;
	
	private void header() {
		boolean valid = true;
		for( int i = 0; i < MAGIC.length; i++ )
			valid &= readChar() == MAGIC[i];
		if( !valid ) exitWithError( "Invalid PSSG Header." );
		
		readInt32(); // size, but for some reason does not match...
		
		pStrings = new String[readInt32()]; //propcount - not needed since all chunks specify their own properties
		cStrings = new String[readInt32()+1];
	}
	
	private void stringMap() {
		for( int i = 1; i < cStrings.length; i++ ) {
			cStrings[readInt32()] = readString();
			int count = readInt32();
			for( int j = 0; j < count; j++ )
				pStrings[readInt32()] = readString();
		}
		
		for( int i = 0; i < cStrings.length; i++ )
			chunkMap.put( i, ChunkType.find( cStrings[i] ) );
		for( int i = 0; i < pStrings.length; i++ )
			propMap.put( i, PropertyType.find( pStrings[i] ) );
	}
	
	private void processProperty( String chunk, int offset, String tab ) {
		//System.out.println( "property at " + offset );
		position( offset );
		int type = readInt32();
		int size = readInt32();
		int stop = position() + size;
		
		debug.write( tab + ":"+size+": " + String.format( "%30s", pStrings[type] ) + " " );
		
		switch( propMap.get( type ) ) {
		
		case CONSTANTCHANNELSTARTTIME: cAnimation.startTime = readFloat32(); break;
		case CONSTANTCHANNELENDTIME: cAnimation.endTime = readFloat32(); break;
			
		case CHANNEL:
			if( chunk.equals( "CHANNELREF" ) ) cChannelRef.channel = readString().substring( 1 );
			break;
		case CHANNELCOUNT: cAnimation.channelCount = readInt32(); break;
		case CONSTANTCHANNELCOUNT: cAnimation.constantChannelCount = readInt32(); break;
		
		case TARGETNAME:
			if( chunk.equals( "CHANNELREF" ) ) cChannelRef.targetName = readString();
			if( chunk.equals( "CONSTANTCHANNEL" ) ) cConstantChannel.targetName = readString();
			break;
		
		case ANIMATION:
		case TYPE:
			debug.write( readString() );
		break;
		
		case VALUE:
			if( chunk.equals( "CONSTANTCHANNEL" ) ) cConstantChannel.value = readFloat32( 3 );
			break;
		case KEYTYPE:
			if( chunk.equals( "ANIMATIONCHANNELDATABLOCK" ) ) cAnimationDataBlock.type = readString();
			if( chunk.equals( "CONSTANTCHANNEL" ) ) cConstantChannel.keyType = readString();
			break;
		case KEYCOUNT: cAnimationDataBlock.keyCount = readInt32(); break;
		case TIMEBLOCK: cAnimationChannel.timeBlock = readString().substring( 1 ); break;
		case VALUEBLOCK: cAnimationChannel.valueBlock = readString().substring( 1 ); break;
		case ID:
			if( chunk.equals( "ANIMATIONCHANNELDATABLOCK" ) ) cAnimationDataBlock.id = readString();
			if( chunk.equals( "ANIMATIONCHANNEL" ) ) cAnimationChannel.id = readString();
			if( chunk.equals( "ANIMATION" ) ) debug.write( readString() );
			break;
		
		default:
			System.out.println( "unimplemented property type: " + type + " ["+ pStrings[type] + "] " + size + " " + stop );
			break;
		}
		
		debug.writeLine();
		position( stop );
	}
	
	private void getProperties( String name, int stop, String tab ) {
		while( position() < stop )
			processProperty( name, position(), tab );
	}
	
	private void getPssgDatabase( int offset, int size, int psize, String tab ) {
		//System.out.println( "pssgdatabase at " + offset + ", " + size + " " + psize );		
		getChunkData( "PSSGDATABASE", position() + psize, position() + size - 4, tab );
		
		System.out.println( "pssgdatabase ended at " + position() + " " + length() );
	}
	
	private void getLibrary( int offset, int size, int psize, String tab ) {
		//System.out.println( "library at " + offset + ", " + size + " " + psize );
		getChunkData( "LIBRARY", position() + psize, position() + size - 4, tab );
	}
	
	private void getAnimation( int offset, int size, int psize, String tab ) {
		//System.out.println( "animation at " + offset + ", " + size + " " + psize );
		cPssg.animations.add( new PssgAnimation() );
		cAnimation = cPssg.animations.get( cPssg.animations.size() - 1 );
		
		getChunkData( "ANIMATION", position() + psize, position() + size - 4, tab );
	}
	
	private void getChannelRef( int offset, int size, int psize, String tab ) {
		//System.out.println( "channel ref at " + offset + ", " + size + " " + psize );
		cAnimation.channelRefs.add( new PssgChannelRef() );
		cChannelRef = cAnimation.channelRefs.get( cAnimation.channelRefs.size() - 1 );
		
		getChunkData( "CHANNELREF", position() + psize, position() + size - 4, tab );
	}
	
	private void getConstantChannel( int offset, int size, int psize, String tab ) {
		//System.out.println( "constant channel at " + offset + ", " + size + " " + psize );
		cAnimation.constantChannels.add( new PssgConstantChannel() );
		cConstantChannel = cAnimation.constantChannels.get( cAnimation.constantChannels.size() - 1 );
		
		getChunkData( "CONSTANTCHANNEL", position() + psize, position() + size - 4, tab );
	}
	
	private void getAnimationChannel( int offset, int size, int psize, String tab ) {
		//System.out.println( "animation channel at " + offset + ", " + size + " " + psize );
		cPssg.animationChannels.add( new PssgAnimationChannel() );
		cAnimationChannel = cPssg.animationChannels.get( cPssg.animationChannels.size() - 1 );
		
		getChunkData( "ANIMATIONCHANNEL", position() + psize, position() + size - 4, tab );
		/*debug.writeLine( tab + "::" + cAnimationChannel.timeBlock );
		debug.writeLine( tab + "::" + cAnimationChannel.valueBlock );
		debug.writeLine( tab + "::" + cAnimationChannel.id );*/
	}
	
	private void getAnimationChannelDataBlock( int offset, int size, int psize, String tab ) {
		//System.out.println( "animation channel data block at " + offset + ", " + size + " " + psize );
		cPssg.animationData.add( new PssgAnimationDataBlock() );
		cAnimationDataBlock = cPssg.animationData.get( cPssg.animationData.size() - 1 );
		
		getChunkData( "ANIMATIONCHANNELDATABLOCK", position() + psize, position() + size - 4, tab );
	}
	
	private void getKeys( int offset, int size, int psize, String tab ) {
		//System.out.println( "keys at " + offset + ", " + size + " " + psize );
		cAnimationDataBlock.data = readFloat32( ((position()+size-4)-position()) / 4 );
		//debug.writeLine( tab + ":" + cAnimationDataBlock.type+": " + ArrayUtils.asString( cAnimationDataBlock.data ) );
	}
	
	private void getAnimationSet( int offset, int size, int psize, String tab ) {
		//System.out.println( "animation set at " + offset + ", " + size + " " + psize );
		getChunkData( "ANIMATIONSET", position() + psize, position() + size - 4, tab );
	}
	
	private void getAnimationRef( int offset, int size, int psize, String tab ) {
		//System.out.println( "animation ref at " + offset + ", " + size + " " + psize );
		getChunkData( "ANIMATIONREF", position() + psize, position() + size - 4, tab );
	}
	
	private void processChunk( int offset, String tab ) {
		position( offset );
		int type = readInt32();
		int size = readInt32();
		int psize = readInt32();
		int stop = position() + size - 4;
		
		debug.writeLine();
		debug.writeLine( tab + cStrings[type] + " [" + offset + "]" );
		debug.writeLine( tab + "::" );
		
		if( chunkMap.get( type ).isDataOnly() ) {
			switch( chunkMap.get( type ) ) {
			
			case KEYS: getKeys( offset, size, psize, tab ); break;
			default:
				System.out.println( "unimplemented data only chunk: " + type + " ["+ cStrings[type] + "] " + size + " " + psize );
				break;
			}
			position( stop );
			return;
		}
		
		switch( chunkMap.get( type ) ) {
		
		case PSSGDATABASE: getPssgDatabase( offset, size, psize, tab ); break;
		case LIBRARY: getLibrary( offset, size, psize, tab ); break;
		
		case ANIMATION: getAnimation( offset, size, psize, tab ); break;
		case CHANNELREF: getChannelRef( offset, size, psize, tab ); break;
		case CONSTANTCHANNEL: getConstantChannel( offset, size, psize, tab ); break;
		case ANIMATIONCHANNEL: getAnimationChannel( offset, size, psize, tab ); break;
		case ANIMATIONCHANNELDATABLOCK: getAnimationChannelDataBlock( offset, size, psize, tab ); break;
		case ANIMATIONSET: getAnimationSet( offset, size, psize, tab ); break;
		case ANIMATIONREF: getAnimationRef( offset, size, psize, tab ); break;
		
		default:
			System.out.println( "unimplemented chunk type: " + type + " ["+ cStrings[type] + "] " + size + " " + psize );
			break;
		}
		
		position( stop );
	}
	
	private void getChunks( int stop, String tab ) {
		while( position() < stop )
			processChunk( position(), tab + "  " );
	}
	
	private void getChunkData( String name, int pstop, int stop, String tab ) {
		getProperties( name, pstop, tab );
		getChunks( stop, tab );
	}

	private String readString() {
		return readString( readInt32() );
	}
	
	@Override
	public String getExtensionName() { return "pssg"; }
	
	private Resource debug;
	
	@Override
	public ModelContainer decode( Resource resource, ModelOptions opts ) {
		load( resource );
		debug = resource.getDirectory().createResource( resource.getBaseName() + ".debug", true );
		debug.openStream();
		
		header();
		stringMap();
		processChunk( position(), "" );
		
		// parse animations if it was an animation file
		//PssgAnimation.channelRefs contains PssgChannelRef
			//PssgChannelRef.targetName = BONE IN MAIN MODEL
			//PssgChannelRef.channel refers to an animation channel by it's id
				//PssgAnimationChannel points to the animation data blocks:
					//PssgAnimationChannel.timeBlock = PssgAnimationDataBlock.id
					//PssgAnimationChannel.valueBlock = PssgAnimationDataBlock.id
		//PssgAnimation.constantChannels contains PssgConstantChannel
			//PssgConstantChannel depicts items within the animation that do not change during the course of the animation
		debug.closeStream();
		ModelContainer mc = new ModelContainer();
		return mc;
	}

	@Override
	public void clear() {
		//TODO: clear all data used by this decoder
	}
}
