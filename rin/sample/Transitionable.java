package rin.sample;

public interface Transitionable<T> {
	public T actual();
	public T getFrame( T end, long dt );
}
