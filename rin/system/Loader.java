package rin.system;

public class Loader<T> {
	private T target = null;
	protected Loader<T> setTarget( T target ) { this.target = target; return this; }
	
	private LoadEvent<T> e = null;
	public Loader<T> onLoad( LoadEvent<T> e ) {
		this.e = e.setTarget( this.target );
		return this;
	}
	
	private Runnable r = null;
	public Loader<T> onLoad( Runnable r ) {
		this.r = r;
		return this;
	}
	
	public void loaded() {
		if( this.e != null && this.target != null )
			this.e.load();
		
		else if( this.r != null )
			this.r.run();
	}
	
}
