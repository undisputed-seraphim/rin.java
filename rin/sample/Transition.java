package rin.sample;

public class Transition<T> {
	private Transitionable<T> from = null, to = null;
	private long duration = 1000L, predivided = 0;
	private long current = 0L;
	
	public Transition( Transitionable<T> from, Transitionable<T> to, long duration ) {
		this.from = from;
		this.to = to;
		this.duration = duration;
		
		if( this.duration != 0 ) {
			this.predivided = 1 / this.duration;
			this.current = 0L;
		}
	}
	
	public T getFrame( long dt ) {
		if( this.duration == 0 || this.current >= this.duration )
			return this.to.actual();
		
		this.current += dt;
		return this.from.getFrame( to.actual(), this.current * this.predivided );
	}
	
}
