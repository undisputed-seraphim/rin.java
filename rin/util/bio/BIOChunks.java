package rin.util.bio;

import java.util.ArrayList;
import rin.util.bio.BIOParts.Part;

public class BIOChunks {
	public static abstract class Chunk {
		private static int items = 0;
		
		public String id;
		public String getId() { return this.id; }
		public Chunk setId( String id ) { this.id = id; return this; }
		
		private BIOFile parent;
		public BIOFile getParent() { return this.parent; }
		public Chunk setParent( BIOFile f ) { this.parent = f; return this; }
		
		private ArrayList<Part<?>> parts = new ArrayList<Part<?>>();
		public ArrayList<Part<?>> getParts() { return this.parts; }
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
		
		public abstract void define();
		
		public Chunk read() {
			for( Part<?> p : this.parts )
				p.read();
			
			return this;
		}
	}
}
