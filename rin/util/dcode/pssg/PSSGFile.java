package rin.util.dcode.pssg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import rin.engine.util.ArrayUtils;
import rin.util.RinUtils;
import rin.util.bio.BIOBuffer;
import rin.util.bio.BIOFile;
import rin.util.bio.BIOTypes.Type;
import static rin.util.bio.BIOTypes.*;
import static rin.util.dcode.pssg.PSSGTypes.*;

public class PSSGFile extends BIOFile {
	public static enum DataOnlyChunks {
		BOUNDINGBOX										( FLOAT32 ),
		DATA											( null ),
		DATABLOCKDATA									( null ),
		DATABLOCKBUFFERED								( null ),
		INDEXSOURCEDATA									( null ),
		INVERSEBINDMATRIX								( FLOAT32 ),
		MODIFIERNETWORKINSTANCEUNIQUEMODIFIERINPUT		( null ),
		RENDERINTERFACEBOUNDBUFFERED					( null ),
		SHADERINPUT										( UINT8 ),
		SHADERPROGRAMCODEBLOCK							( CHAR ),
		SKINJOINT										( UINT8 ),
		TEXTUREIMAGEBLOCKDATA							( UINT8 ),
		TRANSFORM										( FLOAT32 );
		
		public Type<?> type;
		
		private <T> DataOnlyChunks( Type<T> type ) { this.type = type; }
		
		public static DataOnlyChunks find( String name ) {
			for( DataOnlyChunks c : DataOnlyChunks.values() )
				if( c.toString().equals( name.toUpperCase() ) )
					return c;
			return null;
		}
	}
	
	public static enum PropertyMap {
		CGSTREAMDATATYPE		( PSSGSTRING, 1 ),
		CGSTREAMNAME			( PSSGSTRING, 1 ),
		CGSTREAMRENDERTYPE		( PSSGSTRING, 1 ),
		CODETYPE				( PSSGSTRING, 1 ),
		COUNT					( UINT32, 1 ),
		CREATOR					( PSSGSTRING, 1 ),
		CREATIONMACHINE			( PSSGSTRING, 1 ),
		DATABLOCK				( PSSGSTRING, 1 ),
		DATATYPE				( PSSGSTRING, 1 ),
		ELEMENTCOUNT			( UINT32, 1 ),
		FORMAT					( PSSGSTRING, 1 ),
		HEIGHT					( UINT32, 1 ),
		INDICES					( PSSGSTRING, 1 ),
		JOINTCOUNT				( UINT32, 1 ),
		JOINT					( PSSGSTRING, 1 ),
		MAXELEMENTCOUNT			( UINT32, 1 ),
		NAME					( PSSGSTRING, 1 ),
		NUMBERMIPMAPLEVELS		( UINT32, 1 ),
		RENDERTYPE				( PSSGSTRING, 1 ),
		RENDERTYPENAME			( PSSGSTRING, 1 ),
		SCALE					( FLOAT32, 3 ),
		SHADER					( PSSGSTRING, 1 ),
		SHADERGROUP				( PSSGSTRING, 1 ),
		SIZE					( UINT32, 1 ),
		SKELETON				( PSSGSTRING, 1 ),
		SOURCE					( PSSGSTRING, 1 ),
		SOURCEID				( UINT32, 1 ),
		STOPTRAVERSAL			( UINT32, 1 ),
		STREAM					( PSSGSTRING, 1 ),
		STREAMID				( UINT32, 1 ),
		STRIDE					( UINT32, 1 ),
		TYPE					( PSSGSTRING, 1 ),
		TEXELFORMAT				( PSSGSTRING, 1 ),
		TEXTURE					( PSSGSTRING, 1 ),
		MATRIXCOUNT				( UINT32, 1 ),
		NICKNAME				( PSSGSTRING, 1 ),
		OFFSET					( UINT32, 1 ),
		PRIMITIVE				( PSSGSTRING, 1 ),
		ID						( PSSGSTRING, 1 ),
		WIDTH					( UINT32, 1 );
		
		public Type<?> type;
		public int amount;
		
		private <T> PropertyMap( Type<T> type, int amount ) { this.type = type; this.amount = amount; }
		
		public static PropertyMap find( String name ) {
			for( PropertyMap p : PropertyMap.values() )
				if( p.toString().equals( name.toUpperCase() ) )
					return p;
			return null;
		}
	}
	
	private static int depth = 0;
	private static String getTabs() { String res = ""; for( int i = 0; i < PSSGFile.depth; i++ ) { res += "   "; } return res; }
	
	public static PSSGFile instance;
	public PSSGFile( String file ) { super( file ); PSSGFile.instance = this; }
	
	public static class Master {
		public ArrayList<VNT> vnt = new ArrayList<VNT>();
		public ArrayList<Skin> skin = new ArrayList<Skin>();
		public ArrayList<Texture> textures = new ArrayList<Texture>();
	}
	
