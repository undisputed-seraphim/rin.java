package rin.system.adapters;

import rin.system.interfaces.Controller;
import rin.system.interfaces.Processor;

public class ControllerAdapter<T> implements Controller<T> {
	
	private Processor processor;
	private T target;
	
	public ControllerAdapter() {
		this( new ProcessorAdapter() );
	}
	
	public ControllerAdapter( Processor p ) {
		this.setProcessor( p );
	}
	
	
	
	@Override
	public Processor getProcessor() { return this.processor; }
	
	@Override
	public final void setProcessor( Processor p ) { this.processor = p; }
	
	@Override
	public  T getTarget() { return this.target; }
	
	@Override
	public void setTarget( T object ) { this.target = object; }
	
}