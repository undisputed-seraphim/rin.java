package rin.system.adapters;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import rin.system.interfaces.Processor;

public class ProcessorAdapter implements Processor {
	
	private ExecutorService pool = Executors.newFixedThreadPool( 5 );
	
	@Override
	public void execute( Runnable process ) {
		this.pool.execute( process );
	}
	
	@Override
	public void executeNow( Runnable process ) {
		process.run();
	}
	
	@Override
	public <R> Future<R> submit( Callable<R> process ) {
		return this.pool.submit( process );
	}
	
	@Override
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
	
	@Override
	public void destroy() {
		this.pool.shutdown();
	}
	
}
