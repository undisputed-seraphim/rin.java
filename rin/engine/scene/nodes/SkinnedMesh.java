package rin.engine.scene.nodes;

public class SkinnedMesh extends RenderedActor {

	protected Skeleton skel = new Skeleton();
	
	public SkinnedMesh( String id ) { super( id ); }
	
	public Skeleton getSkeleton() { return skel; }
	
	@Override
	public void process( double dt ) {
		super.process( dt );
		skel.update( dt );
	}
}
