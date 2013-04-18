package rin.engine.resource.formats.pssg;

import java.nio.ByteBuffer;
import java.util.ArrayList;
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
	
	private String[] propMap;
	private String[] chunkMap;
	
	private ArrayList<Animation> anims = new ArrayList<Animation>();
	private Animation canim;
	private ChannelRef cref;
	private ConstantChannel cconstant;
	private String ctime;
	private String cvalue;
	
	
	public static class AnimationChannel {
		public String id;
		public String time;
		public String value;
		
		public AnimationChannel( String id ) {
			this.id = id.substring( 1 );
		}
		
		public void setBlocks( String time, String value ) {
			this.time = time;
			this.value = value;
		}
	}
	
	public static class ChannelRef {
		String target;
		AnimationChannel channel;
	}
	
	public static class ConstantChannel {
		float[] values;
		String target;
	}
	
	public static class Animation {
		public ConstantChannel[] constants;
		public ChannelRef[] refs;
		public int ccount = 0;
		public int rcount = 0;
	}
	
	private AnimationChannel findAnimationChannel( String id ) {
		System.out.println( id );
		for( Animation a : anims )
			for( ChannelRef cr : a.refs )
				if( cr.channel.id.toUpperCase().equals( id.toUpperCase() ) )
					return cr.channel;
		
		System.err.println( "ANIMATION DID NOT EXIST: " + id );
		return new AnimationChannel( "asdf" );
	}
	
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
		
		if( ccur.toUpperCase().equals( "ANIMATION" ) ) {
			anims.add( new Animation() );
			canim = anims.get( anims.size() - 1 );
		} else if( ccur.toUpperCase().equals( "CHANNELREF" ) ) {
			canim.refs[canim.rcount] = new ChannelRef();
			cref = canim.refs[canim.rcount++];
		} else if( ccur.toUpperCase().equals( "CONSTANTCHANNEL" ) ) {
			canim.constants[canim.ccount] = new ConstantChannel();
			cconstant = canim.constants[canim.ccount++];
		}
		
		while( position() < propStop ) {
			int pindex = readInt32();
			int psize = readInt32();
			int ppstop = position() + psize;
			
			String pcur = propMap[pindex];
			if( pcur.toUpperCase().equals( "TARGETNAME" ) && ccur.toUpperCase().equals( "CHANNELREF" ) ) {
				cref.target = readString( readInt32() );
			} else if( pcur.toUpperCase().equals( "TARGETNAME" ) && ccur.toUpperCase().equals( "CONSTANTCHANNEL" ) ) {
				cconstant.target = readString( readInt32() );
			} else if( pcur.toUpperCase().equals( "TARGETNAME" ) ) {
				System.out.println( "targetName: " + readString( readInt32() ) );
			} else if( pcur.toUpperCase().equals( "CHANNEL" ) ) {
				cref.channel = new AnimationChannel( readString( readInt32() ) );
			} else if( pcur.toUpperCase().equals( "ANIMATIONCOUNT" ) ) {
				System.out.println( "animationCount: " + readInt32() );
			} else if( pcur.toUpperCase().equals( "ID" )  && ccur.toUpperCase().equals( "ANIMATIONCHANNEL" ) ) {
				findAnimationChannel( readString( readInt32() ) ).setBlocks( ctime, cvalue );
			}  else if( pcur.toUpperCase().equals( "ID" ) ) {
				System.out.println( "id: " + readString( readInt32() ) );
			} else if( pcur.toUpperCase().equals( "CHANNELCOUNT" ) ) {
				canim.refs = new ChannelRef[ readInt32() ];
			} else if( pcur.toUpperCase().equals( "CONSTANTCHANNELCOUNT" ) ) {
				canim.constants = new ConstantChannel[ readInt32() ];
			} else if( pcur.toUpperCase().equals( "CONSTANTCHANNELSTARTTIME" ) ) {
				System.out.println( "constantChannelStartTime: " + readFloat32() );
			} else if( pcur.toUpperCase().equals( "CONSTANTCHANNELENDTIME" ) ) {
				System.out.println( "constantChannelEndTime: " + readFloat32() );
			} else if( pcur.toUpperCase().equals( "TIMEBLOCK" ) ) {
				ctime = readString( readInt32() );
			} else if( pcur.toUpperCase().equals( "VALUEBLOCK" ) ) {
				cvalue = readString( readInt32() );
			} else if( pcur.toUpperCase().equals( "ANIMATION" ) ) {
				System.out.println( "animation: " + readString( readInt32() ) );
			} else if( pcur.toUpperCase().equals( "KEYCOUNT" ) ) {
				//System.out.println( "keyCount: " + readInt32() );
				keyCount = readInt32();
			} else if( pcur.toUpperCase().equals( "KEYTYPE" ) ) {
				keyType = readString( readInt32() );
			} else if( pcur.toUpperCase().equals( "VALUE" ) ) {
				cconstant.values = readFloat32( 4 );
				//System.out.println( position() + " " + ppstop );
			} else {
				System.out.println( "prop: " + propMap[pindex] );
			}
			
			position( ppstop );
		}
		
		/*
		 * animationchanneldatablock
		 * 		id > animationchannel
		 * 		KEYS > floats
		 */
		
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
		//if( ccur.toUpperCase().equals( "ANIMATIONCHANNEL" ) )
			//System.exit( 0 );
	}
	
	@Override
	public PSSGResource decode( ResourceIdentifier resource ) {
		this.data = ByteBuffer.wrap( FileUtils.asByteArray( Engine.MAINDIR + "packs/meruru/models/meruru/PC22_MOTION1.pssg" ) );
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
