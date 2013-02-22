package rin.gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

public class GUIManager {
	public static final Runnable DESTROY = new Runnable() { public void run() { GUIManager.destroy(); } };
	public static final Font DEFAULT_FONT = new Font( "Arial", Font.PLAIN, 11 );
	
	public static enum Position {
		LEFT	( SwingConstants.LEFT ),
		CENTER	( SwingConstants.CENTER ),
		RIGHT	( SwingConstants.RIGHT );
		
		protected int value;
		private Position( int value ) { this.value = value; }
	}
	
	public static enum Alignment {
		LEFT	( 0.0f ),
		CENTER	( 0.5f ),
		RIGHT	( 1.0f );
		
		protected float value;
		private Alignment( float value ) { this.value = value; }
	}
	
	public static enum ModifierKey {
		SHIFT,
		ALT,
		CTRL;
	}

	public static class Defaults {
		
	}

	private static HashMap<String, GUIComponent<?, ?>> elements = new HashMap<String, GUIComponent<?, ?>>();
	private static int elementCount = 0;
	
	private static String checkId( String id ) { return id == null ? "$$$Element-" + GUIManager.elementCount : id; }
	public static boolean find( String id ) { return GUIManager.elements.containsKey( id ); }
	public static GUIComponent<?, ?> get( String id ) {
		if( GUIManager.find( id ) )
			return GUIManager.elements.get( id );
		return null;
	}
	
	private static boolean add( String id, GUIComponent<?, ?> component ) {
		if( GUIManager.elements.containsKey( id ) ) {
			System.out.println( "[ERROR] id already existed." );
			return false;
		}
		GUIManager.elements.put( id, component );
		GUIManager.elementCount++;
		return true;
	}
	
	public static void remove( GUIComponent<?, ?> component ) { GUIManager.remove( component.id ); }
	public static void remove( String id ) {
		if( !GUIManager.elements.containsKey( id ) ) {
			System.out.println( "[ERROR] GUIManager is not managing a component with id " + id + "." );
			return;
		}
		
		GUIManager.get( id ).destroy();
		GUIManager.elements.remove( id );
		GUIManager.elementCount--;
	}
	
	public static void removeAll() {
		for( String id : GUIManager.elements.keySet() ) {
			if( GUIManager.get( id ).target != null ) {
				GUIManager.get( id ).destroy();
				GUIManager.elementCount--;
			}
		}
		GUIManager.elements.clear();
		GUIManager.elementCount = 0;
	}
	
	public static Window getWindow( String id ) { return (Window)GUIManager.elements.get( id ); }
	public static Window createWindow() { return GUIManager.createWindow( null ); }
	public static Window createWindow( String id ) {
		id = GUIManager.checkId( id );
		if( GUIManager.add( id, new Window( id ) ) )
			return GUIManager.getWindow( id );
		return null;
	}
	
	public static Panel getPanel( String id ) { return (Panel)GUIManager.elements.get( id ); }
	public static Panel createPanel() { return GUIManager.createPanel( null ); }
	public static Panel createPanel( String id ) {
		id = GUIManager.checkId( id );
		if( GUIManager.add( id, new Panel( id ) ) )
			return GUIManager.getPanel( id );
		return null;
	}
	
	public static TabbedPane getTabbedPane( String id ) { return (TabbedPane)GUIManager.elements.get( id ); }
	public static TabbedPane createTabbedPane() { return GUIManager.createTabbedPane( null ); }
	public static TabbedPane createTabbedPane( String id ) {
		id = GUIManager.checkId( id );
		if( GUIManager.add( id, new TabbedPane( id ) ) )
			return GUIManager.getTabbedPane( id );
		return null;
	}
	
	public static ScrollPane getScrollPane( String id ) { return (ScrollPane)GUIManager.elements.get( id ); }
	public static ScrollPane createScrollPane() { return GUIManager.createScrollPane( null ); }
	public static ScrollPane createScrollPane( String id ) {
		id = GUIManager.checkId( id );
		if( GUIManager.add( id, new ScrollPane( id ) ) )
			return GUIManager.getScrollPane( id );
		return null;
	}
	
