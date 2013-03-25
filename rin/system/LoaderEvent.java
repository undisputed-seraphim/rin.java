package rin.system;

import rin.system.interfaces.Event;

public abstract class LoaderEvent<T> extends Event<T> {
	
	@Override
	public abstract T handle();
	
}