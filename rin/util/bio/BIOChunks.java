package rin.util.bio;

import java.util.ArrayList;
import rin.util.bio.BIOParts.Part;
import rin.util.bio.BIOTypes.Type;

public class BIOChunks {
	public static abstract class Chunk {
		public static Chunk copy( final Chunk chunk, String id ) {
			return new Chunk( id ) {
				@Override public void define( Chunk c ) {
					chunk.define( this );
				}
			};
		}
		private static int items = 0;
		
		public String id;
		public String getId() { return this.id; }
		public Chunk setId( String id ) { this.id = id; return this; }
		
		private BIOFile parent;
		public BIOFile getParent() { return this.parent; }
		public Chunk setParent( BIOFile f ) { this.parent = f; return this; }
		
		private ArrayList<Part<?>> parts = new ArrayList<Part<?>>();
		public ArrayList<Part<?>> getParts() { return this.parts; }		
		@SuppressWarnings("unchecked") public <T> ArrayList<Part<T>> getParts( Class<T> cls ) {
			ArrayList<Part<T>> res = new ArrayList<Part<T>>();
			for( Part<?> p : this.parts )
				if( cls.isInstance( p.getData()[0] ) )
					res.add( (Part<T>)p );
			return res;
		}
		
		public Part<?> getPart( String id ) {
			for( Part<?> p : this.parts )
				if( p.id.equals( id ) )
					return p;
			
			return null;
		}
		
		public Chunk() { this( "Chunk-" + Chunk.items++ ); }
		public Chunk( String id ) { this.id = id; }
		
		public <T> Chunk addPart( Part<T> p ) { return this.addPart( p, false ); }
		public <T> Chunk addPart( Part<T> p, boolean read ) {
			this.parts.add( p.setParent( this ) );
			
			if( read )
				p.read();
			
			return this;
		}
		
		public <T> Chunk addPart( BIOTypes.Type<T> type, int amount ) { return this.addPart( type, amount, false ); }
		public <T> Chunk addPart( BIOTypes.Type<T> type, int amount, boolean read ) {
			this.parts.add( new Part<T>( type, amount ) {
			}.setParent( this ) );
			
			if( read )
				this.parts.get( this.parts.size() - 1 ).read();
			
			return this;
		}
		
		public <T> Chunk addPart( BIOTypes.Type<T> type, int amount, String id ) { return this.addPart( type, amount, id, false ); }
		public <T> Chunk addPart( BIOTypes.Type<T> type, int amount, String id, boolean read ) {
			this.parts.add( new Part<T>( type, amount, id ) {
			}.setParent( this ) );
			
			if( read )
				this.parts.get( this.parts.size() - 1 ).read();
			
			return this;
		}
		
		public abstract void define( Chunk c );
		
		public Chunk copy( String id ) {
			final Chunk chunk = this;
			return new Chunk( id ) {
				@Override public void define( Chunk c ) {
					chunk.define( this );
				}
			};
		}
		
		private <T> T get( String id, Class<T> cls ) {
			for( Part<?> p : this.parts )
				if( p.id.equals( id ) )
					if( cls.isInstance( p.getData()[0] ) )
						return cls.cast( p.getData()[0] );
			return null;
		}
		
		private <T> T[] getArray( String id, Class<T> cls ) {
			for( Part<T> p : this.getParts( cls ) )
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
		public int getInt( String id ) { return this.get( id, Integer.class ); }
		public float getFloat( String id ) { return this.get( id, Float.class ); }
		public double getDouble( String id ) { return this.get( id, Double.class ); }
		public long getLong( String id ) { return this.get( id, Long.class ); }
		
		public Chunk read() {
			for( Part<?> p : this.parts )
				p.read();
			
			return this;
		}
	}
}
