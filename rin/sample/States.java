package rin.sample;

import rin.system.GameState;
import static rin.system.RInput.*;

public class States {
	public static GameState TITLE = new GameState( "title" ) {

		@Override public void onEnter() {
			System.out.println( "Entering Title State." );
		}
		
		@Override public void main() {
			if( Keyboard.isKeyDown( Keyboard.KEY_A ) )
				System.out.println( "SUCCESS" );
		}
		
		@Override public void onExit() {
			System.out.println( "Exiting Title State" );
		}
		
	};
	
	public static GameState MENU = new GameState( "mainmenu" ) {

		@Override public void main() {
			// TODO Auto-generated method stub
		}
		
	};
}
