package rin.engine.resource.model.gmo;

import static rin.engine.resource.model.gmo.GmoSpec.*;

import java.nio.ByteBuffer;

import rin.engine.resource.Resource;
import rin.engine.resource.model.ModelContainer;
import rin.engine.resource.model.ModelDecoder;
import rin.engine.resource.model.ModelOptions;
import rin.util.Buffer;
import rin.util.bio.BinaryReader;

public class GmoDecoder extends BinaryReader implements ModelDecoder {
	
	private GMO gmo;
	private SubFile cSubFile;
	
	private VertexBias cVertexBias;
	private TextureBias cTextureBias;
	
	private Surface cSurface;
	private Mesh cMesh;
	private Vertices cVertices;
	
	private Texture cTexture;
	private Material cMaterial;
	
	private Skeleton cSkeleton;
	private Animation cAnimation;
	
	private Resource debug;
	
	public GMO getData() {
		return gmo;
	}
	
	private void header() {
		//System.out.println( "GMO file: " + length() );
		
		// validate header using MAGIC
		boolean valid = true;
		for( int i = 0; i < MAGIC.length; i++ )
			valid &= readChar() == MAGIC[i];
		
		if( !valid ) exitWithError( "Not a valid GMO Header." );
	}
	
	private void getRoot( int offset, short hsize, int size ) {
		//System.out.println( "root at " + offset + " " + size );
		advance( 8 );
		gmo = new GMO( getName() );
		
		position( offset + hsize );
		while( position() < (offset + size) )
			processChunk( position() );
		
		//System.out.println( "root " + gmo.name + " finished." );
	}
	
	private void getSub( int offset, short hsize, int size ) {
		//System.out.println( "sub at " + offset + " " + size + " " + ( (offset + hsize) - position() ) );
		advance( 8 );
		gmo.files.add( new SubFile( getName() ) );
		cSubFile = gmo.files.get( gmo.files.size() - 1 );
		cSkeleton = cSubFile.skeleton;
		
		position( offset + hsize );
		while( position() < (offset + size) )
			processChunk( position() );
		
		//System.out.println( "subfile " + cSubFile.name + " finished." );
	}
	
	private void getBias( int offset, short hsize, int size ) {
		//System.out.println( "bias at " + offset + " " + size + " " + hsize );
		int type = readInt32();
		switch( type ) {
		
		case T_BIAS_VERTEX:
			cSubFile.vbias = new VertexBias();
			cVertexBias = cSubFile.vbias;
			cVertexBias.bx = readFloat32();
			cVertexBias.by = readFloat32();
			cVertexBias.bz = readFloat32();
			cVertexBias.sx = readFloat32();
			cVertexBias.sy = readFloat32();
			cVertexBias.sz = readFloat32();
			break;
			
		case T_BIAS_TEXTURE:
			cSubFile.tbias = new TextureBias();
			cTextureBias = cSubFile.tbias;
			cTextureBias.bu = readFloat32();
			cTextureBias.bv = readFloat32();
			cTextureBias.su = readFloat32();
			cTextureBias.sv = readFloat32();
			break;
			
		default:
			System.out.println( "UNKNOWN bias type: " + type );
			break;
		}
	}
	
	private void getBone( int offset, short hsize, int size ) {
		//System.out.println( "bones at " + offset + " " + size + " " + ( (offset + hsize) - position() ) + " " + (16 * 4) );
		advance( 8 );
		cSkeleton.bones.add( new Bone( getName() ) );

		position( offset + hsize );
		while( position() < (offset + size) )
			processChunk( position() );
	}
	
	private void getBoneParent( int offset, short hsize, int size ) {
		//System.out.println( "bone reference at " + offset + " " + size );
		cSkeleton.bones.get( cSkeleton.bones.size() - 1 ).parent = readHex(); readInt8( 3 );
	}
	
	private void getBoneLinks( int offset, short hsize, int size ) {
		//System.out.println( "bone references at " + offset + " " + size + " " + hsize );
		cSkeleton.refs = new int[ readInt32() ];
		for( int i = 0; i < cSkeleton.refs.length; i++ ) {
			cSkeleton.refs[i] = readHex(); readInt8( 3 );
		}
	}
	
