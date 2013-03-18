package rin.util.bio;

import java.util.ArrayList;
import rin.util.bio.BIOParts.Part;
import rin.util.bio.BIOTypes.Type;

public class BIOChunks {
	public static abstract class Chunk {
		private static int items = 0;
		
		public static Chunk copy( final Chunk chunk, String id ) {
			return new Chunk( id ) {
				@Override public void define( Chunk c ) {
					chunk.define( this );
				}
			};
		}
		
		public String id;
		public String getId() { return this.id; }
		public Chunk setId( String id ) { this.id = id; return this; }
		
		private BIOFile parent;
		public BIOFile getParent() { return this.parent; }
		public Chunk setParent( BIOFile f ) { this.parent = f; return this; }
		
		private ArrayList<Part<?,?>> parts = new ArrayList<Part<?,?>>();
		public ArrayList<Part<?,?>> getParts() { return this.parts; }		
		@SuppressWarnings("unchecked") public <R, T extends Type<R>, F extends Part<R,T>> ArrayList<F> getParts( T type ) {
			ArrayList<F> res = new ArrayList<F>();
			for( Part<?,?> p : this.parts )
				if( p.type.getClass().isInstance( type ) ) {
					res.add( (F)p );
					
				}
			return res;
		}
		
		public Part<?,?> getPart( String id ) {
			for( Part<?,?> p : this.parts )
				if( p.id.equals( id ) )
					return p;
			
			return null;
		}
		
		public Chunk() { this( "Chunk-" + Chunk.items++ ); }
		public Chunk( String id ) { this.id = id; }
		
		public <R, T extends Type<R>> Chunk addPart( Part<R, T> p ) { return this.addPart( p, false ); }
		public <R, T extends Type<R>> Chunk addPart( Part<R, T> p, boolean read ) {
			this.parts.add( p.setParent( this ) );
			
			if( read )
				p.read();
			
			return this;
		}
		
		public <R, T extends Type<R>> Chunk addPart( T type ) { return this.addPart( type, 1, false ); }
		public <R, T extends Type<R>> Chunk addPart( T type, boolean read ) { return this.addPart( type, 1, read ); }
		public <R, T extends Type<R>> Chunk addPart( T type, long amount ) { return this.addPart( type, amount, false ); }
		public <R, T extends Type<R>> Chunk addPart( T type, long amount, boolean read ) {
			this.parts.add( new Part<R,T>( type, amount ) {
			}.setParent( this ) );
			
			if( read )
				this.parts.get( this.parts.size() - 1 ).read();
			
			return this;
		}
		
		public <R, T extends Type<R>> Chunk addPart( T type, String id ) { return this.addPart( type, 1, id, false ); }
		public <R, T extends Type<R>> Chunk addPart( T type, String id, boolean read ) { return this.addPart( type, 1, id, read ); }
		public <R, T extends Type<R>> Chunk addPart( T type, long amount, String id ) { return this.addPart( type, amount, id, false ); }
		public <R, T extends Type<R>> Chunk addPart( T type, long amount, String id, boolean read ) {
			this.parts.add( new Part<R, T>( type, amount, id ) {
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
		
		public <R, T extends Type<R>> R get( T type, String id ) {
			for( Part<R,T> p : this.getParts( type ) )
				if( p.id.equals( id ) )
					return p.getData()[0];
			
			return null;
		}
		
		public <R, T extends Type<R>> R[] getArray( T type, String id ) {
			for( Part<R,T> p : this.getParts( type ) )
				if( p.id.equals( id ) )
					return p.getData();
			
			return null;
		}
		
		public Chunk read() {
			for( Part<?,?> p : this.parts )
				p.read();
			
			return this;
		}
	}
}
