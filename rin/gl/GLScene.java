package rin.gl;

import java.util.ArrayList;

import rin.engine.resource.Resource;
import rin.engine.resource.ResourceIdentifier;
import rin.gl.lib3d.Actor;
import rin.gl.lib3d.Camera;
import static rin.engine.Engine.*;
import rin.system.interfaces.Loader;
import rin.system.SingletonThread;

public class GLScene extends SingletonThread<GLScene> {
	private static GLScene instance = null;
	public static GLScene get() { return GLScene.instance; }
	
	public Camera getCamera() { return GL.get().getCamera(); }
	
	public static void init() { GLScene.instance = new GLScene(); }
	
	public GLScene() {
		super( "rin.ai | Scene Thread" );
		super.start();
	}
	
	private static volatile ArrayList<Actor> actors = new ArrayList<Actor>();
	public static ArrayList<Actor> getActors() { return GLScene.actors; }
	public static Actor getActor( String name ) {
		for( Actor a : GLScene.actors ) {
			System.out.println( a.getName() );
			if( a.getName().equals( name ) )
				return a;
		}
		return null;
	}
	
	public Loader<Actor> addModel( Resource resource ) {
		return addModel( ModelFormat.fromString( resource.getExtension() ), resource );
	}
	public Loader<Actor> addModel( ModelFormat format, Resource resource ) {
		return GL.addModel( format, resource );
	}
	/*public Loader<Actor> addModel( ModelParams p ) { return GL.addModel( p ); }
	public Loader<Actor> addModel( ModelFormat format, ResourceIdentifier resource ) {
		return GL.addModel( format, resource );
	}*/
	public Loader<Actor> addShape( ShapeParams p ) { return GL.addShape( p ); }
	
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
