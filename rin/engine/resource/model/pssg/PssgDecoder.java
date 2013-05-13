package rin.engine.resource.model.pssg;

import static rin.engine.resource.model.pssg.PssgSpec.*;

import java.util.HashMap;

import rin.engine.resource.Directory;
import rin.engine.resource.Resource;
import rin.engine.resource.image.ImageContainer;
import rin.engine.resource.image.dds.DdsUtils;
import rin.engine.resource.model.ModelContainer;
import rin.engine.resource.model.ModelDecoder;
import rin.engine.resource.model.ModelOptions;
import rin.engine.util.ArrayUtils;
import rin.engine.util.binary.ProfiledBinaryReader;
import rin.gl.TextureManager;
import rin.gl.lib3d.Animation;
import rin.gl.lib3d.FrameSet;
import rin.gl.lib3d.JointNode;
import rin.gl.lib3d.Node;
import rin.gl.lib3d.RenderNode;
import rin.gl.lib3d.SkinNode;

public class PssgDecoder extends ProfiledBinaryReader implements ModelDecoder {

	private boolean DEBUG = false;
	
	private HashMap<Integer, ChunkType> chunkMap = new HashMap<Integer, ChunkType>();
	String[] cStrings;
	String[] pStrings;
	
	private ModelContainer mc;
	private Pssg cPssg;
	private Node cNode;
	
	private void header() {
		boolean valid = true;
		for( int i = 0; i < MAGIC.length; i++ )
			valid &= readChar() == MAGIC[i];
		if( !valid ) exitWithError( "Invalid PSSG Header." );
		
		readInt32(); // size, but for some reason does not match...
		
		pStrings = new String[readInt32()];
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
	}

