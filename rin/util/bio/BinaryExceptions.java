package rin.util.bio;

public class BinaryExceptions {

	public static final class BinaryBufferRangeException extends RuntimeException {

		private static final long serialVersionUID = 7L;
		
		private int capacity;
		private int start;
		private int end;
		
		public BinaryBufferRangeException( int capacity, int start, int end ) {
			super( "BinaryBufferRangeException - Range: " + start + "-" + end + " Capacity: " + capacity );
			this.capacity = capacity;
			this.start = start;
			this.end = end;
		}
		
		public int getCapacity() { return this.capacity; }
		public int getStart() { return this.start; }
		public int getEnd() { return this.end; }
		
	}
}
