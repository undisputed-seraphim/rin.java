package rin.gui;

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
	
	private static HashMap<String, GUIComponent<?>> elements = new HashMap<String, GUIComponent<?>>();
	private static int elementCount = 0;
	
	private static String checkId( String id ) { return id == null ? "$$$Element-" + GUIManager.elementCount : id; }
	public static boolean find( String id ) { return GUIManager.elements.containsKey( id ); }
	public static GUIComponent<?> get( String id ) {
		if( GUIManager.find( id ) )
			return GUIManager.elements.get( id );
		return null;
	}
	
	private static boolean add( String id, GUIComponent<?> component ) {
		if( GUIManager.elements.containsKey( id ) ) {
			System.out.println( "[ERROR] id already existed." );
			return false;
		}
		GUIManager.elements.put( id, component );
		GUIManager.elementCount++;
		return true;
	}
	
	public static void remove( GUIComponent<?> component ) { GUIManager.remove( component.id ); }
	public static void remove( String id ) {
		if( !GUIManager.elements.containsKey( id ) ) {
			System.out.println( "[ERROR] GUIManager is not managing a component with id " + id + "." );
			return;
		}
		
		GUIComponent<?> component = GUIManager.get( id );
		for( GUIComponent<?> g : component.children ) {
			if( GUIManager.elements.containsKey( g.id ) )
				GUIManager.elements.remove( g.id );
			g = g.destroy();
		}
		component.children.clear();
		component.destroy();
		GUIManager.elements.remove( id );
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
	
	public static TextField getTextField( String id ) { return (TextField)GUIManager.elements.get( id ); }
	public static TextField createTextField() { return GUIManager.createTextField( null ); }
	public static TextField createTextField( int cols ) { return GUIManager.createTextField( null, cols ); }
	public static TextField createTextField( String id ) { return GUIManager.createTextField( id, 15 ); }
	public static TextField createTextField( String id, int cols ) {
		id = GUIManager.checkId( id );
		if( GUIManager.add( id, new TextField( id, cols ) ) )
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
	public static Container createContainer() { return GUIManager.createContainer( null, GUIManager.Alignment.LEFT ); }
	public static Container createContainer( String id ) {  return GUIManager.createContainer( id, GUIManager.Alignment.LEFT ); }
	public static Container createContainer( GUIManager.Alignment alignment ) { return GUIManager.createContainer( null, alignment ); }
	public static Container createContainer( String id, GUIManager.Alignment alignment ) {
		id = GUIManager.checkId( id );
		if( GUIManager.add( id, new Container( id, alignment ) ) )
			return GUIManager.getContainer( id );
		return null;
	}
	
	public static Columns getColumns( String id ) { return (Columns)GUIManager.elements.get( id ); }
	public static Columns createColumns( int cols ) { return GUIManager.createColumns( null, cols ); }
	public static Columns createColumns( int cols, Alignment halign ) { return GUIManager.createColumns( null, cols, halign ); }
	public static Columns createColumns( String id, int cols ) { return GUIManager.createColumns( id, cols, Alignment.LEFT ); }
	public static Columns createColumns( String id, int cols, Alignment halign ) { return createColumns( id, cols, halign, Alignment.CENTER ); }
	public static Columns createColumns( String id, int cols, Alignment halign, Alignment valign ) {
		id = GUIManager.checkId( id );
		if( GUIManager.add( id, new Columns( id, cols, halign ) ) )
			return GUIManager.getColumns( id );
		return null;
	}
	
	public static Pair getPair( String id ) { return (Pair)GUIManager.elements.get( id ); }
	public static Pair createPair() { return GUIManager.createPair( null, null, null ); }
	public static Pair createPair( String id ) { return GUIManager.createPair( id, null, null ); }
	public static Pair createPair( GUIComponent<?> l, GUIComponent<?> r ) { return GUIManager.createPair( null, l, r ); }
	public static Pair createPair( String id, GUIComponent<?> l, GUIComponent<?> r ) {
		id = GUIManager.checkId( id );
		if( GUIManager.add( id, new Pair( id, l, r ) ) )
			return GUIManager.getPair( id );
		return null;
	}
	
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
		public GUIBoxLayout( JComponent target ) { super( target, BoxLayout.Y_AXIS ); }
		public GUIBoxLayout( JComponent target, int axis ) { super( target, axis ); }
	}
	
	public static class GUISpringLayout extends SpringLayout implements GUILayout {}
	
	public static class GUIGroupLayout extends GroupLayout implements GUILayout {
		public GUIGroupLayout( JComponent target ) { super( target ); }
	}
	
	public static void destroy() {
		for( String id : GUIManager.elements.keySet() )
			GUIManager.elements.get( id ).destroy();
		GUIManager.elements.clear();
		GUIManager.elementCount = 0;
	}
}
