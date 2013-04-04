package rin.system.interfaces;

public abstract class State implements Runnable {

	private volatile boolean destroyRequested = false;
	
	public void onEnter() {}
	
	public abstract void body();
	
	public final void run() {
		this.onEnter();
		while( !this.destroyRequested ) {
			this.body();
		}
		this.onExit();
	}
	
	public void onExit() {}

	public final void push() {
		this.onEnter();
		this.body();
		this.onExit();
	}
	
	public final void pop() {
		this.destroyRequested = true;
	}
	
	public final void start() {
		new Thread( this ).start();
	}
}
