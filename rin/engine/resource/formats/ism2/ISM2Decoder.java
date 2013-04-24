package rin.engine.resource.formats.ism2;

import java.nio.ByteBuffer;
import java.util.TreeMap;

import rin.engine.resource.ResourceIdentifier;
import rin.engine.resource.formats.ism2.ISM2Spec.ISM2Mesh;
import rin.engine.util.ArrayUtils;
import rin.engine.util.FileUtils;
import rin.util.bio.BinaryReader;
import static rin.engine.resource.formats.ism2.ISM2Spec.*;

public class ISM2Decoder extends BinaryReader {

	private static boolean DEBUG = true;
	
	private ByteBuffer buffer;
	private long fileSize;
	private int chunkCount;
	private TreeMap<Long, Integer> chunkMap;
	private String[] propMap;
	
	private ISM2Data data;
	private ISM2Mesh cmesh;
	
	public ISM2Data getData() {
		return data;
	}

	@Override
	public ByteBuffer getBuffer() {
		return buffer;
	}
	
	private boolean header() {
		char[] magic = readChar( 4 );
		advance( 12 );
		fileSize = readUInt32();
		chunkCount = readInt32();
		advance( 8 );
		
		if( DEBUG ) System.out.println( "ISM2 file, size: " + length() );
		
		// ensure ISM2 file
		return magic[0] == 'I' && magic[1] == 'S'
				&& magic[2] == 'M' && magic[3] == '2';
	}
	
	private void chunkList() {
		chunkMap = new TreeMap<Long, Integer>();
		for( int i = 0; i < chunkCount; i++ ) {
			int id = readInt32();
			long offset = readUInt32();
			chunkMap.put( offset, id );
		}
		
		if( DEBUG ) System.out.println( ArrayUtils.asString( chunkMap ) );
	}
	
	private void getTexture() {
		// textures can contain up to 8 properties each, from the propMap
		for( int i = 0; i < 7; i++ ) {
			if( DEBUG ) System.out.println( propMap[ readInt32() ] );
		}
	}
	
	private void getMesh() {
		long size = readUInt32();
		int count = readInt32();
		System.out.println( "Mesh 1: " + propMap[readInt32()] );
		System.out.println( "Mesh 2: " + propMap[readInt32()] );
		System.out.println( "Mesh 3: " + readInt32() );
		System.out.println( "Mesh 4: " + readInt32() );
		System.out.println( "Mesh 5: " + readInt32() );
		data.meshList[data.meshCount++] = new ISM2Mesh();
		cmesh = data.meshList[data.meshCount-1];
		
		long[] offsets = getOffsets( count );
		for( int i = 0; i < offsets.length; i++ )
			processNode( offsets[i] );
	}
	
	private VertexType getVertexType( long offset ) {
		position( (int)offset );
		VertexType res = new VertexType();
		res.type = readInt32();
		res.count = readInt32();
		res.vtype = readInt32();
		res.vsize = readInt32();
		res.voffset = readUInt32();
		res.offset = readUInt32();
		
		//if( DEBUG ) System.out.println( res );
		return res;
	}
	
	private void getVertices() {
		System.out.println( "vertices at " + (position() - 4) );
		long size = readUInt32();
		int count = readInt32();
		int type = readInt32();
		int vertexCount = readInt32();
		long vertexSize = readUInt32();
		System.out.println( "vertices 1: " + readInt32() );
		
		ISM2VertexData tmp = new ISM2VertexData();
		VertexType[] types = new VertexType[count];
		boolean normaled = false;
		long start = 0L;
		
		long[] offsets = getOffsets( count );
		for( int i = 0; i < offsets.length; i++ ) {
			types[i] = getVertexType( offsets[i] );
			if( types[i].type == T_VERT_VERTICES_NORMAL ) normaled = true;
			start = types[i].offset;
		}

		position( (int)start );
		switch( type ) {
		
		case T_VERT_VERTICES:
			System.out.println( "vertices: " + position() + " " + vertexCount + " " + vertexSize );
			tmp.v = new float[vertexCount*3];
			if( normaled )
				tmp.n = new float[vertexCount*3];
			
			for( int i = 0; i < vertexCount; i++ ) {
				for( int j = 0; j < types.length; j++ ) {
					position( (int)start + (int)(i*vertexSize+types[j].voffset) );
					switch( types[j].type ) {
					
					case T_VERT_VERTICES_VERTEX:
						tmp.v[i*3] = readFloat32();
						tmp.v[i*3+1] = readFloat32();
						tmp.v[i*3+2] = readFloat32();
						break;
						
					case T_VERT_VERTICES_NORMAL:
						tmp.n[i*3] = readFloat16();
						tmp.n[i*3+1] = readFloat16();
						tmp.n[i*3+2] = readFloat16();
						break;
						
					default:
						//System.out.println( "unknown T_VERT_VERTICES sub type: " + types[j].type );
						break;
					}
				}
			}
			break;
			
		case T_VERT_TEXCOORDS:
			System.out.println( "texcoords: " + position() + " " + vertexCount + " " + vertexSize );
			tmp.t = new float[vertexCount*2];
			
			for( int i = 0; i < vertexCount; i++ ) {
				for( int j = 0; j < types.length; j++ ) {
					position( (int)start + (int)(i*vertexSize+types[j].voffset) );
					tmp.t[i*2] = readFloat16();
					tmp.t[i*2+1] = readFloat16();
					//System.out.println( "Uv: " + readFloat16() + " " + readFloat16() );
				}
			}
			break;
			
		default:
			System.out.println( "unknown vertex type: " + type );
			break;
		}
		
		cmesh.vdata.add( tmp );
	}
	
