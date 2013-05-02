package rin.engine.resource.model.ism2;

import static rin.engine.resource.model.ism2.Ism2Spec.*;

import java.util.TreeMap;

import rin.engine.resource.Resource;
import rin.engine.resource.model.ModelContainer;
import rin.engine.resource.model.ModelDecoder;
import rin.engine.resource.model.ModelOptions;
import rin.util.bio.BaseBinaryReader;

public class Ism2Decoder extends BaseBinaryReader implements ModelDecoder {
	
	private int chunkCount;
	private TreeMap<Integer, Integer> chunkOffsets = new TreeMap<Integer, Integer>();
	private String[] stringMap;
	
	private boolean isAnimation = false;
	private String name;
	
	private Ism2Model cModel;
	private Ism2VertexData cVertexData;
	
	private Ism2Texture cTexture;
	
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
		
		cModel = new Ism2Model();
		//debug.writeLine( "Strings: " + ArrayUtils.asString( stringMap ) );
	}
	
	private void getTextureList( int offset, int hsize ) {
		System.out.println( "texture list at " + offset + " " + hsize );
		
		int[] offsets = getOffsets( readInt32() );
		cModel.textures = new Ism2Texture[offsets.length];
		for( int i = 0; i < offsets.length; i++ ) {
			processChunk( offsets[i] );
			cModel.textures[i] = cTexture;
		}
	}
	
	private void getTexture( int offset, int hsize ) {
		System.out.println( "texture at " + offset + " " + hsize );
		cTexture = new Ism2Texture();
		cTexture.data[0] = stringMap[ hsize ];
		for( int i = 1; i < 7; i++ )
			cTexture.data[i] = stringMap[ readInt32() ];
	}
	
	private void getC11( int offset, int hsize ) {
		System.out.println( "c11 at " + offset + " " + hsize );
		int count = readInt32();
		
		cModel.vdata = new Ism2VertexData[ count ];
		int[] offsets = getOffsets( count );
		for( int i = 0; i < offsets.length; i++ ) {
			cModel.vdata[ i ] = new Ism2VertexData();
			cVertexData = cModel.vdata[ i ];
			processChunk( offsets[i] );
		}
	}
	
	private void getC10( int offset, int hsize ) {
		System.out.println( "c10 at " + offset + " " + hsize );
		int count = readInt32();
		System.out.println( stringMap[ readInt32() ] );
		System.out.println( stringMap[ readInt32() ] );
		advance( 3 * 4 );
		
		int[] offsets = getOffsets( count );
		for( int i : offsets )
			processChunk( i );
	}
	
	private void getTriangles( int offset, int hsize ) {
		System.out.println( "triangles at " + offset + " " + hsize );
		int count = readInt32();
		System.out.println( "unknown: " + stringMap[ readInt32() ] );
		readInt32( 2 ); //TODO: unknown
		int triangles = readInt32();
		
		int[] offsets = getOffsets( count );
		for( int i : offsets )
			processChunk( i );
	}
	
	private void getIndices( int offset, int hsize ) {
		System.out.println( "indices at " + offset + " " + hsize );
		int count = readInt32();
		System.out.println( "unknown: " + readInt32() );
		readInt32(); //TODO: unknown
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
		
		//if( DEBUG ) System.out.println( res );
		return res;
	}
	
	private void getVertices( int offset, int hsize ) {
		System.out.println( "vertices at " + offset + " " + hsize );
		int count = readInt32();
		int vtype = readInt32();
		int verts = readInt32(); //4
		int bytes = readInt32();
		
		int[] offsets = getOffsets( count );
		int start = 0;
		Ism2VertexInfo[] types = new Ism2VertexInfo[count];
		for( int i = 0; i < offsets.length; i++ ) {
			types[i] = getVertexInfo( offsets[i] );
			//if( types[i].type == T_VERTEX_NORMAL ) cVertexData.normaled = true;
			start = types[i].offset;
		}

		position( start );
		switch( vtype ) {
		
		case T_VERTICES_VERTEX:
			cVertexData.v = new float[ verts * 3 ];
			for( int i = 0; i < verts; i++ ) {
				for( int j = 0; j < types.length; j++ ) {
					System.out.println( start + " " + i + " " + types[j].vsize + " " + types[j].vsize );
					position( start + i * types[j].vsize + types[j].voffset );
					switch( types[j].type ) {
					
					case T_VERTEX_POSITION:
						cVertexData.v[ i*3 ] = readFloat32();
						cVertexData.v[ i+1 ] = readFloat32();
						cVertexData.v[ i*3+2 ] = readFloat32();
						break;
						
					case T_VERTEX_NORMAL:
						debug.writeLine( "normal" );
						debug.writeLine( readInt16() / 65535.0f );
						debug.writeLine( readInt16() / 65535.0f );
						debug.writeLine( readInt16() / 65535.0f );
						debug.writeLine();
						break;
						
					case T_VERTEX_3:
						debug.writeLine( "vertex 3 " + types[j].count );
						debug.writeLine( readInt16() / 65535.0f );
						debug.writeLine( readInt16() / 65535.0f );
						debug.writeLine( readInt16() / 65535.0f );
						debug.writeLine( readInt16() / 65535.0f );
						debug.writeLine();
						break;
						
					case T_VERTEX_14:
						debug.writeLine( "vertex 14 " + types[j].count );
						debug.writeLine( readInt16() / 65535.0f );
						debug.writeLine( readInt16() / 65535.0f );
						debug.writeLine( readInt16() / 65535.0f );
						debug.writeLine( readInt16() / 65535.0f );
						debug.writeLine();
						break;
						
					default:
						debug.writeLine( "TYPE " + types[j].type + ": size " + types[j].vsize + " offset " + types[j].voffset + " count " + types[j].count + " " + types[j].vtype );
						/*debug.writeLine( readFloat32() );
						debug.writeLine( readFloat32() );
						debug.writeLine( readFloat32() );
						debug.writeLine( readFloat32() );*/
						debug.writeLine();
						//System.out.println( "Unknown v type " + types[j].type + " count " + types[j].count + " " + bytes );
						break;
					}
				}
			}
			break;
			
		case T_VERTICES_TEXCOORD:
			System.out.println( "texcoord... but for what? " + bytes + " " + types.length );
			break;
			
		case T_VERTICES_WEIGHT:
			for( int i = 0; i < verts; i++ ) {
				for( int j = 0; j < types.length; j++ ) {
					position( start + i * types[j].vsize + types[j].voffset );
					switch( types[j].type ) {
					
					case T_WEIGHT_WEIGHT:
						/*debug.writeLine( "type 1" + " " + types[j].count );
						debug.writeLine( readFloat32() );
						debug.writeLine( readFloat32() );
						debug.writeLine( readFloat32() );
						debug.writeLine( readFloat32() );
						debug.writeLine();*/
						break;
						
					case T_WEIGHT_BONE:
						/*debug.writeLine( "type 7" + " " + types[j].count );
						debug.writeLine( readInt16() );
						debug.writeLine( readInt16() );
						debug.writeLine( readInt16() );
						debug.writeLine( readInt16() );
						debug.writeLine();*/
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
	
	private void getC3( int offset, int hsize ) {
		System.out.println( "c3 at " + offset + " " + hsize );
		int count = readInt32();
		System.out.println( stringMap[ readInt32() ] );
		System.out.println( stringMap[ readInt32() ] );
		
		int[] offsets = getOffsets( count );
		for( int i : offsets )
			processChunk( i );
	}
	
	private void getC4( int offset, int hsize ) {
		System.out.println( "c4 at " + offset + " " + hsize );
		int count = readInt32();
		System.out.println( stringMap[ readInt32() ] );
		System.out.println( stringMap[ readInt32() ] );
		advance( 4 * 11 );
		
		int[] offsets = getOffsets( count );
		for( int i : offsets )
			processChunk( i );
	}
	
	private void getC50( int offset, int hsize ) {
		System.out.println( "c50 at " + offset + " " + hsize );
		int count = readInt32();
		readInt32( 2 ); //TODO: unknown
		
		int[] offsets = getOffsets( count );
		for( int i : offsets )
			processChunk( i );
	}
	
	private void getC49( int offset, int hsize ) {
		System.out.println( "c49 at " + offset + " " + hsize );
		int count = readInt32();
		System.out.println( stringMap[ readInt32() ] );
		System.out.println( stringMap[ readInt32() ] );
		
		int[] offsets = getOffsets( count );
		for( int i : offsets )
			processChunk( i );
	}
	
	private void getC48( int offset, int hsize ) {
		System.out.println( "c48 at " + offset + " " + hsize );
		int count = readInt32();
		String n1 = stringMap[ readInt32() ];
		String n2 = stringMap[ readInt32() ];
		cTransform = new Ism2TransformData( n1, n2 );
		for( int i = 0; i < 16; i++ )
			System.out.println( readFloat32() );
		
		int[] offsets = getOffsets( count );
		for( int i : offsets )
			processChunk( i );
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
		
		case T_TRANSFORM_INDEX:
			//debug.writeLine( "shorts [" + stride + "]: " + ArrayUtils.asString( readInt16( count ) ) );
			for( int i = 0; i < count; i++ )
				System.out.println( stringMap[ readInt16() ] );
			break;
			
		case T_TRANSFORM_MATRIX:
			for( int i = 0; i < count; i++ )
				readFloat32( cTransform.stride );
			break;
			
		case T_TRANSFORM_FRAME:
			System.out.println( "THIS SHOULD BE ANIMATION ONLY?" );
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
		case C_TEXTURE: getTexture( offset, hsize ); break;
		
		case C_11: getC11( offset, hsize ); break;
		case C_10: getC10( offset, hsize ); break;
		case C_TRIANGLES: getTriangles( offset, hsize ); break;
		case C_INDICES: getIndices( offset, hsize ); break;
		case C_VERTICES: getVertices( offset, hsize ); break;
		
		//case C_3: getC3( offset, hsize ); break;
		//case C_4: getC4( offset, hsize ); break;
		case C_50: getC50( offset, hsize ); break;
		case C_49: getC49( offset, hsize ); break;
		//case C_48: getC48( offset, hsize ); break;
		case C_TRANSFORM_DATA: getTransformData( offset, hsize ); break;
		
		case C_ANIMATION: getAnimation( offset, hsize ); break;
		case C_ANIMATION_FRAME: getAnimationFrame( offset, hsize ); break;
		case C_FRAME_TRANSFORM: getFrameTransform( offset, hsize ); break;
			
		default:
			System.out.println( "unknown chunk type: " + type + " [" + String.format( "0x%02x", type ) + "] at " + offset );
		}
	}
	
	private Resource debug;
	
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
	
	@Override
	public String getExtensionName() { return "ism2"; }
	
	@Override
	public ModelContainer decode( Resource resource, ModelOptions options ) {
		load( resource );
		//Ism2Options opts = (Ism2Options)options;
		name = resource.getBaseName();
		debug = resource.getDirectory().createResource( resource.getBaseName() + ".debug", true );
		debug.openStream();
		
		header();
		chunkList();

		//TODO: check for texture files
		
		// construct model container
		ModelContainer mc = new ModelContainer();
		
		// check for animations
		//TODO: add this check to the options object for ism2
		if( !isAnimation ) {
			System.out.println( "SEARCHING FOR ANIMATIONS" );
			
			//TODO: add 'animation directory' to options
		} else System.out.println( cAnimation.frames.size() );
		
		debug.closeStream();
		return mc;
	}
	
	@Override
	public void clear() {
		//TODO: clear all data used
	}

}
