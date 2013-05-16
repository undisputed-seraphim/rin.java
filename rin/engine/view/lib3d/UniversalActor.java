package rin.engine.view.lib3d;

import static org.lwjgl.opengl.GL20.glUniformMatrix4;

import java.util.ArrayList;

import rin.gl.GL;
import rin.gl.lib3d.Poly;

public class UniversalActor extends Poly {
	private Skeleton skel = new Skeleton( new JointNode( "$r__skel$root" ), true );
	private Mesh mesh = new Mesh( new RenderNode( "$r__mesh$root" ), true );
	
	public Skeleton getSkeleton() { return skel; }	
	public Mesh getMesh() { return mesh; }
	
	@Override
	public void render() { render( false ); }
	
	@Override
	public void render( boolean unique ) {
		double dt = 1.5;
		if( skel != null )
			skel.update( dt );
		
		if( mesh != null ) {
			mesh.update( dt );
			glUniformMatrix4( GL.getUniform( "mMatrix"), false, this.getMatrix().gl() );
			mesh.render();
		}
	}
}
