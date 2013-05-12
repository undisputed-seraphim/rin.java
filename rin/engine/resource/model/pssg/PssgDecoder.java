package rin.engine.resource.model.pssg;

import static rin.engine.resource.model.pssg.PssgSpec.*;

import java.util.HashMap;

import rin.engine.resource.Directory;
import rin.engine.resource.Resource;
import rin.engine.resource.ResourceManager;
import rin.engine.resource.image.dds.DdsUtils;
import rin.engine.resource.model.ModelContainer;
import rin.engine.resource.model.ModelDecoder;
import rin.engine.resource.model.ModelOptions;
import rin.gl.lib3d.ModelScene;
import rin.gl.lib3d.Node;
import rin.gl.lib3d.RenderNode;
import rin.gl.lib3d.SkinNode;
import rin.util.bio.BaseBinaryReader;

public class PssgDecoder extends BaseBinaryReader implements ModelDecoder {

	private HashMap<Integer, ChunkType> chunkMap = new HashMap<Integer, ChunkType>();
	String[] cStrings;
	//private HashMap<Integer, PropertyType> propMap = new HashMap<Integer, PropertyType>();
	String[] pStrings;
	
	private Pssg cPssg;
	private Pssg cAnimPssg;
	
	/*private Pssg cPssg = new Pssg();
	private PssgNode cNode;
	private PssgDataBlock cDataBlock;
	
	private PssgAnimation cAnimation;
	private PssgAnimationDataBlock cAnimationDataBlock;
	private PssgAnimationChannel cAnimationChannel;
	private PssgChannelRef cChannelRef;
	private PssgConstantChannel cConstantChannel;
	
	private int jointNodes = 0;
	private int skinJoints = 0;
	private int renderNodes = 0;
	private int skinNodes = 0;*/
	
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
		/*for( int i = 0; i < pStrings.length; i++ )
			propMap.put( i, PropertyType.find( pStrings[i] ) );*/
	}
	
	/*private void processProperty( String chunk, int offset, String tab ) {
		//System.out.println( "property at " + offset );
		position( offset );
		int type = readInt32();
		int size = readInt32();
		int stop = position() + size;
		
		//debug.write( tab + ":"+size+": " + String.format( "%30s", pStrings[type] ) + " " );
		
		switch( propMap.get( type ) ) {
		
		case ELEMENTCOUNT: cDataBlock.count = readInt32(); break;
		case DATATYPE: cDataBlock.dataType = readString(); break;
		case RENDERTYPE: cDataBlock.renderType = readString(); break;
		case JOINT: cNode.id = readString().substring( 1 ); break;
		//case SOURCE:
			//if( cNode instanceof PssgSkinNode ) ((PssgSkinNode)cNode).source = readString().substring( 1 );
			//if( cNode instanceof PssgRenderNode ) ((PssgRenderNode)cNode).source = readString().substring( 1 );
			//break;
		case INDICES:
			if( cNode instanceof PssgSkinNode ) ((PssgSkinNode)cNode).indices = readString().substring( 1 );
			if( cNode instanceof PssgRenderNode ) ((PssgRenderNode)cNode).indices = readString().substring( 1 );
			break;
		case SHADER:
			if( cNode instanceof PssgSkinNode ) ((PssgSkinNode)cNode).shader = readString().substring( 1 );
			if( cNode instanceof PssgRenderNode ) ((PssgRenderNode)cNode).shader = readString().substring( 1 );
			break;
		case OFFSET:
			if( chunk.equals( "DATABLOCKSTREAM" ) ) cDataBlock.offset = readInt32();
			break;
		case STRIDE:
			if( chunk.equals( "DATABLOCKSTREAM" ) ) cDataBlock.stride = readInt32();
			break;
			
		case CONSTANTCHANNELSTARTTIME: cAnimation.startTime = readFloat32(); break;
		case CONSTANTCHANNELENDTIME: cAnimation.endTime = readFloat32(); break;
		case CHANNEL:
			if( chunk.equals( "CHANNELREF" ) ) cChannelRef.channel = readString().substring( 1 );
			break;
		case CHANNELCOUNT: cAnimation.channelCount = readInt32(); break;
		case CONSTANTCHANNELCOUNT: cAnimation.constantChannelCount = readInt32(); break;
		case TARGETNAME:
			String target = readString();
			if( chunk.equals( "CHANNELREF" ) ) cChannelRef.targetName = target;
			if( chunk.equals( "CONSTANTCHANNEL" ) ) cConstantChannel.targetName = target;
			break;
		
		case ANIMATION:
		case TYPE:
			readString();//debug.write( readString() );
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
			if( chunk.equals( "ANIMATION" ) ) cAnimation.id = readString();
			if( chunk.equals( "ROOTNODE" ) || chunk.equals( "NODE" ) ) cNode.id = readString();
			if( chunk.equals( "JOINTNODE" ) || chunk.equals( "SKINNODE" ) ) cNode.id = readString();
			if( chunk.equals( "RENDERNODE" ) ) cNode.id = readString();
			if( chunk.equals( "DATABLOCK" ) ) cDataBlock.id = readString();
			break;
		
		default:
			System.out.println( "unimplemented property type: " + type + " ["+ pStrings[type] + "] " + size + " " + stop );
			break;
		}
		
		//debug.writeLine();
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
	
	private void getRootNode( int offset, int size, int psize, String tab ) {
		//System.out.println( "root node at " + offset + ", " + size + " " + psize );
		cPssg.root = new PssgRootNode();
		cNode = cPssg.root;
		
		getChunkData( "ROOTNODE", position() + psize, position() + size - 4, tab );
	}
	
	private void getTransform( int offset, int size, int psize, String tab ) {
		cNode.transform = readFloat32( 16 );
	}
	
	private void getBoundingBox( int offset, int size, int psize, String tab ) {
		cNode.bbox = readFloat32( 6 );
	}
	
	private void getNode( int offset, int size, int psize, String tab ) {
		//System.out.println( "node at " + offset + ", " + size + " " + psize );
		cNode.children.add( new PssgNode() );
		PssgNode tmp = cNode;
		cNode = cNode.children.get( cNode.children.size() - 1);
		
		getChunkData( "NODE", position() + psize, position() + size - 4, tab );
		cNode = tmp;
	}
	
	private void getJointNode( int offset, int size, int psize, String tab ) {
		jointNodes++;
		//System.out.println( "joint node at " + offset + ", " + size + " " + psize );
		cNode.children.add( new PssgJointNode() );
		PssgNode tmp = cNode;
		cNode = cNode.children.get( cNode.children.size() - 1);
		
		getChunkData( "JOINTNODE", position() + psize, position() + size - 4, tab );
		cNode = tmp;
	}
	
	private void getSkinNode( int offset, int size, int psize, String tab ) {
		skinNodes++;
		//System.out.println( "skin node at " + offset + ", " + size + " " + psize );
		cNode.children.add( new PssgSkinNode() );
		PssgNode tmp = cNode;
		cNode = cNode.children.get( cNode.children.size() - 1);
		
		getChunkData( "SKINNODE", position() + psize, position() + size - 4, tab );
		cNode = tmp;
	}
	
	private void getModifierNetworkInstance( int offset, int size, int psize, String tab ) {
		System.out.println( "modifier network instance at " + offset + ", " + size + " " + psize );
		
		getChunkData( "MODIFIERNETWORKINSTANCE", position() + psize, position() + size - 4, tab );
	}
	
	//private void getRenderInstanceSource( int offset, int size, int psize, String tab ) {
		//System.out.println( "render instance source at " + offset + ", " + size + " " + psize );
		
		//getChunkData( "RENDERINSTANCESOURCE", position() + psize, position() + size - 4, tab );
	//}
	
	private void getRenderNode( int offset, int size, int psize, String tab ) {
		renderNodes++;
		//System.out.println( "render node at " + offset + ", " + size + " " + psize );
		cNode.children.add( new PssgRenderNode() );
		PssgNode tmp = cNode;
		cNode = cNode.children.get( cNode.children.size() - 1);
		
		getChunkData( "RENDERNODE", position() + psize, position() + size - 4, tab );
		cNode = tmp;
	}
	
	private void getRenderStreamInstance( int offset, int size, int psize, String tab ) {
		System.out.println( "render stream instance at " + offset + ", " + size + " " + psize );
		
		getChunkData( "RENDERSTREAMINSTANCE", position() + psize, position() + size - 4, tab );
	}
	
	private void getSkinJoint( int offset, int size, int psize, String tab ) {
		skinJoints++;
		//System.out.println( "skin joint at " + offset + ", " + size + " " + psize );
		cNode.children.add( new PssgSkinJoint() );
		PssgNode tmp = cNode;
		cNode = cNode.children.get( cNode.children.size() - 1);
		
		getChunkData( "SKINJOINT", position() + psize, position() + size - 4, tab );
		cNode = tmp;
	}
	
	private void getDataBlock( int offset, int size, int psize, String tab ) {
		//System.out.println( "datablock at " + offset + ", " + size + " " + psize );
		cPssg.dataBlocks.add( new PssgDataBlock() );
		cDataBlock = cPssg.dataBlocks.get( cPssg.dataBlocks.size() - 1 );
		
		getChunkData( "DATABLOCK", position() + psize, position() + size - 4, tab );
	}
	
	private void getDataBlockStream( int offset, int size, int psize, String tab ) {
		//System.out.println( "datablock stream at " + offset + ", " + size + " " + psize );
		
		getChunkData( "DATABLOCKSTREAM", position() + psize, position() + size - 4, tab );
	}
	
	private void getDataBlockData( int offset, int size, int psize, String tab ) {
		if( cDataBlock.dataType.toLowerCase().equals( "float4" ) ) {
			cDataBlock.fdata = readFloat32( cDataBlock.count * 4 );
		} else if( cDataBlock.dataType.toLowerCase().equals( "uchar4" ) ) {
			cDataBlock.sdata = readUInt8( cDataBlock.count * 4 );
		} else if( cDataBlock.dataType.toLowerCase().equals( "float3" ) ) {
			cDataBlock.fdata = readFloat32( cDataBlock.count * 3 );
		} else System.err.println( "Unknown datablock datatype: " + cDataBlock.dataType );
	}
	
	private void getModifierNetwork( int offset, int size, int psize, String tab ) {
		System.out.println( "modifier network at " + offset + ", " + size + " " + psize );
		
		getChunkData( "DATABLOCKSTREAM", position() + psize, position() + size - 4, tab );
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
		//debug.writeLine( tab + "::" + cAnimationChannel.timeBlock );
		//debug.writeLine( tab + "::" + cAnimationChannel.valueBlock );
		//debug.writeLine( tab + "::" + cAnimationChannel.id );
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
	
		//debug.writeLine();
		//debug.writeLine( tab + cStrings[type] + " [" + offset + "]" );
		//debug.writeLine( tab + "::" );
		
		if( chunkMap.get( type ).isDataOnly() ) {
			switch( chunkMap.get( type ) ) {
			
			case TRANSFORM: getTransform( offset, size, psize, tab ); break;
			case BOUNDINGBOX: getBoundingBox( offset, size, psize, tab ); break;
			case DATABLOCKDATA: getDataBlockData( offset, size, psize, tab ); break;
			case KEYS: getKeys( offset, size, psize, tab ); break;
			default:
				System.out.println( "unimplemented data only chunk: " + type + " ["+ cStrings[type] + "] " + size + " " + psize );
				break;
			}
			//System.out.println( "data only chunk " + chunkMap.get( type ) + " ended at " + position() + " of " + stop );
			position( stop );
			return;
		}
		
		switch( chunkMap.get( type ) ) {
		
		case PSSGDATABASE: getPssgDatabase( offset, size, psize, tab ); break;
		case LIBRARY: getLibrary( offset, size, psize, tab ); break;
		
		case ROOTNODE: getRootNode( offset, size, psize, tab ); break;
		case NODE: getNode( offset, size, psize, tab ); break;
		case JOINTNODE: getJointNode( offset, size, psize, tab ); break;
		case SKINNODE: getSkinNode( offset, size, psize, tab ); break;
		case MODIFIERNETWORKINSTANCE: getModifierNetworkInstance( offset, size, psize, tab ); break;
		//case RENDERINSTANCESOURCE: getRenderInstanceSource( offset, size, psize, tab ); break;
		case RENDERNODE: getRenderNode( offset, size, psize, tab ); break;
		case RENDERSTREAMINSTANCE: getRenderStreamInstance( offset, size, psize, tab ); break;
		case SKINJOINT: getSkinJoint( offset, size, psize, tab ); break;
		
		case DATABLOCK: getDataBlock( offset, size, psize, tab ); break;
		case DATABLOCKSTREAM: getDataBlockStream( offset, size, psize, tab ); break;
		
		case MODIFIERNETWORK: getModifierNetwork( offset, size, psize, tab ); break;
		
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
	}*/

	private void getPssgDatabase( int offset, int stop, int pstop, PssgDatabase chunk ) {
		if( cPssg == null ) {
			cPssg = new Pssg();
			cPssg.database = chunk;
		} else {
			cAnimPssg = new Pssg();
			cAnimPssg.database = chunk;
		}
		
		getChildChunks( pstop, stop, chunk );
	}
	
	private void getLibrary( int offset, int stop, int pstop, PssgLibrary chunk ) {
		chunk.type = readPString();

		getChildChunks( pstop, stop, chunk );
	}
	
	private void getTexture( int offset, int stop, int pstop, PssgTexture chunk ) {
		chunk.width = readPInt32();
		chunk.height = readPInt32();
		chunk.texelFormat = readPString();
		readPInt32(); //transient
		readPInt32(); //resolveMSAA
		readPInt32(); //wrapS
		readPInt32(); //wrapT
		readPInt32(); //wrapR
		readPInt32(); //minFilter
		readPInt32(); //magFilter
		readPInt32();
		readPInt32();
		readPInt32();
		readPInt32();
		int automipmap = readPInt32();
		if( automipmap == 0 )
			readPInt32(); //numberMipMapLevels
		readPInt32(); //imageBlockCount
		readPInt32(); //allocationStrategy
		chunk.filename = readPString();
		
		getChildChunks( pstop, stop, chunk );
		cPssg.cache( chunk );
	}
	
	private void getTextureImageBlock( int offset, int stop, int pstop, PssgTexture chunk ) {
		chunk.blockName = readPString();
		chunk.size = readPInt32();
		
		getChildChunks( pstop, stop, chunk );
	}
	
	private void getTextureImageBlockData( int offset, int stop, int pstop, PssgTexture chunk ) {
		//Directory dir = ResourceManager.getPackDirectory( "rin", "images" );
		chunk.data = readInt8( chunk.size );
		if( chunk.texelFormat.toLowerCase().equals( "dxt1" ) )
			DdsUtils.fromRawDXT1( chunk.width, chunk.height, chunk.data ).test();
		else if( chunk.texelFormat.toLowerCase().equals( "dxt3" ) )
			DdsUtils.fromRawDXT3( chunk.width, chunk.height, chunk.data ).test();
	}
	
	private void getDataBlock( int offset, int stop, int pstop, PssgDataBlock chunk ) {
		readPInt32();
		readPInt32();
		chunk.count = readPInt32();
		readPInt32();
		chunk.id = readPString();
		
		getChildChunks( pstop, stop, chunk );
		cPssg.cache( chunk );
	}
	
	private void getDataBlockStream( int offset, int stop, int pstop, PssgDataBlock chunk ) {
		chunk.renderType = readPString();
		chunk.dataType = readPString();
		chunk.offset = readPInt32();
		chunk.stride = readPInt32();
	}
	
	private void getDataBlockData( int offset, int stop, int pstop, PssgDataBlock chunk ) {
		String type = chunk.dataType.toLowerCase();
		if( type.equals( "float4" ) ) {
			chunk.fdata = readFloat32( chunk.count * 4 );
		} else if( type.equals( "uchar4" ) ) {
			chunk.sdata = readUInt8( chunk.count * 4 );
		} else if( type.equals( "float3" ) ) {
			chunk.fdata = readFloat32( chunk.count * 3 );
		} else System.err.println( "unimplemented DataBlockData dataType: " + type );
	}
	
	private void getShaderProgram( int offset, int stop, int pstop, PssgShaderProgram chunk ) {
		readPInt32();
		chunk.id = readPString();
		
		getChildChunks( pstop, stop, chunk );
		cPssg.cache( chunk );
	}
	
	private void getShaderProgramCode( int offset, int stop, int pstop, PssgShaderProgramCode chunk ) {
		chunk.codeSize = readPInt32();
		readPInt32();
		readPInt32();
		readPInt32();
		readPInt32();
		chunk.codeType = readPString();
		
		getChildChunks( pstop, stop, chunk );
	}
	
	private void getShaderProgramCodeBlock( int offset, int stop, int pstop, PssgShaderProgramCodeBlock chunk ) {
		chunk.data = readString( ((PssgShaderProgramCode)chunk.parent).codeSize );
	}
	
	private void getCgStream( int offset, int stop, int pstop, PssgCgStream chunk ) {
		chunk.cgStreamName = readPString();
		chunk.cgStreamDataType = readPString();
		chunk.cgStreamRenderType = readPString();
	}
	
	private void getShaderInputDefinition( int offset, int stop, int pstop, PssgShaderInputDefinition chunk ) {
		chunk.inputName = readPString();
		chunk.type = readPString();
		chunk.format = readPString();
	}
	
	private void getRootNode( int offset, int stop, int pstop, PssgRootNode chunk ) {
		readPInt32();
		chunk.id = readPString();
		
		getChildChunks( pstop, stop, chunk );
		cPssg.cache( chunk );
	}
	
	private void getNode( int offset, int stop, int pstop, PssgNode chunk ) {
		readPInt32();
		readPString();
		chunk.id = readPString();
		
		getChildChunks( pstop, stop, chunk );
		cPssg.cache( chunk );
	}
	
	private void getJointNode( int offset, int stop, int pstop, PssgJointNode chunk ) {
		readPInt32();
		readPString();
		chunk.id = readPString();
		
		getChildChunks( pstop, stop, chunk );
		cPssg.cache( chunk );
	}
	
	private void getRenderNode( int offset, int stop, int pstop, PssgRenderNode chunk ) {
		readPInt32();
		readPString();
		chunk.id = readPString();
		
		getChildChunks( pstop, stop, chunk );
		cPssg.cache( chunk );
	}
	
	private void getSkinNode( int offset, int stop, int pstop, PssgSkinNode chunk ) {
		chunk.jointCount = readPInt32();
		chunk.skeleton = readPString();
		readPInt32();
		readPString();
		chunk.id = readPString();
		
		getChildChunks( pstop, stop, chunk );
		cPssg.cache( chunk );
	}
	
	private void getSkinJoint( int offset, int stop, int pstop, PssgSkinJoint chunk ) {
		chunk.joint = readPString().substring( 1 );
	}
	
	private void getModifierNetworkInstance( int offset, int stop, int pstop, PssgModifierNetworkInstance chunk ) {
		readPInt32();
		readPInt32();
		int ptype = readInt32();
		readInt32();
		if( pStrings[ptype].toLowerCase().equals( "parametercount" ) ) {
			readInt32();
			readPInt32();
			readPInt32();
		} else readPInt32();
		
		readPString();
		readPInt32();
		chunk.indices = readPString().substring( 1 );
		readPInt32();
		chunk.shader = readPString().substring( 1 );
		readPInt32();
		chunk.id = readPString();
		
		getChildChunks( pstop, stop, chunk );
	}
	
	private void getRenderStreamInstance( int offset, int stop, int pstop, PssgRenderStreamInstance chunk ) {
		readPInt32();
		chunk.indices = readPString().substring( 1 );
		readPInt32();
		chunk.shader = readPString().substring( 1 );
		readPInt32();
		chunk.id = readPString();
		
		getChildChunks( pstop, stop, chunk );
	}
	
	private void getRenderInstanceSource( int offset, int stop, int pstop, PssgRenderInstanceSource chunk ) {
		chunk.source = readPString().substring( 1 );
		
		getChildChunks( pstop, stop, chunk );
	}
	
	private void getTransform( int offset, int stop, int pstop, PssgSceneNode<?> chunk ) {
		chunk.transform = readFloat32( 16 );
	}
	
	private void getBoundingBox( int offset, int stop, int pstop, PssgSceneNode<?> chunk ) {
		chunk.bbox = readFloat32( 6 );
	}
	
	private void processChunk( int offset, PssgChunk<?> p ) {
		position( offset );
		int type = readInt32();
		int size = readInt32();
		int psize = readInt32();
		int stop = position() + size - 4;
		int pstop = position() + psize;
		
		ChunkType ct = chunkMap.get( type );
		switch( ct ) {
		case PSSGDATABASE: getPssgDatabase( offset, stop, pstop, new PssgDatabase().set( ct, p ) ); break;
		case LIBRARY: getLibrary( offset, stop, pstop, new PssgLibrary().set( ct, p ) ); break;
		
		case TEXTURE: getTexture( offset, stop, pstop, new PssgTexture().set( ct, p ) ); break;
		case TEXTUREIMAGEBLOCK: getTextureImageBlock( offset, stop, pstop, (PssgTexture)p ); break;
		case TEXTUREIMAGEBLOCKDATA: getTextureImageBlockData( offset, stop, pstop, (PssgTexture)p ); break;
		
		case DATABLOCK: getDataBlock( offset, stop, pstop, new PssgDataBlock().set( ct, p ) ); break;
		case DATABLOCKSTREAM: getDataBlockStream( offset, stop, pstop, (PssgDataBlock)p ); break;
		case DATABLOCKDATA: getDataBlockData( offset, stop, pstop, (PssgDataBlock)p ); break;
		
		case SHADERPROGRAM: getShaderProgram( offset, stop, pstop, new PssgShaderProgram().set( ct, p ) ); break;
		case SHADERPROGRAMCODE: getShaderProgramCode( offset, stop, pstop, new PssgShaderProgramCode().set( ct, p ) ); break;
		case SHADERPROGRAMCODEBLOCK: getShaderProgramCodeBlock( offset, stop, pstop, new PssgShaderProgramCodeBlock().set( ct, p ) ); break;
		case CGSTREAM: getCgStream( offset, stop, pstop, new PssgCgStream().set( ct, p ) ); break;
		case SHADERINPUTDEFINITION: getShaderInputDefinition( offset, stop, pstop, new PssgShaderInputDefinition().set( ct, p ) ); break;
		
		case ROOTNODE: getRootNode( offset, stop, pstop, new PssgRootNode().set( ct, p) ); break;
		case NODE: getNode( offset, stop, pstop, new PssgNode().set( ct, p ) ); break;
		case JOINTNODE: getJointNode( offset, stop, pstop, new PssgJointNode().set( ct, p ) ); break;
		case RENDERNODE: getRenderNode( offset, stop, pstop, new PssgRenderNode().set( ct, p ) ); break;
		case SKINNODE: getSkinNode( offset, stop, pstop, new PssgSkinNode().set( ct, p ) ); break;
		case MODIFIERNETWORKINSTANCE: getModifierNetworkInstance( offset, stop, pstop, new PssgModifierNetworkInstance().set( ct, p ) ); break;
		case RENDERSTREAMINSTANCE: getRenderStreamInstance( offset, stop, pstop, new PssgRenderStreamInstance().set( ct, p ) ); break;
		case RENDERINSTANCESOURCE: getRenderInstanceSource( offset, stop, pstop, new PssgRenderInstanceSource().set( ct, p ) ); break;
		case SKINJOINT: getSkinJoint( offset, stop, pstop, new PssgSkinJoint().set( ct, p ) ); break;
		case TRANSFORM: getTransform( offset, stop, pstop, (PssgSceneNode<?>)p ); break;
		case BOUNDINGBOX: getBoundingBox( offset, stop, pstop, (PssgSceneNode<?>)p ); break;
		
		default:
			System.out.println( cStrings[type] + " at " + offset + " not yet implemented." + size + " " + psize + " " + stop );
			break;
		}
		
		if( position() != stop )
			System.out.println( "[WARNING] chunk " + cStrings[type] + " ended early. " + position() + " " + stop );
		position( stop );
	}
	
	private void getChildChunks( int pstop, int stop, PssgChunk<?> chunk ) {
		position( pstop );
		while( position() < stop )
			processChunk( position(), chunk );
	}
	
	private String readString() {
		return readString( readInt32() );
	}
	
	private String readPString() {
		readInt32( 2 );
		return readString();
	}
	
	private int readPInt32() {
		readInt32( 2 );
		return readInt32();
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
		processChunk( position(), null );
		
		cPssg.print( debug );
		debug.closeStream();
		ModelContainer mc = new ModelContainer();
		mc.startScene( new Node( "nope" ) );
		return mc;
	}
		
		// parse animations if it was an animation file
		//PssgAnimation.channelRefs contains PssgChannelRef
			//PssgChannelRef.targetName = BONE IN MAIN MODEL
			//PssgChannelRef.channel refers to an animation channel by it's id
				//PssgAnimationChannel points to the animation data blocks:
					//PssgAnimationChannel.timeBlock = PssgAnimationDataBlock.id
					//PssgAnimationChannel.valueBlock = PssgAnimationDataBlock.id
		//PssgAnimation.constantChannels contains PssgConstantChannel
			//PssgConstantChannel depicts items within the animation that do not change during the course of the animation
		/*cPssg.root.print( debug, "" );
		//debug.writeLine();
		//debug.writeLine( "skin nodes: " + skinNodes );
		//debug.writeLine( "joint nodes: " + jointNodes );
		//debug.writeLine( "skin joints: " + skinJoints );
		//debug.writeLine( "render nodes: " + renderNodes );
		cPssg.printAnimations( debug );
		debug.closeStream();
		ModelContainer mc = new ModelContainer();
		ModelScene scene = mc.startScene( new Node( cPssg.root.id ) );
		
		for( PssgNode n : cPssg.root.children )
			addToScene( n, scene.getRoot() );
		
		scene.ready();
		return mc;
	}

	private void addToScene( PssgNode pn, Node n ) {
		Node node = null;
		if( pn instanceof PssgSkinNode ) node = new SkinNode( pn.id );
		if( pn instanceof PssgRenderNode ) node = new RenderNode( pn.id );
		else node = new Node( pn.id );
		n.add( node );
		
		for( PssgNode pnc : pn.children )
			addToScene( pnc, node );
	}*/
	
	@Override
	public void clear() {
		//TODO: clear all data used by this decoder
	}
}
