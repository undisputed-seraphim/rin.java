package rin.engine.event;

public class TrackerEvent implements Runnable {
	
	private Trackable target;
	
	public Trackable getTrackable() { return this.target; }
	
	public TrackerEvent setTarget( Trackable t ) {
		this.target = t;
		return this;
	}

	@Override
	public void run() {}

}
