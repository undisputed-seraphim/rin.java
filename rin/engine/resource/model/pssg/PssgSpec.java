package rin.engine.resource.model.pssg;

import java.util.ArrayList;
import java.util.HashMap;

import rin.engine.resource.Resource;
import rin.engine.util.ArrayUtils;

public class PssgSpec {

	public static final char[] MAGIC = new char[] { 'P', 'S', 'S', 'G' };
	
	public static enum ChunkType {
		PSSGDATABASE					( false ),
		LIBRARY							( false ),
		
		TEXTURE							( false ),
		TEXTUREIMAGEBLOCK				( false ),
		TEXTUREIMAGEBLOCKDATA			( true ),
		
		DATABLOCK						( false ),
		DATABLOCKSTREAM					( false ),
		DATABLOCKDATA					( true ),
		
		SHADERPROGRAM					( false ),
		SHADERPROGRAMCODE				( false ),
		SHADERPROGRAMCODEBLOCK			( false ),
		CGSTREAM						( false ),
		SHADERINPUTDEFINITION			( false ),
		
		ROOTNODE						( false ),
		TRANSFORM						( true ),
		BOUNDINGBOX						( true ),
		NODE							( false ),
		JOINTNODE						( false ),
		SKINNODE						( false ),
		MODIFIERNETWORKINSTANCE			( false ),
		RENDERINSTANCESOURCE			( false ),
		RENDERNODE						( false ),
		RENDERSTREAMINSTANCE			( false ),
		SKINJOINT						( false ),
		
