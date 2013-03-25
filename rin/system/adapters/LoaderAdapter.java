package rin.system.adapters;

import rin.system.interfaces.Event;
import rin.system.interfaces.Loader;

public class LoaderAdapter<T> implements Loader<T> {
	
	private volatile T target = null;
	private Event<T> event = null;
	
	@Override
	public LoaderAdapter<T> setTarget( T target ) {
		this.target = target;
		return this;
	}
	
	@Override
	public LoaderAdapter<T> onLoad( Event<T> e ) {
		this.event = e.setTarget( this.target );
		return this;
	}
	
	@Override
	public void loaded() {
		if( this.event != null )
			this.event.setTarget( this.target ).handle();
	}
	
}
