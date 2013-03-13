package rin.util.bio;

import java.util.ArrayList;

import rin.util.bio.BIOChunk.Part;

public class BIOFile {	
	private BIOBuffer buffer = null;
	public BIOBuffer getBuffer() { return this.buffer; }
	
	private ArrayList<BIOChunk> chunks = new ArrayList<BIOChunk>();
	
	public BIOFile( String file ) {
		this.buffer = BIOBuffer.fromFile( file );
	}
	
	public BIOFile addChunk( BIOChunk c ) { return this.addChunk( c, false ); }
	public BIOFile addChunk( BIOChunk c, boolean read ) {
		this.chunks.add( c.setParent( this ) );
		
		if( read )
			c.read();
		
		return this;
	}
	
	public BIOChunk getChunk( String id ) {
		for( BIOChunk c : this.chunks )
			if( c.id.equals( id ) )
				return c;
		
		return null;
	}
	
	public void previewChunks() {
		for( BIOChunk c : this.chunks ) {
			System.out.println( c.id + " [" + c.getParts().size() + " parts]" );
			for( Part<?> p : c.getParts() ) {
				System.out.println( "\t" + p.id + " [" + p.type.toString() + " " + p.amount + "]" );
				System.out.println( "\t\t" + BIOBuffer.asString( p.getData() ) );
			}
		}
	}
	
	private <T> T get( String id, Class<T> cls ) {
		for( BIOChunk c : this.chunks )
			for( Part<?> p : c.getParts() )
				if( p.id.equals( id ) )
					if( cls.isInstance( p.getData()[0] ) )
						return cls.cast( p.getData()[0] );
						
		return null;
	}
	
	public Byte getByte( String id ) { return this.get( id, Byte.class ); }
	public Character getChar( String id ) { return this.get( id, Character.class ); }
	public String getString( String id ) { return this.get( id, String.class ); }
	public Short getShort( String id ) { return this.get( id, Short.class ); }
	public Integer getInt( String id ) { return this.get( id, Integer.class ); }
	public Float getFloat( String id ) { return this.get( id, Float.class ); }
	public Double getDouble( String id ) { return this.get( id, Double.class ); }
	public Long getLong( String id ) { return this.get( id, Long.class ); }
}