	public static class VNT {
		public short[] sindices = new short[0];
		public int[] iindices = new int[0];
		
		public float[] v = new float[0];
		public float[] n = new float[0];
		public float[] t = new float[0];
	}
	
	public static class Skin {
		public short[] sindices = new short[0];
		public int[] iindices = new int[0];
		
		public short[] skindices = new short[0];
		public float[] weights = new float[0];
		public float[] sv = new float[0];
		public float[] sn = new float[0];
		public float[] st = new float[0];
	}
	
	public static class Texture {
		public String format;
		public String id;
		public int width;
		public int height;
		public byte[] data;
	}
	
	public static class ShaderGroup extends Node {
		
	}
	
	public static class ShaderInputDefinition extends Node {
		public String name;
		public String type;
		public String format;
	}
	
	public static class ShaderInput {
		
	}
	
	public static final PSSG PSSG = new PSSG();
	public Master getMaster() { return PSSG.master; }
	
	public static class Skeleton {
		public HashMap<String, ArrayList<PSSGChunk<?>>> nodes = new HashMap<String, ArrayList<PSSGChunk<?>>>();
		public void addMatrix( PSSGChunk<?> skeleton, PSSGChunk<?> matrix ) {
			String name = skeleton.getProperty( "id" ).data;
			if( !this.nodes.containsKey( name ) )
				this.nodes.put( name, new ArrayList<PSSGChunk<?>>() );
			
			this.nodes.get( name ).add( matrix );
		}
		
		public void print() {
			for( String name : this.nodes.keySet() ) {
				ArrayList<PSSGChunk<?>> current = this.nodes.get( name );
				System.out.println( "Skeleton " + name );
				for( PSSGChunk<?> m : current ) {
					System.out.println( " matrix: " + BIOBuffer.asString( m.data ) );
				}
			}
		}
	}
	
	public static class DataBlock extends Node {
		public String renderType;
		public String dataType;
		public int size;
		public int elementCount;
		public int offset;
		public int stride;
		public float[] fdata = new float[0];
		public int[] idata = new int[0];
		public short[] sdata = new short[0];
		
		public void print() {
			System.out.println( PSSGFile.getTabs() + "DATA " + this.id + " [" + this.renderType + "] [" + this.dataType + "]" );
			System.out.println( PSSGFile.getTabs() + "   " + "i: " + this.idata.length + " f: " + this.fdata.length + " s: " + this.sdata.length );
		}
	}
	
	public static class Node {
		String id;
		String nickname;
		int stopTraversal;
		
		String transform;
		String bbox;
		
		public ArrayList<Node> children = new ArrayList<Node>();
		public Node find( String id ) {
			if( this.id != null )
				if( this.id.toUpperCase().equals( id.toUpperCase() ) )
					return this;
			
			Node node;
			for( Node n : this.children ) {
				if( n.id.toUpperCase().equals( id.toUpperCase() ) )
					return n;
				else
					if( ( node = n.find( id ) ) != null )
						return node;
			}
			
			return null;
		}
		
		public void print() {
			System.out.println( this.getClass().toString() + PSSGFile.getTabs() + this.id + " [" + this.transform + "] [" + this.bbox + "]" );
			PSSGFile.depth++;
			for( Node n : this.children )
				/*if( !(n instanceof RenderNode ) )
					if( !(n instanceof DataBlock ) )
						if( !(n instanceof RenderDataSource ) )
							if( !(n instanceof RenderIndexSource ) )*/
								n.print();
			PSSGFile.depth--;
		}
	}
	
	public static class JointNode extends Node {}
	
	public static class RenderDataSource extends Node {
		public void print() {
			System.out.println( PSSGFile.getTabs() + "DATASOURCE: " + this.id );
			PSSGFile.depth++;
			for( Node n : this.children )
				n.print();
			PSSGFile.depth--;
		}
	}
	
	public static class RenderIndexSource extends Node {
		public String primitive;
		public String format;
		public int count;
		public short[] sdata = new short[0];
		public int[] idata = new int[0];
		
		public void print() {
			System.out.println( PSSGFile.getTabs() + "INDEXSOURCE: " + this.id + " [s" + this.sdata.length + "] [i" + this.idata.length +"]" );
			System.out.println( PSSGFile.getTabs() + "count: " + this.count + " format: " + this.format + " prim: " + this.primitive );
		}
	}
	
	public static class RenderStream extends Node {
		public String datablock;
		
		public void print() {
			System.out.println( PSSGFile.getTabs() + "stream: " + this.id + " >> DataBlock: " + this.datablock );
		}
	}
	
	public static class RIStream {
		public int id;
		public String stream;
	}
	
	public static class RISource {
		public String source;
	}
	
