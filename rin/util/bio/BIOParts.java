package rin.util.bio;

import rin.util.bio.BIOChunks.Chunk;

public class BIOParts {
	public static abstract class Part<T> {
		private static int items = 0;
		
		private Chunk parent = null;
		public Chunk getParent() { return this.parent; }
		public Part<T> setParent( Chunk c ) { this.parent = c; return this; }
			
		private T[] data = null;
		public T[] getData() { return this.data; }
		public void read() { this.data = this.parent.getParent().getBuffer().<T>read( this.type, this.amount ); }
			
		public BIOTypes.Type<T> type;
		public int amount;
		public String id;
			
		public Part( BIOTypes.Type<T> type, int amount ) { this( type, amount, "Part-" + Part.items++ ); }
		public Part( BIOTypes.Type<T> type, int amount, String id ) {
			this.id = id;
			this.type = type;
			this.amount = amount;
		}
	}
}
