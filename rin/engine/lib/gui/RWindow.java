package rin.engine.lib.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import rin.engine.lib.gui.GUIFactory.GUIEvent;
import rin.engine.lib.gui.event.GUIWindowFocusListener;
import rin.engine.lib.gui.event.GUIWindowListener;

public class RWindow extends RComponent<JFrame, RWindow> implements GUIWindowFocusListener<RWindow>,
		GUIWindowListener<RWindow> {
	
	private JFrame window;
	
	public RWindow( String id ) {
		super( id, new JPanel() );
		this.setValidParents( Void.class );
		this.window = new JFrame();
		this.window.add( this.target );
		this.swing().setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
	}
	
	@Override
	public JFrame swing() { return this.window; }
	
	@Override
	protected RWindow actual() { return this; }
	
	public RWindow setTitle( String title ) {
		this.swing().setTitle( title );
		return this.update();
	}
	
	@Override
	public RWindow update() {
		super.update();
		this.swing().pack();
		
		return this;
	}
	
	@Override
	public RWindow add( RComponent<?, ?> component ) {
		// RMenuBars must be added in a special way to RWindows
		if( component instanceof RMenuBar ) {
			this.swing().setJMenuBar( ((RMenuBar)component).swing() );
			this.children.put( component.id, component );
			return this.update();
		} else if( component instanceof RToolBar ) {
			this.swing().add( component.target, BorderLayout.PAGE_START );
			this.children.put( component.id, component );
			return this.update();
		}
		
		return super.add( component );
	}
	
	// WINDOW FOCUS EVENTS
	
	private boolean isWindowFocusListening = false;
	
	@Override
	public boolean isWindowFocusListening() { return this.isWindowFocusListening; }
	
	@Override
	public RWindow setWindowFocusListening( boolean listen ) {
		if( !this.isWindowFocusListening && listen ) {
			this.swing().addWindowFocusListener( this );
			this.isWindowFocusListening = true;
		} else if( this.isWindowFocusListening && !listen ) {
			this.swing().removeWindowFocusListener( this );
			this.isWindowFocusListening = false;
		}
		
		return this;
	}
	
	@Override
	public RWindow onWindowFocusGained( GUIEvent<RWindow> e ) {
		this.setWindowFocusListening( true );
		this.runOnWindowFocusGained = e.setTarget( this );
		return this;
	}
	
	@Override
	public RWindow onWindowFocusLost( GUIEvent<RWindow> e ) {
		this.setWindowFocusListening( true );
		this.runOnWindowFocusLost = e.setTarget( this );
		return this;
	}
	
	// WINDOW EVENTS
	
	private boolean isWindowListening = false;
	
	@Override
	public boolean isWindowListening() { return this.isWindowListening; }
	
	@Override
	public RWindow setWindowListening( boolean listen ) {
		if( !this.isWindowListening && listen ) {
			this.swing().addWindowListener( this );
			this.isWindowListening = true;
		} else if( this.isWindowListening && !listen ) {
			this.swing().removeWindowListener( this );
			this.isWindowListening = false;
		}
		
		return this;
	}
	
	@Override
	public RWindow onWindowClosing( GUIEvent<RWindow> e ) {
		this.setWindowListening( true );
		this.runOnWindowClosing = e.setTarget( this );
		return this;
	}
	
	@Override
	public void destroy() {
		this.setWindowFocusListening( false );
		
		this.swing().removeAll();
		super.destroy();
		this.swing().dispose();
	}
	
}
