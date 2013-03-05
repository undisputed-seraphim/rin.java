package rin.system;

public abstract class SingletonThread extends Thread {	
	private boolean destroyRequested = false;
	public boolean isDestroyRequested() { return this.destroyRequested; }
	public void requestDestroy() { this.destroyRequested = true; }
	public void requestDestroyAndWait() {
		this.destroyRequested = true;
		try {
			this.join();
		} catch( InterruptedException e ) {
			System.out.println( "[ERROR] InterruptedException while waiting for " + this.getName() );
			this.destroy();
		}
	}
	
	private String name = "";
	public SingletonThread( String name ) { this.name = name; }

	@Override public final void run() {
		super.setName( this.name );
		this.loop();
	}
	
	public void loop() {
		this.preload();
		
		while( !this.destroyRequested )
			this.main();
		
		this.destroy();
	}
	
	public void preload() {}
	
	public abstract void main();
	
	@Override public void destroy() {}
}