	public static class RenderInstanceStream {
		public String sourceId = "";
		public String streamId = "";
	}
	
	public static class RenderStreamInstance {
		public String id;
		public int sourceCount;
		public String indices;
		public int streamCount;
		public String shader;
		public int allocationStrategy;
		public ArrayList<RIStream> index = new ArrayList<RIStream>();
		public RISource source;
		public ArrayList<RenderInstanceStream> streams = new ArrayList<RenderInstanceStream>();
	}
	
	public static class RenderNode extends Node {
		public RenderStreamInstance instance;
		
		public void print() {
			if( this.instance != null ) {
				System.out.println( PSSGFile.getTabs() + "RENDERNODE " + this.instance.id );
				PSSGFile.depth++;
				for( RIStream ri : this.instance.index )
					System.out.println( PSSGFile.getTabs() + "Indices " + ri.id + ": " + ri.stream );
				System.out.println( PSSGFile.getTabs() + "instance: " + this.instance.source.source );
				for( RenderInstanceStream ri : this.instance.streams )
					System.out.println( PSSGFile.getTabs() + "Source: " + ri.sourceId + " Stream: " + ri.streamId );
				for( Node n : this.children )
					System.out.println( PSSGFile.getTabs() + "Joint: " + n.id );
				PSSGFile.depth--;
			}
		}
	}
	
	public static class SkinNode extends RenderNode {
		public int jointCount;
		public String skeleton;
		
		public void print() {
			System.out.println("SKINNODE");
			super.print();
		}
	}
	
	public static class SkinJoint extends Node {
		public String joint;
	}
	
	public static class RootNode extends Node {}
	
	public static class PSSG {
		public long size;
		public long propertyTypes;
		public long chunkTypes;
		public PSSGChunk<?> root;
		public Master master = new Master();
		
		public Skeleton skeleton = new Skeleton();
		public RootNode rootNode = new RootNode();
		public Node getNode( String id ) {
			if( this.rootNode.id.toUpperCase().equals( id.toUpperCase() ) )
				return this.rootNode;
			
			for( Node n : this.rootNode.children )
				if( n.id.toUpperCase().equals( id.toUpperCase() ) )
					return n;
			return null;
		}
		
		//public RenderNode getRenderNode( String id ) { return (RenderNode)this.rootNode.find( id ); }
		public ArrayList<RenderNode> getRenderNodes() {
			ArrayList<RenderNode> res = new ArrayList<RenderNode>();
			for( Node n : this.rootNode.children )
				if( n instanceof RenderNode )
					res.add( (RenderNode)n );
			return res;
		}
		
		public DataBlock getDataBlockById( String id ) {
			for( Node n : this.rootNode.children )
				if( n instanceof DataBlock )
					if( n.id.toUpperCase().equals( id.substring( 1 ).toUpperCase() ) )
						return (DataBlock)n;
			return null;
		}
		
		public ArrayList<RenderDataSource> getDataSources() {
			ArrayList<RenderDataSource> res = new ArrayList<RenderDataSource>();
			for( Node n : this.rootNode.children )
				if( n instanceof RenderDataSource )
					res.add( (RenderDataSource)n );
			return res;
		}
		
		public void addSkeleton( PSSGChunk<?> skel ) {
			
		}
		
		public void addToStream( PSSGChunk<?> rendernode, PSSGChunk<?> item ) {
			String id = rendernode.getProperty( "id" ).data;
			
			RenderNode node = (RenderNode)this.rootNode.find( id );
			if( item.name.equals( "RENDERSTREAMINSTANCE" ) ) {
				node.instance = new RenderStreamInstance();
				node.instance.id = item.getProperty( "id" ).data;
				node.instance.indices = item.getProperty( "indices" ).data;
				node.instance.shader = item.getProperty( "shader" ).data;
			} else if( item.name.equals( "MODIFIERNETWORKINSTANCE" ) ) {
				node.instance = new RenderStreamInstance();
				node.instance.id = item.getProperty( "id" ).data;
				node.instance.indices = item.getProperty( "indices" ).data;
				node.instance.shader = item.getProperty( "shader" ).data;
			} else if( item.name.equals( "MODIFIERNETWORKINSTANCEMODIFIERINPUT" ) ) {
				
			} else if( item.name.equals( "RISTREAM" ) ) {
				RIStream stream = new RIStream();
				stream.id = Integer.parseInt( item.getProperty( "id" ).data );
				stream.stream = item.getProperty( "stream" ).data;
				node.instance.index.add( stream );
			} else if( item.name.equals( "RENDERINSTANCESOURCE" ) ) {
				RISource source = new RISource();
				source.source = item.getProperty( "source" ).data;
				node.instance.source = source;
			} else if( item.name.equals( "RENDERINSTANCESTREAM" ) ) {
				RenderInstanceStream stream = new RenderInstanceStream();
				stream.sourceId = item.getProperty( "sourceID" ).data;
				stream.streamId = item.getProperty( "streamID" ).data;
				node.instance.streams.add( stream );
			}
			
			if( rendernode.name.equals( "RENDERNODE" ) ) {
			} else if( rendernode.name.equals( "SKINNODE" ) ) {
			}
		}
		