	private void getBoneMatrices( int offset, short hsize, int size ) {
		//System.out.println( "bone matrices at " + offset + " " + size + " " + hsize );
		cSkeleton.mats = new float[readInt32()][16];
		for( int i = 0; i < cSkeleton.mats.length; i++ )
			cSkeleton.mats[i] = readFloat32( 16 );
	}
	
	private void getBoneTranslation( int offset, short hsize, int size ) {
		//System.out.println( "bone translation at " + offset + " " + size + " " + hsize );
		cSkeleton.bones.get( cSkeleton.bones.size() - 1 ).tran = readFloat32( 3 );
	}
	
	private void getBoneQuaternion( int offset, short hsize, int size ) {
		//System.out.println( "bone quaternion at " + offset + " " + size + " " + hsize );
		cSkeleton.bones.get( cSkeleton.bones.size() - 1 ).quat = readFloat32( 4 );
	}
	
	private void getBoneSurface( int offset, short hsize, int size ) {
		//System.out.println( "bone surface at " + offset + " " + size + " " + hsize );
		cSkeleton.bones.get( cSkeleton.bones.size() - 1 ).surface = readHex(); readInt8( 3 );
	}
	
	private void getSurface( int offset, short hsize, int size ) {
		//System.out.println( "surface at " + offset + " " + size + " " + hsize );
		advance( 8 );
		cSubFile.surfaces.add( new Surface( getName() ) );
		cSurface = cSubFile.surfaces.get( cSubFile.surfaces.size() - 1 );
		
		position( offset + hsize );
		while( position() < (offset + size) )
			processChunk( position() );
		
		// construct surface meshes
		for( Mesh m : cSurface.meshes ) {
			Vertices tmp = cSurface.vertices.get( m.vertices );
			int tv = 0, tt = 0;
			m.v = new float[ m.i.length * 3 ];
			m.t = new float[ m.i.length * 2 ];
			for( int i = 0; i < m.i.length; i++ ) {
				switch( m.type ) {
				
				case T_MESH_TRI:
					m.v[ tv++ ] = applyBias( tmp.v[ m.i[i] * 3 ], cVertexBias.sx, cVertexBias.bx );
					m.v[ tv++ ] = applyBias( tmp.v[ m.i[i] * 3 + 1 ], cVertexBias.sy, cVertexBias.by );
					m.v[ tv++ ] = applyBias( tmp.v[ m.i[i] * 3 + 2 ], cVertexBias.sz, cVertexBias.bz );
					break;
					
				case T_MESH_QUAD:
					m.v[ tv++ ] = applyBias( tmp.v[ m.i[i] * 3 ], cVertexBias.sx, cVertexBias.bx );
					m.v[ tv++ ] = applyBias( tmp.v[ m.i[i] * 3 + 1 ], cVertexBias.sy, cVertexBias.by );
					m.v[ tv++ ] = applyBias( tmp.v[ m.i[i] * 3 + 2 ], cVertexBias.sz, cVertexBias.bz );
					break;
				}
				
				m.t[ tt++ ] = applyBias( tmp.t[ m.i[i] * 2 ], cTextureBias.su, cTextureBias.bu );
				m.t[ tt++ ] = applyBias( tmp.t[ m.i[i] * 2 + 1 ], cTextureBias.sv, cTextureBias.bv );
			}
		}
		//System.out.println( "surface " + cSurface.name + " finished." );
	}
	
	private void getMesh( int offset, short hsize, int size ) {
		//System.out.println( "mesh at " + offset + " " + size + " " + hsize );
		advance( 8 );
		cSurface.meshes.add( new Mesh( getName() ) );
		cMesh = cSurface.meshes.get( cSurface.meshes.size() - 1 );
		
		position( offset + hsize );
		while( position() < (offset + size) )
			processChunk( position() );
		
		//System.out.println( "mesh " + cMesh.name + " finished." );
	}
	
	private void getMeshMaterial( int offset, short hsize, int size ) {
		//System.out.println( "mesh material at " + offset + " " + size + " " + hsize );
		cMesh.material = readHex(); readInt8( 3 );
	}
	