	public static TextField getTextField( String id ) { return (TextField)GUIManager.elements.get( id ); }
	public static TextField createTextField() { return GUIManager.createTextField( null ); }
	public static TextField createTextField( String id ) {
		id = GUIManager.checkId( id );
		if( GUIManager.add( id, new TextField( id ) ) )
			return GUIManager.getTextField( id );
		return null;
	}
	
	public static CheckBox getCheckBox( String id ) { return (CheckBox)GUIManager.elements.get( id ); }
	public static CheckBox createCheckBox() { return GUIManager.createCheckBox( null ); }
	public static CheckBox createCheckBox( String id ) {
		id = GUIManager.checkId( id );
		if( GUIManager.add( id, new CheckBox( id ) ) )
			return GUIManager.getCheckBox( id );
		return null;
	}
	
	public static Container getContainer( String id ) { return (Container)GUIManager.elements.get( id ); }
	public static Container createContainer() { return GUIManager.createContainer( null ); }
	public static Container createContainer( String id ) {
		id = GUIManager.checkId( id );
		if( GUIManager.add( id, new Container( id ) ) )
			return GUIManager.getContainer( id );
		return null;
	}
	
	public static Columns getColumns( String id ) { return (Columns)GUIManager.elements.get( id ); }
	public static Columns createColumns( int cols ) { return GUIManager.createColumns( null, cols ); }
	public static Columns createColumns( String id, int cols ) {
		id = GUIManager.checkId( id );
		if( GUIManager.add( id, new Columns( id, cols ) ) )
			return GUIManager.getColumns( id );
		return null;
	}
	
	public static Pair getPair( String id ) { return (Pair)GUIManager.elements.get( id ); }
	public static Pair createPair() { return GUIManager.createPair( null ); }
	public static Pair createPair( String id ) {
		id = GUIManager.checkId( id );
		if( GUIManager.add( id, new Pair( id ) ) )
			return GUIManager.getPair( id );
		return null;
	}
	
	public static Button getButton( String id ) { return (Button)GUIManager.elements.get( id ); }
	public static Button createButton() { return GUIManager.createButton( null ); }
	public static Button createButton( String id ) {
		id = GUIManager.checkId( id );
		if( GUIManager.add( id, new Button( id ) ) )
			return GUIManager.getButton( id );
		return null;
	}
	
	public static MenuBar getMenuBar( String id ) { return (MenuBar)GUIManager.elements.get( id ); }
	public static MenuBar createMenuBar() { return GUIManager.createMenuBar( null ); }
	public static MenuBar createMenuBar( String id ) {
		id = GUIManager.checkId( id );
		if( GUIManager.add( id, new MenuBar( id ) ) )
			return GUIManager.getMenuBar( id );
		return null;
	}
	
	public static Menu getMenu( String id ) { return (Menu)GUIManager.elements.get( id ); }
	public static Menu createMenu( String text ) { return GUIManager.createMenu( null, text, "\0" ); }
	public static Menu createMenu( String text, String mnemonic ) { return GUIManager.createMenu( null, text, mnemonic ); }
	public static Menu createMenu( String id, String text, String mnemonic ) {
		id = GUIManager.checkId( id );
		if( GUIManager.add( id, new Menu( id, text, mnemonic ) ) )
			return GUIManager.getMenu( id );
		return null;
	}
	
	public static MenuItem getMenuItem( String id ) { return (MenuItem)GUIManager.elements.get( id ); }
	public static MenuItem createMenuItem( String text ) { return GUIManager.createMenuItem( null, text, "\0" ); }
	public static MenuItem createMenuItem( String text, String mnemonic ) { return GUIManager.createMenuItem( null, text, mnemonic ); }
	public static MenuItem createMenuItem( String id, String text, String mnemonic ) {
		id = GUIManager.checkId( id );
		if( GUIManager.add( id, new MenuItem( id, text, mnemonic ) ) )
			return GUIManager.getMenuItem( id );
		return null;
	}
	
	/* ------------- events ------------- */
	public static class GUIEvent<T> implements Runnable {
		public T target = null;
		protected Runnable r = null;
		public AWTEvent source = null;
		@SuppressWarnings("unchecked") public <T2> T2 setTarget( T c ) { this.target = c; return (T2)this; }
		public void execute( AWTEvent src ) { this.source = src; this.run(); }
		@Override public void run() { if( this.r != null ) this.r.run(); }
	}
	