		public void addNode( PSSGChunk<?> parent, PSSGChunk<?> item ) {
			String name = parent.name;
			String iname = item.name;
			String id = parent.getProperty( "id" ).data;

			if( item.name.equals( "DATABLOCK" ) ) {
				DataBlock block = new DataBlock();
				block.id = item.getProperty( "id" ).data;
				block.size = Integer.parseInt( item.getProperty( "size" ).data );
				block.elementCount = Integer.parseInt( item.getProperty( "elementCount" ).data );
				this.rootNode.children.add( block );
			} else if( item.name.equals( "DATABLOCKSTREAM" ) ) {
				DataBlock tmp = (DataBlock)this.rootNode.find( id );
				tmp.dataType = item.getProperty( "dataType" ).data;
				tmp.renderType = item.getProperty( "renderType" ).data;
				tmp.offset = Integer.parseInt( item.getProperty( "offset" ).data );
				tmp.stride = Integer.parseInt( item.getProperty( "stride" ).data );
			} else if( item.name.equals( "DATABLOCKDATA" ) ) {
				DataBlock tmp = (DataBlock)this.rootNode.find( id );
				String dt = tmp.dataType.toUpperCase();
				if( dt.equals( "FLOAT3" ) || dt.equals( "FLOAT4" ) ) {
					tmp.fdata = new float[tmp.elementCount * (tmp.stride / 4)];
					for( int i = 0; i < tmp.elementCount * (tmp.stride / 4); i++ )
						tmp.fdata[i] = FLOAT.getData( PSSGFile.instance.getBuffer().actual() );
				} else if( dt.equals( "UCHAR4" ) ) {
					tmp.sdata = new short[tmp.elementCount * (tmp.stride / 4) * 4];
					for( int i = 0; i < tmp.elementCount * (tmp.stride / 4) * 4; i++ )
						tmp.sdata[i] = UINT8.getData( PSSGFile.instance.getBuffer().actual() );
				}
			} else if( item.name.equals( "RENDERDATASOURCE" ) ) {
				RenderDataSource ds = new RenderDataSource();
				ds.id = item.getProperty( "id" ).data;
				this.rootNode.children.add( ds );
			} else if( item.name.equals( "RENDERINDEXSOURCE" ) ) {
				RenderIndexSource src = new RenderIndexSource();
				src.id = item.getProperty( "id" ).data;
				src.format = item.getProperty( "format" ).data;
				src.primitive = item.getProperty( "primitive" ).data;
				src.count = Integer.parseInt( item.getProperty( "count" ).data );
				this.rootNode.find( parent.getProperty( "id" ).data ).children.add( src );
			} else if( item.name.equals( "INDEXSOURCEDATA" ) ) {
				RenderIndexSource src = (RenderIndexSource)this.rootNode.find( id );
				String f = src.format.toUpperCase();
				if( f.equals( "UCHAR" ) ) {
					src.sdata = new short[src.count];
					for( int i = 0; i < src.count; i++ )
						src.sdata[i] = UINT8.getData( PSSGFile.instance.getBuffer().actual() );
				} else if( f.equals( "USHORT" ) ) {
					src.idata = new int[src.count];
					for( int i = 0; i < src.count; i++ )
						src.idata[i] = UINT16.getData( PSSGFile.instance.getBuffer().actual() );
				}
			} else if( item.name.equals( "RENDERSTREAM" ) ) {
				RenderStream rs = new RenderStream();
				rs.id = item.getProperty( "id" ).data;
				rs.datablock = item.getProperty( "datablock" ).data;
				this.rootNode.find( parent.getProperty( "id" ).data ).children.add( rs );
			} else if( item.name.equals( "SKINJOINT" ) ) {
				SkinJoint node = new SkinJoint();
				node.id = item.getProperty( "joint" ).data;
				this.rootNode.find( item.parent.getProperty( "id" ).data ).children.add( node );
			} else if( name.equals( "ROOTNODE" ) ) {
					this.rootNode.id = id;
					this.rootNode.stopTraversal = Integer.parseInt( parent.getProperty( "stopTraversal" ).data );
			} else if( name.equals( "NODE" ) ) {
				if( this.rootNode.find( id ) == null ) {
					Node node = new Node();
					node.id = id;
					node.stopTraversal = Integer.parseInt( parent.getProperty( "stopTraversal" ).data );
					node.nickname = parent.getProperty( "nickname" ).data;
					this.rootNode.find( parent.parent.getProperty( "id" ).data ).children.add( node );
				}
			} else if( name.equals( "JOINTNODE" ) ) {
				if( this.rootNode.find( id ) == null ) {
					Node node = new JointNode();
					node.id = id;
					node.stopTraversal = Integer.parseInt( parent.getProperty( "stopTraversal" ).data );
					node.nickname = parent.getProperty( "nickname" ).data;
					this.rootNode.find( parent.parent.getProperty( "id" ).data ).children.add( node );
				}
			} else if( name.equals( "RENDERNODE" ) ) {
				if( this.rootNode.find( id ) == null ) {
					Node node = new RenderNode();
					node.id = id;
					node.stopTraversal = Integer.parseInt( parent.getProperty( "stopTraversal" ).data );
					node.nickname = parent.getProperty( "nickname" ).data;
					this.rootNode.find( parent.parent.getProperty( "id" ).data ).children.add( node );
				}
			} else if( name.equals( "SKINNODE" ) ) {
				if( this.rootNode.find( id ) == null ) {
					SkinNode node = new SkinNode();
					node.id = id;
					node.stopTraversal = Integer.parseInt( parent.getProperty( "stopTraversal" ).data );
					node.nickname = parent.getProperty( "nickname" ).data;
					node.jointCount = Integer.parseInt( parent.getProperty( "jointCount" ).data );
					node.skeleton = parent.getProperty( "skeleton" ).data;
					//System.out.println( node.skeleton + " " + node.nickname + " " + node.jointCount );
					this.rootNode.find( parent.parent.getProperty( "id" ).data ).children.add( node );
				}
			}
			
			if( iname.equals( "TRANSFORM" ) ) {
				if( name.equals( "ROOTNODE" ) )
					this.rootNode.transform = BIOBuffer.asString( item.data );
				else
					if( this.rootNode.find( parent.getProperty( "id" ).data ) != null )
						this.rootNode.find( parent.getProperty( "id" ).data ).transform = BIOBuffer.asString( item.data );
			} else if( iname.equals( "BOUNDINGBOX" ) ) {
				if( name.equals( "ROOTNODE" ) )
					this.rootNode.bbox = BIOBuffer.asString( item.data );
				else
					if( this.rootNode.find( parent.getProperty( "id" ).data ) != null )
						this.rootNode.find( parent.getProperty( "id" ).data ).bbox = BIOBuffer.asString( item.data );
			}
			
		}
		