	private void getPssgDatabase( int offset, int stop, int pstop, PssgDatabase chunk ) {
		if( cPssg == null ) {
			mc = new ModelContainer();
			cPssg = new Pssg();
			cPssg.database = chunk;
		}
		
		getChildChunks( pstop, stop, cPssg.database );
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
		chunk.data = readInt8( chunk.size );
		
		/*if( chunk.texelFormat.toUpperCase().equals( "DXT1" ) ) {
			DdsUtils.fromRawDXT1( chunk.width, chunk.height, chunk.data ).test();
		} else if( chunk.texelFormat.toUpperCase().equals( "DXT3" ) ) {
			DdsUtils.fromRawDXT3( chunk.width, chunk.height, chunk.data ).test();
		}*/
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
	
	private void getShaderGroup( int offset, int stop, int pstop, PssgShaderGroup chunk ) {
		readPInt32(); //parameterCount
		readPInt32(); //parameterSavedCount
		readPInt32(); //parameterStreamCount
		readPInt32(); //instancesRequireSorting
		readPInt32(); //defaultRenderSortPriority
		readPInt32(); //passCount
		chunk.id = readPString();
		
		getChildChunks( pstop, stop, chunk );
		cPssg.cache( chunk );
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
	
	private void getShaderInput( int offset, int stop, int pstop, PssgShaderInput chunk ) {
		readPInt32(); //parameterID
		chunk.type = readPString();
		if( chunk.type.equals( "texture" ) ) {
			readPString();
			chunk.texture = readPString().substring( 1 );
		}
		
	}
	
	private void getShaderInstance( int offset, int stop, int pstop, PssgShaderInstance chunk ) {
		chunk.shaderGroup = readPString().substring( 1 );
		readPInt32(); //parameterCount
		readPInt32(); //parameterSavedCount
		readPInt32(); //renderSortPriority
		chunk.id = readPString();
		cPssg.cache( chunk );
	}
	
	private void getRootNode( int offset, int stop, int pstop, PssgRootNode chunk ) {
		readPInt32();
		chunk.id = readPString();
		
		mc.startScene( new Node( chunk.id ) );
		cNode = mc.getScene().getRoot();
		cNode.setScale( 0.01f, 0.01f, 0.01f );
		cNode.setRotation( 90, 0, 0 );
		getChildChunks( pstop, stop, chunk );
		cPssg.cache( chunk );
	}
	
	private void getNode( int offset, int stop, int pstop, PssgNode chunk ) {
		readPInt32();
		readPString();
		chunk.id = readPString();
		Node tmp = cNode;
		
		cNode = cNode.add( new Node( chunk.id ) );
		getChildChunks( pstop, stop, chunk );
		cNode = tmp;
		cPssg.cache( chunk );
	}
	
	private void getJointNode( int offset, int stop, int pstop, PssgJointNode chunk ) {
		readPInt32();
		readPString();
		chunk.id = readPString();
		Node tmp = cNode;
		
		cNode = cNode.add( new JointNode( chunk.id ) );
		getChildChunks( pstop, stop, chunk );
		cNode = tmp;
		cPssg.cache( chunk );
	}
	
	private void getRenderNode( int offset, int stop, int pstop, PssgRenderNode chunk ) {
		readPInt32();
		readPString();
		chunk.id = readPString();
		Node tmp = cNode;
		
		cNode = cNode.add( new RenderNode( chunk.id ) );
		getChildChunks( pstop, stop, chunk );
		cNode = tmp;
		cPssg.cache( chunk );
	}
	
	private void getSkinNode( int offset, int stop, int pstop, PssgSkinNode chunk ) {
		chunk.jointCount = readPInt32();
		chunk.skeleton = readPString().substring( 1 );
		readPInt32();
		readPString();
		chunk.id = readPString();
		Node tmp = cNode;
		
		cNode = cNode.add( new SkinNode( chunk.id ) );
		getChildChunks( pstop, stop, chunk );
		cNode = tmp;
		cPssg.cache( chunk );
	}
	
	private void getSkinJoint( int offset, int stop, int pstop, PssgSkinJoint chunk ) {
		chunk.joint = readPString().substring( 1 );
		((SkinNode)cNode).addJoint( (JointNode)mc.getScene().find( chunk.joint ) );
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
	
	private void getRenderDataSource( int offset, int stop, int pstop, PssgRenderData chunk ) {
		readPInt32(); //streamCount
		int ptype = readInt32();
		readInt32();
		if( pStrings[ptype].equalsIgnoreCase( "id" ) ) {
			chunk.id = readString();
		} else {
			readString();
			chunk.id = readPString();
		}
		
		getChildChunks( pstop, stop, chunk );
		cPssg.cache( chunk );
	}
	
	private void getRenderIndexSource( int offset, int stop, int pstop, PssgRenderData chunk ) {
		chunk.primitive = readPString();
		chunk.format = readPString();
		chunk.count = readPInt32();
		readPInt32(); //allocationStrategy
		readPString(); //id, havnt found a use for this one
		
		getChildChunks( pstop, stop, chunk );
	}
	
	private void getIndexSourceData( int offset, int stop, int pstop, PssgRenderData chunk ) {
		if( chunk.format.equals( "uchar" ) ) {
			chunk.data = readUInt8( stop - position() );
		} else if( chunk.format.equals( "ushort" ) ) {
			chunk.data = readInt16( (stop - position()) / 2 );
		}
	}
	
	private void getRenderStream( int offset, int stop, int pstop, PssgRenderStream chunk ) {
		chunk.dataBlock = readPString().substring( 1 );
		readPInt32(); //subStream
		chunk.id = readPString();
	}
	
	private void getTransform( int offset, int stop, int pstop, PssgSceneNode<?> chunk ) {
		chunk.transform = readFloat32( 16 );
		
		cNode.setBaseMatrix( chunk.transform );
	}
	
	private void getBoundingBox( int offset, int stop, int pstop, PssgSceneNode<?> chunk ) {
		chunk.bbox = readFloat32( 6 );
	}
	
	private void getSkeleton( int offset, int stop, int pstop, PssgSkeleton chunk ) {
		readPInt32(); //matrixCount
		chunk.id = readPString();
		
		getChildChunks( pstop, stop, chunk );
		cPssg.cache( chunk );
	}
	
	private void getInverseBindMatrix( int offset, int stop, int pstop, PssgInverseBindMatrix chunk ) {
		chunk.matrix = readFloat32( 16 );
	}
	
	private void getAnimation( int offset, int stop, int pstop, PssgAnimation chunk ) {
		chunk.channelCount = readPInt32();
		chunk.constantChannelCount = readPInt32();
		chunk.start = readPFloat32();
		chunk.end = readPFloat32();
		chunk.id = readPString();
		
		getChildChunks( pstop, stop, chunk );
		cPssg.cache( chunk );
	}
	
	private void getChannelRef( int offset, int stop, int pstop, PssgChannelRef chunk ) {
		chunk.channel = readPString().substring( 1 );
		chunk.targetName = readPString();
		//if( cPssg.getSceneNode( chunk.targetName ) == null )
			//System.out.println( chunk.targetName + " target not found." );
	}
	
	private void getConstantChannel( int offset, int stop, int pstop, PssgConstantChannel chunk ) {
		chunk.value = readPFloat32s();
		chunk.targetName = readPString();
		chunk.keyType = readPString();
	}
	
	private void getAnimationChannel( int offset, int stop, int pstop, PssgAnimationChannel chunk ) {
		chunk.timeBlock = readPString().substring( 1 );
		chunk.valueBlock = readPString().substring( 1 );
		chunk.id = readPString();
		
		cPssg.cache( chunk );
	}
	
	private void getAnimationDataBlock( int offset, int stop, int pstop, PssgAnimationDataBlock chunk ) {
		chunk.keyCount = readPInt32();
		chunk.keyType = readPString();
		chunk.id = readPString();
		
		getChildChunks( pstop, stop, chunk );
		cPssg.cache( chunk );
	}
	
	private void getKeys( int offset, int stop, int pstop, PssgAnimationDataBlock chunk ) {
		chunk.data = readFloat32( (stop - position()) / 4 );
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
		case SHADERINSTANCE: getShaderInstance( offset, stop, pstop, new PssgShaderInstance().set( ct, p ) ); break;
		case SHADERGROUP: getShaderGroup( offset, stop, pstop, new PssgShaderGroup().set( ct, p ) ); break;
		case SHADERINPUT: getShaderInput( offset, stop, pstop, new PssgShaderInput().set( ct, p ) ); break;
		
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
		
		case RENDERDATASOURCE: getRenderDataSource( offset, stop, pstop, new PssgRenderData().set( ct, p ) ); break;
		case RENDERINDEXSOURCE: getRenderIndexSource( offset, stop, pstop, (PssgRenderData)p ); break;
		case INDEXSOURCEDATA: getIndexSourceData( offset, stop, pstop, (PssgRenderData)p ); break;
		case RENDERSTREAM: getRenderStream( offset, stop, pstop, new PssgRenderStream().set( ct, p ) ); break;
		
		case SKELETON: getSkeleton( offset, stop, pstop, new PssgSkeleton().set( ct, p ) ); break;
		case INVERSEBINDMATRIX: getInverseBindMatrix( offset, stop, pstop, new PssgInverseBindMatrix().set( ct, p ) ); break;
		
		case ANIMATION: getAnimation( offset, stop, pstop, new PssgAnimation().set( ct, p ) ); break;
		case CHANNELREF: getChannelRef( offset, stop, pstop, new PssgChannelRef().set( ct, p ) ); break;
		case CONSTANTCHANNEL: getConstantChannel( offset, stop, pstop, new PssgConstantChannel().set( ct, p ) ); break;
		case ANIMATIONCHANNEL: getAnimationChannel( offset, stop, pstop, new PssgAnimationChannel().set( ct, p ) ); break;
		case ANIMATIONCHANNELDATABLOCK: getAnimationDataBlock( offset, stop, pstop, new PssgAnimationDataBlock().set( ct, p ) ); break;
		case KEYS: getKeys( offset, stop, pstop, (PssgAnimationDataBlock)p ); break;
		default:
			//if( DEBUG )
				System.out.println( cStrings[type] + " at " + offset + " not yet implemented." + size + " " + psize + " " + stop );
			break;
		}
		
		if( position() != stop && DEBUG )
			System.out.println( "[WARNING] chunk " + cStrings[type] + " ended early. " + position() + " " + stop );
		position( stop );
	}
	
	private void printProperties( int pstop ) {
		while( position() < pstop ) {
			System.out.println( pStrings[readInt32()] );
			advance( readInt32() );
		}
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
	
	private float readPFloat32() {
		readInt32();
		if( readInt32() != 4 ) {
			System.out.println( "Exiting due to incorrect value being read." );
			System.exit( 0 );
		}
		return readFloat32();
	}
	
	private float[] readPFloat32s() {
		readInt32();
		int amount = readInt32();
		if( !(amount % 4 == 0) ) {
			System.out.println( "PssgDecoder#readPFloat32s(): amount was not a multiple of four. wrote data type." );
			System.exit( 0 );
		}
		return readFloat32( amount / 4 );

	}
	
	@Override
	public String getExtensionName() { return "pssg"; }
	
	private Resource debug;
	
	@Override
	public ModelContainer decode( Resource resource, ModelOptions opts ) {
		startProfiler();
		
		load( resource );
		Directory dir = resource.getDirectory();
		debug = dir.createResource( resource.getBaseName() + ".debug", true );
		
		header();
		stringMap();
		processChunk( position(), null );
		
		if( dir.containsResource( resource.getBaseName() + "_anim1.pssg" ) ) {
			Resource anim = dir.getResource( resource.getBaseName() + "_anim1.pssg" );
			System.err.println( "SEX" );
			load( anim );
			header();
			stringMap();
			processChunk( position(), null );
		}
		
		debug.openStream();
		if( DEBUG ) cPssg.print( debug );
		
		for( PssgSceneNode<?> sn : cPssg.sceneNodeMap.values() ) {
			if( sn instanceof PssgSkinNode ) {
				System.out.println( "SKINNODE " + sn.id );
				PssgSkinNode skinNode = (PssgSkinNode)sn;
				SkinNode node = (SkinNode)mc.getScene().find( sn.id );
				for( int i = 0; i < skinNode.jointCount; i++ ) {
					PssgInverseBindMatrix ibm = (PssgInverseBindMatrix)cPssg.skeletonMap.get( skinNode.skeleton ).children.get( i );
					node.getJoints().get( i ).setInverseBindMatrix( ibm.matrix );
				}
				for( PssgModifierNetworkInstance mni : sn.find( PssgModifierNetworkInstance.class ) ) {
					PssgTexture tex = cPssg.getTextureFromShaderInstance( mni.shader );
					if( tex != null ) {
						if( TextureManager.find( tex.filename ) == -1 ) {
							ImageContainer ic = null;
							if( tex.texelFormat.toUpperCase().equals( "DXT1" ) ) {
								ic = DdsUtils.fromRawDXT1( tex.width, tex.height, tex.data );
							} else if( tex.texelFormat.toUpperCase().equals( "DXT3" ) ) {
								ic = DdsUtils.fromRawDXT3( tex.width, tex.height, tex.data );
							} else System.out.println( "UNKNOWN TEXTURE FORMAT " + tex.texelFormat );
							if( ic != null ) {
								ic.setName( tex.filename );
								((SkinNode)mc.getScene().find( sn.id )).setTexture( ic );
							}
						} else {
							node.setTexture( TextureManager.find( tex.filename ) );
						}
					}
					for( PssgRenderInstanceSource ris : mni.find( PssgRenderInstanceSource.class ) ) {
						PssgRenderData rd = cPssg.renderDataMap.get( ris.source );
						System.out.println( "  Data source: " + rd.id );
						System.out.println( "  data length: " + rd.data.length );
						node.setIndices( rd.data );
						for( PssgRenderStream rs : rd.find( PssgRenderStream.class ) ) {
							PssgDataBlock db = cPssg.dataBlockMap.get( rs.dataBlock );
							System.out.println( "    " + db.dataType + " " + db.renderType + " " + db.id );
							if( db.renderType.equalsIgnoreCase( "skinnablevertex" ) ) {
								node.setVertices( db.fdata );
							} else if( db.renderType.equalsIgnoreCase( "st" ) ) {
								node.setTexcoords( db.fdata );
							} else if( db.renderType.equalsIgnoreCase( "skinindices" ) ) {
								node.setBoneIndices( db.sdata );
							} else if( db.renderType.equalsIgnoreCase( "skinweights" ) ) {
								node.setBoneWeights( db.fdata );
							}
						}
					}
				}
				node.build();
				//node.setScale( 0.01f, 0.01f, 0.01f );
				//node.setRotation( 90.0f, 0.0f, 0.0f );
			} else if( sn instanceof PssgRenderNode ) {
				PssgRenderNode rn = (PssgRenderNode)sn;
				for( PssgModifierNetworkInstance mni : rn.find( PssgModifierNetworkInstance.class ) ) {
					
				}
			}
		}
		mc.getScene().ready();
		mc.getScene().setScale( 0.01f, 0.01f, 0.01f );
		mc.getScene().setRotation( 90.0f, 0.0f, 0.0f );
		
		for( PssgAnimation a : cPssg.animationMap.values() ) {
			Animation current = mc.getScene().addAnimation( a.id );
			current.setTimes( a.start, a.end );
			for( PssgChunk<?> c : a.children ) {
				if( c instanceof PssgChannelRef ) {
					PssgChannelRef node = (PssgChannelRef)c;
					if( mc.getScene().find( node.targetName ) != null ) {
						FrameSet fs = current.getFrames( node.targetName );
						PssgAnimationChannel ac = cPssg.channelMap.get( node.channel );
						float[] times = cPssg.animationDataMap.get( ac.timeBlock ).data;
						PssgAnimationDataBlock db = cPssg.animationDataMap.get( ac.valueBlock );
						if( db.keyType.toLowerCase().equals( "rotation" ) ) {
							fs.setRotationData( times, db.data );
						} else if( db.keyType.toLowerCase().equals( "scale" ) ) {
							fs.setScaleData( times, db.data );
						} else if( db.keyType.toLowerCase().equals( "translation" ) ) {
							fs.setTranslationData( times, db.data );
						} else System.err.println( "unknown keytype: " + db.keyType );
					}
					//debug.writeLine( "  TIMES: " + ArrayUtils.asString( cPssg.animationDataMap.get( ac.timeBlock ).data ) );
					//debug.writeLine( "  VALUES: " + ArrayUtils.asString( cPssg.animationDataMap.get( ac.valueBlock ).data ) );
				} else if( c instanceof PssgConstantChannel ) {
					PssgConstantChannel node = (PssgConstantChannel)c;
					FrameSet fs = current.getFrames( node.targetName );
					if( node.keyType.toLowerCase().equals( "rotation" ) ) {
						fs.setRotationData( null, node.value );
					} else if( node.keyType.toLowerCase().equals( "scale" ) ) {
						fs.setScaleData( null, node.value );
					} else if( node.keyType.toLowerCase().equals( "translation" ) ) {
						fs.setTranslationData( null, node.value );
					} else System.err.println( "unknown keytype: " + node.keyType );
				}
			}
		}
		debug.closeStream();
		//mc.startScene( new Node( "nope" ) );
		
		stopProfiler();
		System.out.println( "Decoded in " + getProfileTimeS() + " seconds." );
		cPssg = null;
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
