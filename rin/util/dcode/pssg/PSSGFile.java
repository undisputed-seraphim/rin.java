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
		BOUNDINGBOX										( PSSGFLOAT ),
		DATA											( null ),
		DATABLOCKDATA									( null ),
		DATABLOCKBUFFERED								( null ),
		INDEXSOURCEDATA									( null ),
		INVERSEBINDMATRIX								( null ),
		MODIFIERNETWORKINSTANCEUNIQUEMODIFIERINPUT		( null ),
		RENDERINTERFACEBOUNDBUFFERED					( null ),
		SHADERINPUT										( UINT8 ),
		SHADERPROGRAMCODEBLOCK							( CHAR ),
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
		SCALE					( PSSGFLOAT, 3 );
		
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
	
	public static class PSSG {
		public long size;
		public long propertyTypes;
		public long chunkTypes;
		public PSSGChunk<?> root;
		
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
		public T[] data;
		
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
	}
	
	@Override public void write() {}
}