		public HashMap<Integer, PSSGChunkInfo> chunkInfoMap = new HashMap<Integer, PSSGChunkInfo>();
		public PSSGChunkInfo getChunkInfo( int index ) { return chunkInfoMap.get( index ); }
		public PSSGChunkInfo getChunkInfo( String name ) {
			for( int i : this.chunkInfoMap.keySet() )
				if( this.chunkInfoMap.get( i ).name.toUpperCase().equals( name.toUpperCase() ) )
					return this.chunkInfoMap.get( i );
			
			return null;
		}
		
		public HashMap<Integer, PSSGPropertyInfo> propInfoMap = new HashMap<Integer, PSSGPropertyInfo>();
		public PSSGPropertyInfo getPropertyInfo( int index ) { return propInfoMap.get( index ); }
		public PSSGPropertyInfo getPropertyInfo( String name ) {
			for( int i : this.propInfoMap.keySet() )
				if( this.propInfoMap.get( i ).name.toUpperCase().equals( name.toUpperCase() ) )
					return this.propInfoMap.get( i );
			
			return null;
		}
	}
	
	public static class PSSGChunkInfo extends PSSGPropertyInfo {
		public long props;
		public PSSGPropertyInfo[] properties;
		
		public void info() {
			System.out.println( "PARAMETER " + this.name );
			System.out.println( "Index:  " + this.index );
			System.out.println( "Property Count: " + this.props );
			for( PSSGPropertyInfo p : this.properties )
				p.info();
		}
	}	
	
	public static class PSSGPropertyInfo {
		public long index;
		public String name;
		
		public void info() {
			System.out.println( "PROPERTY " + this.name );
			System.out.println( "Index: " + this.index );
		}
	}
	
	public static class PSSGChunk<T> {
		public long index;
		public String name = "";
		public long chunksize;
		public long propsize;
		public boolean hasData = false;
		public T[] data;
		
		public PSSGChunk() {}
		
		public ArrayList<PSSGProperty<?>> properties = new ArrayList<PSSGProperty<?>>();
		public void printProperties() { for( PSSGProperty<?> p : this.properties ) System.out.println( p.toString() ); }
		public PSSGProperty<?> getProperty( String name ) {
			for( PSSGProperty<?> p : this.properties )
				if( p.name.toUpperCase().equals( name.toUpperCase() ) )
					return p;
			return null;
		}
		
