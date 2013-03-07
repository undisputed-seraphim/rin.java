package rin.sample;

import rin.gl.lib3d.properties.TransitionableProperty;

public class Transition<T extends TransitionableProperty<T>> {
	private T target = null, original = null, to = null;
	public T getTarget() { return this.target; }
	
	private long duration = 5000, current = 0;
	private float predivided = 0;
	
	private boolean ascending = true;
	public boolean isAscending() { return this.ascending; }
	
	public Transition( T target, T to, long duration ) {
		this.target = target;
		this.original = target.copy();
		this.to = to;
		this.duration = duration * 1000000;
		
		if( this.duration != 0 ) {
			this.predivided = 1.0f / this.duration;
			this.current = 0;
		}
	}
	
	public void reverse() { this.ascending = !this.ascending; }
	
	public void update( long dt ) {
		if( this.isAscending() ) {
			if( this.duration == 0 || this.current >= this.duration )
				return;
		
			this.current += dt;
		} else {
			if( this.current <= 0 )
				return;
			
			this.current -= dt;
		}
		this.target.update( this.original, this.to, this.current * this.predivided );
	}
	
}
