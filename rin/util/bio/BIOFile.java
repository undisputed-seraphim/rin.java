package rin.util.bio;

import java.util.ArrayList;

import rin.util.IO;
import rin.util.bio.BIOChunks.Chunk;
import rin.util.bio.BIOParts.Part;
import rin.util.bio.BIOTypes.Type;

public abstract class BIOFile {
	private BIOBuffer buffer = null;
	public BIOBuffer getBuffer() { return this.buffer; }
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
	
	public void read( Type<?> t, int amount ) {
		System.out.println( BIOBuffer.asString( this.getBuffer().read( t, amount ) ) );
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
	
	private <T> T[] getArray( String id, Class<T> cls ) {
		for( Chunk c : this.chunks )
			for( Part<T> p : c.getParts( cls ) )
				if( p.id.equals( id ) )
					return p.getData();
						
		return null;
	}
	
	public short getUByte( String id ) { return this.get( id, Short.class ); }
	public byte getByte( String id ) { return this.get( id, Byte.class ); }
	public char getChar( String id ) { return this.get( id, Character.class ); }
	public String getString( String id ) { return this.get( id, String.class ); }
	
	public Integer[] getUShorts( String id ) { return this.getArray( id, Integer.class ); }
	public int getUShort( String id ) { return this.get( id, Integer.class ); }
	public short getShort( String id ) { return this.get( id, Short.class ); }
	
	public long getUInt( String id ) { return this.get( id, Long.class ); }
	public Long[] getUInts( String id ) { return this.getArray( id, Long.class ); }
	public int getInt( String id ) { return this.get( id, Integer.class ); }
	public float getFloat( String id ) { return this.get( id, Float.class ); }
	public double getDouble( String id ) { return this.get( id, Double.class ); }
	public long getLong( String id ) { return this.get( id, Long.class ); }
}