		public PSSGChunk<?> parent = null;
		public ArrayList<PSSGChunk<?>> children = new ArrayList<PSSGChunk<?>>();
		public ArrayList<PSSGChunk<?>> getChildren( String name ) {
			ArrayList<PSSGChunk<?>> res = new ArrayList<PSSGChunk<?>>();
			Stack<PSSGChunk<?>> stack = new Stack<PSSGChunk<?>>();
			stack.push( this );
			while( !stack.empty() ) {
				PSSGChunk<?> tmp = stack.pop();
				for( PSSGChunk<?> c : tmp.children )
					stack.push( c );
				
				if( tmp.name != null )
					if( tmp.name.toUpperCase().equals( name.toUpperCase() ) )
						res.add( tmp );
			}
			return res;
		}
		
		public void info() { System.out.println( this.toString() ); PSSGFile.depth = 0; }
		public void tree() {
			System.out.println( PSSGFile.getTabs() + this.name );
			
			PSSGFile.depth++;
			for( PSSGChunk<?> c : this.children )
				c.tree();
			PSSGFile.depth--;
		}
		
		public String toString() {
			String res = PSSGFile.getTabs() + "NODE " + this.name + "\n";
					//PSSGFile.getTabs() + "   " + "Index: " + this.index + "\n" +
					//PSSGFile.getTabs() + "   " + "Size: " + this.chunksize + "\n" +
					//PSSGFile.getTabs() + "   " + "Property Size: " + this.propsize + "\n";
			for( PSSGProperty<?> p : this.properties ) { res += p.toString(); }
			
			PSSGFile.depth++;
			for( PSSGChunk<?> c : this.children )
				res += c.toString();
			PSSGFile.depth--;
			
			return res;
		}
	}
	
	public RenderDataSource getDataSourceById( String id ) {
		for( Node n : PSSG.rootNode.children )
			if( n instanceof RenderDataSource )
				if( n.id.toUpperCase().equals( id.toUpperCase() ) )
					return (RenderDataSource)n;
		return null;
	}
	
	public PSSGChunk<?> getChunkTypeById( String type, String id ) {
		for( PSSGChunk<?> c : this.getChunks( type ) )
			if( c.getProperty( "id" ) != null )
				if( c.getProperty( "id" ).data.toUpperCase().equals( id.toUpperCase() ) )
					return c;
		return null;
	}
	
	public PSSGChunk<?> getSourceById( String id ) { return this.getChunkTypeById( "RENDERDATASOURCE", id ); }
	public PSSGChunk<?> getTextureByShaderInstanceId( String id ) {
		PSSGChunk<?> instance = this.getChunkTypeById( "SHADERINSTANCE", id );
		String group = instance.getProperty( "shaderGroup" ).data.substring( 1 );
		PSSGChunk<?> sg = this.getChunkTypeById( "SHADERGROUP", group );
		for( PSSGChunk<?> c : sg.getChildren( "SHADERINPUT" ) )
			if( c.getProperty( "texture" ) != null )
				return this.getChunkTypeById( "TEXTURE", c.getProperty( "texture" ).data.substring( 1 ) );
		return null;
	}
	
	public byte[] getTextureData( String id ) {
		for( Texture t : PSSG.master.textures )
			if( t.id.toUpperCase().equals( id.toUpperCase() ) )
				return t.data;
		return null;
	}
	
	public ArrayList<PSSGChunk<?>> getChunks( String name ) {
		ArrayList<PSSGChunk<?>> res = new ArrayList<PSSGChunk<?>>();
		Stack<PSSGChunk<?>> stack = new Stack<PSSGChunk<?>>();
		stack.push( PSSG.root );
		while( !stack.empty() ) {
			PSSGChunk<?> tmp = stack.pop();
			for( PSSGChunk<?> c : tmp.children )
				stack.push( c );
			
			if( tmp.name != null )
				if( tmp.name.toUpperCase().equals( name.toUpperCase() ) )
					res.add( tmp );
		}
		return res;
	}
	
	public static class PSSGProperty<T> {
		public long index;
		public long size;
		public String name;
		public Type<T> type;
		public int amount;
		public String data;
		
		public PSSGProperty( Type<T> type, long index, long size ) { this.type = type; this.index = index; this.size = size; }
		
		public String toString() {
			PSSGFile.depth++;
			String res =  PSSGFile.getTabs() + "PROPERTY " + this.name + ": " + this.data + "\n";
					//PSSGFile.getTabs() + "Index: " + this.index + "\n" +
					//PSSGFile.getTabs() + "Size: " + this.size + "\n" + 
					//PSSGFile.getTabs() + "Data: " + this.data + "\n";
			PSSGFile.depth--;
			return res;
		}
	}
	
