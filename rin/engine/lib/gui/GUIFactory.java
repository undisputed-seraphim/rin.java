package rin.engine.lib.gui;

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
	
	public static abstract class DesktopEvent extends GUIEvent<RDesktop> {}
	public static RDesktop getDesktop( String id ) { return GUIManager.getDesktop( id ); }
	public static RDesktop createDesktop() { return createDesktop( getNextId() ); }
	public static RDesktop createDesktop( final String id ) {
		return invokeLaterAndWait( new Callable<RDesktop>() {
			@Override public RDesktop call() { return new RDesktop( id ); }
		});
	}
	
	public static abstract class DesktopWindowEvent extends GUIEvent<RDesktopWindow> {}
	public static RDesktopWindow getDesktopWindow( String id ) { return GUIManager.getDesktopWindow( id ); }
	public static RDesktopWindow createDesktopWindow() { return createDesktopWindow( getNextId() ); }
	public static RDesktopWindow createDesktopWindow( final String id ) {
		return invokeLaterAndWait( new Callable<RDesktopWindow>() {
			@Override public RDesktopWindow call() { return new RDesktopWindow( id ); }
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
	
	public static abstract class CanvasEvent extends GUIEvent<RCanvas> {}
	public static RCanvas getCanvas( String id ) { return GUIManager.getCanvas( id ); }
	public static RCanvas createCanvas() { return createCanvas( getNextId() ); }
	public static RCanvas createCanvas( final String id ) {
		return invokeLaterAndWait( new Callable<RCanvas>() {
			@Override public RCanvas call() { return new RCanvas( id ); }
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
	public static RButton createButton() { return createButton( getNextId(), null ); }
	public static RButton createButton( String text ) { return createButton( getNextId(), text ); }
	public static RButton createButton( final String id, final String text ) {
		return invokeLaterAndWait( new Callable<RButton>() {
			@Override public RButton call() { return new RButton( id, text ); }
		});
	}
	
	public static abstract class CheckBoxEvent extends GUIEvent<RCheckBox> {}
	public static RCheckBox getCheckBox( String id ) { return GUIManager.getCheckBox( id ); }
	public static RCheckBox createCheckBox() { return createCheckBox( getNextId(), null ); }
	public static RCheckBox createCheckBox( String id ) { return createCheckBox( id, null ); }
	public static RCheckBox createCheckBox( final String id, final String text ) {
		return invokeLaterAndWait( new Callable<RCheckBox>() {
			@Override public RCheckBox call() { return new RCheckBox( id, text ); }
		});
	}
	
}
