package rin.gl.event;

import rin.engine.Engine;
import rin.gl.lib3d.interfaces.Transitionable;
import rin.gl.lib3d.Actor;
import rin.util.math.Vec3;

public class Transition extends GLEvent {
	public static enum Property {
		POSITION,
		POSITION_X,
		POSITION_Y,
		POSITION_Z,
		
		ROTATION,
		ROTATION_X,
		ROTATION_Y,
		ROTATION_Z,
		
		SCALE,
		SCALE_X,
		SCALE_Y,
		SCALE_Z
	}
	
	public static enum Type {
		LINEAR
	}
	
	protected volatile Transitionable target;
	protected Property property = null;
	protected Type type = null;
	
	protected long delay = 0;
	protected long duration = 0;
	protected long start = 0;
	protected Number targetValueN = 0;
	protected Number[] targetValueA;
	
	private boolean started = false;
	
	public Transition( final Transitionable target ) { this( target, Property.POSITION ); }
	public Transition( final Transitionable target, Property property ) {
		this.target = target;
		this.setProperty( property );
		this.setType( Type.LINEAR );
	}
	
	public Property getProperty() { return this.property; }
	public Transition set( Property property ) { return this.setProperty( property ); }
	public Transition setProperty( Property property ) {
		this.property = property;
		return this;
	}
	
	public Number getTargetValueN() { return this.targetValueN; }
	public Transition to( Number val ) { return this.setTargetValue( val ); }
	public Transition setTargetValue( Number val ) {
		this.targetValueN = val;
		return this;
	}
	
	public Number[] getTargetValueA() { return this.targetValueA; }
	public Transition to( Number[] val ) { return this.setTargetValue( val ); }
	public Transition setTargetValue( Number[] val ) {
		this.targetValueA = val;
		return this;
	}
	
	public Type getType() { return this.type; }
	public Transition using( Type type ) { return this.setType( type ); }
	public Transition setType( Type type ) {
		this.type = type;
		return this;
	}
	
	public long getDelay() { return this.delay; }
	public Transition in( long ms ) { return this.setDelay( ms ); }
	public Transition setDelay( long ms ) {
		this.delay = ms;
		return this;
	}
	
	public long getDuration() { return this.duration; }
	public Transition until( long ms ) { return this.setDuration( ms ); }
	public Transition setDuration( long ms ) {
		this.duration = ms;
		return this;
	}
	
	/* push this transition to the event thread */
	public Transition start() {
		this.start = System.currentTimeMillis();
		return (Transition)GLEventThread.fire( this );
	}
	
	public void update() {
		if( !this.started ) {
			this.started = true;
			System.out.println( "Transition starts!" );
		} else {
			System.out.println( "updating " + ((Actor)this.target).getName() );
			Vec3 rot = Engine.getScene().getActor( 1 ).getRotation();
			float x = rot.x + 0.01f;
			float y = rot.y;
			float z = rot.z;
			Engine.getScene().getActor( 1 ).setRotation( x, y, z );
		}
	}
	
	public Transition onComplete( Runnable r ) {
		return this;
	}
}
