package rin.system;

public class Destroyable<T> extends Thread {
	private T _instance = null;
	protected T get() { return this._instance; }

	private boolean destroyRequested = false;
	public void destroy() { this.destroyRequested = true; }
	public boolean isDestroyRequested() { return this.destroyRequested; }
	
}
