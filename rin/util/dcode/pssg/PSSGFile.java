package rin.util.dcode.pssg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import rin.util.bio.BIOBuffer;
import rin.util.bio.BIOFile;
import rin.util.bio.BIOTypes.Type;
import static rin.util.bio.BIOTypes.*;
import static rin.util.dcode.pssg.PSSGTypes.PSSGSTRING;

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
		public Long size;
		public Long propertyTypes;
		public Long chunkTypes;
		public PSSGNode root;
		
		public HashMap<Integer, Parameter> param_map = new HashMap<Integer, Parameter>();
		public Parameter getParameter( int index ) { return param_map.get( index ); }
		public Parameter getParameter( String name ) {
			for( int i : this.param_map.keySet() )
				if( this.param_map.get( i ).name.toUpperCase().equals( name.toUpperCase() ) )
					return this.param_map.get( i );
			
			return null;
		}
		
		public HashMap<Integer, Property> prop_map = new HashMap<Integer, Property>();
		public Property getProperty( int index ) { return prop_map.get( index ); }
		public Property getProperty( String name ) {
			for( int i : this.prop_map.keySet() )
				if( this.prop_map.get( i ).name.toUpperCase().equals( name.toUpperCase() ) )
					return this.prop_map.get( i );
			
			return null;
		}
	}
	
	public static class Header {
		public String pssg;
		public Long chunksize;
		public Long props;
		public Long params;
	}
	
	public static class Parameter extends Property {
		public Long props;
		public Property[] properties;
		
		public void info() {
			System.out.println( "PARAMETER " + this.name );
			System.out.println( "Index:  " + this.index );
			System.out.println( "Property Count: " + this.props );
			for( Property p : this.properties )
				p.info();
		}
	}	
	
	public static class Property {
		public Long index;
		public Long namelength;
		public String name;
		
		public void info() {
			System.out.println( "PROPERTY " + this.name );
			System.out.println( "Index: " + this.index );
		}
	}
	
	public static class PSSGNode {
		public Long index;
		public String name;
		public Long chunksize;
		public Long propsize;
		public boolean hasData = false;
		public Byte[] data;
		
		public ArrayList<PSSGProperty<?>> properties = new ArrayList<PSSGProperty<?>>();
		public ArrayList<PSSGNode> children = new ArrayList<PSSGNode>();
		
		public static PSSGNode create() {
			/*PSSGNode node = new PSSGNode();
			
			node.index = PSSGFile.instance.read( UINT32 );
			Parameter param = PSSG.param_map.get( node.index.intValue() );
			node.name = param.name;
			
			node.chunksize = PSSGFile.instance.read( UINT32 );
			long chunkStop = PSSGFile.instance.getBuffer().position() + node.chunksize;
		
			node.propsize = PSSGFile.instance.read( UINT32 );
			long propStop = PSSGFile.instance.getBuffer().position() + node.propsize;
			
			while( PSSGFile.instance.getBuffer().position() < propStop )
				PSSGProperty.create( node );
			
			if( DataOnlyChunks.find( node.name ) ) {
				node.hasData = true;
				node.data = PSSG.buffer.read( BYTE, chunkStop - PSSG.buffer.position() );
			} else {
				while( PSSGFile.instance.getBuffer().position() < chunkStop ) {
					node.children.add( PSSGNode.create() );
				}
			}
			
			return node;*/
			return null;
		}
		
		public void info() { System.out.println( this.toString() ); PSSGFile.depth = 0; }
		public void info( boolean tree ) {
			PSSGFile.depth = 0;
			
			System.out.println();
		}
		
		public String toString() {
			String res = PSSGFile.getTabs() + "NODE " + this.name + "\n"+
					PSSGFile.getTabs() + "   " + "Index: " + this.index + "\n" +
					PSSGFile.getTabs() + "   " + "Size: " + this.chunksize + "\n" +
					PSSGFile.getTabs() + "   " + "Property Size: " + this.propsize + "\n";
			for( PSSGProperty<?> p : this.properties ) { res += p.toString(); }
			
			PSSGFile.depth++;
			for( PSSGNode n : this.children )
				res += n.toString();
			PSSGFile.depth--;
			
			return res;
		}
	}
	
	public static class PSSGProperty<T> {
		public Long index;
		public Long size;
		public String name;
		public Type<T> type;
		public T data;
		
		public static void create( PSSGNode parent ) {
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
		
		public static <T> PSSGProperty<T> create( Type<T> type, Long index, Long size, String name ) {
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
	
	public void printMaps() { this.printParamMap(); this.printPropMap(); }
	public void printParamMap() { this.printMap( PSSGFile.PSSG.param_map, "PARAMETER_MAP" ); }
	public void printPropMap() { this.printMap( PSSGFile.PSSG.prop_map, "PROPERTY_MAP" ); }
	
	private void printMap( Map<Integer,? extends Property> m, String title ) {
		if( !title.equals( "" ) )
			System.out.println( "_______" + title + "_______" );
		
		for( int i : m.keySet() )
			System.out.println( "|\t" + i + "\t" + m.get( i ).name );
		
		System.out.println();
	}
	
	@Override public void read() {
		this.addChunk( PSSGChunks.PSSGHEADER );
		
		/*for( int i = 0; i < PSSG.header.params; i++ )
			this.addChunk( PSSGChunks.PARAM_LIST.copy( "param_" + i ) );
		
		PSSG.root = PSSGNode.create();
		PSSG.root.info();*/
	}
	
	@Override public void write() {}
}
