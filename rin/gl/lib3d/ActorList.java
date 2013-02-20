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
import rin.util.math.Vec3;

public class ActorList extends Thread {
	private static ActorList _instance = new ActorList();
	private static ConcurrentLinkedQueue<Actor> queue = new ConcurrentLinkedQueue<Actor>();
	
	private static boolean destroyRequested = false;
	public static void requestDestroy() { ActorList.destroyRequested = true; }
	
	public ActorList() {
		super( "rin.ai | Actor Thread" );
		//new GLRenderThread().start();
		this.start();
	}
	
	public static ActorList get() { return ActorList._instance; }
	
	public static void add( Actor a ) {
		ActorList.get().addActor( a );
	}
	
	private List<Actor> actors = Collections.synchronizedList( new ArrayList<Actor>() );
	private List<GLEvent> events = Collections.synchronizedList( new ArrayList<GLEvent>() );
	
	public List<Actor> getActors() { return this.actors; }
	public void addActor( Actor a ) {
		this.actors.add( a );
	}
	
	public void addEvent() {
		this.events.add( new Transition( (Transitionable)this.actors.get( 0 ) ) );
	}
	
	public void addEvent( GLEvent e ) {
		this.events.add( e );
	}
	
	public void run() {
		while( !ActorList.destroyRequested ) {
			if( this.events.size() > 0 ) {
				System.out.println( "hi" );
				this.actors.get( 0 ).spin( 0.001f, 0.0f, 0.0f );
			}
			
			try {
				Thread.sleep( 1L );
			} catch( InterruptedException e ) {
				
			}
		}
	}
}