	private void getMeshWeights( int offset, short hsize, int size ) {
		//System.out.println( "mesh weights at " + offset + " " + size + " " + hsize );
		cMesh.weighted = new int[readInt32()];
		for( int i = 0; i < cMesh.weighted.length; i++ ) {
			cMesh.weighted[i] = readInt32();
			//System.out.println( "bone " + cSkeleton.bones.get( cMesh.weighted[i] ).name + " has weights." );
		}
	}
	
	private void getMeshIndices( int offset, short hsize, int size ) {
		//System.out.println( "mesh indices at " + offset + " " + size + " " + hsize );
		cMesh.vertices = readHex(); readInt8( 3 );
		cMesh.type = readHex();
		cMesh.indexed = readHex(); readInt8( 2 );
		cMesh.count = readInt32();
		cMesh.stride = readInt32();
		cMesh.i = new int[ cMesh.stride * cMesh.count ];
		switch( cMesh.indexed ) {
		
		case T_MESH_INDEXED:
			for( int i = 0; i < cMesh.count; i++ )
				for( int j = 0; j < cMesh.stride; j++ )
					cMesh.i[i+j] = readUInt16();
			break;
			
		default:
			//TODO: word describing first index, not sure what to do after
			break;
		}
		//System.out.println( "Mesh indices finished at " + position() );
	}
	
	private void getMeshWat( int offset, short hsize, int size ) {
		//System.out.println( "mesh wat at " + offset + " " + size + " " + hsize );
		//System.out.println( getName() + " " + ((offset+size)-position()));
		//position( offset + hsize );
		//while( position() < (offset + size) )
			//processChunk( position() );
		position( offset + size );
	}
	
	private void getVertices( int offset, short hsize, int size ) {
		//System.out.println( "vertices at " + offset + " " + size + " " + hsize );
		advance( 8 );
		cSurface.vertices.add( new Vertices( getName() ) );
		cVertices = cSurface.vertices.get( cSurface.vertices.size() - 1 );

		position( offset + hsize );
		int vtype = readInt32();
		cVertices.texelFormat = getFormatMask( vtype, 2, TEXEL_INDEX );
		cVertices.colorFormat = getFormatMask( vtype, 3, COLOR_INDEX );
		cVertices.normalFormat = getFormatMask( vtype, 2, NORMAL_INDEX );
		cVertices.vertexFormat = getFormatMask( vtype, 2, VERTEX_INDEX );
		cVertices.weightFormat = getFormatMask( vtype, 2, WEIGHT_INDEX );
		cVertices.indexFormat = getFormatMask( vtype, 2, INDEX_INDEX );
		cVertices.weightCount = getFormatMask( vtype, 3, NUMWEIGHTS_INDEX );
		cVertices.morphCount = getFormatMask( vtype, 3, NUMVERTS_INDEX );
		cVertices.bypass = getFormatMask( vtype, 1, BYPASS_INDEX );
		cVertices.count = readInt32();
		advance( 8 );
		
		int stride = cVertices.getStride();
		int start = offset + hsize + 8 + stride - 4; //TODO: suspicious
		if( cVertices.texelFormat != 0 )
			cVertices.t = new float[ cVertices.count * 2 ];
		cVertices.v = new float[ cVertices.count * 3 ];
		
		int tt = 0, tv = 0;
		for( int j = 0; j < cVertices.count; j++ ) {
			position( start + (j*stride) );
			cVertices.t[tt++] = readUInt16() / 65535.0f;
			cVertices.t[tt++] = readUInt16() / 65535.0f;
			
			readInt16();
			
			cVertices.v[tv++] = readInt16() / 65535.0f;
			cVertices.v[tv++] = readInt16() / 65535.0f;
			cVertices.v[tv++] = readInt16() / 65535.0f;
			
			/*for( int i = 0; i < cVertices.weightCount; i++ ) {
				System.out.println( " weight: " + readUInt16() );
			}*/
		}
	}
	
