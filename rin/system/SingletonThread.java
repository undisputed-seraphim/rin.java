package rin.system;

public abstract class SingletonThread<T> extends Thread {
	private T instance;
	public T getInstance() { return this.instance; }
	
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

	private long start, dt = 0;
	public long getDt() { return this.dt; }
	
	@Override public final void run() {
		super.setName( this.name );
		this.start = System.nanoTime();
		this.loop();
	}
	
	public void loop() {
		this.preload();
		
		while( !this.destroyRequested ) {
			long current = System.nanoTime();
			this.dt = current - this.start;
			this.start = current;
			this.main();
		}
		
		this.destroy();
	}
	
	public void preload() {}
	
	public abstract void main();
	
	@Override public void destroy() {}
}
