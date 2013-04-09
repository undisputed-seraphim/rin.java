package rin.sample;

import rin.engine.Engine;
import rin.gl.lib3d.Actor;
import rin.system.LoaderEvent;
import rin.system.interfaces.State;

public class States {

	public static final State STATE_MENU = new State() {
		
		public void body() {
			System.out.println( "in body" );
		}
		
	};
	
	public static final State STATE_GAME = new State() {
		
		@Override
		public void onEnter() {
			Engine.init( 600, 900 );
			/*Engine.getScene().addModel( new Engine.ModelParams( Engine.ModelFormat.PSSG, "meruru", "meruru/models" ) ).onLoad( new LoaderEvent<Actor>() {
				public void handle() {
					System.out.println( this.target.getName() );
				}
			});*/
			Engine.start();
		}
		
		@Override
		public void body() {
			System.out.println( "inside game loop" );
		}
		
	};
}