		MODIFIERNETWORK					( false ),
		
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
			//System.out.println( "Unimplemented chunk type " + s );
			return NULL;
		}
	}
	
	/*public static enum PropertyType {
		CREATOR,
		ID,
		
		ELEMENTCOUNT,
		RENDERTYPE,
		DATATYPE,
		OFFSET,
		STRIDE,
		JOINT,
		//SOURCE,
		INDICES,
		SHADER,
		
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
	
	public static class PssgNode {
		public String id;
		public float[] transform = new float[0];
		public float[] bbox = new float[0];
		public ArrayList<PssgNode> children = new ArrayList<PssgNode>();
		
		public void printChildren( Resource debug, String tab ) {
			for( PssgNode n : children )
				n.print( debug, tab + " " );
		}
		
		public void print( Resource debug, String tab ) {
			debug.writeLine( tab + "NODE ["+id+"]" );
			printChildren( debug, tab );
		}
	}
	
	public static class PssgRootNode extends PssgNode {
		public void print( Resource debug, String tab ) {
			debug.writeLine( tab + "ROOTNODE ["+id+"]" );
			printChildren( debug, tab );
		}
	}
	
	public static class PssgJointNode extends PssgNode {
		public void print( Resource debug, String tab ) {
			debug.writeLine( tab + "JOINTNODE ["+id+"]" );
			printChildren( debug, tab );
		}
	}

	public static class PssgSkinNode extends PssgNode {
		public String shader;
		public String indices;
		//public String source;
		
		public void print( Resource debug, String tab ) {
			debug.writeLine( tab + "SKINNODE ["+id+"]" );
			debug.writeLine( tab + "::Shader: " + shader );
			debug.writeLine( tab + "::Indices: " + indices );
			//debug.writeLine( tab + "::Source: " + source );
			printChildren( debug, tab );
		}
	}
	
	public static class PssgRenderNode extends PssgNode {
		public String shader;
		public String indices;
		//public String source;
		
		public void print( Resource debug, String tab ) {
			debug.writeLine( tab + "RENDERNODE ["+id+"]" );
			debug.writeLine( tab + "::Shader: " + shader );
			debug.writeLine( tab + "::Indices: " + indices );
			//debug.writeLine( tab + "::Source: " + source );
			printChildren( debug, tab );
		}
	}
	
	public static class PssgSkinJoint extends PssgNode {
		public void print( Resource debug, String tab ) {
			debug.writeLine( tab + "SKINJOINT ["+id+"]" );
			printChildren( debug, tab );
		}
	}
	
	public static class PssgDataBlock {
		public String id;
		public String dataType;
		public String renderType;
		public int count;
		public int stride;
		public int offset;
		
		public float[] fdata = new float[0];
		public short[] sdata = new short[0];
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
		
		public void print( Resource debug ) {
			debug.write( "Animation " + id );
			for( PssgChannelRef cr : channelRefs )
				debug.writeLine( " Channel " + cr.channel + ": " + cr.targetName );
			for( PssgConstantChannel cc : constantChannels )
				debug.writeLine( " Constant Channel " + cc.keyType + ": " + cc.targetName );
		}
	}
	
	public static class Pssg {
		public PssgRootNode root;
		public ArrayList<PssgDataBlock> dataBlocks = new ArrayList<PssgDataBlock>();
		
		public ArrayList<PssgAnimation> animations = new ArrayList<PssgAnimation>();
		public ArrayList<PssgAnimationDataBlock> animationData = new ArrayList<PssgAnimationDataBlock>();
		public ArrayList<PssgAnimationChannel> animationChannels = new ArrayList<PssgAnimationChannel>();
		
		public void printAnimations( Resource debug ) {
			for( PssgAnimation a : animations )
				a.print( debug );
		}
	}*/
	
	public static class Pssg {
		public PssgDatabase database;
		public HashMap<String, PssgTexture> textureMap = new HashMap<String, PssgTexture>();
		public HashMap<String, PssgDataBlock> dataBlockMap = new HashMap<String, PssgDataBlock>();
		public HashMap<String, PssgShaderProgram> shaderMap = new HashMap<String, PssgShaderProgram>();
		public HashMap<String, PssgSceneNode<?>> sceneNodeMap = new HashMap<String, PssgSceneNode<?>>();
		
		public void cache( PssgTexture t ) { textureMap.put( t.filename, t ); }
		public void cache( PssgDataBlock db ) { dataBlockMap.put( db.id, db ); }
		public void cache( PssgShaderProgram sp ) { shaderMap.put( sp.id, sp ); }
		public void cache( PssgSceneNode<?> sn ) { sceneNodeMap.put( sn.id, sn ); }
		
		public void print( Resource debug ) {
			database.print( debug, "" );
		}
	}
	
	public static class PssgChunk<T> {
		public String name;
		public PssgChunk<?> parent;
		public ArrayList<PssgChunk<?>> children = new ArrayList<PssgChunk<?>>();
		
		@SuppressWarnings("unchecked")
		public T set( ChunkType t, PssgChunk<?> p ) {
			name = t.toString();
			parent = p;
			if( p != null ) p.children.add( this );
			return (T)this;
		}
		
		public void print( Resource debug, String tab ) {
			for( PssgChunk<?> c : children )
				c.print( debug, tab + " " );
		}
	}
	
	public static class PssgDatabase extends PssgChunk<PssgDatabase> {
		@Override public void print( Resource debug, String tab ) {
			debug.writeLine( tab + name );
			super.print( debug, tab );
		}
	}
	
	public static class PssgLibrary extends PssgChunk<PssgLibrary> {
		public String type;
		@Override public void print( Resource debug, String tab ) {
			debug.writeLine( tab + name );
			super.print( debug, tab );
		}
	}
	
	public static class PssgDataBlock extends PssgChunk<PssgDataBlock> {
		public String id;
		public int count = 0;
		public String renderType;
		public String dataType;
		public int offset = 0;
		public int stride = 0;
		public float[] fdata = new float[0];
		public short[] sdata = new short[0];
		@Override public void print( Resource debug, String tab ) {
			debug.writeLine( tab + name + "     [CACHED: " + id + "]" );
			debug.writeLine( tab + "::Id: " + id );
			debug.writeLine( tab + "::count: " + count );
			debug.writeLine( tab + "::renderType: " + renderType );
			debug.writeLine( tab + "::dataType: " + dataType );
			debug.writeLine( tab + "::offset: " + offset );
			debug.writeLine( tab + "::stride: " + stride );
			debug.writeLine( tab + "::sdata size: " + sdata.length );
			debug.writeLine( tab + "::fdata size: " + fdata.length );
			super.print( debug, tab );
		}
	}
	
	public static class PssgShaderProgram extends PssgChunk<PssgShaderProgram> {
		public String id;
		@Override public void print( Resource debug, String tab ) {
			debug.writeLine( tab + name + "     [CACHED: " + id +"]" );
			super.print( debug, tab );
		}
	}
	
	public static class PssgShaderProgramCode extends PssgChunk<PssgShaderProgramCode> {
		public String codeType;
		public int codeSize;
		@Override public void print( Resource debug, String tab ) {
			debug.writeLine( tab + name );
			debug.writeLine( tab + "::codeType: " + codeType );
			debug.writeLine( tab + "::codeSize: " + codeSize );
			super.print( debug, tab );
		}
	}
	
	public static class PssgShaderProgramCodeBlock extends PssgChunk<PssgShaderProgramCodeBlock> {
		public String data;
		@Override public void print( Resource debug, String tab ) {
			debug.writeLine( tab + name );
			debug.writeLine( tab + "::data: " + data.length() );
			//debug.writeLine( data );
			super.print( debug, tab );
		}
	}
	
	public static class PssgCgStream extends PssgChunk<PssgCgStream> {
		public String cgStreamName;
		public String cgStreamDataType;
		public String cgStreamRenderType;
		@Override public void print( Resource debug, String tab ) {
			debug.writeLine( tab + name );
			debug.writeLine( tab + "::cgStreamName: " + cgStreamName );
			debug.writeLine( tab + "::cgStreamDataType: " + cgStreamDataType );
			debug.writeLine( tab + "::cgStreamRenderType: " + cgStreamRenderType );
			super.print( debug, tab );
		}
	}
	
	public static class PssgShaderInputDefinition extends PssgChunk<PssgShaderInputDefinition> {
		public String inputName;
		public String type;
		public String format;
		@Override public void print( Resource debug, String tab ) {
			debug.writeLine( tab + name );
			debug.writeLine( tab + "::inputName: " + inputName );
			debug.writeLine( tab + "::type: " + type );
			debug.writeLine( tab + "::format: " + format );
			super.print( debug, tab );
		}
	}
	
	public static class PssgTexture extends PssgChunk<PssgTexture> {
		public String filename;
		public String texelFormat;
		public String blockName;
		public int size;
		public int width = 0;
		public int height = 0;
		public byte[] data = new byte[0];
		@Override public void print( Resource debug, String tab ) {
			debug.writeLine( tab + name + "     [CACHED: " + filename +"]" );
			debug.writeLine( tab + "::width: " + width );
			debug.writeLine( tab + "::height: " + height );
			debug.writeLine( tab + "::texelFormat: " + texelFormat );
			debug.writeLine( tab + "::blockName: " + blockName );
			debug.writeLine( tab + "::size: " + size );
			debug.writeLine( tab + "::data: " + data.length );
			super.print( debug, tab );
		}
	}
	
	public static class PssgSceneNode<T> extends PssgChunk<T> {
		public String id;
		public float[] transform = new float[0];
		public float[] bbox = new float[0];
		@Override public void print( Resource debug, String tab ) {
			debug.writeLine( tab + name + "     [CACHED: " + id +"]" );
			debug.writeLine( tab + "::transform: " + ArrayUtils.asString( transform ) );
			debug.writeLine( tab + "::bbox: " + ArrayUtils.asString( bbox ) );
			super.print( debug, tab );
		}
	}
	
	public static class PssgRootNode extends PssgSceneNode<PssgRootNode> {}
	
	public static class PssgNode extends PssgSceneNode<PssgNode> {

	}
	
	public static class PssgJointNode extends PssgSceneNode<PssgJointNode> {}

	public static class PssgRenderNode extends PssgSceneNode<PssgRenderNode> {

	}
	
	public static class PssgSkinNode extends PssgSceneNode<PssgSkinNode> {
		public int jointCount = 0;
		public String skeleton;
	}
	
	public static class PssgSkinJoint extends PssgChunk<PssgSkinJoint> {
		public String joint;
		@Override public void print( Resource debug, String tab ) {
			debug.writeLine( tab + name );
			debug.writeLine( tab + "::Joint: " + joint );
			super.print( debug, tab );
		}
	}
	
	public static class PssgModifierNetworkInstance extends PssgChunk<PssgModifierNetworkInstance> {
		public String id;
		public String indices;
		public String shader;
		@Override public void print( Resource debug, String tab ) {
			debug.writeLine( tab + name );
			debug.writeLine( tab + "::id: " + id );
			debug.writeLine( tab + "::indices: " + indices );
			debug.writeLine( tab + "::shader: " + shader );
			super.print( debug, tab );
		}
	}
	
	public static class PssgRenderStreamInstance extends PssgChunk<PssgRenderStreamInstance> {
		public String id;
		public String indices;
		public String shader;
		@Override public void print( Resource debug, String tab ) {
			debug.writeLine( tab + name );
			debug.writeLine( tab + "::id: " + id );
			debug.writeLine( tab + "::indices: " + indices );
			debug.writeLine( tab + "::shader: " + shader );
			super.print( debug, tab );
		}
	}
	
	public static class PssgRenderInstanceSource extends PssgChunk<PssgRenderInstanceSource> {
		public String source;
		@Override public void print( Resource debug, String tab ) {
			debug.writeLine( tab + name );
			debug.writeLine( tab + "::source: " + source );
			super.print( debug, tab );
		}
	}
}