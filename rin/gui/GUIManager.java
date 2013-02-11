package rin.gui;

import java.util.ArrayList;

public class GUIManager {
	private static ArrayList<GUIComponent> elements = new ArrayList<GUIComponent>();
	private static int elementCount = 0;
	
	public static Window createWindow() {
		GUIManager.elements.add( new Window() );
		return (Window)GUIManager.elements.get( GUIManager.elementCount++ );
	}
	
	public static Panel createPanel() { return new Panel(); }
	public static TabbedPane createTabbedPane() { return new TabbedPane(); }
	
	public static void destroy() {
		for( GUIComponent g : GUIManager.elements )
			g = g.destroy();
		GUIManager.elements.clear();
		GUIManager.elementCount = 0;
	}
}
