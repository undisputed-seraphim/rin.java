package rin.system;

public abstract class SingletonThread extends Thread {	
	private boolean destroyRequested = false;
	public boolean isDestroyRequested() { return this.destroyRequested; }
	public void requestDestroy() { this.destroyRequested = true; }
	
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
