package rin.world;

import java.util.concurrent.Future;

import rin.system.adapters.ControllerAdapter;
import rin.system.interfaces.Processor;
import rin.world.adapters.WorldAdapter;
import rin.world.interfaces.World;

public class WorldController extends ControllerAdapter<World> {
	
	public WorldController() {
		super();
		this.setTarget( new WorldAdapter() );
	}
	
	public WorldController( World w ) {
		super();
		this.setTarget( w );
	}
	
	public WorldController( Processor p ) {
		super( p );
		this.setTarget( new WorldAdapter() );
	}
	
	public WorldController( World w, Processor p ) {
		super( p );
		this.setTarget( w );
	}
	
	
	public World getWorld() {
		return this.getTarget();
	}
	
	
	
	public void execute( WorldEvent e ) {
		e.setTarget( this.getTarget() );
		this.getProcessor().execute( e );
	}
	
	public void executeNow( WorldEvent e ) {
		e.setTarget( this.getTarget() );
		this.getProcessor().executeNow( e );
	}
	
	public Future<World> submit( WorldEvent e ) {
		e.setTarget( this.getTarget() );
		return this.getProcessor().submit( e );
	}
	
	public World submitNow( WorldEvent e ) {
		e.setTarget( this.getTarget() );
		return this.getProcessor().submitNow( e );
	}
	
}
