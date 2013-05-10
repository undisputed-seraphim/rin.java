package rin.engine.lib.gui;

import java.awt.Canvas;

public class RCanvas extends RComponent<Canvas, RCanvas> {

	public RCanvas( String id ) {
		super( id, new Canvas() );
		this.canHaveChildren = false;
	}
	
	@Override
	public Canvas swing() { return (Canvas)target; }
	
	@Override
	public RCanvas actual() { return this; }
}
