package rin.engine.scene.nodes;

public abstract class AbstractRenderNode extends AbstractSceneNode {

	public AbstractRenderNode( String id ) { super( id ); }
	
	@Override
	public void process() {
		
	}
	
	public abstract void render();
}
