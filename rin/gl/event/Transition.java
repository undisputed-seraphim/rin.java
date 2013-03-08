package rin.gl.event;

import rin.gl.lib3d.properties.TransitionableProperty;

public class Transition<T extends TransitionableProperty<T>> {
	protected Type type;
	private static enum Type {
		INTERPOLATE
	}
	
	protected T target = null, original = null, to = null;
	public T getTarget() { return this.target; }
	
	protected long duration = 5000, current = 0;
	protected float predivided = 0;
	
	private boolean ascending = true;
	public boolean isAscending() { return this.ascending; }
	
	private boolean started = false;
	public boolean isStarted() { return this.started; }
	
	private boolean finished = false;
	public boolean isFinished() { return this.finished; }
	
	public static class Interpolate<T1 extends TransitionableProperty<T1>> extends Transition<T1> {
		public Interpolate( T1 target, T1 to, long duration ) {
			this.type = Type.INTERPOLATE;
			
			this.target = target;
			this.original = target.copy();
			this.to = to;
			this.duration = duration * 1000000;
			
			if( this.duration != 0 ) {
				this.predivided = 1.0f / this.duration;
				this.current = 0;
			}
		}
	}
	
	public Transition<?> reverse() {
		if( !this.started )
			this.current = this.duration;

		this.ascending = !this.ascending;
		this.finished = false;
		return this;
	}
	
	private TransitionEvent runOnStart = null;
	public Transition<?> onStart( TransitionEvent e ) {
		this.runOnStart = e.setTarget( this );
		return this;
	}
	
	private TransitionEvent runOnFinish = null;
	public Transition<?> onFinish( TransitionEvent e ) {
		this.runOnFinish = e.setTarget( this );
		return this;
	}
	
	public void update( long dt ) {
		if( !this.finished ) {
			if( this.isAscending() ) {
				
				if( this.current == 0 && !this.started ) {
					this.started = true;
					
					if( this.runOnStart != null )
						this.runOnStart.run();
				}
				
				if( this.current == this.duration ) {
					this.finished = true;
					
					if( this.runOnFinish != null )
						this.runOnFinish.run();
					
					return;
				}
			
				this.current += dt;
				this.current = this.current > this.duration ? this.duration : this.current;
				
			} else {

				if( this.current == 0 ) {
					this.finished = true;
					
					if( this.runOnFinish != null )
						this.runOnFinish.run();
					
					return;
				}
				
				if( this.current == this.duration && !this.started ) {
					this.started = true;
					
					if( this.runOnStart != null )
						this.runOnStart.run();
				}
				
				this.current -= dt;
				this.current = this.current < 0 ? 0 : this.current;
			}
			
			/* clamp the dt between 0.0 - 1.0 */
			float dif = this.current * this.predivided;
			dif = dif > 1 ? 1 : dif;
			dif = dif < 0 ? 0 : dif;
			
			switch( this.type ) {
			
			case INTERPOLATE: this.target.doInterpolate( this.original, this.to, dif ); break;
			
			}
		}
	}
	
}
