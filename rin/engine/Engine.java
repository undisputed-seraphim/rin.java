package rin.engine;

import rin.gl.*;
import rin.gl.model.Model;

public class Engine {
	/* type constants for switches */
	private String name;
	private boolean ready = false,
					running = false;
	/* modules */
	private GL gl;
	
	/* constructors */
	public Engine() { this( "No Name" ); }
	public Engine( String name ) {
		this.name = name;
		
		/* initialize gl module */
		this.gl = new GL();
		if( this.gl.ready() )
			this.ready = true;
	}
	
	/* getters */
	public boolean ready() { return this.ready; }
	public boolean running() { return this.running; }
	public boolean glRunning() { return this.gl.running(); }
	
	/* run the game */
	public void run() { this.run( this.gl.getWidth(), this.gl.getHeight() ); }
	public void run( int both ) { this.run( both, both ); }
	public void run( int width, int height ) {
		this.start( width, height );
		this.loop();
		this.stop();
	}
	
	/* engines start method, does everything needed to start the game */
	public void start() { this.start( this.gl.getWidth(), this.gl.getHeight() ); }
	public void start( int both ) { this.start( both, both ); }
	public void start( int width, int height ) { this.glStart( width, height ); }
	
	/* main engine loop */
	public void loop() {
		while( this.glRunning() )
			this.update();
	}
	
	/* engines update method, handles updating of all modules */
	public void update() {
		if( this.running )
			this.glUpdate();
	}
	
	/* stop the gl module and add anything that needs to be done when game is exited here */
	public void stop() { this.glStop(); }
	
	/* start the opengl module's context, at set width/height */
	public void glStart() { this.glStart( this.gl.getWidth(), this.gl.getHeight() ); }
	public void glStart( int both ) { this.glStart( both, both ); }
	public void glStart( int width, int height ) {
		if( !this.running ) {
			this.gl.show( width, height );
			this.running = true;
		}
	}
	
	/* wrapper to start gl module's Display */
	public void glUpdate() {
		if( !this.gl.paused()  ) {
			this.gl.update();
		}
	}
	
	/* wrapper to stop gl module's Display */
	public void glStop() {
		if( this.running ) {
			this.gl.hide();
			this.running = false;
		}
	}
	
	/* modify game aspects */
	public void addCharacter( String name ) {
		this.gl.getScene().addModel( name, Model.Format.DAE );
	}
	
	/* print to system out a debug line for all the info this object contains */
	public void debug() {
		System.out.print( "Debug of [Rin object] '" + this + "' {\n" +
				"\t" + "name: " + this.name + "\n" +
				"\t" + "ready: " + ( this.ready ? "true" : "false" ) + "\n" +
				"\t" + "running: " + ( this.running ? "true" : "false" ) + "\n" +
				"\t" + "gl: " + this.gl.debug() +"\n}\n" );
	}
}