	private void getMaterial( int offset, short hsize, int size ) {
		//System.out.println( "material at " + offset + " " + size );
		advance( 8 );
		cSubFile.materials.add( new Material( getName() ) );
		cMaterial = cSubFile.materials.get( cSubFile.materials.size() - 1 );
		
		position( offset + hsize );
		while( position() < (offset + size) )
			processChunk( position() );
	}
	
	private void getTextureLayer( int offset, short hsize, int size ) {
		//System.out.println( "texture layer at " + offset + " " + size + " " + hsize );
		advance( 8 );
		getName();
		
		position( offset + hsize );
		while( position() < (offset + size) )
			processChunk( position() );
	}
	
	private void getTextureReference( int offset, short hsize, int size ) {
		//System.out.println( "texture reference at " + offset + " " + size + " " + hsize );
		cMaterial.texture = readHex(); readInt8( 3 );
	}
	
	private void getTexture( int offset, short hsize, int size ) {
		//System.out.println( "texture at " + offset + " " + size + " " + hsize );
		advance( 8 );
		cSubFile.textures.add( new Texture( getName() ) );
		cTexture = cSubFile.textures.get( cSubFile.textures.size() - 1 );
		
		position( offset + hsize );
		while( position() < (offset + size) )
			processChunk( position() );
	}
	
	private void getTextureFile( int offset, short hsize, int size ) {
		//System.out.println( "texture file at " + offset + " " + size + " " + hsize );
		cTexture.file = getName();
	}
	
	private void getTextureData( int offset, short hsize, int size ) {
		//System.out.println( "texture data at " + offset + " " + size + " " + hsize + " " + cTexture.file );
		int texSize = readInt32();
		//cTexture.data = readInt8( texSize );
		
		if( readChar() == 'M' && readChar() == 'I' && readChar() == 'G' && readChar() == '.' ) {
			char[] version = readChar( 4 );
			char[] format = readChar( 4 );
			advance( 4 );
			
			int w = 0, h = 0;
			
			int imgOffset = 0;
			int imgFormat = 0;
			int imgPixel = 0;
			int imgDepth = 0;
			short[] imgData8 = new short[0];
			
			int palOffset = 0;
			int palFormat = 0;
			int palColors = 0;
			short[] palData8 = new short[0];
			
			while( position() < (offset+size) ) {
				int begin = position();
				int section = readInt16(); advance( 6 );
				int secSize = readInt32(); advance( 4 );
				
				switch( section ) {
				
				case T_TEXTURE_GIM_IMG:
					imgOffset = position() + readInt16(); advance( 2 );
					imgFormat = readInt16();
					imgPixel = readInt16();
					w = readInt16();
					h = readInt16();
					imgDepth = readInt16();
					
					switch( imgFormat ) {
					
					case 0x05:
						position( imgOffset ); advance( 16 );
						imgData8 = readUInt8( (begin+secSize) - position() );
						break;
						
					default:
						System.err.println( "unimplemented gim image format!" );
						break;
					}
					break;
					
				case T_TEXTURE_GIM_PAL:
					palOffset = position() + readInt16(); advance( 2 );
					palFormat = readInt16(); advance( 2 );
					palColors = readInt16();
					
					switch( palFormat ) {
					
					case T_GIM_PAL_RGBA8888:
						position( palOffset ); advance( 16 );
						palData8 = readUInt8( (begin+secSize) - position() );
						break;
						
					default:
						System.err.println( "unimplemented gim pallete format!" );
						break;
					}
					break;
					
				default:
					//System.out.println( "unkown section: " + section );
					break;
				}
				position( begin + secSize );
			}
			
			//construct actual image data from palData and imgData
			cTexture.width = w;
			cTexture.height = h;
			cTexture.rawData = new short[ w * h * 4 ];
			for( int i = 0; i < w * h; i++ ) {
				cTexture.rawData[ i*4 ] = palData8[ imgData8[i] * 4 ];
				cTexture.rawData[ i*4+1 ] = palData8[ imgData8[i] * 4 + 1 ];
				cTexture.rawData[ i*4+2 ] = palData8[ imgData8[i] * 4 + 2 ];
				cTexture.rawData[ i*4+3 ] = palData8[ imgData8[i] * 4 + 3 ];
			}
		} else System.err.println( "[ERROR] UKNOWN TEXTURE DATA FORMAT" );
		
		position( offset + size );
	}
	
