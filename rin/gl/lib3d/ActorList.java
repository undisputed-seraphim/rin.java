package rin.gl.lib3d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import rin.gl.event.GLEvent;
import rin.gl.event.Transition;
import rin.gl.lib3d.Actor;
import rin.gl.lib3d.data.GLRenderThread;
import rin.gl.lib3d.interfaces.Transitionable;
import rin.gl.model.ModelManager;
import rin.gl.model.ModelManager.Format;
import rin.system.RParams;
import rin.util.math.Vec3;

public class ActorList extends Thread {
	private static ActorList _instance;
	
	private static boolean destroyRequested = false;
	public static void requestDestroy() { ActorList.destroyRequested = true; }
	
	public ActorList() {
		super( "rin.ai | Actor Event Thread" );
		ActorList.destroyRequested = false;
		this.start();
	}
	
	public static void init() { ActorList._instance = new ActorList(); }
	public static ActorList get() { return ActorList._instance; }
	
	public static void add( Actor a ) {
		ActorList.get().addActor( a );
	}
	
	private List<Actor> actors = Collections.synchronizedList( new ArrayList<Actor>() );
	
	public List<Actor> getActors() { return this.actors; }
	public void addActor( Actor a ) {
		this.actors.add( a );
	}
	
	public void run() {
		while( !ActorList.destroyRequested ) {
			for( Actor a : this.actors )
				a.update( 0 );
			
			try {
				Thread.sleep( 1L );
			} catch( InterruptedException e ) {
				
			}
		}
	}
}
