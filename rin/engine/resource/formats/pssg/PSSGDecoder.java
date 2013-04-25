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
import rin.util.dcode.pssg.PSSGFile;

public class PSSGDecoder extends BinaryReader {
	
	private ByteBuffer data;
	private String magic;
	private long fileSize;
	private int propCount;
	private int chunkCount;
	
	private String keyType = "";
	private int keyCount = -1;
	
	private String[] propMap;
	private String[] chunkMap;
	
	private ArrayList<Animation> anims = new ArrayList<Animation>();
	private static HashMap<String, AnimationChannelDataBlock> blockMap = new HashMap<String, AnimationChannelDataBlock>();
	private static HashMap<String, AnimationChannel> chanMap = new HashMap<String, AnimationChannel>();
	private Animation canim;
	private ChannelRef cref;
	private ConstantChannel cconstant;
	private AnimationChannelDataBlock cblock;
	private String ctime;
	private String cvalue;
	
	public static class AnimationChannelDataBlock {
		public String id;
		public float[] data;
		public String type;
		
		public AnimationChannelDataBlock( String id ) {
			this.id = id;
		}
	}
	
	public static class AnimationChannel {
		public String id;
		public String time;
		public String value;
		
		public AnimationChannel( String id ) {
			this.id = id.substring( 1 );
		}
		
		public void setBlocks( String time, String value ) {
			//System.out.println( time + " " + value );
			this.time = time;
			this.value = value;
		}
	}
	
	public static class ChannelRef {
		public String target;
		public String channel;
	}
	
	public static class ConstantChannel {
		public float[] values;
		public String target;
	}
	
	public static class Channel {
		public String type;
		public float[] data = new float[0];
		
		public Channel( String type ) {
			this.type = type;
		}
	}
	
	public static class Bone {
		public String id;
		
		public ArrayList<Channel> channels = new ArrayList<Channel>();
		public ArrayList<float[]> constants = new ArrayList<float[]>();
		
		public Bone( String id ) {
			this.id = id;
			this.channels.add( new Channel( "Scale" ) );
			this.channels.add( new Channel( "Rotation" ) );
			this.channels.add( new Channel( "Time" ) );
			this.channels.add( new Channel( "Translation" ) );
			this.channels.add( new Channel( "MorphTargetWeight1" ) );
		}
		
		public Channel getChannel( String type ) {
			for( Channel c : channels )
				if( c.type.equals( type ) )
					return c;
			
			return null;
		}
		
		public void addChannel( String id ) {
			AnimationChannel cur = chanMap.get( id );
			if( cur != null ) {
				AnimationChannelDataBlock time = blockMap.get( cur.time );
				if( time != null )
					getChannel( "Time" ).data = time.data;
				
				time = blockMap.get( cur.value );
				//System.out.println( cur.time );
				if( time != null ) {
					getChannel( time.type ).data = time.data;
				}
			}
		}
	}
	
	public static class ActualAnimation {
		public String id;
		public HashMap<String, Bone> boneMap = new HashMap<String, Bone>();
		
		public ActualAnimation( String id ) {
			this.id = id;
		}
		
		public Bone getBone( String id ) {
			//if( id.substring( 0, 1 ).equals( "#" ) )
			//	id = id.substring( 1 );
			
			if( !boneMap.containsKey( id ) )
				boneMap.put( id, new Bone( id ) );
			
			return boneMap.get( id );
		}
	}
	
	public static class Animation {
		public String id;
		public float start;
		public float end;
		public ConstantChannel[] constants;
		public ChannelRef[] refs;
		public int ccount = 0;
		public int rcount = 0;
		
		public void print() {
			System.out.println( "Animation id: " + id );
			System.out.println( "\tStart: " + start + " Finish: " + end );
			System.out.println( "\tChannels: " + refs.length );
			System.out.println( "\t\tFirst ten: " );
			for( int i = 0; i < 10; i++ ) {
				System.out.println( "\t\t\tChannel: " + findAnimationChannel( refs[i].channel ).id );
				System.out.println( "\t\t\tTarget: " + refs[i].target );
			}
			System.out.println( "\tConstant Channels: " + constants.length );
		}
	}
	
	public Animation getAnimation( String id ) {
		for( Animation a : anims )
			if( a.id.toUpperCase().equals( id.toUpperCase() ) )
				return a;
		
		return null;
	}
	
	private static AnimationChannel findAnimationChannel( String id ) {
		if( chanMap.containsKey( id ) )
			return chanMap.get( id );
		
		//System.err.println( "ANIMATION DID NOT EXIST: " + id );
		chanMap.put( id, new AnimationChannel( id ) );
		return chanMap.get( id );
	}
	
	private AnimationChannelDataBlock findAnimationBlock( String id ) {
		if( blockMap.containsKey( id ) )
			return blockMap.get( id );
		
		//System.err.println( "ANIMATIONBLOCK DID NOT EXIST: " + id );
		blockMap.put( id, new AnimationChannelDataBlock( id ) );
		return blockMap.get( id );
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
		//System.out.println( "header: " + magic + " " + fileSize + " " + propCount + " " + chunkCount );
	}
	
	private void infoList() {
		for( int i = 0; i < chunkCount; i++ ) {
			chunkMap[readInt32()] = readString( (int)readUInt32() );
			
			int sum = readInt32();
			for( int j = 0; j < sum; j++ )
				propMap[readInt32()] = readString( (int)readUInt32() );
		}
		
		//System.out.println( "chunks: " + ArrayUtils.asString( chunkMap ) );
		//System.out.println( "props: " + ArrayUtils.asString( propMap ) );
	}
	
