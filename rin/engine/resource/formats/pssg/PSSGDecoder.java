package rin.engine.resource.formats.pssg;

import java.nio.ByteBuffer;
import java.util.HashMap;

import rin.engine.Engine;
import rin.engine.resource.ResourceDecoder;
import rin.engine.resource.ResourceIdentifier;
import rin.engine.util.ArrayUtils;
import rin.engine.util.FileUtils;
import rin.util.bio.BinaryReader;

public class PSSGDecoder extends BinaryReader implements ResourceDecoder {
	
	private ByteBuffer data;
	private String magic;
	private long fileSize;
	private int propCount;
	private int chunkCount;
	
	private int keyCount = -1;
	private String keyType = "";
	private int count = 0;
	
	private String[] propMap;
	private String[] chunkMap;
	
	private boolean dataOnlyChunk( int index ) {
		return chunkMap[index].toUpperCase().equals( "KEYS" );
	}
	
	private void header() {
		magic = readString( 4 );
		fileSize = readUInt32();
		propCount = readInt32();
		chunkCount = readInt32();
		
		propMap = new String[propCount];
		chunkMap = new String[chunkCount+1];
		System.out.println( "header: " + magic + " " + fileSize + " " + propCount + " " + chunkCount );
	}
	
	private void infoList() {
		for( int i = 0; i < chunkCount; i++ ) {
			chunkMap[readInt32()] = readString( (int)readUInt32() );
			
			int sum = readInt32();
			for( int j = 0; j < sum; j++ )
				propMap[readInt32()] = readString( (int)readUInt32() );
		}
		
		System.out.println( "chunks: " + ArrayUtils.asString( chunkMap ) );
		System.out.println( "props: " + ArrayUtils.asString( propMap ) );
	}
	
	private void readChunks() {		
		int index = readInt32();
		System.out.println( "chunk: " + chunkMap[index] );
		String ccur = chunkMap[index];
		
		long size = readUInt32();
		long chunkStop = position() + size;
		
		long propSize = readUInt32();
		long propStop = position() + propSize;
		
		while( position() < propStop ) {
			int pindex = readInt32();
			int psize = readInt32();
			int ppstop = position() + psize;
			
			String pcur = propMap[pindex];
			if( pcur.toUpperCase().equals( "TARGETNAME" ) ) {
				System.out.println( "targetName: " + readString( readInt32() ) );
			} else if( pcur.toUpperCase().equals( "CHANNEL" ) ) {
				System.out.println( "channel: " + readString( readInt32() ) );
			} else if( pcur.toUpperCase().equals( "ANIMATIONCOUNT" ) ) {
				System.out.println( "animationCount: " + readInt32() );
			} else if( pcur.toUpperCase().equals( "ID" ) ) {
				System.out.println( "id: " + readString( readInt32() ) );
			} else if( pcur.toUpperCase().equals( "CHANNELCOUNT" ) ) {
				System.out.println( "channelCount: " + readInt32() );
			} else if( pcur.toUpperCase().equals( "CONSTANTCHANNELCOUNT" ) ) {
				System.out.println( "constantChannelCount: " + readInt32() );
			} else if( pcur.toUpperCase().equals( "KEYCOUNT" ) ) {
				//System.out.println( "keyCount: " + readInt32() );
				keyCount = readInt32();
			} else if( pcur.toUpperCase().equals( "KEYTYPE" ) ) {
				keyType = readString( readInt32() );
			} else if( pcur.toUpperCase().equals( "VALUE" ) ) {
				System.out.println( readFloat32( 8 ) );
			} else {
				System.out.println( "prop: " + propMap[pindex] );
			}
			
			position( ppstop );
		}
		
		if( !dataOnlyChunk( index ) ) {
			if( position() != chunkStop ) {
				while( position() < chunkStop ) {
					readChunks();
				}
			}
		} else {
			if( chunkMap[index].toUpperCase().equals( "KEYS" ) ) {
				if( keyType.equals( "Rotation" ) ) {
					System.out.println( "KEYS " + chunkStop + " " + (position() + keyCount * 4 * 4) );
				} else {
					System.out.println( "KEYS" + chunkStop + " " + (position() + keyCount * 4 * 3) );
				}
			}
		}
		
		position( (int)chunkStop );
		if( chunkMap[index].toUpperCase().equals( "CONSTANTCHANNEL" ) )
			count++;
		if( chunkMap[index].toUpperCase().equals( "ANIMATION" ) ) {
			System.out.println( count );
			System.exit( 0 );
		}
	}
	
	@Override
	public PSSGResource decode( ResourceIdentifier resource ) {
		this.data = ByteBuffer.wrap( FileUtils.asByteArray( Engine.MAINDIR + "packs\\meruru\\models\\meruru\\PC22_MOTION1.pssg" ) );
		header();
		
		infoList();
		
		while( position() < fileSize )
			readChunks();
		
		PSSGResource res = new PSSGResource( resource );
		return res;
	}

	@Override
	public ByteBuffer getBuffer() {
		return this.data;
	}
	
}
