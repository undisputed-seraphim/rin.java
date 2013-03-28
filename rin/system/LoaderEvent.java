package rin.system;

import rin.system.interfaces.Event;

public class LoaderEvent<T> extends Event<T> {
	
	@Override
	public void handle() {}
	
	@Override
	public T result() { return this.target; }
	
}