	private void readChunks() {		
		int index = readInt32();
		//System.out.println( "chunk: " + chunkMap[index] );
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
				//System.out.println( "CHANNEL: " + cref.target );
			} else if( pcur.toUpperCase().equals( "TARGETNAME" ) && ccur.toUpperCase().equals( "CONSTANTCHANNEL" ) ) {
				cconstant.target = readString( readInt32() );
				//System.out.println( "CONSTANT: " + cconstant.target );
			} else if( pcur.toUpperCase().equals( "TARGETNAME" ) ) {
				System.out.println( "targetName: " + readString( readInt32() ) );
			} else if( pcur.toUpperCase().equals( "CHANNEL" ) ) {
				cref.channel = readString( readInt32() );
				chanMap.put( cref.channel, new AnimationChannel( cref.channel ) );
				//System.out.println( "CHANNEL: " + cref.channel );
			} else if( pcur.toUpperCase().equals( "ANIMATIONCOUNT" ) ) {
				readInt32();
				//System.out.println( "animationCount: " + readInt32() );
			} else if( pcur.toUpperCase().equals( "ID" ) && ccur.toUpperCase().equals( "ANIMATIONCHANNEL" ) ) {
				findAnimationChannel( readString( readInt32() ) ).setBlocks( ctime, cvalue );
				blockMap.put( ctime, new AnimationChannelDataBlock( ctime ) );
				blockMap.put( cvalue, new AnimationChannelDataBlock( cvalue ) );
			} else if( pcur.toUpperCase().equals( "ID" ) && ccur.toUpperCase().equals( "ANIMATIONCHANNELDATABLOCK" ) ) {
				cblock = findAnimationBlock( readString( readInt32() ) );
				cblock.type = keyType;
			} else if( pcur.toUpperCase().equals( "ID" ) && ccur.toUpperCase().equals( "ANIMATION" ) ) {
				canim.id = readString( readInt32() );
			} else if( pcur.toUpperCase().equals( "CHANNELCOUNT" ) ) {
				canim.refs = new ChannelRef[ readInt32() ];
				//System.out.println( "CHANELS: " + canim.refs.length );
			} else if( pcur.toUpperCase().equals( "CONSTANTCHANNELCOUNT" ) ) {
				canim.constants = new ConstantChannel[ readInt32() ];
			} else if( pcur.toUpperCase().equals( "CONSTANTCHANNELSTARTTIME" ) ) {
				canim.start = readFloat32();
			} else if( pcur.toUpperCase().equals( "CONSTANTCHANNELENDTIME" ) ) {
				canim.end = readFloat32();
			} else if( pcur.toUpperCase().equals( "TIMEBLOCK" ) ) {
				ctime = readString( readInt32() ).substring( 1 );
			} else if( pcur.toUpperCase().equals( "VALUEBLOCK" ) ) {
				cvalue = readString( readInt32() ).substring( 1 );
			} else if( pcur.toUpperCase().equals( "ANIMATION" ) ) {
				readString( readInt32() );
				//System.out.println( "animation: " + readString( readInt32() ) );
			} else if( pcur.toUpperCase().equals( "KEYCOUNT" ) ) {
				//System.out.println( "keyCount: " + readInt32() );
				keyCount = readInt32();
			} else if( pcur.toUpperCase().equals( "KEYTYPE" ) ) {
				keyType = readString( readInt32() );
			} else if( pcur.toUpperCase().equals( "VALUE" ) ) {
				cconstant.values = readFloat32( 4 );
			} else {
				//System.out.println( "prop: " + propMap[pindex] );
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
				if( cblock.type.equals( "Rotation" ) ) {
					cblock.data = readFloat32( keyCount * 4 );
				} else if( cblock.type.equals( "Time" ) || cblock.type.equals( "MorphTargetWeight1" ) ) {
					cblock.data = readFloat32( keyCount );
					//System.out.println( cblock.type + " " + chunkStop + " " + position() );
				} else if( cblock.type.equals( "Scale" ) || cblock.type.equals( "Translation" ) ) {
					cblock.data = readFloat32( keyCount * 3);
				} else {
					System.out.println( "unknown: " + cblock.type );
				}
			}
		}
		
		position( (int)chunkStop );
		//if( ccur.toUpperCase().equals( "ANIMATIONCHANNEL" ) )
			//System.exit( 0 );
	}
	
	//@Override
	public PSSGResource decode( ResourceIdentifier resource ) {
		this.data = ByteBuffer.wrap( resource.asByteArray() );
		header();
		
		infoList();
		
		while( position() < fileSize )
			readChunks();
		
		PSSGResource res = new PSSGResource( resource );
		
		//getAnimation( "PC22_B_ATTACK_01" ).print();
		
		// create the actual animations
		ActualAnimation cur;
		for( Animation a : anims ) {
			res.getAnimationMap().put( a.id, new ActualAnimation( a.id ) );
			cur = res.getAnimation( a.id );
			
			for( ChannelRef cr : a.refs ) {
				cur.getBone( cr.target ).addChannel( cr.channel );
			}
			
			for( ConstantChannel con : a.constants ) {
				cur.getBone( con.target ).constants.add( con.values );
			}
			
			//System.out.println( ArrayUtils.asString( cur.boneMap ) );
		}
		
		//PSSGFile model = new PSSGFile( Engine.MAINDIR + "packs/meruru/models/meruru/meruru.pssg" );
		//model.read();
		//model.PSSG.rootNode.print();
		return res;
	}

	@Override
	public ByteBuffer getBuffer() {
		return this.data;
	}
	
}
