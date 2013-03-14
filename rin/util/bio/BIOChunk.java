package rin.util.bio;

import java.util.ArrayList;

public class BIOChunk {
	private static int items = 0;
	
	protected static class Part<T> {
		private static int items = 0;
		
		private BIOChunk parent;
		public BIOChunk getParent() { return this.parent; }
		public Part<T> setParent( BIOChunk c ) { this.parent = c; return this; }
		
		private T[] data;
		public T[] getData() { return this.data; }
		public void read() { this.data = this.parent.parent.getBuffer().<T>read( this.type, this.amount ); }
		
		public BIOTypes.Types<T> type;
		public int amount;
		public String id;
		
		public Part( BIOTypes.Types<T> type, int amount ) { this( type, amount, "Part-" + Part.items++ ); }
		public Part( BIOTypes.Types<T> type, int amount, String id ) {
			this.id = id;
			this.type = type;
			this.amount = amount;
		}
	}
	
	public String id;
	private BIOFile parent;
	public BIOFile getParent() { return this.parent; }
	public BIOChunk setParent( BIOFile f ) { this.parent = f; return this; }
	
	private ArrayList<Part<?>> parts = new ArrayList<Part<?>>();
	public ArrayList<Part<?>> getParts() { return this.parts; }
	public Part<?> getPart( String id ) {
		for( Part<?> p : this.parts )
			if( p.id.equals( id ) )
				return p;
		
		return null;
	}
	
	public BIOChunk() { this( "Chunk-" + BIOChunk.items++ ); }
	public BIOChunk( String id ) { this.id = id; this.load(); }
	
	public void load() {}
	
	public <T> BIOChunk addPart( BIOTypes.Types<T> type, int amount ) { return this.addPart( type, amount, false ); }
	public <T> BIOChunk addPart( BIOTypes.Types<T> type, int amount, boolean read ) {
		this.parts.add( new Part<T>( type, amount ).setParent( this ) );
		
		if( read )
			this.parts.get( this.parts.size() - 1 ).read();
		
		return this;
	}
	
	public <T> BIOChunk addPart( BIOTypes.Types<T> type, int amount, String id ) { return this.addPart( type, amount, id, false ); }
	public <T> BIOChunk addPart( BIOTypes.Types<T> type, int amount, String id, boolean read ) {
		this.parts.add( new Part<T>( type, amount, id ).setParent( this ) );
		
		if( read )
			this.parts.get( this.parts.size() - 1 ).read();
		
		return this;
	}
	
	public BIOChunk read() {
		for( Part<?> p : this.parts )
			p.read();
		
		return this;
	}
}