	public void printInfo() { this.printChunkInfo(); this.printPropertyInfo(); }
	public void printChunkInfo() { this.printMap( PSSGFile.PSSG.chunkInfoMap, "PARAMETER_MAP" ); }
	public void printPropertyInfo() { this.printMap( PSSGFile.PSSG.propInfoMap, "PROPERTY_MAP" ); }
	
	private void printMap( Map<Integer,? extends PSSGPropertyInfo> m, String title ) {
		if( !title.equals( "" ) )
			System.out.println( "_______" + title + "_______" );
		
		for( int i : m.keySet() )
			System.out.println( "|\t" + i + "\t" + m.get( i ).name );
		
		System.out.println();
	}
	
	@Override public void read() {
		this.addChunk( PSSGChunks.PSSGHEADER );
		
		for( int i = 0; i < PSSG.chunkTypes; i++ )
			this.addChunk( PSSGChunks.INFOLIST );
		
		this.addChunk( PSSGChunks.PSSGDATABASE );
		//PSSG.rootNode.print();
		
		/*for( RenderDataSource src : PSSG.getDataSources() ) {
			VNT vnt = new VNT();
			Skin skin = new Skin();
			//System.out.println( src.id );
			String res = "";
			for( Node n : src.children ) {
				if( n instanceof RenderIndexSource ) {
					RenderIndexSource tmp = (RenderIndexSource)n;
					//System.out.println( tmp.format );
					if( tmp.format.toUpperCase().equals( "UCHAR" ) ) {
						vnt.sindices = tmp.sdata;
						skin.sindices = tmp.sdata;
					} else {
						vnt.iindices = tmp.idata;
						skin.iindices = tmp.idata;
					}
				} else if( n instanceof RenderStream ) {
					RenderStream tmp = (RenderStream)n;
					DataBlock db = PSSG.getDataBlockById( tmp.datablock );
					String rt = db.renderType.toUpperCase();
					if( rt.equals( "VERTEX" ) ) {
						res += "v ";
						vnt.v = db.fdata;
					} else if( rt.equals( "NORMAL" ) ) {
						res += "n ";
						vnt.n = db.fdata;
					} else if( rt.equals( "ST" ) ) {
						res += "st ";
						vnt.t = db.fdata;
						skin.st = db.fdata;
					} else if( rt.equals( "SKININDICES" ) ) {
						res += "skindices ";
						skin.skindices = db.sdata;
					} else if( rt.equals( "SKINWEIGHTS" ) ) {
						res += "skinweights ";
						skin.weights = db.fdata;
					} else if( rt.equals( "SKINNABLEVERTEX" ) ) {
						res += "skinvertex ";
						skin.sv = db.fdata;
					} else if( rt.equals( "SKINNABLENORMAL" ) ) {
						res += "skinnormal ";
						skin.sn = db.fdata;
					} else {
						System.out.println( rt + ": unkown variable!" );
					}
				}
			}
			//System.out.println( res );
			
			if( vnt.v.length != 0 )
				PSSG.master.vnt.add( vnt );
			if( skin.sv.length != 0 ) {
				PSSG.master.skin.add( skin );
			}
		}*/
		
		this.data = new PSSGData();
		ArrayList<PSSGChunk<?>> instances = this.getChunks("MODIFIERNETWORKINSTANCE");
		this.data.sources = new PSSGDataSource[ instances.size() ];
		for( int i = 0; i < instances.size(); i++ ) {
			PSSGDataSource src = new PSSGDataSource();
			if( !instances.get( i ).parent.name.equals( "SKINNODE" ) ) {
				this.data.sources[i] = src;
				continue;
			}
			
			PSSGChunk<?> skin = instances.get( i ).parent;
			src.skeleton = skin.getProperty( "skeleton" ).data.substring( 1 );
			int count = 0;
			for( PSSGChunk<?> c : skin.children ) {
				if( c.name.equals( "SKINJOINT" ) ) {
					src.joints.add( c.getProperty( "joint" ).data.substring( 1 ) );
					src.jmats.add( ArrayUtils.toFloatArray( PSSG.skeleton.nodes.get( src.skeleton ).get( count ).data ) );
				}
			}
			
			String indices = instances.get( i ).getProperty( "indices" ).data.substring( 1 );
			String shader = instances.get( i ).getProperty( "shader" ).data.substring( 1 );
			
			PSSGChunk<?> source = this.getSourceById( indices );
			for( Node n : this.getDataSourceById( source.getProperty( "id" ).data ).children ) {
				if( n instanceof RenderIndexSource ) {
					RenderIndexSource tmp = (RenderIndexSource)n;
					if( tmp.format.toUpperCase().equals( "UCHAR" ) ) {
						src.indices = RinUtils.convert( tmp.sdata ).to( new int[tmp.sdata.length] );
					} else {
						src.indices = tmp.idata;
					}
				}
				else if( n instanceof RenderStream ) {
					RenderStream tmp = (RenderStream)n;
					DataBlock db = PSSG.getDataBlockById( tmp.datablock );
					String rt = db.renderType.toUpperCase();
					if( rt.equals( "VERTEX" ) ) {
						src.v = db.fdata;
					} else if( rt.equals( "NORMAL" ) ) {
						src.n = db.fdata;
					} else if( rt.equals( "ST" ) ) {
						src.t = db.fdata;
					} else if( rt.equals( "SKININDICES" ) ) {
						src.skindices = db.sdata;
					} else if( rt.equals( "SKINWEIGHTS" ) ) {
						src.weights = db.fdata;
					} else if( rt.equals( "SKINNABLEVERTEX" ) ) {
						src.v = db.fdata;
					} else if( rt.equals( "SKINNABLENORMAL" ) ) {
						src.n = db.fdata;
					} else {
						System.out.println( rt + ": unkown variable!" );
					}
				}
			}
			
			PSSGChunk<?> texture = this.getTextureByShaderInstanceId( shader );
			PSSGTexture tex = new PSSGTexture();
			tex.id = texture.getProperty( "id" ).data;
			tex.width = Integer.parseInt( texture.getProperty( "width" ).data );
			tex.height = Integer.parseInt( texture.getProperty( "height" ).data );
			tex.format = texture.getProperty( "texelFormat" ).data;
			tex.data = this.getTextureData( tex.id );
			if( texture.getProperty( "numberMipMapLevels" ) != null )
				tex.mipmaps = Integer.parseInt( texture.getProperty( "numberMipMapLevels" ).data );
			src.texture = tex;
			
			this.data.sources[i] = src;
		}
		this.data.skel = new ActualSkeleton();
		//this.data.skel.print();
		//this.getData().print();
	}
	
