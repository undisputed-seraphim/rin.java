package rin.util.dcode.pssg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import rin.util.bio.BIOFile;
import rin.util.bio.BIOTypes.Type;
import static rin.util.bio.BIOTypes.*;
import static rin.util.dcode.pssg.PSSGTypes.*;

public class PSSGFile extends BIOFile {
	public static enum DataOnlyChunks {
		BOUNDINGBOX,
		DATA,
		DATABLOCKDATA,
		DATABLOCKBUFFERED,
		INDEXSOURCEDATA,
		INVERSEBINDMATRIX,
		MODIFIERNETWORKINSTANCEUNIQUEMODIFIERINPUT,
		RENDERINTERFACEBOUNDBUFFERED,
		SHADERINPUT,
		SHADERPROGRAMCODEBLOCK,
		TEXTUREIMAGEBLOCKDATA,
		TRANSFORM;
		
		public static boolean find( String name ) {
			for( DataOnlyChunks c : DataOnlyChunks.values() )
				if( c.toString().equals( name.toUpperCase() ) )
					return true;
			
			return false;
		}
	}
	
	private static int depth = 0;
	private static String getTabs() { String res = ""; for( int i = 0; i < PSSGFile.depth; i++ ) { res += "   "; } return res; }
	
	public static final HashMap<String, Type<?>> propertyMap = new HashMap<String, Type<?>>();
	static {
		propertyMap.put( "creator",					PSSGSTRING );
	}
	
	public static PSSGFile instance;
	public PSSGFile( String file ) { super( file ); PSSGFile.instance = this; }
	
	public static final PSSG PSSG = new PSSG();
	
	public static class PSSG {
		public long size;
		public long propertyTypes;
		public long chunkTypes;
		public PSSGChunk root;
		
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
	
	public static class Header {
		public String pssg;
		public Long chunksize;
		public Long props;
		public Long params;
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
	
	public static class PSSGChunk {
		public long index;
		public String name;
		public long chunksize;
		public long propsize;
		public boolean hasData = false;
		public byte[] data;
		
		public ArrayList<PSSGProperty<?>> properties = new ArrayList<PSSGProperty<?>>();
		public ArrayList<PSSGChunk> children = new ArrayList<PSSGChunk>();
		
		public void info() { System.out.println( this.toString() ); PSSGFile.depth = 0; }
		public void tree() {
			System.out.println( PSSGFile.getTabs() + "CHUNK " + this.name );
			if( this.children.size() > 0 )
				System.out.println( PSSGFile.getTabs() + "Children: " );
			
			for( PSSGChunk c : this.children )
				c.tree();
		}
		
		public String toString() {
			String res = PSSGFile.getTabs() + "NODE " + this.name + "\n"+
					PSSGFile.getTabs() + "   " + "Index: " + this.index + "\n" +
					PSSGFile.getTabs() + "   " + "Size: " + this.chunksize + "\n" +
					PSSGFile.getTabs() + "   " + "Property Size: " + this.propsize + "\n";
			for( PSSGProperty<?> p : this.properties ) { res += p.toString(); }
			
			PSSGFile.depth++;
			for( PSSGChunk c : this.children )
				res += c.toString();
			PSSGFile.depth--;
			
			return res;
		}
	}
	
	public static class PSSGProperty<T> {
		public Long index;
		public Long size;
		public String name;
		public Type<T> type;
		public long amount;
		public T data;
		
		public static void create( PSSGChunk parent ) {
			/*Long index = PSSGFile.instance.read( UINT32 );
			Long size = PSSGFile.instance.read( UINT32 );
			
			Property prop = PSSG.prop_map.get( index.intValue() );
			if( prop != null ) {
				if( propertyMap.containsKey( prop.name ) ) {
					parent.properties.add( PSSGProperty.create( propertyMap.get( prop.name ), index, size, prop.name ) );
				} else {
					PSSGFile.instance.getBuffer().advance( size.intValue() );
				}
			}*/
		}
		
		public static <T> PSSGProperty<T> create( Type<T> type, long index, long size, String name, T data ) {
			/*PSSGProperty<T> res = new PSSGProperty<T>();
			
			res.index = index;
			res.size = size;
			res.name = name;
			res.type = type;
			res.data = type.getData( PSSGFile.instance.getBuffer().actual() );
			
			return res;*/
			return null;
		}
		
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
		
		PSSG.root.tree();
		/*for( int i = 0; i < PSSG.header.params; i++ )
			this.addChunk( PSSGChunks.PARAM_LIST.copy( "param_" + i ) );
		
		PSSG.root = PSSGNode.create();
		PSSG.root.info();*/
	}
	
	@Override public void write() {}
}
