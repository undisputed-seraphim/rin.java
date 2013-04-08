package rin.engine.event;

public class Tracker {

	private final Trackable trackee;
	
	private TrackerEvent runOnActivate = null;
	private TrackerEvent runOnUpdate = null;
	private TrackerEvent runOnComplete = null;
	
	public Tracker( Trackable t ) {
		this.trackee = t;
	}
	
	
	public Tracker onActivate( TrackerEvent e ) {
		this.runOnActivate = e.setTarget( this.trackee );
		return this;
	}
	
	public Tracker onUpdate( TrackerEvent e ) {
		this.runOnUpdate = e.setTarget( this.trackee );
		return this;
	}
	
	public Tracker onComplete( TrackerEvent e ) {
		this.runOnComplete = e.setTarget( this.trackee );
		return this;
	}
	
	
	public void activate() {
		if( this.runOnActivate != null )
			this.runOnActivate.run();
	}
	
	public void update() {
		if( this.runOnUpdate != null )
			this.runOnUpdate.run();
	}
	
	public void complete() {
		if( this.runOnComplete != null )
			this.runOnComplete.run();
	}
	
}
