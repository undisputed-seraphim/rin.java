package rin.system.interfaces;

import java.util.concurrent.Callable;

import rin.system.exceptions.CancelledException;

public abstract class Event<T> implements Runnable, Callable<T>, Cancellable {
	
	public T target;
	private boolean started = false;
	private boolean cancelled = false;
	
	public final Event<T> setTarget( T target ) {
		this.target = target;
		return this;
	}
	
	public abstract void handle() throws CancelledException;
	public abstract T result();
	
	@Override
	public final void run() {
		try {
			this.handle();
		} catch( CancelledException ex ) {
			System.out.println( "this event was cancelled while running." );
		}
	}
	
	@Override
	public final T call() {
		return this.result();
	}
	
	@Override
	public final void cancel() {
		
		if( this.started )
			throw new CancelledException();
		
		else
			this.cancelled = true;
		
	}
	
}
