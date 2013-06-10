package rin.engine.util;

public abstract class UniquePointer {
	
	protected final int id;
	
	public UniquePointer() {
		id = Unique.getNextId();
		//Unique.cache( this );
	}
	
	public int getUniqueId() { return id; }
	
}
