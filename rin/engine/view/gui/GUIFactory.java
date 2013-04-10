package rin.engine.view.gui;

import java.util.concurrent.Callable;

public class GUIFactory extends SwingDispatcher {

	private static GUIManager get() { return GUIManager.get(); }	
	private static String getNextId() { return GUIManager.getNextId(); }
	
	public static abstract class GUIEvent<T> {
		public T target;
		protected GUIEvent<T> setTarget( T target ) { this.target = target; return this; }
		public abstract void run();
	}
	
	public static abstract class WindowEvent extends GUIEvent<RWindow> {}
	
	// CONTAINERS
	
	public static RWindow createWindow() { return createWindow( getNextId() ); }
	public static RWindow createWindow( final String id ) {
		return invokeLaterAndWait( new Callable<RWindow>() {
			@Override public RWindow call() {
				RWindow res = new RWindow( id );
				get().windows.put( id, res );
				return res;
			}
		});
	}
	public static RWindow getWindow( String id ) { return GUIManager.getWindow( id ); }
	
	public static RPanel createPanel() { return createPanel( getNextId() ); }
	public static RPanel createPanel( final String id ) {
		return invokeLaterAndWait( new Callable<RPanel>() {
			@Override public RPanel call() {
				RPanel res = new RPanel( id );
				get().components.put( id, res );
				return res;
			}
		});
	}
	public static RPanel getPanel( String id ) { return GUIManager.getPanel( id ); }
	
	// MENU STUFF
	
	public static RMenuBar createMenuBar() { return createMenuBar( getNextId() ); }
	public static RMenuBar createMenuBar( final String id ) {
		return invokeLaterAndWait( new Callable<RMenuBar>() {
			@Override public RMenuBar call() {
				RMenuBar res = new RMenuBar( id );
				get().components.put( id, res );
				return res;
			}
		});
	}
	public static RMenuBar getMenuBar( String id ) { return GUIManager.getMenuBar( id ); }
	
	public static RMenu createMenu( String text ) { return createMenu( getNextId(), text ); }
	public static RMenu createMenu( final String id, final String text ) {
		return invokeLaterAndWait( new Callable<RMenu>() {
			@Override public RMenu call() {
				RMenu res = new RMenu( id, text );
				get().components.put( id, res );
				return res;
			}
		});
	}
	public static RMenu getMenu( String id ) { return GUIManager.getMenu( id ); }
	
	public static RMenuItem createMenuItem( String text ) { return createMenuItem( getNextId(), text ); }
	public static RMenuItem createMenuItem( final String id, final String text ) {
		return invokeLaterAndWait( new Callable<RMenuItem>() {
			@Override public RMenuItem call() {
				RMenuItem res = new RMenuItem( id, text );
				get().components.put( id, res );
				return res;
			}
		});
	}
	public static RMenuItem getMenuItem( String id ) { return GUIManager.getMenuItem( id ); }
	
}
