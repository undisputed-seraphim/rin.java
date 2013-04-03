package rin.system;

import java.util.concurrent.LinkedBlockingQueue;

import rin.system.interfaces.State;

public class StateController {
	private LinkedBlockingQueue<State> states;
	
	public StateController() {
		 this.states = new LinkedBlockingQueue<State>();
	}
	
	public void add( State state ) {
		this.states.add( state );
	}

}
