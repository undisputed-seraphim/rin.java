package rin.system.interfaces;

import java.util.concurrent.Callable;

public abstract class Event<T> implements Runnable, Callable<T>, Cancellable {
	
	public T target;
	
	public final Event<T> setTarget( T target ) {
		this.target = target;
		return this;
	}
	
	public abstract void handle();
	public abstract T result();
	
	@Override
	public final void run() {
		this.handle();
	}
	
	@Override
	public final T call() {
		return this.result();
	}
	
	@Override
	public final void cancel() {
	}
	
}
