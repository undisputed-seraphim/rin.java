package rin.system.interfaces;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public interface Processor extends Destructor {
	
	public void execute( Runnable process );
	public void executeNow( Runnable process );
	
	public <T> Future<T> submit( Callable<T> process );
	public <T> T submitNow( Callable<T> process );
	
}
