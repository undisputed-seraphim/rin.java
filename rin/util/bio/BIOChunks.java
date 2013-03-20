package rin.util.bio;

import java.util.ArrayList;
import rin.util.bio.BIOParts.Part;
import static rin.util.bio.BIOTypes.*;

public class BIOChunks {
	public static abstract class Chunk extends BIOReader {
		private static int items = 0;
		
		public static Chunk copy( final Chunk chunk, String id ) {
			return new Chunk( id ) {
				@Override public void define( Chunk c ) {
					chunk.define( this );
				}
			};
		}
		
		@Override public BIOBuffer getBuffer() { return this.getParent().getBuffer(); }
		
		public String id;
		public String getId() { return this.id; }
		public Chunk setId( String id ) { this.id = id; return this; }
		
		private BIOFile parent;
		public BIOFile getParent() { return this.parent; }
		public Chunk setParent( BIOFile f ) { this.parent = f; return this; }
		
		private ArrayList<Part<?>> parts = new ArrayList<Part<?>>();
		public ArrayList<Part<?>> getParts() { return this.parts; }		
		@SuppressWarnings("unchecked") public <T> ArrayList<Part<T>> getParts( Type<T> type ) {
			ArrayList<Part<T>> res = new ArrayList<Part<T>>();
			for( Part<?> p : this.parts )
				if( p.type.getClass().isInstance( type ) ) {
					res.add( (Part<T>)p );
					
				}
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
		
		public <T> Chunk addPart( Type<T> type ) { return this.addPart( type, 1, false ); }
		public <T> Chunk addPart( Type<T> type, boolean read ) { return this.addPart( type, 1, read ); }
		public <T> Chunk addPart( Type<T> type, long amount ) { return this.addPart( type, amount, false ); }
		public <T> Chunk addPart( Type<T> type, long amount, boolean read ) {
			this.parts.add( new Part<T>( type, amount ) {
			}.setParent( this ) );
			
			if( read )
				this.parts.get( this.parts.size() - 1 ).read();
			
			return this;
		}
		
		public <T> Chunk addPart( Type<T> type, String id ) { return this.addPart( type, 1, id, false ); }
		public <T> Chunk addPart( Type<T> type, String id, boolean read ) { return this.addPart( type, 1, id, read ); }
		public <T> Chunk addPart( Type<T> type, long amount, String id ) { return this.addPart( type, amount, id, false ); }
		public <T> Chunk addPart( Type<T> type, long amount, String id, boolean read ) {
			this.parts.add( new Part<T>( type, amount, id ) {
			}.setParent( this ) );
			
			if( read )
				this.parts.get( this.parts.size() - 1 ).read();
			
			return this;
		}
		
		public abstract void define( Chunk c );
		
		public Chunk copy() { return this.copy( "Chunk-" + Chunk.items++ ); }
		public Chunk copy( String id ) {
			final Chunk chunk = this;
			return new Chunk( id ) {
				@Override public void define( Chunk c ) {
					chunk.define( this );
				}
			};
		}
		
		public <T> T get( Type<T> type, String id ) {
			for( Part<T> p : this.getParts( type ) )
				if( p.id.equals( id ) )
					return p.getData()[0];
			
			return null;
		}
		
		public <T> T[] getArray( Type<T> type, String id ) {
			for( Part<T> p : this.getParts( type ) )
				if( p.id.equals( id ) )
					return p.getData();
			
			return null;
		}
		
		public Chunk read() {
			for( Part<?> p : this.parts )
				p.read();
			
			return this;
		}
	}
}
