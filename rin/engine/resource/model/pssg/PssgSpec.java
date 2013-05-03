package rin.engine.resource.model.pssg;

import java.util.ArrayList;

public class PssgSpec {

	public static final char[] MAGIC = new char[] { 'P', 'S', 'S', 'G' };
	
	public static enum ChunkType {
		PSSGDATABASE					( false ),
		LIBRARY							( false ),
		
		ANIMATION						( false ),
		CHANNELREF						( false ),
		CONSTANTCHANNEL					( false ),
		ANIMATIONCHANNEL				( false ),
		ANIMATIONCHANNELDATABLOCK		( false ),
		KEYS							( true ),
		ANIMATIONSET					( false ),
		ANIMATIONREF					( false ),
		NULL							( false );
		
		private boolean data;
		private ChunkType( boolean dataOnly ) {
			data = dataOnly;
		}
		
		public boolean isDataOnly() { return data; }
		public static ChunkType find( String s ) {
			if( s == null ) return NULL;
			for( ChunkType ct : ChunkType.values() )
				if( ct.toString().toUpperCase().equals( s.toUpperCase() ) )
					return ct;
			System.out.println( "Unimplemented chunk type " + s );
			return NULL;
		}
	}
	
	public static enum PropertyType {
		CREATOR,
		ID,
		
		ANIMATION,
		CHANNEL,
		CHANNELCOUNT,
		CONSTANTCHANNELCOUNT,
		CONSTANTCHANNELENDTIME,
		CONSTANTCHANNELSTARTTIME,
		KEYCOUNT,
		KEYTYPE,
		TIMEBLOCK,
		VALUEBLOCK,
		VALUE,
		
		
		TARGETNAME,
		TYPE,
		NULL;
		
		public static PropertyType find( String s ) {
			if( s == null ) return NULL;
			for( PropertyType pt : PropertyType.values() )
				if( pt.toString().toUpperCase().equals( s.toUpperCase() ) )
					return pt;
			System.out.println( "Unimplemented property " + s );
			return NULL;
		}
	}
	
	public static class PssgAnimationDataBlock {
		public int keyCount;
		public String type;
		public String id;
		
		public float[] data;
	}
	
	public static class PssgAnimationChannel {
		public String id;
		public String timeBlock;
		public String valueBlock;
	}
	
	public static class PssgChannelRef {
		public String channel;
		public String targetName;
	}
	
	public static class PssgConstantChannel {
		public String keyType;
		public String targetName;
		public float[] value;
	}
	
	public static class PssgAnimation {
		public String id;
		public float startTime;
		public float endTime;
		public int channelCount;
		public int constantChannelCount;
		
		public ArrayList<PssgChannelRef> channelRefs = new ArrayList<PssgChannelRef>();
		public ArrayList<PssgConstantChannel> constantChannels = new ArrayList<PssgConstantChannel>();
	}
	
	public static class Pssg {
		public ArrayList<PssgAnimation> animations = new ArrayList<PssgAnimation>();
		public ArrayList<PssgAnimationDataBlock> animationData = new ArrayList<PssgAnimationDataBlock>();
		public ArrayList<PssgAnimationChannel> animationChannels = new ArrayList<PssgAnimationChannel>();
	}
	
}