	private void getTriangles() {
		long size = readUInt32();
		int count = readInt32();
		System.out.println( "Triangles 1: " + propMap[readInt32()] );
		System.out.println( "Triangles 2: " + readInt32() );
		System.out.println( "Triangles 3: " + readInt32() );
		int triangleCount = readInt32();
		
		long[] offsets = getOffsets( count );
		for( int i = 0; i < offsets.length; i++ )
			processNode( offsets[i] );
	}
	
	private void getIndices() {
		long size = readUInt32();
		int count = readInt32();
		int dataType = readInt32();
		System.out.println( "indices 1: " + readInt32() );
		
		int triangleCount = count / 3;
		ISM2IndexData tmp = new ISM2IndexData();
		tmp.i = new int[triangleCount*3];
		switch( dataType ) {
		
		case T_INDEX_INT16:
			for( int i = 0; i < triangleCount; i++ ) {
				tmp.i[i*3] = readUInt16();
				tmp.i[i*3+1] = readUInt16();
				tmp.i[i*3+2] = readUInt16();
			}
			break;
			
		case T_INDEX_INT32:
			for( int i = 0; i < triangleCount; i++ ) {
				tmp.i[i*3] = readInt32();
				tmp.i[i*3+1] = readInt32();
				tmp.i[i*3+2] = readInt32();
			}
			break;
			
		default:
			System.out.println( "unknown index type: " + dataType );
			break;
		}
		
		cmesh.idata.add( tmp );
	}
	
	private void processNode( long offset ) {
		position( (int)offset );
		int node = readInt32();
		
		switch( node ) {
		
		case N_TEXTURE: getTexture(); return;
		case N_MESH: getMesh(); return;
		case N_VERTICES: getVertices(); return;
		case N_TRIANGLES: getTriangles(); return;
		case N_INDICES: getIndices(); return;
			
		default:
			System.out.println( "unknown node: " + node + " [" + String.format( "0x%02x", node ) + "] at " + offset );
			return;
		}
	}
	
	private void getProperties() {
		long size = readUInt32();
		long[] offsets = getOffsets( readInt32() );
		
		propMap = new String[offsets.length];
		for( int i = 0; i < offsets.length; i++ ) {
			position( (int)offsets[i] );
			String prop = "";
			char c;
			
			while( (c = readChar()) != '\0' )
				prop += c;
			
			propMap[i] = prop;
		}
		
		if( DEBUG ) System.out.println( ArrayUtils.asString( propMap ) );
	}
	
	private void getTextures() {
		long size = readUInt32();
		
		long[] offsets = getOffsets( readInt32() );
		for( int i = 0; i < offsets.length; i++ )
			processNode( offsets[i] );
	}
	
	private void getVertexData() {
		long size = readUInt32();
		
		long[] offsets = getOffsets( readInt32() );
		data.meshList = new ISM2Mesh[offsets.length];
		for( int i = 0; i < offsets.length; i++ )
			processNode( offsets[i] );
	}
	
	private void processChunk( long offset ) {
		position( (int)offset );
		int chunk = readInt32();
		
		switch( chunk ) {
		
		case C_PROPERTIES: getProperties(); return;
		case C_TEXTURES: getTextures(); return;
		case C_VERTEXDATA: getVertexData(); return;
		
		default:
			System.out.println( "unknown chunk: " + chunk + " [" + String.format( "0x%02x", chunk ) + "] at " + offset );
			return;
		}
	}
	
	public ISM2Decoder( String file ) {
		buffer = ByteBuffer.wrap( FileUtils.asByteArray( file ) );
		init();
	}
	
	public ISM2Decoder( ResourceIdentifier resource ) {
		buffer = ByteBuffer.wrap( resource.asByteArray() );
		init();
	}
	
	private void init() {
		data = new ISM2Data();
		
		// read header
		if( !header() ) exitWithError( "Incorrect ISM2 Header." );
		if( !(fileSize == length()) ) exitWithError( "Header claims incorrect file size." );
		
		// read list of chunk offsets
		chunkList();
		
		// process chunks
		for( Long l : chunkMap.keySet() )
			processChunk( l );
		
		// create data object for file
		for( ISM2Mesh m : data.meshList ) {
			int tv = 0;
			for( int i = 0; i < m.idata.size(); i++ ) {
				int[] index = m.idata.get( i ).i;
				float[] v = new float[index.length];
				for( int j = 0; j < index.length; j++ ) {
					if( index[j] > m.vdata.get( i ).v.length )
						System.err.println( "fail" );
					/*v[tv++] = m.vdata.get( i ).v[index[j]];
					v[tv++] = m.vdata.get( i ).v[index[j]+1];
					v[tv++] = m.vdata.get( i ).v[index[j]+2];*/
				}
				data.v = v;
			}
		}
	}
	
	private long[] getOffsets( int amount ) {
		long[] res = new long[amount];
		for( int i = 0; i < amount; i++ )
			res[i] = readUInt32();
		return res;
	}
	
	private void exitWithError( String error ) {
		System.err.println( error );
		System.exit( 0 );
	}
	
}
