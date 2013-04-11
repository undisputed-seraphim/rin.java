package rin.engine.view.gui;

import java.util.HashMap;
import java.util.concurrent.Callable;

public class GUIManager extends SwingDispatcher {
	
	private static GUIManager instance = null;
	public static GUIManager get() {
		if( GUIManager.instance == null )
			GUIManager.instance = new GUIManager();
		
		return GUIManager.instance;
	}
	
	protected HashMap<String, RWindow> windows = new HashMap<String, RWindow>();
	protected HashMap<String, RComponent<?, ?>> components = new HashMap<String, RComponent<?, ?>>();
	private static int count = 0;
	
	protected static String getNextId() {
		return "__$RComponent-" + GUIManager.count++;
	}
	
	public static void print() {
		System.out.println( get().windows.size() + " " + get().components.size() );
	}
	
	// CONTAINERS
	
	public static RWindow getWindow( final String id ) {
		return invokeLaterAndWait( new Callable<RWindow>() {
			@Override public RWindow call() {
				return get().windows.get( id );
			}
		});
	}
	
	public static RPanel getPanel( final String id ) {
		return invokeLaterAndWait( new Callable<RPanel>() {
			@Override public RPanel call() {
				return (RPanel)(get().components.get( id ));
			}
		});
	}
	
	public static RToolBar getToolBar( final String id ) {
		return invokeLaterAndWait( new Callable<RToolBar>() {
			@Override public RToolBar call() {
				return (RToolBar)(get().components.get( id ));
			}
		});
	}
	
	// MENUS
	
	public static RMenuBar getMenuBar( final String id ) {
		return invokeLaterAndWait( new Callable<RMenuBar>() {
			@Override public RMenuBar call() {
				return (RMenuBar)(get().components.get( id ));
			}
		});
	}
	
	public static RMenu getMenu( final String id ) {
		return invokeLaterAndWait( new Callable<RMenu>() {
			@Override public RMenu call() {
				return (RMenu)(get().components.get( id ));
			}
		});
	}
	
	public static RMenuItem getMenuItem( final String id ) {
		return invokeLaterAndWait( new Callable<RMenuItem>() {
			@Override public RMenuItem call() {
				return (RMenuItem)(get().components.get( id ));
			}
		});
	}
	
	public static RContextMenu getContextMenu( final String id ) {
		return invokeLaterAndWait( new Callable<RContextMenu>() {
			@Override public RContextMenu call() {
				return (RContextMenu)(get().components.get( id ));
			}
		});
	}
	
	// INPUTS
	
	public static RButton getButton( final String id ) {
		return invokeLaterAndWait( new Callable<RButton>() {
			@Override public RButton call() {
				return (RButton)(get().components.get( id ));
			}
		});
	}

}
