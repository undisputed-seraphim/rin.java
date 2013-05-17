package rin.engine.resource.model.ism2;

import static rin.engine.resource.model.ism2.Ism2Spec.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import rin.engine.resource.Directory;
import rin.engine.resource.FormatManager;
import rin.engine.resource.Resource;
import rin.engine.resource.image.ImageContainer;
import rin.engine.resource.model.ModelContainer;
import rin.engine.resource.model.ModelDecoder;
import rin.engine.resource.model.ModelOptions;
import rin.engine.resource.model.Surface;
import rin.engine.util.ArrayUtils;
import rin.engine.util.binary.ProfiledBinaryReader;
import rin.engine.view.gl.GLSkinNode;
import rin.engine.view.lib3d.Animation;
import rin.engine.view.lib3d.Frame;
import rin.engine.view.lib3d.UniversalActor;
import rin.engine.view.lib3d.JointNode;

public class Ism2Decoder extends ProfiledBinaryReader implements ModelDecoder {
	
	private boolean DEBUG = true;
	
	private int chunkCount;
	private TreeMap<Integer, Integer> chunkOffsets = new TreeMap<Integer, Integer>();
	private HashMap<Integer, JointNode> jointMap = new HashMap<Integer, JointNode>();
	private HashMap<String, JointNode> boneToJoint = new HashMap<String, JointNode>();
	private ArrayList<String> boneMap = new ArrayList<String>();
	private ArrayList<float[]> poseMap = new ArrayList<float[]>();
	private String[] stringMap;
	
	private Ism2Model cModel;
	private UniversalActor cActor;
	private JointNode cJoint;
	
	private JointNode cSkelRoot;
	private GLSkinNode cMeshRoot;
	private ArrayList<String> skinNodes = new ArrayList<String>();
	
	private String cAnimationName;
	private Animation cAnimation;
	private Frame cFrame;
	private int cFrameType;
	
	private String cSampler;
	//private Surface cSurface;
	//public Ism2Model getData() { return cModel; }
	private Ism2VertexData cVertexData;
	
