package rin.engine.system;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Processor {

	private ExecutorService pool;
	public Processor( int threads ) {
		this.pool = Executors.newFixedThreadPool( threads );
	}
	
	public void execute( Runnable process ) {
		this.pool.execute( process );
	}
	
	public void executeNow( Runnable process ) {
		process.run();
	}
	
	public <R> Future<R> submit( Callable<R> process ) {
		return this.pool.submit( process );
	}
	
	public <R> R submitNow( Callable<R> process ) {
		Future<R> f = this.pool.submit( process );
		R res = null;
		
		try {
			res = f.get();
		} catch( InterruptedException e ) {
			//e.printStackTrace();
		} catch( ExecutionException e ) {
			//e.printStackTrace();
		}
		
		return res;
	}
	
}