	private void getAnimation( int offset, short hsize, int size ) {
		//System.out.println( "animations at " + offset + " " + size + " " + ( (offset + hsize) - position() ) );
		advance( 8 );
		cSubFile.anims.add( new Animation( getName() ) );
		cAnimation = cSubFile.anims.get( cSubFile.anims.size() - 1 );
		
		position( offset + hsize );
		while( position() < (offset + size) )
			processChunk( position() );
		
		//System.out.println( "animation " + cAnimation.name + " finished." );
	}
	
	private void getAnimationTimes( int offset, short hsize, int size ) {
		//System.out.println( "animation times at " + offset + " " + size );
		cAnimation.start = readFloat32();
		cAnimation.finish = readFloat32();
	}
	
	private void getAnimationT( int offset, short hsize, int size ) {
		//System.out.println( "animation T at " + offset + " " + size + " " + hsize );
		cAnimation.unknown1 = readFloat32();
	}
	
	private void getAnimationLinks( int offset, short hsize, int size ) {
		//System.out.println( "animation links at " + offset + " " + size + " " + ( (offset + hsize) - position() ) );
		AnimationLink link = new AnimationLink();
		link.bone = readHex(); readInt8( 3 );
		link.type = readInt32(); readInt32(); //TODO: unknown
		link.frame = readHex(); readInt8( 3 );
		cAnimation.links.add( link );
	}
	
	private void getAnimationT2( int offset, short hsize, int size ) {
		//System.out.println( "animation t2 at " + offset + " " + size );
		cAnimation.unknown2 = readInt32();
	}
	
	private void getAnimationFrame( int offset, short hsize, int size ) {
		//System.out.println( "frame at " + offset + " " + size + " " + ( (offset + hsize) - position() ) );
		advance( 8 );
		FrameSet set = new FrameSet( getName() );
		
		position( offset + hsize );
		set.type = readHex(); readInt8( 3 );
		set.stride = readInt32();
		set.count = readInt32();
		readInt32(); //TODO: unknown
		for( int i = 0; i < set.count; i++ ) {
			KeyFrame frame = new KeyFrame();
			frame.time = readFloat16();
			frame.data = readFloat16( set.stride );
		}
		cAnimation.frames.add( set );
	}
	
	private void getTextureAnimation( int offset, short hsize, int size ) {
		//System.out.println( "texture animation at " + offset + " " + size + " " + hsize );
		advance( 8 );
		getName();
		
		position( offset + hsize );
		while( position() < (offset + size) )
			processChunk( position() );
	}
	
	private void getWat( int offset, short hsize, int size ) {
		//System.out.println( "WWWAAATTT at " + offset + " " + size + " " + hsize );
		readInt32();
	}
	
