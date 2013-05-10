package rin.engine.lib.gui;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import javax.swing.SwingUtilities;

public class SwingDispatcher {

	protected static <R> Future<R> invokeLater( Callable<R> process ) {
		FutureTask<R> task = new FutureTask<R>( process );
		
		// if called from EDT, run in current thread
		if( SwingUtilities.isEventDispatchThread() ) {
			task.run();
			return task;
		}
		
		SwingUtilities.invokeLater( task );
		return task;
	}
	
	protected static <R> R invokeLaterAndWait( Callable<R> process ) {
		try {
			return invokeLater( process ).get();
		} catch( InterruptedException e ) {
			//TODO: something here
		} catch( ExecutionException e ) {
			//TODO: something here
		}
		
		return null;
	}
	
}
