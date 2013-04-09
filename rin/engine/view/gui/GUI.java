package rin.engine.view.gui;

import java.util.HashMap;

public class GUI {
	
	private static GUI instance = null;
	public static GUI get() {
		if( GUI.instance == null )
			GUI.instance = new GUI();
		
		return GUI.instance;
	}
	
	protected HashMap<String, GUIWindow> windows = new HashMap<String, GUIWindow>();
	protected HashMap<String, GUIComponent<?>> components = new HashMap<String, GUIComponent<?>>();
	private static int count = 0;
	
	protected static String getNextId() {
		return "r$Component-" + GUI.count++;
	}
}
