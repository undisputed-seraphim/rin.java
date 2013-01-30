package rin.engine;

import rin.gl.*;
import rin.gl.lib3d.shape.ComplexShape;
import rin.gl.model.Model;

public class Engine {
	/* type constants for switches */
	private String name;
	private boolean ready = false,
					running = false;
	
	/* constructors */
	public Engine() { this( "No Name" ); }
	public Engine( String name ) {
		this.name = name;
		GL.create();
	}
	
	/* getters */
	public boolean ready() { return this.ready; }
	public boolean running() { return this.running; }
	public boolean glRunning() { return GL.isRunning(); }
	
	/* run the game */
	public void run() { this.run( GL.getWidth(), GL.getHeight() ); }
	public void run( int both ) { this.run( both, both ); }
	public void run( int width, int height ) {
		this.start( width, height );
		this.loop();
		this.stop();
	}
	
	/* engines start method, does everything needed to start the game */
	public void start() { this.start( GL.getWidth(), GL.getHeight() ); }
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
	public void glStart() { this.glStart( GL.getWidth(), GL.getHeight() ); }
	public void glStart( int both ) { this.glStart( both, both ); }
	public void glStart( int width, int height ) {
		if( !this.running ) {
			GL.show();
			this.running = true;
		}
	}
	
	/* wrapper to start gl module's Display */
	public void glUpdate() {
		if( this.running )
			GL.update();
	}
	
	/* wrapper to stop gl module's Display */
	public void glStop() {
		if( this.running ) {
			GL.forceDestroy();
			this.running = false;
		}
	}
	
	/* modify game aspects */
	public void addCharacter( String name ) {
		GL.getScene().addModel( name, Model.Format.DAE );
	}
	
	public void addComplexShape( ComplexShape shape ) {
		GL.getScene().addComplexShape( shape );
	}
}