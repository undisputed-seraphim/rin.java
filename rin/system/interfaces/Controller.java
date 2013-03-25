package rin.system.interfaces;

public interface Controller<T> {
	public Processor getProcessor();
	public void setProcessor( Processor p );
	
	public T getTarget();
	public void setTarget( T object );
}
