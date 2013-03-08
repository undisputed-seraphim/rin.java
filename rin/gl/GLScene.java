package rin.gl;

import java.util.ArrayList;

import rin.gl.lib3d.Actor;
import rin.system.SingletonThread;

public class GLScene extends SingletonThread<GLScene> {
	private static GLScene instance = null;
	public static GLScene get() {
		if( GLScene.instance == null )
			GLScene.instance = new GLScene();
		
		return GLScene.instance;
	}
	
	public static void init() { GLScene.instance = new GLScene(); }
	
	public GLScene() {
		super( "rin.ai | Scene Thread" );
		super.start();
	}
	
	private static volatile ArrayList<Actor> actors = new ArrayList<Actor>();
	public static ArrayList<Actor> getActors() { return GLScene.actors; }
	
	public static Actor getActor( String name ) {
		for( Actor a : GLScene.actors )
			if( a.getName().equals( name ) )
				return a;
		return null;
	}
	
	@Override public void main() {
		synchronized( GLScene.getActors() ) {
			for( Actor a : GLScene.actors ) {
				a.update( this.getDt() );
			}
		}
	}
	
	@Override public void destroy() {
		GLScene.actors.clear();
	}
}
