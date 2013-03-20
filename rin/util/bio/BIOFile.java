package rin.util.bio;

import java.util.ArrayList;

import rin.util.IO;
import rin.util.bio.BIOChunks.Chunk;
import rin.util.bio.BIOParts.Part;
import rin.util.bio.BIOTypes.Type;

public abstract class BIOFile extends BIOReader {
	private BIOBuffer buffer = null;
	@Override public BIOBuffer getBuffer() { return this.buffer; }
	public void setBuffer( BIOBuffer buffer ) { this.buffer = buffer; }
	
	private ArrayList<Chunk> chunks = new ArrayList<Chunk>();
	public ArrayList<Chunk> getChunks() { return this.chunks; }

	public BIOFile( String file ) { this.buffer = BIOBuffer.fromByteArray( IO.file.asByteArray( file ) ); }
	
	public BIOFile addChunk( Chunk c ) { return this.addChunk( c, false ); }
	public BIOFile addChunk( Chunk c, boolean read ) {
		this.chunks.add( c.setParent( this ) );
		c.define( c );
		
		if( read )
			c.read();
		
		return this;
	}
	
	public Chunk getChunk( Class<?> cls, String name ) {
		name = name.trim();
		try {
			try {
				return (Chunk)cls.getField( name.toUpperCase() ).get( null );
			} catch( IllegalArgumentException e ) {
				System.out.println( "Chunk ["+name+"] unavailable." );
			} catch( IllegalAccessException e ) {
				System.out.println( "Chunk ["+name+"] has the wrong access modifier or is otherwise inaccessible." );
			}
		} catch( NoSuchFieldException e ) {
			System.out.println( "Chunk ["+name+"] does not exist or is not named properly." );
		} catch( SecurityException e ) {
			System.out.println( "Chunk ["+name+"] has the wrong access modifier or is otherwise inaccessible." );
		}
		
		return null;
	}
	
	public Chunk getChunk( String id ) {
		for( Chunk c : this.chunks )
			if( c.id.equals( id ) )
				return c;
		
		return null;
	}
	
	public abstract void read();
	public abstract void write();
	
	public <T> T get( Type<T> type, String id ) {
		for( Chunk c : this.chunks )
			for( Part<T> p : c.getParts( type ) )
				if( p.id.equals( id ) )
					return p.getData()[0];
		
		return null;
	}
	
	public <T> T[] getArray( Type<T> type, String id ) {
		for( Chunk c : this.chunks )
			for( Part<T> p : c.getParts( type ) )
				if( p.id.equals( id ) )
					return p.getData();
		
		return null;
	}
}
