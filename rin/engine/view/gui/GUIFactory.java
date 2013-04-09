package rin.engine.view.gui;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import javax.swing.SwingUtilities;

public class GUIFactory {

	private static <R> Future<R> invokeLater( Callable<R> process ) {
		FutureTask<R> task = new FutureTask<R>( process );
		SwingUtilities.invokeLater( task );
		
		return task;
	}
	
	private static <R> R invokeLaterAndWait( Callable<R> process ) {
		try {
			return invokeLater( process ).get();
		} catch( InterruptedException e ) {
			//TODO: something here
		} catch( ExecutionException e ) {
			//TODO: something here
		}
		
		return null;
	}

	public static GUIWindow createWindow() { return createWindow( GUI.getNextId() ); }
	public static GUIWindow createWindow( final String id ) {
		return invokeLaterAndWait( new Callable<GUIWindow>() {
			@Override public GUIWindow call() {
				GUIWindow res = new GUIWindow( id );
				GUI.get().windows.put( id, res );
				return res;
			}
		});
	}
	
	public static GUIWindow getWindow( final String id ) {
		return invokeLaterAndWait( new Callable<GUIWindow>() {
			@Override public GUIWindow call() {
				return GUI.get().windows.get( id );
			}
		});
	}
	
}
