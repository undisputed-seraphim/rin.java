package rin.engine.view.gui;

import java.util.concurrent.Callable;

public class GUIFactory extends SwingDispatcher {

	private static String getNextId() { return GUIManager.getNextId(); }
	
	public static abstract class GUIEvent<G> {
		public G target;
		protected GUIEvent<G> setTarget( G target ) { this.target = target; return this; }
		public abstract void run();
	}
	
	// CONTAINERS
	
	public static abstract class WindowEvent extends GUIEvent<RWindow> {}
	public static RWindow getWindow( String id ) { return GUIManager.getWindow( id ); }
	public static RWindow createWindow() { return createWindow( getNextId() ); }
	public static RWindow createWindow( final String id ) {
		return invokeLaterAndWait( new Callable<RWindow>() {
			@Override public RWindow call() { return new RWindow( id ); }
		});
	}
	
	public static abstract class PanelEvent extends GUIEvent<RPanel> {}
	public static RPanel getPanel( String id ) { return GUIManager.getPanel( id ); }
	public static RPanel createPanel() { return createPanel( getNextId() ); }
	public static RPanel createPanel( final String id ) {
		return invokeLaterAndWait( new Callable<RPanel>() {
			@Override public RPanel call() { return new RPanel( id ); }
		});
	}
	
	public static abstract class ToolBarEvent extends GUIEvent<RToolBar> {}
	public static RToolBar getToolBar( String id ) { return GUIManager.getToolBar( id ); }
	public static RToolBar createToolBar() { return createToolBar( getNextId() ); }
	public static RToolBar createToolBar( final String id ) {
		return invokeLaterAndWait( new Callable<RToolBar>() {
			@Override public RToolBar call() { return new RToolBar( id ); }
		});
	}
	
	// MENU STUFF
	
	public static abstract class MenuBarEvent extends GUIEvent<RMenuBar> {}
	public static RMenuBar getMenuBar( String id ) { return GUIManager.getMenuBar( id ); }
	public static RMenuBar createMenuBar() { return createMenuBar( getNextId() ); }
	public static RMenuBar createMenuBar( final String id ) {
		return invokeLaterAndWait( new Callable<RMenuBar>() {
			@Override public RMenuBar call() { return new RMenuBar( id ); }
		});
	}
	
	public static abstract class MenuEvent extends GUIEvent<RMenu> {}
	public static RMenu getMenu( String id ) { return GUIManager.getMenu( id ); }
	public static RMenu createMenu( String text ) { return createMenu( getNextId(), text ); }
	public static RMenu createMenu( final String id, final String text ) {
		return invokeLaterAndWait( new Callable<RMenu>() {
			@Override public RMenu call() { return new RMenu( id, text ); }
		});
	}
	
	public static abstract class MenuItemEvent extends GUIEvent<RMenuItem> {}
	public static RMenuItem getMenuItem( String id ) { return GUIManager.getMenuItem( id ); }
	public static RMenuItem createMenuItem( String text ) { return createMenuItem( getNextId(), text ); }
	public static RMenuItem createMenuItem( final String id, final String text ) {
		return invokeLaterAndWait( new Callable<RMenuItem>() {
			@Override public RMenuItem call() { return new RMenuItem( id, text ); }
		});
	}
	
	public static abstract class ContextMenuEvent extends GUIEvent<RContextMenu> {}
	public static RContextMenu getContextMenu( String id ) { return GUIManager.getContextMenu( id ); }
	public static RContextMenu createContextMenu() { return createContextMenu( getNextId() ); }
	public static RContextMenu createContextMenu( final String id ) {
		return invokeLaterAndWait( new Callable<RContextMenu>() {
			@Override public RContextMenu call() { return new RContextMenu( id ); }
		});
	}
	
	// INPUTS
	
	public static abstract class ButtonEvent extends GUIEvent<RButton> {}
	public static RButton getButton( String id ) { return GUIManager.getButton( id ); }
	public static RButton createButton( String text ) { return createButton( getNextId(), text ); }
	public static RButton createButton( final String id, final String text ) {
		return invokeLaterAndWait( new Callable<RButton>() {
			@Override public RButton call() { return new RButton( id, text ); }
		});
	}
	
}
