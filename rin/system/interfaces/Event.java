package rin.system.interfaces;

import java.util.concurrent.Callable;

public abstract class Event<T> implements Callable<T> {
	
	public T target;
	
	public final Event<T> setTarget( T target ) {
		this.target = target;
		return this;
	}
	
	public abstract T handle();
	
	@Override
	public final T call() {
		return this.handle();
	}
	
}
