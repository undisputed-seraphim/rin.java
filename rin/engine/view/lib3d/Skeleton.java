package rin.engine.view.lib3d;

import java.util.HashMap;

import rin.engine.lib.gui.GUIFactory;
import rin.engine.lib.gui.GUIManager;
import rin.engine.system.ident.NodeTree;

public class Skeleton extends NodeTree<JointNode> {
	
	private HashMap<String, Animation> animations = new HashMap<String, Animation>();
	private Animation currentAnimation;
	
	public Skeleton( JointNode j ) { super( j, false ); }
	public Skeleton( JointNode j, boolean cache ) {
		super( j, cache );
	}
	
	public Animation getCurrentAnimation() { return currentAnimation; }
	
	public Animation addAnimation( String id ) {
		animations.put( id, new Animation( id ) );
		if( currentAnimation == null )
			currentAnimation = animations.get( id );
		
		return animations.get( id );
	}
	
	public void finish() {
		//GUIFactory.createWindow( "skeleton" ).setTitle( "skeleton" );
		for( final JointNode n : this ) {
			/*GUIManager.getWindow( "skeleton" ).add( GUIFactory.createButton( n.getId() + " true" ).onClick( new GUIFactory.ButtonEvent() {
				@Override
				public void run() {
					n.update = !n.update;
					this.target.setText( n.getId() + " " + n.update );
				}
			}) );*/
			n.finish();
		}
		//GUIManager.getWindow( "skeleton" ).show();
	}
	
	public void bufferAnimations() {
		for( Animation a : animations.values() ) {
			for( JointNode jn : this ) {
				Frame cFrame = a.getFrame( jn.getId() );
				if( cFrame != null ) {
					if( cFrame.tTime != null ) {
						for( int i = 0; i < cFrame.tTime.length; i++ ) {
							float[] t = new float[3];
							t[0] = cFrame.tData[i][0];
							t[1] = cFrame.tData[i][4];
							t[2] = cFrame.tData[i][8];
							cFrame.btData.put( cFrame.tTime[i], t );
						}
						//cFrame.tData = null;
					}
				}
			}
		}
	}
	
	public void update( double dt ) {
		// update animation and
		if( currentAnimation != null )
			currentAnimation.update( dt );
		
		// update this skeletons joints, traversing in the proper order by default
		for( JointNode jn : this ) {
			//if( jn.update )
				jn.update( dt );
			//if( k++ == 5 ) break;
		}
	}
}
