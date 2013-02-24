package rin.gui;

import java.util.Stack;
import java.util.HashMap;

import javax.swing.SwingUtilities;

public class GUIManager {
	private static final Runnable DESTROY = new Runnable() { public void run() { GUIManager.removeAll(); } };

	private static HashMap<String, GUIComponent<?, ?>> elements = new HashMap<String, GUIComponent<?, ?>>();
	protected static int elementCount = 0;
	
	protected static boolean find( String id ) { return GUIManager.elements.containsKey( id ); }
	protected static GUIComponent<?, ?> get( String id ) {
		if( GUIManager.find( id ) )
			return GUIManager.elements.get( id );
		return null;
	}
	
	protected static boolean add( String id, GUIComponent<?, ?> component ) {
		if( GUIManager.find( id ) ) {
			System.out.println( "[ERROR] id " + id + " already exists." );
			return false;
		}

		GUIManager.elements.put( id, component );
		GUIManager.elementCount++;
		return true;
	}
	
	protected static void unwatch( String id ) { GUIManager.elements.remove( id ); }
	public static void remove( GUIComponent<?, ?> component ) { GUIManager.remove( component.id ); }
	public static void remove( String id ) {
		if( !GUIManager.find( id ) ) {
			System.out.println( "[ERROR] GUIManager is not managing a component with id " + id + "." );
			return;
		} else {
			GUIManager.get( id ).destroy();
			GUIManager.elements.remove( id );
			GUIManager.elementCount--;
		}
	}
	
	public static void removeAll() {
		Stack<String> stack = new Stack<String>();
		for( String id : GUIManager.elements.keySet() )
			stack.push( id );
		
		while( !stack.empty() ) {
			String cur = stack.pop();
			if( GUIManager.find( cur ) )
				GUIManager.remove( cur );
		}
		
		GUIManager.elements.clear();
		GUIManager.elementCount = 0;
	}
	
	public static void destroy() {
		SwingUtilities.invokeLater( GUIManager.DESTROY );
	}
}
