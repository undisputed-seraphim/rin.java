package rin.engine.lib.gui;

public class RPauseDialog {

	public RPauseDialog() {
		new Thread( new Runnable() {
			private volatile boolean destroy = false;
			public void run() {
				GUIFactory.createWindow( "test" ).onWindowClosing( new GUIFactory.WindowEvent() {

					@Override
					public void run() {
						destroy = true;
						GUIFactory.getWindow( "test" ).hide().destroy();
					}
					
				}).show();
				
				while( !destroy );
			}
		}).run();
	}
}
