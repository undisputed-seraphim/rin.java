package rin.gui;

import java.awt.LayoutManager;

import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class GUIFactory extends GUIManager {
	public static final Font DEFAULT_FONT = new Font( "Arial", Font.PLAIN, 11 );
	public static final Font DEFAULT_FONT_11 = new Font( "Arial", Font.PLAIN, 11 );
	public static final Font DEFAULT_FONT_12 = new Font( "Arial", Font.PLAIN, 12 );
	public static final Font DEFAULT_FONT_13 = new Font( "Arial", Font.PLAIN, 13 );
	
	volatile static boolean building = false;
	volatile static boolean async = false;
	public static void setAsync( boolean val ) { GUIFactory.async = val; }
	
	private static long limit = 5000;
	public static void setBuildTimeout( long ms ) { GUIFactory.limit = ms; }
	
	public static void waitForBuild( long ms ) { GUIFactory.setBuildTimeout( ms ); GUIFactory.waitForBuild(); }
	public static void waitForBuild() {
		long time = System.currentTimeMillis();
		while( GUIFactory.building ) {
			if( (System.currentTimeMillis() - time) > GUIFactory.limit && !(GUIFactory.limit == 0) ) {
				System.out.println( "[WARNING] Wait limit of " + GUIFactory.limit + " ms reached while waiting for build to complete." );
				break;
			}
		}
	}
	
	private static String checkId( String id ) { return id == null ? "$$$Element-" + GUIManager.elementCount : id; }
	
	/* ----------- convenience interface to build your gui ---------- */
	public static abstract class GUI implements Runnable {
		public GUI() { GUIFactory.building = true; SwingUtilities.invokeLater( this ); }
		@Override public final void run() {
			this.build();
			GUIFactory.building = false;
		}
		public abstract void build();
	}
	
	/* ----------- convenience enums ------------ */
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
		public static int getPosition( Alignment alignment ) {
			for( Position p : Position.values() )
				if( p.toString().equals( alignment.toString() ) )
					return p.value;
			return Position.CENTER.value;
		}
		private Alignment( float value ) { this.value = value; }
	}
	
	public static enum ModifierKey {
		SHIFT,
		ALT,
		CTRL;
	}
	
	public static enum Orientation {
		HORIZONTAL	( SwingConstants.HORIZONTAL ),
		VERTICAL	( SwingConstants.VERTICAL );
		
		protected int value;
		private Orientation( int value ) { this.value = value; }
	}

	public static class Defaults {
		
	}
	
	/* ------- convenience gui component creation methods ------- */
	public static Window getWindow( String id ) { return (Window)GUIManager.get( id ); }
	public static Window createWindow() { return GUIFactory.createWindow( null ); }
	public static Window createWindow( String id ) {
		id = GUIFactory.checkId( id );
		if( GUIManager.add( id, new Window( id ) ) )
			return GUIFactory.getWindow( id );
		return null;
	}
	
	public static Container getContainer( String id ) { return (Container)GUIManager.get( id ); }
	public static Container createContainer() { return GUIFactory.createContainer( null ); }
	public static Container createContainer( String id ) {
		id = GUIFactory.checkId( id );
		if( GUIManager.add( id, new Container( id ) ) )
			return GUIFactory.getContainer( id );
		return null;
	}
	
	public static Panel getPanel( String id ) { return (Panel)GUIManager.get( id ); }
	public static Panel createPanel() { return GUIFactory.createPanel( null ); }
	public static Panel createPanel( String id ) {
		id = GUIFactory.checkId( id );
		if( GUIManager.add( id, new Panel( id ) ) )
			return GUIFactory.getPanel( id );
		return null;
	}
	
	public static TabbedPane getTabbedPane( String id ) { return (TabbedPane)GUIManager.get( id ); }
	public static TabbedPane createTabbedPane() { return GUIFactory.createTabbedPane( null ); }
	public static TabbedPane createTabbedPane( String id ) {
		id = GUIFactory.checkId( id );
		if( GUIManager.add( id, new TabbedPane( id ) ) )
			return GUIFactory.getTabbedPane( id );
		return null;
	}
	
	public static ScrollPane getScrollPane( String id ) { return (ScrollPane)GUIManager.get( id ); }
	public static ScrollPane createScrollPane() { return GUIFactory.createScrollPane( null ); }
	public static ScrollPane createScrollPane( String id ) {
		id = GUIFactory.checkId( id );
		if( GUIManager.add( id, new ScrollPane( id ) ) )
			return GUIFactory.getScrollPane( id );
		return null;
	}
	
	public static List getList( String id ) { return (List)GUIManager.get( id ); }
	public static List createList() { return GUIFactory.createList( null ); }
	public static List createList( String id ) {
		id = GUIFactory.checkId( id );
		if( GUIManager.add( id, new List( id ) ) )
			return GUIFactory.getList( id );
		return null;
	}
	
	public static ListItem getListItem( String id ) { return (ListItem)GUIManager.get( id ); }
	public static ListItem createListItem( String text ) { return GUIFactory.createListItem( null, text ); }
	public static ListItem createListItem( String id, String text ) {
		id = GUIFactory.checkId( id );
		if( GUIManager.add( id, new ListItem( id, text ) ) )
			return GUIFactory.getListItem( id );
		return null;
	}
	
	public static Columns getColumns( String id ) { return (Columns)GUIManager.get( id ); }
	public static Columns createColumns( int cols ) { return GUIFactory.createColumns( null, cols ); }
	public static Columns createColumns( String id, int cols ) {
		id = GUIFactory.checkId( id );
		if( GUIManager.add( id, new Columns( id, cols ) ) )
			return GUIFactory.getColumns( id );
		return null;
	}
	
	public static Pair getPair( String id ) { return (Pair)GUIManager.get( id ); }
	public static Pair createPair() { return GUIFactory.createPair( null ); }
	public static Pair createPair( String id ) {
		id = GUIFactory.checkId( id );
		if( GUIManager.add( id, new Pair( id ) ) )
			return GUIFactory.getPair( id );
		return null;
	}
	
	public static Label getLabel( String id ) { return (Label)GUIManager.get( id ); }
	public static Label createLabel() { return GUIFactory.createLabel( null, null ); }
	public static Label createLabel( String text ) { return GUIFactory.createLabel( null, text ); }
	public static Label createLabel( String id, String text ) {
		id = GUIFactory.checkId( id );
		if( GUIManager.add( id, new Label( id, text ) ) )
			return GUIFactory.getLabel( id );
		return null;
	}
	
	public static TextField getTextField( String id ) { return (TextField)GUIManager.get( id ); }
	public static TextField createTextField() { return GUIFactory.createTextField( null ); }
	public static TextField createTextField( String id ) {
		id = GUIFactory.checkId( id );
		if( GUIManager.add( id, new TextField( id ) ) )
			return GUIFactory.getTextField( id );
		return null;
	}
	
	public static TextArea getTextArea( String id ) { return (TextArea)GUIManager.get( id ); }
	public static TextArea createTextArea() { return GUIFactory.createTextArea( null ); }
	public static TextArea createTextArea( String id ) {
		id = GUIFactory.checkId( id );
		if( GUIManager.add( id, new TextArea( id ) ) )
			return GUIFactory.getTextArea( id );
		return null;
	}
	
	public static CheckBox getCheckBox( String id ) { return (CheckBox)GUIManager.get( id ); }
	public static CheckBox createCheckBox() { return GUIFactory.createCheckBox( null ); }
	public static CheckBox createCheckBox( String id ) {
		id = GUIFactory.checkId( id );
		if( GUIManager.add( id, new CheckBox( id ) ) )
			return GUIFactory.getCheckBox( id );
		return null;
	}
	
	public static ComboBox getComboBox( String id ) { return (ComboBox)GUIManager.get( id ); }
	public static ComboBox createComboBox() { return GUIFactory.createComboBox( null ); }
	public static ComboBox createComboBox( String id ) {
		id = GUIFactory.checkId( id );
		if( GUIManager.add( id, new ComboBox( id ) ) )
			return GUIFactory.getComboBox( id );
		return null;
	}
	
	public static ComboItem getComboItem( String id ) { return (ComboItem)GUIManager.get( id ); }
	public static ComboItem createComboItem( String text ) { return GUIFactory.createComboItem( null, text ); }
	public static ComboItem createComboItem( String id, String text ) {
		id = GUIFactory.checkId( id );
		if( GUIManager.add( id, new ComboItem( id, text ) ) )
			return GUIFactory.getComboItem( id );
		return null;
	}
	
	public static Button getButton( String id ) { return (Button)GUIManager.get( id ); }
	public static Button createButton() { return GUIFactory.createButton( null ); }
	public static Button createButton( String id ) {
		id = GUIFactory.checkId( id );
		if( GUIManager.add( id, new Button( id ) ) )
			return GUIFactory.getButton( id );
		return null;
	}
	
	public static Slider getSlider( String id ) { return (Slider)GUIManager.get( id ); }
	public static Slider createSlider() { return GUIFactory.createSlider( null ); }
	public static Slider createSlider( String id ) {
		id = GUIFactory.checkId( id );
		if( GUIManager.add( id, new Slider( id ) ) )
			return GUIFactory.getSlider( id );
		return null;
	}
	
	public static MenuBar getMenuBar( String id ) { return (MenuBar)GUIManager.get( id ); }
	public static MenuBar createMenuBar() { return GUIFactory.createMenuBar( null ); }
	public static MenuBar createMenuBar( String id ) {
		id = GUIFactory.checkId( id );
		if( GUIManager.add( id, new MenuBar( id ) ) )
			return GUIFactory.getMenuBar( id );
		return null;
	}
	
	public static Menu getMenu( String id ) { return (Menu)GUIManager.get( id ); }
	public static Menu createMenu( String text ) { return GUIFactory.createMenu( null, text, "\0" ); }
	public static Menu createMenu( String text, String mnemonic ) { return GUIFactory.createMenu( null, text, mnemonic ); }
	public static Menu createMenu( String id, String text, String mnemonic ) {
		id = GUIFactory.checkId( id );
		if( GUIManager.add( id, new Menu( id, text, mnemonic ) ) )
			return GUIFactory.getMenu( id );
		return null;
	}
	
	public static MenuItem getMenuItem( String id ) { return (MenuItem)GUIManager.get( id ); }
	public static MenuItem createMenuItem( String text ) { return GUIFactory.createMenuItem( null, text, "\0" ); }
	public static MenuItem createMenuItem( String text, String mnemonic ) { return GUIFactory.createMenuItem( null, text, mnemonic ); }
	public static MenuItem createMenuItem( String id, String text, String mnemonic ) {
		id = GUIFactory.checkId( id );
		if( GUIManager.add( id, new MenuItem( id, text, mnemonic ) ) )
			return GUIFactory.getMenuItem( id );
		return null;
	}
	
	/* ------------- events ------------- */
	public static class GUIEvent<T> implements Runnable {
		public T target = null;
		private Runnable r = null;
		public GUIEvent() {}
		public GUIEvent( Runnable r ) { this.r = r; }
		@SuppressWarnings("unchecked") public <T2> T2 setTarget( T c ) { this.target = c; return (T2)this; }
		@Override public void run() { if( this.r != null ) this.r.run(); }
	}
	
	public static class WindowEvent extends GUIEvent<Window> {
		public WindowEvent() {}
		public WindowEvent( Runnable r ) { super( r ); }
	}
	
	public static class ContainerEvent extends GUIEvent<Container> {
		public ContainerEvent() {}
		public ContainerEvent( Runnable r ) { super( r ); }
	}
	
	public static class PanelEvent extends GUIEvent<Panel> {
		public PanelEvent() {}
		public PanelEvent( Runnable r ) { super( r ); }
	}
	
	public static class CheckBoxEvent extends GUIEvent<CheckBox> {
		public CheckBoxEvent() {}
		public CheckBoxEvent( Runnable r ) { super( r ); }
	}
	
	public static class ComboBoxEvent extends GUIEvent<ComboBox> {
		public String value = "";
		public int previousIndex = 0, currentIndex = 0;
		public ComboBoxEvent() {}
		public ComboBoxEvent( Runnable r ) { super( r ); }
	}
	
	public static class ComboItemEvent extends GUIEvent<ComboItem> {
		public String value = "";
		public int previousIndex = 0, currentIndex = 0;
		public ComboItemEvent() {}
		public ComboItemEvent( Runnable r ) { super( r ); }
	}
	
	public static class ButtonEvent extends GUIEvent<Button> {
		public ButtonEvent() {}
		public ButtonEvent( Runnable r ) { super( r ); }
	}
	
	public static class SliderEvent extends GUIEvent<Slider> {
		public int value = 0;
		public SliderEvent() {}
		public SliderEvent( Runnable r ) { super( r ); }
	}
	
	public static class ColumnsEvent extends GUIEvent<Columns> {
		public ColumnsEvent() {}
		public ColumnsEvent( Runnable r ) { super( r ); }
	}
	
	public static class PairEvent extends GUIEvent<Pair> {
		public PairEvent() {}
		public PairEvent( Runnable r ) { super( r ); }
	}
	
	public static class LabelEvent extends GUIEvent<Label> {
		public String value = "";
		public LabelEvent() {}
		public LabelEvent( Runnable r ) { super( r ); }
	}
	
	public static class TextFieldEvent extends GUIEvent<TextField> {
		public String value = "";
		public TextFieldEvent() {}
		public TextFieldEvent( Runnable r ) { super( r ); } 
	}
	
	public static class TextAreaEvent extends GUIEvent<TextArea> {
		public String value = "";
		public TextAreaEvent() {}
		public TextAreaEvent( Runnable r ) { super( r ); } 
	}

	public static class ScrollPaneEvent extends GUIEvent<ScrollPane> {
		public int position = 0, delta = 0;
		public ScrollPaneEvent() {}
		public ScrollPaneEvent( Runnable r ) { super( r ); } 
	}
	
	public static class ListEvent extends GUIEvent<List> {
		public int[] recentlySelected = new int[0];
		public int[] recentlyDeselected = new int[0];
		public int[] selected = new int[0];
		public ListEvent() {}
		public ListEvent( Runnable r ) { super( r ); }
	}
	
	public static class ListItemEvent extends GUIEvent<ListItem> {
		public String text = "";
		public int[] recentlySelected = new int[0];
		public int[] recentlyDeselected = new int[0];
		public int[] selected = new int[0];
		public ListItemEvent() {}
		public ListItemEvent( Runnable r ) { super( r ); }
	}
	
	public static class TabbedPaneEvent extends GUIEvent<TabbedPane> {
		public String title = "";
		public Container tab = null;
		public int previousIndex = 0, currentIndex = 0;
		public TabbedPaneEvent() {}
		public TabbedPaneEvent( Runnable r ) { super( r ); } 
	}
	
	public static class MenuBarEvent extends GUIEvent<MenuBar> {
		public MenuBarEvent() {}
		public MenuBarEvent( Runnable r ) { super( r ); }
	}
	
	public static class MenuEvent extends GUIEvent<Menu> {
		public MenuEvent() {}
		public MenuEvent( Runnable r ) { super( r ); }
	}
	
	public static class MenuItemEvent extends GUIEvent<MenuItem> {
		public MenuItemEvent() {}
		public MenuItemEvent( Runnable r ) { super( r ); }
	}
	
	public static abstract class Dispatch implements Runnable {
		public Dispatch() {
			if( !GUIFactory.async )
				GUIFactory.waitForBuild();
			SwingUtilities.invokeLater( this );
		}
		
		public Dispatch( final Runnable r ) {
			if( !GUIFactory.async )
				GUIFactory.waitForBuild();
			SwingUtilities.invokeLater( r );
		}
		
		@Override public abstract void run();
	}
	
	public static class OnLoadEvent implements Runnable {
		protected java.awt.Container target;
		GUIComponent<?, ?> component;
		public OnLoadEvent setTargets( java.awt.Container target, GUIComponent<?, ?> c ) {
			this.target = target;
			this.component = c;
			return this;
		}
		@Override public void run() {}
	}
	
	/* ------------ convenience layouts ------------- */
	public static interface GUILayout extends LayoutManager {}
	
	public static class GridLayout extends java.awt.GridLayout implements GUILayout {
		private static final long serialVersionUID = 1L;
		public GridLayout( int rows, int cols ) { super( rows, cols ); }
	}
	
	public static class GridBagLayout extends java.awt.GridBagLayout implements GUILayout {
		private static final long serialVersionUID = 1L;
	}
	
	public static class BorderLayout extends java.awt.BorderLayout implements GUILayout {
		private static final long serialVersionUID = 1L;
		public BorderLayout() { super(); }
		public BorderLayout(int hgap, int vgap) { super( hgap, vgap ); }
	}
	
	public static class FlowLayout extends java.awt.FlowLayout implements GUILayout {
		private static final long serialVersionUID = 1L;
		public FlowLayout() { super(); this.setAlignment( FlowLayout.LEFT ); }
	}
	
	public static class BoxLayout extends javax.swing.BoxLayout implements GUILayout {
		private static final long serialVersionUID = 1L;
		public BoxLayout( JComponent target ) { super( target, BoxLayout.X_AXIS ); }
		public BoxLayout( JComponent target, int axis ) { super( target, axis ); }
	}
	
	public static class SpringLayout extends javax.swing.SpringLayout implements GUILayout {}
	
	public static class GroupLayout extends javax.swing.GroupLayout implements GUILayout {
		public GroupLayout( JComponent target ) { super( target ); }
	}
	
	/* ---------- miscellaneous convenience re-declarations --------- */
	public static class Dimension extends java.awt.Dimension {
		private static final long serialVersionUID = 1L;
		public Dimension( int width, int height ) { super( width, height ); }
	}
	
	public static class Font extends java.awt.Font {
		private static final long serialVersionUID = 1L;
		public Font( String name, int style, int size ) { super( name, style, size ); }
	}
}