	private Ism2Texture cTexture;
	//private Ism2KeyFrame cFrame;
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
	}
	
	private void chunkList() {
		debug( "CHUNKLIST" );
		chunkOffsets.clear();
		
		for( int i = 0; i < chunkCount; i++ ) {
			int id = readInt32();
			int offset = readInt32();
			chunkOffsets.put( offset, id );
		}
		//debug.writeLine( ArrayUtils.asString( chunkOffsets ) );
		
		for( int i : chunkOffsets.keySet() )
			processChunk( i, " " );
	}
	
	private void getStrings( int offset, int hsize, String tab ) {
		//System.out.println( "strings at " + offset + " " + hsize );
		debug( tab + "STRINGS" );
		
		int[] offsets = getOffsets( readInt32() );		
		stringMap = new String[offsets.length];
		for( int i = 0; i < offsets.length; i++ ) {
			position( offsets[i] );
			stringMap[i] = readString();
		}
		
		if( cModel == null ) {
			cModel = new Ism2Model();
			cActor = mc.getActor();
		}
		debug( tab + "::Strings: " + ArrayUtils.asString( stringMap ) );
	}
	
	private void getTextureList( int offset, int hsize, String tab ) {
		//System.out.println( "texture list at " + offset + " " + hsize );
		debug( tab + "TEXTURELIST" );
		
		int[] offsets = getOffsets( readInt32() );
		cModel.textures = new Ism2Texture[offsets.length];
		for( int i = 0; i < offsets.length; i++ ) {
			processChunk( offsets[i], tab + " " );
			cModel.textures[i] = cTexture;
		}
	}
	
	private void getTexture( int offset, int hsize, String tab ) {
		//System.out.println( "texture at " + offset + " " + hsize );
		debug( tab + "TEXTURE" );
		
		cTexture = new Ism2Texture();
		cTexture.data[0] = stringMap[ hsize ];
		debug( tab + "::data0: " + cTexture.data[0] );
		for( int i = 1; i < 7; i++ ) {
			cTexture.data[i] = stringMap[ readInt32() ];
			debug( tab + "::data"+i+": " + cTexture.data[i] );
		}
		cModel.textureMap.put( cTexture.data[0], cTexture.data[3].substring( 0, cTexture.data[3].indexOf( "." ) ) );
	}
	
	private void getSamplerList( int offset, int hsize, String tab ) {
		debug( tab + "SAMPLERLIST" );
		
		int[] offsets = getOffsets( readInt32() );
		for( int i : offsets )
			processChunk( i, tab + " " );
	}
	
	private void getSampler( int offset, int hsize, String tab ) {
		debug( tab + "SAMPLER" );
		int count = readInt32();
		
		cSampler = stringMap[readInt32()];
		debug( tab + "::id: " + cSampler );
		debug( tab + "::?: " + stringMap[readInt32()] );
		debug( tab + "::fx: " + stringMap[readInt32()] );
		debug( tab + "::?: " + stringMap[readInt32()] );
		
		int[] offsets = getOffsets( count );
		for( int i : offsets )
			processChunk( i, tab + " " );
	}
	
	private void getSampleList( int offset, int hsize, String tab ) {
		debug( tab + "SAMPLELIST" );
		
		int[] offsets = getOffsets( readInt32() );
		for( int i : offsets )
			processChunk( i, tab + " " );
	}
	
	private void getSample( int offset, int hsize, String tab ) {
		debug( tab + "SAMPLE" );
		int count = readInt32();
		
		debug( tab + "::id: " + stringMap[readInt32()] );
		debug( tab + "::?: " + stringMap[readInt32()] );
		
		int[] offsets = getOffsets( count );
		for( int i : offsets )
			processChunk( i, tab + " " );
	}
	
	private void getSampleProperty( int offset, int hsize, String tab ) {
		debug( tab + "SAMPLEPROPERTY" );
		int count = readInt32();
		int type = readInt32();
		int amount = readInt32();
		
		debug( tab + "::type: " + type );
		debug( tab + "::amount: " + amount );
		debug( tab + "::?: " + readInt32() );
		
		int[] offsets = getOffsets( count );
		switch( type ) {
		case 2: case 6:
			for( int i : offsets )
				processChunk( i, tab + " " );
			break;
		case 21:
			position( offsets[0] );
			if( cModel.samplerToTexture.get( cSampler ) == null )
				cModel.samplerToTexture.put( cSampler, stringMap[readInt32()] );
			debug( tab + "::texture: " + cModel.samplerToTexture.get( cSampler ) );
			break;
		default:
			debug( tab + " [UNIMPLEMENTED] sample property type " + type );
			break;
		}
	}
	
	private void getFxList( int offset, int hsize, String tab ) {
		debug( tab + "FXLIST" );
		
		int[] offsets = getOffsets( readInt32() );
		for( int i : offsets )
			processChunk( i, tab + " " );
	}
	
	private void getFx( int offset, int hsize, String tab ) {
		debug( tab + "FX" );
		int count = readInt32();
		
		debug( tab + "::id: " + stringMap[readInt32()] );
		debug( tab + "::?: " + readInt32() );
		debug( tab + "::?: " + readInt32() );
		debug( tab + "::?: " + readInt32() );
		debug( tab + "::?: " + readInt32() );
		debug( tab + "::?: " + readInt32() );
		debug( tab + "::?: " + readInt32() );
		debug( tab + "::?: " + readInt32() );
		
		int[] offsets = getOffsets( count );
		for( int i : offsets )
			processChunk( i, tab + " " );
	}
	
	private void getFxProperty( int offset, int hsize, String tab ) {
		debug( tab + "FXPROPERTY" );
		int count = readInt32();
		debug( tab + "::?: " + readInt32() );
		
		int[] offsets = getOffsets( count );
		for( int i : offsets ) {
			position( i );
			debug( tab + " ::?: " + readInt32() );
			debug( tab + " ::?: " + readInt32() );
			int subcount = readInt32();
			debug( tab + " ::?: " + readInt32() );
			for( int j = 0; j < subcount; j++ ) {
				debug( tab + "  " + "::?: " + readFloat32() );
			}
		}
	}
	
	private void getSkinList( int offset, int hsize, String tab ) {
		//System.out.println( "SKINLIST at " + offset + " " + hsize );
		debug( tab + "SKINLIST" );
		
		int count = readInt32();
		debug( tab + "::?: " + readInt32() ); //unknown
		debug( tab + "::?: " + readInt32() ); //unknown
		
		int[] offsets = getOffsets( count );
		for( int i : offsets )
			processChunk( i, tab + " " );
	}
	
	private void getSkin( int offset, int hsize, String tab ) {
		//System.out.println( "SKIN at " + offset + " " + hsize );
		debug( tab + "SKIN" );
		
		int count = readInt32();
		debug( tab + "::?: " + stringMap[ readInt32() ] );
		debug( tab + "::?: " + stringMap[ readInt32() ] );
		
		int[] offsets = getOffsets( count );
		for( int i : offsets )
			processChunk( i, tab + " " );
	}
	
	private void getSkeleton( int offset, int hsize, String tab ) {
		//System.out.println( "SKELETON at " + offset + " " + hsize );
		debug( tab + "SKELETON" );
		
		int count = readInt32();
		String mesh = stringMap[ readInt32() ];
		cTransform = new Ism2TransformData( mesh );
		readInt32(); //unknown
		float[] t = readFloat32( 16 );
		debug( tab + "::Mesh: " + mesh );
		debug( tab + "::transform: " + ArrayUtils.asString( t ) );
		
		int[] offsets = getOffsets( count );
		for( int i : offsets )
			processChunk( i, tab + " " );
		
		// after skeleton is read, give the joints their default bind pose matrices
		for( int i = 0; i < boneMap.size(); i++ ) {
			boneToJoint.get( boneMap.get( i ) ).setInverseBindMatrix( poseMap.get( i ) );
		}
	}
	
	private void getTransformData( int offset, int hsize, String tab ) {
		//System.out.println( "skinning at " + offset + " " + hsize );
		debug( tab + "TRANSFORMDATA" );
		
		cTransform.count = readInt32();
		readInt32(); //TODO: unknown
		cTransform.type = readInt32();
		cTransform.stride = readInt32();
		readInt32(); //TODO: unknown
		readInt32(); //TODO: unknown
		debug( tab + "::type: " + cTransform.type );
		debug( tab + "::stride: " + cTransform.stride );
		int count = cTransform.count / cTransform.stride;
		
		switch( cTransform.type ) {
		case T_TRANSFORM_BONE:
			for( int i = 0; i < count; i++ )
				boneMap.add( stringMap[ readInt16() ] );
			break;
		case T_TRANSFORM_MATRIX:
			for( int i = 0; i < count; i++ )
				poseMap.add( readFloat32( cTransform.stride ) );
			break;
		case T_TRANSFORM_FRAME:
			float[] time = new float[count];
			float[][] data = new float[count][cTransform.stride-1];
			//cTransform.time = new float[count];
			//cTransform.data = new float[count][cTransform.stride-1];
			for( int i = 0; i < count; i++ ) {
				time[i] = readFloat16();
				//debug( tab + "::time: " + cTransform.time[i] );
				for( int j = 0; j < cTransform.stride - 1; j++ )
					data[i][j] = readFloat16();
				//debug( tab + "::data: " + ArrayUtils.asString( cTransform.data[i] ) );
			}
			switch( cFrameType ) {
			case T_FRAME_TRANSLATE: cFrame.setTranslateData( time, data ); break;
			case T_FRAME_ROTATEX: cFrame.setRotateXData( time, data ); break;
			case T_FRAME_ROTATEY: cFrame.setRotateYData( time, data ); break;
			case T_FRAME_ROTATEZ: cFrame.setRotateZData( time, data ); break;
			default:
				System.out.println( "UNKNOWN ANIMATION FRAME TYPE: " + cFrameType );
				break;
			}
			break;
		default:
			exitWithError( "UNKOWN transform TYPE " + cTransform.type );
			break;
		}
	}
	
	private void getMeshList( int offset, int hsize, String tab ) {
		//System.out.println( "MESHLIST at " + offset + " " + hsize );
		debug( tab + "MESHLIST" );
		int count = readInt32();
		
		cModel.vdata = new Ism2VertexData[ count ];
		int[] offsets = getOffsets( count );
		for( int i = 0; i < offsets.length; i++ ) {
			cModel.vdata[ i ] = new Ism2VertexData();
			cVertexData = cModel.vdata[ i ];
			processChunk( offsets[i], tab + " " );
			System.out.println( cVertexData.v.length );
			mc.setVertices( cVertexData.v );
			mc.setNormals( cVertexData.n );
			mc.setTexcoords( cVertexData.t );
		}
	}
	
	private void getMesh( int offset, int hsize, String tab ) {
		//System.out.println( "MESH at " + offset + " " + hsize );
		debug( tab + "MESH" );
		int count = readInt32();
		String id = stringMap[readInt32()];
		debug( tab + "::id: " + id );
		debug( tab + "::?: " + stringMap[ readInt32() ] );
		advance( 3 * 4 ); //unknown
		
		// create the root render node for this mesh
		cMeshRoot = new GLSkinNode( id );
		cActor.getMesh().add( cMeshRoot );
		
		int[] offsets = getOffsets( count );
		for( int i : offsets )
			processChunk( i , tab + " " );
		
		// add obtained vertices to mesh root
		cMeshRoot.setVertices( cVertexData.v );
		cMeshRoot.setNormals( cVertexData.n );
		cMeshRoot.setTexcoords( cVertexData.t );
		cMeshRoot.setBoneIndices( cVertexData.b );
		cMeshRoot.setBoneWeights( cVertexData.w );
	}
	
	private Ism2VertexInfo getVertexInfo( int offset ) {
		position( offset );
		Ism2VertexInfo res = new Ism2VertexInfo();
		res.type = readInt32();
		res.count = readInt32();
		res.vtype = readInt32();
		res.vsize = readInt32();
		res.voffset = readInt32();
		res.offset = readInt32();
		
		return res;
	}
	
	private void getVertices( int offset, int hsize, String tab ) {
		//System.out.println( "vertices at " + offset + " " + hsize );
		debug( tab + "VERTICES" );
		
		int count = readInt32();
		int vtype = readInt32();
		int verts = readInt32(); //4
		int bytes = readInt32();
		debug( tab + "::?: " + readInt32() );
		debug( tab + "::vertex type: " + vtype + " " + hsize );
		
		int[] offsets = getOffsets( count );
		int start = 0;
		Ism2VertexInfo[] types = new Ism2VertexInfo[count];
		for( int i = 0; i < offsets.length; i++ ) {
			types[i] = getVertexInfo( offsets[i] );
			debug( tab + "::VINFO["+i+"] " + types[i].toString() );
			if( types[i].type == T_VERTEX_NORMAL ) cVertexData.normaled = true;
			start = types[i].offset;
		}

		position( start );
		switch( vtype ) {
		case T_VERTICES_VERTEX:
			cVertexData.v = new float[ verts * 3 ];
			if( cVertexData.normaled ) cVertexData.n = new float[ verts * 3 ];
			for( int i = 0; i < verts; i++ ) {
				for( int j = 0; j < types.length; j++ ) {
					position( start + i * bytes + types[j].voffset );
					switch( types[j].type ) {
					
					case T_VERTEX_POSITION:
						cVertexData.v[ i*3 ] = readFloat32();
						cVertexData.v[ i*3+1 ] = readFloat32();
						cVertexData.v[ i*3+2 ] = readFloat32();
						break;
						
					case T_VERTEX_NORMAL:
						cVertexData.n[ i*3 ] = readFloat16();
						cVertexData.n[ i*3+1 ] = readFloat16();
						cVertexData.n[ i*3+2 ] = readFloat16();
						break;
						
					case T_VERTEX_3:
						//i assume these are RGBA colors, float16
						break;
						
					case T_VERTEX_14:
						//no clue what these are either
						break;
						
					case T_VERTEX_15:
						//no clue what these are for yet
						break;
						
					default:
						System.err.println( "Unknown vertex type: " + types[j].type );
						break;
					}
				}
			}
			break;
		case T_VERTICES_TEXCOORD:
			cVertexData.t = new float[ verts * 2 ];
			for( int i = 0; i < verts; i++ ) {
				cVertexData.t[i*2] = readFloat16();
				cVertexData.t[i*2+1] = readFloat16();
			}
			break;
		case T_VERTICES_WEIGHT:
			cVertexData.b = new float[ verts * 4 ];
			cVertexData.w = new float[ verts * 4 ];
			for( int i = 0; i < verts; i++ ) {
				for( int j = 0; j < types.length; j++ ) {
					position( start + i * types[j].vsize + types[j].voffset );
					switch( types[j].type ) {
					
					case T_WEIGHT_WEIGHT:
						cVertexData.w[ i*4 ] = readFloat32();
						cVertexData.w[ i*4+1 ] = readFloat32();
						cVertexData.w[ i*4+2 ] = readFloat32();
						cVertexData.w[ i*4+3 ] = readFloat32();
						break;
						
					case T_WEIGHT_BONE:
						cVertexData.b[ i*4 ] = readInt16();
						cVertexData.b[ i*4+1 ] = readInt16();
						cVertexData.b[ i*4+2 ] = readInt16();
						cVertexData.b[ i*4+3 ] = readInt16();
						break;
						
					default:
						System.out.println( "Unknown w type " + types[j].type );
						break;
					}
				}
			}
			break;	
		default:
			System.out.println( "UNKNOWN VERTEX TYPE: " + vtype );
			break;
		}
	}
	
	private void getTriangles( int offset, int hsize, String tab ) {
		//System.out.println( "triangles at " + offset + " " + hsize );
		debug( tab + "TRIANGLES" );
		
		int count = readInt32();
		String material = stringMap[ readInt32() ];
		String texture = "";
		String sampler = cModel.materialToSampler.get( material );
		if( sampler != null ) {
			String tex = cModel.samplerToTexture.get( sampler );
			if( tex != null ) {
				texture = cModel.textureMap.get( tex );
			} else texture = "";
		} else texture = "";
		skinNodes.add( texture );
		readInt32( 2 ); //TODO: unknown
		readInt32();
		debug( tab + "::texture: " + texture );
		
		int[] offsets = getOffsets( count );
		for( int i : offsets )
			processChunk( i, tab + " " );
	}
	
	private void getIndices( int offset, int hsize, String tab ) {
		//System.out.println( "indices at " + offset + " " + hsize );
		debug( tab + "INDICES" );
		int count = readInt32();
		int type = readInt32();
		debug( tab + "::?: " + readInt32() );
		int[] in = new int[count];
		
		switch( type ) {
		
		case 5:
			System.out.println( "index type 5" );
			in = readUInt16( count );
			break;
			
		case 7:
			System.out.println( "index type 7" );
			for( int i = 0; i < count / 3; i++ ) {
				System.exit( 0 );
			}
			break;
			
		default:
			System.out.println( "UNKNOWN INDEX TYPE: " + type );
			break;
		}
		
		GLSkinNode skin = new GLSkinNode( skinNodes.get( skinNodes.size() - 1 ) );
		skin.setIndices( in );
		
		// add the needed joints to the skin node
		for( int i = 0; i < in.length; i++ )
			for( int j = 0; j < 4; j++ )
				if( cVertexData.w[in[i]*4+j] != 0.0f )
					skin.addJoint( boneToJoint.get( boneMap.get( (int)cVertexData.b[in[i]*4+j] ) ), (int)cVertexData.b[in[i]*4+j] );
		
		cMeshRoot.add( skin );
		/*int[] tmp = cSurface.getIndices();
		ArrayList<String> tmp2 = new ArrayList<String>();
		
		for( int i = 0; i < tmp.length; i++ ) {
			for( int j = 0; j < 4; j++ )
				if( cVertexData.w[tmp[i]*4+j] != 0.0f ) {
					if( !tmp2.contains( boneMap.get( (int)cVertexData.b[tmp[i]*4+j] ) ) )
						tmp2.add( boneMap.get( (int)cVertexData.b[tmp[i]*4+j] ) );
				}
		}
		debug( tab + "::" + tmp2.size() + " bones for this set of indices: " + ArrayUtils.asString( tmp2 ) );*/
	}
	
	private void getBoundingBox( int offset, int hsize, String tab ) {
		//System.out.println( "BOUNDINGBOX at " + offset + " " + hsize );
		debug( tab + "BOUNDINGBOX" );
		debug( tab + "::?: " + readInt32() ); //unknown
		debug( tab + "::?: " + readInt32() ); //unknown
		debug( tab + "::minX: " + readFloat32() );
		debug( tab + "::minY: " + readFloat32() );
		debug( tab + "::minZ: " + readFloat32() );
		debug( tab + "::?: " + readFloat32() ); //unknown
		debug( tab + "::maxX: " + readFloat32() );
		debug( tab + "::maxY: " + readFloat32() );
		debug( tab + "::maxZ: " + readFloat32() );
		debug( tab + "::?: " + readFloat32() ); //unknown
	}
	
	private void getC3( int offset, int hsize, String tab ) {
		debug( tab + "C3 ["+toHex(3)+"]" );
		System.out.println( "c3 at " + offset + " " + hsize );
		int count = readInt32();
		System.out.println( stringMap[ readInt32() ] );
		System.out.println( stringMap[ readInt32() ] );
		
		int[] offsets = getOffsets( count );
		for( int i : offsets )
			processChunk( i, tab + " " );
	}
	
	private void getC4( int offset, int hsize, String tab ) {
		//System.out.println( "c4 at " + offset + " " + hsize );
		debug( tab + "C4 ["+toHex(4)+"]" );
		
		int count = readInt32();
		debug( tab + "::?: " + stringMap[ readInt32() ] );
		debug( tab + "::?: " + stringMap[ readInt32() ] );
		debug( tab + "::?: " + readInt32() );
		debug( tab + "::?: " + readInt32() );
		debug( tab + "::?: " + readInt32() );
		debug( tab + "::?: " + readInt32() );
		debug( tab + "::?: " + readInt32() );
		debug( tab + "::?: " + readInt32() );
		debug( tab + "::?: " + readInt32() );
		debug( tab + "::?: " + readInt32() );
		debug( tab + "::?: " + readInt32() );
		debug( tab + "::?: " + readInt32() );
		debug( tab + "::?: " + readInt32() );
		//advance( 4 * 11 );
		
		int[] offsets = getOffsets( count );
		for( int i : offsets )
			processChunk( i, tab + " " );
	}
	
	private void getC5( int offset, int hsize, String tab ) {
		debug( tab + "C5 ["+toHex(5)+"]" + " " + offset );
		int count = readInt32();
		String id = stringMap[ readInt32() ];
		debug( tab + "::id: " + stringMap[ readInt32() ] );
		String bone = stringMap[ readInt32() ];
		debug( tab + "::?: " + readInt32() );
		int poffset = readInt32();
		debug( tab + "::parent bone offset: " + poffset );
		debug( tab + "::child bones: " + readInt32() );
		debug( tab + "::?: " + readInt32() );
		debug( tab + "::?: " + readInt32() );
		debug( tab + "::?: " + readInt32() );
		debug( tab + "::?: " + readInt32() );
		debug( tab + "::?: " + readInt32() );
		debug( tab + "::?: " + readInt32() );
		debug( tab + "::?: " + readInt32() );
		
		// start the model scene if it's the first joint, otherwise add to poffset joint
		JointNode joint = new JointNode( id );
		jointMap.put( offset, joint );
		boneToJoint.put( bone, joint );
		if( cSkelRoot == null ) cSkelRoot = cActor.getSkeleton().add( joint );
		else jointMap.get( poffset ).add( joint );
		cJoint = joint;
		
		int[] offsets = getOffsets( count );
		for( int i : offsets )
			processChunk( i, tab + " " );
		
		cJoint.finish();
	}
	
	private void getC91( int offset, int hsize, String tab ) {
		debug( tab + "C91 ["+toHex(91)+"]" + " " + offset );

		int[] offsets = getOffsets( readInt32() );
		for( int i : offsets ) {
			/*position( i );
			int type = readInt32();
			String prop = stringMap[ readInt32() ];
			
			switch( type ) {
			case 20:
				debug( tab + "::translate: " + ArrayUtils.asString( readFloat32( 3 ) ) );
				break;
			default:
				debug( tab + "::" + prop + ": " + "[UNIMPLEMENTED] " + type );
				break;
			}*/
			processChunk( i, tab + " " );
		}
		debug( tab + "C91-END " + position() );
	}
	
	private void getC92( int offset, int hsize, String tab ) {
		debug( tab + "C92 ["+toHex(92)+"]" );
		
		//int[] offsets = getOffsets( readInt32() );
		//  for( int i : offsets )
		//	processChunk( i, tab + " " );
		debug( tab + "C92-END " + position() );
	}
	
	// translate
	private void getC20( int offset, int hsize, String tab ) {
		debug( tab + "C20 ["+toHex(20)+"]" + stringMap[ hsize ] + " " + offset );
		cJoint.setJointTranslation( readFloat32( 3 ) );
	}
	
	// jointOrientX
	private void getC103( int offset, int hsize, String tab ) {
		debug( tab + "C103 ["+toHex(103)+"]" + stringMap[ hsize ] + " " + offset );
		cJoint.setJointRotateX( readFloat32( 4 ) );
	}
	
	// jointOrientY
	private void getC104( int offset, int hsize, String tab ) {
		debug( tab + "C104 ["+toHex(104)+"]" + stringMap[ hsize ] + " " + offset );
		cJoint.setJointRotateY( readFloat32( 4 ) );
	}
	
	// jointOrientZ
	private void getC105( int offset, int hsize, String tab ) {
		debug( tab + "C105 ["+toHex(105)+"]" + stringMap[ hsize ] + " " + offset );
		cJoint.setJointRotateZ( readFloat32( 4 ) );
	}
	
	private void getC114( int offset, int hsize, String tab ) {
		debug( tab + "C114 ["+toHex(114)+"]" + stringMap[ hsize ] + " " + offset );
	}
	
	private void getC115( int offset, int hsize, String tab ) {
		debug( tab + "C115 ["+toHex(115)+"]" + stringMap[ hsize ] + " " + offset );
	}
	
	private void getC116( int offset, int hsize, String tab ) {
		debug( tab + "C116 ["+toHex(116)+"]" + stringMap[ hsize ] + " " + offset );
	}
	
	private void getC117( int offset, int hsize, String tab ) {
		debug( tab + "C117 ["+toHex(117)+"]" + stringMap[ hsize ] + " " + offset );
	}
	
	private void getC118( int offset, int hsize, String tab ) {
		debug( tab + "C118 ["+toHex(118)+"]" + stringMap[ hsize ] + " " + offset );
	}
	
	private void getC119( int offset, int hsize, String tab ) {
		debug( tab + "C119 ["+toHex(119)+"]" + stringMap[ hsize ] + " " + offset );
	}
	
	private void getC122( int offset, int hsize, String tab ) {
		debug( tab + "C122 ["+toHex(122)+"]" + stringMap[ hsize ] + " " + offset );
	}
	
	private void getC123( int offset, int hsize, String tab ) {
		debug( tab + "C123 ["+toHex(123)+"]" + stringMap[ hsize ] + " " + offset );
	}
	
	private void getC124( int offset, int hsize, String tab ) {
		debug( tab + "C124 ["+toHex(124)+"]" + stringMap[ hsize ] + " " + offset );
	}
	
	private void getC125( int offset, int hsize, String tab ) {
		debug( tab + "C125 ["+toHex(125)+"]" + stringMap[ hsize ] + " " + offset );
	}
	
	private void getC126( int offset, int hsize, String tab ) {
		debug( tab + "C126 ["+toHex(126)+"]" + stringMap[ hsize ] + " " + offset );
	}
	
	private void getMaterialList( int offset, int hsize, String tab ) {
		//System.out.println( "MATERIALLIST at " + offset + " " + hsize );
		debug( tab + "MATERIALLIST" );
		
		int count = readInt32();
		debug( tab + "::skin: " + stringMap[ readInt32() ] );
		debug( tab + "::?: " + "unknown: " + readInt32() );
		debug( tab + "::?: " + readInt32() );
		
		int[] offsets = getOffsets( count );
		for( int i : offsets )
			processChunk( i, tab + " " );
	}
	
	private void getMaterial( int offset, int hsize, String tab ) {
		//System.out.println( "MATERIAL at " + offset + " " + hsize );
		debug( tab + "MATERIAL" );
		
		int count = readInt32();
		String id = stringMap[readInt32()];
		String sampler = stringMap[readInt32()];
		cModel.materialToSampler.put( id, sampler );
		readInt32(); //unknown, always 0
		debug( tab + "::id: " + id );
		debug( tab + "::sampler: " + sampler );
		
		int[] offsets = getOffsets( count );
		for( int i : offsets ) {
			position( i );
			debug( tab + "::unknown: " + readInt32() );
			debug( tab + "::unknown: " + readInt32() );
			debug( tab + "::unknown: " + readInt32() );
			debug( tab + "::?: " + stringMap[ readInt32() ] );
			debug( tab + "::?: " + stringMap[ readInt32() ] );
			debug( tab + "::unknown: " + readInt32() );
			debug( tab + "::unknown: " + readInt32() );
		}
	}
	
	private void getAnimation( int offset, int hsize, String tab ) {
		debug( tab + "ANIMATION" );
		cAnimation = cActor.getSkeleton().addAnimation( cAnimationName );
		
		int count = readInt32();
		debug( tab + "::?: " + readInt32() );
		debug( tab + "::?: " + readInt32() );
		debug( tab + "::?: " + readInt32() );
		debug( tab + "::?: " + readInt32() );
		debug( tab + "::?: " + readInt32() );
		
		int[] offsets = getOffsets( count );
		for( int i : offsets )
			processChunk( i, tab + " " );
	}
	
	private void getAnimationFrame( int offset, int hsize, String tab ) {
		debug( tab + "ANIMATIONFRAME" );
		int count = readInt32();
		String frame = stringMap[readInt32()];
		debug( tab + "::frame: " + frame );
		debug( tab + "::?: " + readInt32() );
		debug( tab + "::?: " + readInt32() );
		debug( tab + "::?: " + readInt32() );
		debug( tab + "::?: " + readInt32() );
		//cFrame = cAnimation.addFrame( frame );
		System.out.println( frame );
		cFrame = cAnimation.addFrame( cActor.getSkeleton().find( frame ) );
		
		int[] offsets = getOffsets( count );
		for( int i : offsets )
			processChunk( i, tab + " " );
	}
	
	private void getFrameTransform( int offset, int hsize, String tab ) {
		debug( tab + "FRAMETRANSFORM" );
		debug( tab + "::?: " + readInt32() );
		String id = stringMap[ readInt32() ];
		debug( tab + "::id: " + id );
		debug( tab + "::?: " + readInt32() );
		debug( tab + "::?: " + readInt32() );
		debug( tab + "::?: " + readInt32() );
		debug( tab + "::?: " + readInt32() );
		debug( tab + "::?: " + readInt32() );
		debug( tab + "::?: " + readInt32() );
		debug( tab + "::?: " + stringMap[readInt32()] );
		debug( tab + "::?: " + readInt32() );
		cFrameType = readInt32();
		debug( tab + "::?: " + readInt32() );
		debug( tab + "::?: " + readInt32() );
		debug( tab + "::?: " + readInt32() );
		//cTransform = cFrame.addTransform( mesh );
		
		position( offset + hsize );
		processChunk( position(), tab + " " );
	}
	
	private void processChunk( int offset, String tab ) {
		position( offset );
		int type = readInt32();
		int hsize = readInt32();
		
		switch( type ) {
		
		case C_STRINGS: getStrings( offset, hsize, tab ); break;
		case C_TEXTURELIST: getTextureList( offset, hsize, tab ); break;
		case C_TEXTURE: getTexture( offset, hsize, tab ); break;
		
		case C_SAMPLERLIST: getSamplerList( offset, hsize, tab ); break;
		case C_SAMPLER: getSampler( offset, hsize, tab ); break;
		case C_SAMPLELIST: getSampleList( offset, hsize, tab ); break;
		case C_SAMPLE: getSample( offset, hsize, tab ); break;
		case C_SAMPLEPROPERTY: getSampleProperty( offset, hsize, tab ); break;
		
		case C_FXLIST: getFxList( offset, hsize, tab ); break;
		case C_FX: getFx( offset, hsize, tab ); break;
		case C_FXPROPERTY: getFxProperty( offset, hsize, tab ); break;
		
		case C_SKINLIST: getSkinList( offset, hsize, tab ); break;
		case C_SKIN: getSkin( offset, hsize, tab ); break;
		case C_SKELETON: getSkeleton( offset, hsize, tab ); break;
		case C_TRANSFORMDATA: getTransformData( offset, hsize, tab ); break;
		
		case C_MESHLIST: getMeshList( offset, hsize, tab ); break;
		case C_MESH: getMesh( offset, hsize, tab ); break;
		case C_TRIANGLES: getTriangles( offset, hsize, tab ); break;
		case C_INDICES: getIndices( offset, hsize, tab ); break;
		case C_VERTICES: getVertices( offset, hsize, tab ); break;
		case C_BOUNDINGBOX: getBoundingBox( offset, hsize, tab ); break;
		
		case C_3: getC3( offset, hsize, tab ); break;
		case C_4: getC4( offset, hsize, tab ); break;
		case C_5: getC5( offset, hsize, tab ); break;
		case C_91: getC91( offset, hsize, tab ); break;
		case C_92: getC92( offset, hsize, tab ); break;
		case C_20: getC20( offset, hsize, tab ); break;
		case C_103: getC103( offset, hsize, tab ); break;
		case C_104: getC104( offset, hsize, tab ); break;
		case C_105: getC105( offset, hsize, tab ); break;
		case C_114: getC114( offset, hsize, tab ); break;
		case C_115: getC115( offset, hsize, tab ); break;
		case C_116: getC116( offset, hsize, tab ); break;
		case C_117: getC117( offset, hsize, tab ); break;
		case C_118: getC118( offset, hsize, tab ); break;
		case C_119: getC119( offset, hsize, tab ); break;
		case C_122: getC122( offset, hsize, tab ); break;
		case C_123: getC123( offset, hsize, tab ); break;
		case C_124: getC124( offset, hsize, tab ); break;
		case C_125: getC125( offset, hsize, tab ); break;
		case C_126: getC126( offset, hsize, tab ); break;
		case C_MATERIALLIST: getMaterialList( offset, hsize, tab ); break;
		case C_MATERIAL: getMaterial( offset, hsize, tab ); break;
		
		case C_ANIMATION: getAnimation( offset, hsize, tab ); break;
		case C_ANIMATIONFRAME: getAnimationFrame( offset, hsize, tab ); break;
		case C_FRAMETRANSFORM: getFrameTransform( offset, hsize, tab ); break;
			
		default:
			System.out.println( "unknown chunk type: " + type + " [" + String.format( "0x%02x", type ) + "] at " + offset );
			debug( tab + String.format( "0x%02x", type ) + " [UNIMPLEMENTED]" );
		}
	}
	
	private Resource debug;
	private ModelContainer mc;
	
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
	
	private String toHex( int id ) {
		return "0x"+Integer.toHexString( id );
	}
	
	private void debug( String text ) {
		if( DEBUG ) debug.writeLine( text );
	}
	
	@Override
	public String getExtensionName() { return "ism2"; }
	
	@Override
	public ModelContainer decode( Resource resource, ModelOptions options ) {
		load( resource );
		
		Ism2Options opts = new Ism2Options();
		if( options != null )
			opts = (Ism2Options)options;
		
		mc = new ModelContainer();
		
		if( DEBUG ) {
			debug = resource.getDirectory().createResource( resource.getBaseName() + ".debug", true );
			debug.openStream();
		}
		
		header();
		chunkList();
		
		// construct model container
		Directory dir = resource.getDirectory();
		
		// check for and apply animations
		//if( opts.isAnimated() ) {
			// animations may be contained within a CL3 archive or as ism2 files
			System.out.println( "SEARCHING FOR ANIMATIONS IN " + opts.getAnimationDirectory() );
			if( dir.containsDirectory( opts.getAnimationDirectory() ) ) {
				Directory animDir = dir.getDirectory( opts.getAnimationDirectory() );
				if( animDir.containsResource( "001.ism2" ) ) {
					Resource anim = animDir.getResource( "001.ism2" );
					cAnimationName = anim.getBaseName();
					load( anim );
					
					header();
					chunkList();
				}
			}
		//}
		
		//check for and apply texture files
		if( dir.containsDirectory( opts.getTextureDirectory() ) ) {
			Directory textureDir = dir.getDirectory( opts.getTextureDirectory() );
			if( textureDir != null ) {
				for( String s : skinNodes ) {
					GLSkinNode sk = (GLSkinNode)cMeshRoot.find( s );
					String tex = sk.getId() + ".tid";
					if( textureDir.containsResource( tex ) ) {
						Resource texture = textureDir.getResource( tex );
						ImageContainer ic = FormatManager.decodeImage( texture );
						ic.setName( texture.getPath() );
						if( ic != null ) {
							sk.setTexture( ic );
						}
					}
				}
			}
		}
		
		if( DEBUG ) debug.closeStream();
		return mc;
	}
	
	@Override
	public void clear() {
		//TODO: clear all data used
	}

}
