package rin.util.bio;

import rin.util.bio.BIOChunks.Chunk;

public class BIOParts {
	public static abstract class Part<R, T extends BIOTypes.Type<R>> {
		private static int items = 0;
		
		private Chunk parent = null;
		public Chunk getParent() { return this.parent; }
		public Part<R, T> setParent( Chunk c ) { this.parent = c; return this; }
			
		private R[] data = null;
		public R[] getData() { return this.data; }
		public void read() { this.data = this.parent.getParent().getBuffer().<R>read( this.type, this.amount ); }
			
		public BIOTypes.Type<R> type;
		public long amount;
		public String id;
			
		public Part( BIOTypes.Type<R> type, long amount ) { this( type, amount, "Part-" + Part.items++ ); }
		public Part( BIOTypes.Type<R> type, long amount, String id ) {
			this.id = id;
			this.type = type;
			this.amount = amount;
		}
	}
}
