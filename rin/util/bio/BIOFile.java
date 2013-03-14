package rin.util.bio;

import java.util.ArrayList;
import rin.util.bio.BIOChunks.Chunk;
import rin.util.bio.BIOParts.Part;
import rin.util.bio.BIOTypes.Type;

public abstract class BIOFile {
	private BIOBuffer buffer = null;
	public BIOBuffer getBuffer() { return this.buffer; }
	public void setBuffer( BIOBuffer buffer ) { this.buffer = buffer; }
	
	private ArrayList<Chunk> chunks = new ArrayList<Chunk>();
	public Chunk[] getChunks() { return (Chunk[])this.chunks.toArray(); }

	public BIOFile( String file ) { this.setBuffer( BIOBuffer.fromFile( file ) ); }
	
	public BIOFile addChunk( Chunk c ) { return this.addChunk( c, false ); }
	public BIOFile addChunk( Chunk c, boolean read ) {
		this.chunks.add( c.setParent( this ) );
		c.define();
		
		if( read )
			c.read();
		
		return this;
	}
	
	public Chunk getChunk( String id ) {
		for( Chunk c : this.chunks )
			if( c.id.equals( id ) )
				return c;
		
		return null;
	}
	
	public void previewChunks() {
		for( Chunk c : this.chunks ) {
			System.out.println( c.id + " [" + c.getParts().size() + " parts]" );
			for( Part<?> p : c.getParts() ) {
				System.out.println( "\t" + p.id + " [" + p.type.toString() + " " + p.amount + "]" );
				System.out.println( "\t\t" + BIOBuffer.asString( p.getData() ) );
			}
		}
	}
	
	public void read( Type<?> ... ts ) {
		for( Type<?> t : ts ) {
			System.out.println( BIOBuffer.asString( this.getBuffer().read( t, 1 ) ) );
		}
	}
	
	public abstract void read();
	public abstract void write();
	
	private <T> T get( String id, Class<T> cls ) {
		for( Chunk c : this.chunks )
			for( Part<?> p : c.getParts() )
				if( p.id.equals( id ) )
					if( cls.isInstance( p.getData()[0] ) )
						return cls.cast( p.getData()[0] );
						
		return null;
	}
	
	public Byte getByte( String id ) { return this.get( id, Byte.class ); }
	public Character getChar( String id ) { return this.get( id, Character.class ); }
	public String getString( String id ) { return this.get( id, String.class ); }
	public Integer getUShort( String id ) { return this.get( id, Integer.class ); }
	public Short getShort( String id ) { return this.get( id, Short.class ); }
	public Integer getInt( String id ) { return this.get( id, Integer.class ); }
	public Float getFloat( String id ) { return this.get( id, Float.class ); }
	public Double getDouble( String id ) { return this.get( id, Double.class ); }
	public Long getLong( String id ) { return this.get( id, Long.class ); }
}
