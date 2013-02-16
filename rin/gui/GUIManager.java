package rin.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

public class GUIManager {
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
	
	public static Font font = new Font( "Arial", Font.PLAIN, 11 );
	
	private static ArrayList<GUIComponent<?>> elements = new ArrayList<GUIComponent<?>>();
	private static int elementCount = 0;
	
	public static Window createWindow() {
		GUIManager.elements.add( new Window() );
		return (Window)GUIManager.elements.get( GUIManager.elementCount++ );
	}
	
	public static Panel createPanel() { return new Panel(); }
	public static TabbedPane createTabbedPane() { return new TabbedPane(); }
	public static CheckBox createCheckBox() { return new CheckBox(); }
	public static Container createContainer() { return new Container(); }
	public static Columns createColumns( int cols ) { return new Columns( cols ); }
	public static Div createDiv() { return new Div(); }
	
	public static void destroy() {
		for( GUIComponent<?> g : GUIManager.elements )
			g = g.destroy();
		GUIManager.elements.clear();
		GUIManager.elementCount = 0;
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
}