	public static class WindowEvent extends GUIEvent<Window> {
		public WindowEvent() {}
		public WindowEvent( Runnable r ) { this.r = r; }
	}
	
	public static class ContainerEvent extends GUIEvent<Container> {
		public ContainerEvent() {}
		public ContainerEvent( Runnable r ) { this.r = r; }
	}
	
	public static class PanelEvent extends GUIEvent<Panel> {
		public PanelEvent() {}
		public PanelEvent( Runnable r ) { this.r = r; }
	}
	
	public static class CheckBoxEvent extends GUIEvent<CheckBox> {
		public CheckBoxEvent() {}
		public CheckBoxEvent( Runnable r ) { this.r = r; }
	}
	
	public static class ButtonEvent extends GUIEvent<Button> {
		public ButtonEvent() {}
		public ButtonEvent( Runnable r ) { this.r = r; }
	}
	
	public static class ColumnsEvent extends GUIEvent<Columns> {
		public ColumnsEvent() {}
		public ColumnsEvent( Runnable r ) { this.r = r; }
	}
	
	public static class PairEvent extends GUIEvent<Pair> {
		public PairEvent() {}
		public PairEvent( Runnable r ) { this.r = r; }
	}
	
	public static class TextFieldEvent extends GUIEvent<TextField> {
		public String value;
		public TextFieldEvent() {}
		public TextFieldEvent( Runnable r ) { this.r = r; } 
	}

	public static class ScrollPaneEvent extends GUIEvent<ScrollPane> {
		public int position, delta;
		public ScrollPaneEvent() {}
		public ScrollPaneEvent( Runnable r ) { this.r = r; } 
	}
	
	public static class TabbedPaneEvent extends GUIEvent<TabbedPane> {
		public String title;
		public Container tab;
		public int previousIndex, currentIndex;
		public TabbedPaneEvent() {}
		public TabbedPaneEvent( Runnable r ) { this.r = r; } 
	}
	
	public static class MenuBarEvent extends GUIEvent<MenuBar> {
		public MenuBarEvent() {}
		public MenuBarEvent( Runnable r ) { this.r = r; }
	}
	
	public static class MenuEvent extends GUIEvent<Menu> {
		public MenuEvent() {}
		public MenuEvent( Runnable r ) { this.r = r; }
	}
	
	public static class MenuItemEvent extends GUIEvent<MenuItem> {
		public MenuItemEvent() {}
		public MenuItemEvent( Runnable r ) { this.r = r; }
	}
	
	/* --------- convenience re-declarations ----------- */
	public static class GUIDimension extends Dimension {
		private static final long serialVersionUID = 1L;
		public GUIDimension( int width, int height ) { super( width, height ); }
	}
	
	public static interface GUILayout extends LayoutManager {}
	
	public static class GUIGridLayout extends GridLayout implements GUILayout {
		private static final long serialVersionUID = 1L;
		public GUIGridLayout( int rows, int cols ) { super( rows, cols ); }
	}
	
	public static class GUIGridBagLayout extends GridBagLayout implements GUILayout {
		private static final long serialVersionUID = 1L;
	}
	
	public static class GUIBorderLayout extends BorderLayout implements GUILayout {
		private static final long serialVersionUID = 1L;
		public GUIBorderLayout() { super(); }
		public GUIBorderLayout(int hgap, int vgap) { super( hgap, vgap ); }
	}
	
	public static class GUIFlowLayout extends FlowLayout implements GUILayout {
		private static final long serialVersionUID = 1L;
		public GUIFlowLayout() { super(); this.setAlignment( FlowLayout.LEFT ); }
	}
	
	public static class GUIBoxLayout extends BoxLayout implements GUILayout {
		private static final long serialVersionUID = 1L;
		public GUIBoxLayout( JComponent target ) { super( target, BoxLayout.X_AXIS ); }
		public GUIBoxLayout( JComponent target, int axis ) { super( target, axis ); }
	}
	
	public static class GUISpringLayout extends SpringLayout implements GUILayout {}
	
	public static class GUIGroupLayout extends GroupLayout implements GUILayout {
		public GUIGroupLayout( JComponent target ) { super( target ); }
	}
	
	public static void destroy() {
		GUIManager.removeAll();
	}
}
