package rin.system;

public abstract class GameState extends Thread {
	/** Returns true if this GameState was started with push() */
	public boolean isStacked() { return this.stacked; }
	private boolean stacked = false;
	
	/** Returns true if this GameState was started with start() */
	public boolean isThreaded() { return this.threaded; }
	private boolean threaded = false;
	
	/** Set whether or not this GameState should loop it's main method */
	public void setLooped( boolean val ) { this.looped = val; }
	public boolean isLooped() { return this.looped; }
	private boolean looped = true;
	
	private boolean destroyRequested = false;
	public boolean isDestroyRequested() { return this.destroyRequested; }
	
	private String name = "No Name";
	public GameState( String name ) { this.name = name; }
	
	/** Make this GameState current */
	public final void push() {
		this.stacked = true;
		this.loop();
	}
	
	/** Immediately exit or stop this GameState */
	public final void pop() {
		this.destroyRequested = true;
		
		if( !this.isLooped() )
			this.onExit();
	}
	
	private final void loop() {
		this.onEnter();
		
		if( this.isLooped() ) {
			while( !this.destroyRequested ) {
				this.main();
			}
		} else {
			this.main();
			return;
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
