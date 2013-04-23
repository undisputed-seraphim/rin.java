package rin.engine.resource.formats.ism2;

import java.nio.ByteBuffer;

import rin.engine.resource.ResourceIdentifier;
import rin.engine.util.ArrayUtils;
import rin.util.bio.BinaryReader;

public class ISM2Decoder extends BinaryReader {

	private ByteBuffer data;
	private HEADER header;
	private CHUNKOFFSETS offsets;
	
	@Override
	public ByteBuffer getBuffer() {
		return data;
	}
	
	private static class HEADER {
		public char[] magic;
		public long size;
		public long chunks;
	}
	
	private boolean header() {
		header = new HEADER();
		header.magic = readChar( 4 );
		advance( 12 );
		header.size = readUInt32();
		header.chunks = readUInt32();
		advance( 8 );
		
		// ensure ISM2 file
		return header.magic[0] == 'I' && header.magic[1] == 'S'
				&& header.magic[2] == 'M' && header.magic[3] == '2';
	}
	
	private static class CHUNKINFO {
		public int id;
		public int offset;
		
		@Override
		public String toString() {
			return "id: " + id + " offset: " + offset + " ";
		}
	}
	
	private static class CHUNKOFFSETS {
		public CHUNKINFO[] chunks;
	}
	
	private void chunkList() {
		offsets = new CHUNKOFFSETS();
		offsets.chunks = new CHUNKINFO[(int)header.chunks];
		for( int i = 0; i < header.chunks; i++ ) {
			offsets.chunks[i] = new CHUNKINFO();
			offsets.chunks[i].id = (int)readUInt32();
			offsets.chunks[i].offset = (int)readUInt32();
		}
	}
	
	private void processChunk( CHUNKINFO info ) {
		position( info.offset );
		if( info.id != readUInt32() ) exitWithError( "Incorrect Chunk offset." );
		
		printHex8();
	}
	
	public ISM2Decoder( ResourceIdentifier resource ) {
		data = ByteBuffer.wrap( resource.asByteArray() );
		
		// read header
		if( !header() ) exitWithError( "Incorrect ISM2 Header." );
		if( !(header.size == length()) ) exitWithError( "Header claims incorrect file size." );
		System.out.println( "ISM2 file, size: " + length() );
		
		// read list of chunk offsets
		chunkList();
		System.out.println( ArrayUtils.asString( offsets.chunks ) );
		
		// process chunks
		for( int i = 0; i < header.chunks; i++ )
			processChunk( offsets.chunks[i] );
	}
	
	private void exitWithError( String error ) {
		System.err.println( error );
		System.exit( 0 );
	}
	
}
