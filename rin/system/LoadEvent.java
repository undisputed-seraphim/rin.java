package rin.system;

public abstract class LoadEvent<T> {
	
	public T target;
	protected LoadEvent<T> setTarget( T target ) { this.target = target; return this; }
	
	public abstract void load();

}
