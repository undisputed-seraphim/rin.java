package rin.engine.resource;

public abstract class ForEach<T> {
	
	public abstract void each( T item );

	public final void run( T[] set ) {
		for( T t : set )
			each( t );
	}
}
