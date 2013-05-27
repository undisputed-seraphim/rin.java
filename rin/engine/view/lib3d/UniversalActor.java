package rin.engine.view.lib3d;

import static org.lwjgl.opengl.GL20.glUniformMatrix4;

import java.util.ArrayList;

import rin.engine.system.Profiled;
import rin.gl.GL;
import rin.gl.lib3d.Poly;

public class UniversalActor extends Poly implements Profiled {
	private Skeleton skel = new Skeleton( new JointNode( "$r__skel$root" ), true );
	private Mesh mesh = new Mesh( new RenderNode( "$r__mesh$root" ), true );
	
	public Skeleton getSkeleton() { return skel; }	
	public Mesh getMesh() { return mesh; }
	
	@Override
	public void render() { render( false ); }
	
	@Override
	public void render( boolean unique ) {
		float dt = delta();
		if( skel != null )
			skel.update( dt );
		
		if( mesh != null ) {
			mesh.update( dt );
			glUniformMatrix4( GL.getUniform( "mMatrix"), false, this.getMatrix().gl() );
			mesh.render();
		}
		System.out.println( "Universal actor rendered in " + dt + " seconds" );
	}
	
	private long dt = -1L;
	private long start = System.nanoTime();
	
	private void updateDt() {
		dt = System.nanoTime() - start;
		start += dt; 
	}
	
	@Override
	public long deltaNano() {
		updateDt();
		return dt;
	}
	
	@Override
	public float deltaMillis() {
		updateDt();
		return dt * 1e-6f;
	}
	
	@Override
	public float delta() {
		updateDt();
		return dt * 1e-9f;
	}
}