	private void processChunk( int offset ) {
		position( offset );
		if( offset == length() ) return;
		
		int type = readUInt16();
		short hsize = readInt16();
		int size = readInt32();
		
		switch( type ) {
		
		case C_ROOT: getRoot( offset, hsize, size ); break;
		case C_SUB: getSub( offset, hsize, size ); break;
		case C_WAT: getWat( offset, hsize, size ); break;
		case C_BIAS: getBias( offset, hsize, size ); break;
		
		case C_BONE: getBone( offset, hsize, size ); break;
		case C_BONE_PARENT: getBoneParent( offset, hsize, size ); break;
		case C_BONE_REF: getBoneLinks( offset, hsize, size ); break;
		case C_BONE_MATRIX: getBoneMatrices( offset, hsize, size ); break;
		case C_BONE_TRANS: getBoneTranslation( offset, hsize, size ); break;
		case C_BONE_QUAT: getBoneQuaternion( offset, hsize, size ); break;
		case C_BONE_SURFACE: getBoneSurface( offset, hsize, size ); break;
		
		case C_SURFACE: getSurface( offset, hsize, size ); break;
		case C_MESH: getMesh( offset, hsize, size ); break;
		case C_MESH_MATERIAL: getMeshMaterial( offset, hsize, size ); break;
		case C_MESH_WEIGHTS: getMeshWeights( offset, hsize, size ); break;
		case C_MESH_INDICES: getMeshIndices( offset, hsize, size ); break;
		case C_MESH_WAT: getMeshWat( offset, hsize, size ); break;
		case C_VERTICES: getVertices( offset, hsize, size ); break;
		
		case C_MATERIAL: getMaterial( offset, hsize, size ); break;
		case C_TEXTURE_LAYER: getTextureLayer( offset, hsize, size ); break;
		case C_TEXTURE_REF: getTextureReference( offset, hsize, size ); break;
		
		case C_TEXTURE: getTexture( offset, hsize, size ); break;
		case C_TEXTURE_FILE: getTextureFile( offset, hsize, size ); break;
		case C_TEXTURE_DATA: getTextureData( offset, hsize, size ); break;
		
		case C_ANIMATION: getAnimation( offset, hsize, size ); break;
		case C_ANIMATION_TIMES: getAnimationTimes( offset, hsize, size ); break;
		case C_ANIMATION_T1: getAnimationT( offset, hsize, size ); break;
		case C_ANIMATION_LINKS: getAnimationLinks( offset, hsize, size ); break;
		case C_ANIMATION_T2: getAnimationT2( offset, hsize, size ); break;
		case C_ANIMATION_FRAME: getAnimationFrame( offset, hsize, size ); break;
		case C_TEXTURE_ANIMATION: getTextureAnimation( offset, hsize, size ); break;
			
		default:
			System.out.println( "unkown chunk type: " + type + " ("+String.format( "0x%04x", type)+") " + hsize + " " + size );
			//position( offset + size );
			break;
		}
		
		if( position() != (offset + size ) ) {
			if( ((offset+size)-position()) < 4 && position() % 4 != 0 ) {
				if( !pad() ) {
					System.out.println("YES");
				}
			} else {
				System.out.println( "("+String.format( "0x%04x", type)+") ended: " + position() + " expexcted: " + (offset + size) + " " + (position()%4==0));
			}
		}
		position( offset + size );
	}
	
	private String getName() {
		String res = "";
		char c;
		while( (c = readChar()) != '\0' ) res += c;
		while( position() % 4 != 0 ) advance( 1 );
		return res;
	}
	
	private int getFormatMask( int value, int length, int index ) {
		return getBits( value, length, index );
	}
	
	private boolean pad() {
		boolean valid = true;
		while( position() % 4 == 0 )
			valid &= readHex8().equals( "0x00" );
		return valid;
	}

	private ByteBuffer buffer;
	
	@Override
	public ByteBuffer getBuffer() {
		return buffer;
	}
	
	@Override
	public String getExtensionName() { return "GMO"; }

	@Override
	public ModelContainer decode( Resource resource, ModelOptions opts ) {
		buffer = ByteBuffer.wrap( resource.toByteArray() );
		setLittleEndian();
		debug = resource.getDirectory().createResource( resource.getBaseName() + ".debug", true );
		
		header();
		
		debug.openStream();
		processChunk( position() );
		debug.closeStream();
		
		//create ModelContainer
		ModelContainer mc = new ModelContainer();
		
		// create meshgroups grouped by texture for each surface
		for( SubFile file : gmo.files ) {
			for( Surface s : file.surfaces ) {
				for( Mesh m : s.meshes ) {
					if( m.weighted.length > 0 ) {
						System.out.println( m.name + " should  have weights..." + s.vertices.get( m.vertices ).weightCount + " " + m.weighted.length );
					}
					MeshGroup mg = s.getGroup( file.materials.get( m.material ).texture );
					mg.v.addAll( Buffer.toArrayList( m.v ) );
					mg.t.addAll( Buffer.toArrayList( m.t ) );
				}
			}
		}
		
		System.out.println( gmo.files.size() );
		System.out.println( "done." );
		return mc;
	}
	
	@Override
	public void clear() {
		//TODO: clear all data used by this decoder
	}
	
}
