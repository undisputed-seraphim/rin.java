package rin.engine.view.gui;

import java.awt.Container;
import java.util.HashMap;

import rin.engine.meta.RinChainable;

public abstract class GUIComponent<T> {

	protected String id;
	protected Container target;
	protected GUIComponent<?> parent;
	protected HashMap<String, GUIComponent<?>> children = new HashMap<String, GUIComponent<?>>();
	
	public GUIComponent( String id, Container target ) {
		this.id = id;
		this.target = target;
	}
	
	protected abstract T swing();
	
	public String getId() { return this.id; }
	
	public boolean isVisible() { return this.target.isVisible(); }
	public boolean isEnabled() { return this.target.isEnabled(); }
	public boolean isFocused() { return this.target.isFoc
	
	@RinChainable
	public GUIComponent<T> show() {
		this.target.setVisible( true );
		return this.update();
	}
	
	@RinChainable
	public GUIComponent<T> hide() {
		this.target.setVisible( false );
		return this.update();
	}
	
	@RinChainable
	public GUIComponent<T> enable() {
		this.target.setEnabled( true );
		return this.update();
	}
	
	@RinChainable
	public GUIComponent<T> disable() {
		this.target.setEnabled( false );
		return this.update();
	}
	
	@RinChainable
	public GUIComponent<T> focus() {
		this.target.requestFocusInWindow();
		return this;
	}
	
	@RinChainable
	public GUIComponent<T> update() {
		this.target.revalidate();
		this.target.repaint();
		
		return this;
	}
	
	@RinChainable
	public GUIComponent<T> add( GUIComponent<?> component ) {
		component.parent = this;
		this.target.add( component.target );
		GUI.get().components.put( component.id, component );
		this.children.put( component.id, component );
		return this.update();
	}
	
	public void destroy() {
		
	}
	
}
