package rin.util.dcode.pssg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import rin.util.bio.BIOBuffer;
import rin.util.bio.BIOFile;
import rin.util.bio.BIOTypes.Type;
import static rin.util.bio.BIOTypes.*;
import static rin.util.dcode.pssg.PSSGTypes.*;

public class PSSGFile extends BIOFile {
	public static enum DataOnlyChunks {
		BOUNDINGBOX										( PSSGFLOAT ),
		DATA											( null ),
		DATABLOCKDATA									( null ),
		DATABLOCKBUFFERED								( null ),
		INDEXSOURCEDATA									( null ),
		INVERSEBINDMATRIX								( PSSGFLOAT ),
		MODIFIERNETWORKINSTANCEUNIQUEMODIFIERINPUT		( null ),
		RENDERINTERFACEBOUNDBUFFERED					( null ),
		SHADERINPUT										( UINT8 ),
		SHADERPROGRAMCODEBLOCK							( CHAR ),
		SKINJOINT										( UINT8 ),
		TEXTUREIMAGEBLOCKDATA							( UINT8 ),
		TRANSFORM										( PSSGFLOAT );
		
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
		CODETYPE				( PSSGSTRING, 1 ),
		CREATOR					( PSSGSTRING, 1 ),
		CREATIONMACHINE			( PSSGSTRING, 1 ),
		INDICES					( PSSGSTRING, 1 ),
		JOINTCOUNT				( UINT32, 1 ),
		JOINT					( PSSGSTRING, 1 ),
		SCALE					( PSSGFLOAT, 3 ),
		SHADER					( PSSGSTRING, 1 ),
		SKELETON				( PSSGSTRING, 1 ),
		SOURCE					( PSSGSTRING, 1 ),
		SOURCEID				( UINT32, 1 ),
		STOPTRAVERSAL			( UINT32, 1 ),
		STREAM					( PSSGSTRING, 1 ),
		STREAMID				( UINT32, 1 ),
		MATRIXCOUNT				( UINT32, 1 ),
		NICKNAME				( PSSGSTRING, 1 ),
		ID						( PSSGSTRING, 1 );
		
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
	
	public static final PSSG PSSG = new PSSG();
	
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
					System.out.println( "matrix: " + BIOBuffer.asString( m.data ) );
				}
			}
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
			System.out.println( PSSGFile.getTabs() + this.id + " [" + this.transform + "] [" + this.bbox + "]" );
			PSSGFile.depth++;
			for( Node n : this.children )
				n.print();
			PSSGFile.depth--;
		}
	}
	
	public static class JointNode extends Node {}
	
	public static class RIStream {
		int id;
		String stream;
	}
	
	public static class RISource {
		String source;
	}
	
	public static class RenderInstanceStream {
		String sourceId;
		String streamId;
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
		RenderStreamInstance instance;
		
		public void print() {
			if( this.instance != null ) {
				System.out.println( PSSGFile.getTabs() + "RENDERNODE " + this.instance.id );
				PSSGFile.depth++;
				for( RIStream ri : this.instance.index )
					System.out.println( PSSGFile.getTabs() + "Indices " + ri.id + ": " + ri.stream );
				System.out.println( PSSGFile.getTabs() + "instance: " + this.instance.source.source );
				for( RenderInstanceStream ri : this.instance.streams )
					System.out.println( PSSGFile.getTabs() + "Source: " + ri.sourceId + " Stream: " + ri.streamId );
				PSSGFile.depth--;
			}
		}
	}
	
	public static class SkinNode extends RenderNode {
		int jointCount;
		String skeleton;
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
		
		public Skeleton skeleton = new Skeleton();
		public RootNode rootNode = null;
		public Node getNode( String id ) {
			if( this.rootNode.id.toUpperCase().equals( id.toUpperCase() ) )
				return this.rootNode;
			
			for( Node n : this.rootNode.children )
				if( n.id.toUpperCase().equals( id.toUpperCase() ) )
					return n;
			return null;
		}
		
		public RenderNode getRenderNode( String id ) { return (RenderNode)this.rootNode.find( id ); }
		
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

			if( item.name.equals( "SKINJOINT" ) ) {
				SkinJoint node = new SkinJoint();
				node.id = item.getProperty( "joint" ).data;
				this.rootNode.find( item.parent.getProperty( "id" ).data ).children.add( node );
			} else if( name.equals( "ROOTNODE" ) ) {
				if( this.rootNode == null ) {
					this.rootNode = new RootNode();
					this.rootNode.id = id;
					this.rootNode.stopTraversal = Integer.parseInt( parent.getProperty( "stopTraversal" ).data );
				}
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
		public String name;
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
			for( PSSGChunk<?> c : this.children )
				if( c.name.toUpperCase().equals( name.toUpperCase() ) )
					res.add( c );
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
			String res = PSSGFile.getTabs() + "NODE " + this.name + "\n"+
					PSSGFile.getTabs() + "   " + "Index: " + this.index + "\n" +
					PSSGFile.getTabs() + "   " + "Size: " + this.chunksize + "\n" +
					PSSGFile.getTabs() + "   " + "Property Size: " + this.propsize + "\n";
			for( PSSGProperty<?> p : this.properties ) { res += p.toString(); }
			
			PSSGFile.depth++;
			for( PSSGChunk<?> c : this.children )
				res += c.toString();
			PSSGFile.depth--;
			
			return res;
		}
	}
	
	public ArrayList<PSSGChunk<?>> getChunks( String name ) {
		ArrayList<PSSGChunk<?>> res = new ArrayList<PSSGChunk<?>>();
		for( PSSGChunk<?> c : PSSG.root.children )
			if( c.name.toUpperCase().equals( name.toUpperCase() ) )
				res.add( c );
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
			String res =  PSSGFile.getTabs() + "PROPERTY " + this.name + "\n" +
					PSSGFile.getTabs() + "Index: " + this.index + "\n" +
					PSSGFile.getTabs() + "Size: " + this.size + "\n" + 
					PSSGFile.getTabs() + "Data: " + this.data + "\n";
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
		PSSG.rootNode.print();
	}
	
	@Override public void write() {}
}
