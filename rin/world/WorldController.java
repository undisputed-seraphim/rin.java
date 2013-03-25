package rin.world;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import rin.system.adapters.ControllerAdapter;
import rin.system.interfaces.Event;

public class WorldController extends ControllerAdapter<World> {
	
	private static WorldController instance;
	private static WorldController get() { return WorldController.instance; }
	
	public WorldController() { super(); WorldController.instance = this; }
	
	public static void execute( WorldEvent<?> e ) {
		
	}
	
	public static <R> Future<R> submit( WorldEvent<R> e ) {
		return WorldController.get().getProcessor().submit( e );
	}
	
	public static <R> submitNow( WorldEvent<R> e ) {
		
	}
	
}
