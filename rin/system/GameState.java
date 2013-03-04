package rin.system;

public abstract class GameState extends Thread {
	/** Returns true if this GameState was started with push() */
	public boolean isStacked() { return this.stacked; }
	private boolean stacked = false;
	
	/** Returns true if this GameState was started with start() */
	public boolean isThreaded() { return this.threaded; }
	private boolean threaded = false;
	
	private boolean destroyRequested = false;
	public boolean isDestroyRequested() { return this.destroyRequested; }
	
	private String name = "No Name";
	public GameState( String name ) { this.name = name; }
	
	/** Make this GameState current */
	public final void push() {
		this.stacked = true;
		this.loop();
	}
	
	/** Immediately exit this GameState */
	public final void pop() { this.destroyRequested = true; }
	
	private final void loop() {
		this.onEnter();
		
		while( !this.destroyRequested ) {
			this.main();
		}

		this.onExit();
	}
	
	/** Convenience method to be run before main */
	public void onEnter() {}
	
	/** Executes the game functionality of this GameState. */
	public abstract void main();
	
	/** Convenience method to be run after main */
	public void onExit() {}
	
	public final void start() { this.threaded = true; super.start(); }
	public final void run() { this.loop(); }
	
	public final void destroy() {
		this.destroyRequested = false;
		this.stacked = false;
		this.threaded = false;
	}
}