	public static class ActualSkeleton {
		public rin.engine.game.entity.animated.Skeleton skel;
		
		public ActualSkeleton() {
			skel = new rin.engine.game.entity.animated.Skeleton();
			System.out.println( "ROOT: " + PSSG.rootNode.id );
			addBone( null, PSSG.rootNode );
		}
		
		private void addBone( rin.engine.game.entity.animated.Bone bone, Node node ) {
			if( bone == null ) {
				bone = skel.addBone( node.id, ArrayUtils.toFloatArray( node.transform.split( " " ) ) );
			} else {
				bone = bone.addBone( node.id, ArrayUtils.toFloatArray( node.transform.split( " " ) ) );
				skel.addBone( bone );
			}

			for( Node n : node.children ) {
				if( !(n instanceof RenderNode ) ) {
					if( !(n instanceof DataBlock ) )
						if( !(n instanceof RenderDataSource ) )
							if( !(n instanceof RenderIndexSource ) )
								addBone( bone, n );
				} else {
					if( n.children.size() > 0 ) {
						RenderNode r = (RenderNode)n;
						//System.out.println( r.id + " appears to be valid...! " + r.instance.indices + " " + r.instance.shader );
						//r.print();
					} else {
						//System.out.println( ((RenderNode)n).id );
					}
				}
			}
		}
		
		public void print() {
			skel.print();
		}
	}
	
	private PSSGData data = null;
	public PSSGData getData() { return this.data; }
	
	public static class PSSGDataSource {
		public String name = "No Name";
		public String skeleton = "";
		
		public int[] indices = new int[0];
		public float[] v = new float[0];
		public float[] n = new float[0];
		public float[] t = new float[0];
		
		public float[] weights = new float[0];
		public short[] skindices = new short[0];
		
		public ArrayList<String> joints = new ArrayList<String>();
		public ArrayList<float[]> jmats = new ArrayList<float[]>();
		
		public PSSGTexture texture = null;
		
		public void print() {
			System.out.println( "\tv: " + this.v.length );
			System.out.println( "\tn: " + this.n.length );
			System.out.println( "\tt: " + this.t.length );
			System.out.println( "\tindices: " + this.indices.length );
			System.out.println( "\tskin weights: " + this.weights.length );
			System.out.println( "\tskin indices: " + this.skindices.length );
			if( this.texture != null )
				this.texture.print();
		}
	}
	
	public static class PSSGTexture {
		public String id;
		public String format;
		public int width;
		public int height;
		public int mipmaps = 0;
		
		public byte[] data = new byte[0];
		
		public void print() {
			System.out.println( "\tTexture " + this.id );
			System.out.println( "\t\tformat: " + this.format );
			System.out.println( "\t\twidth: " + this.width );
			System.out.println( "\t\theight: " + this.height );
		}
	}
	
	public static class PSSGData {
		public PSSGDataSource[] sources;
		public ActualSkeleton skel;
		
		public void print() {
			for( int i = 0; i < this.sources.length; i++ ) {
				System.out.println( "source " + i );
				this.sources[i].print();
			}
		}
	}
	
	@Override public void write() {}
}
