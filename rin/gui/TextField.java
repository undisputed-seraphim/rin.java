package rin.gui;

import java.awt.event.KeyEvent;

import javax.swing.JTextField;

import rin.gui.GUIFactory.TextFieldEvent;

public class TextField extends GUIComponent<TextField, TextFieldEvent> {
	private static int items = 0;
	
	private String def = "";
	private int limit = 100;
	private boolean numeric = false;
	
	public TextField() { this( "TextField-" + TextField.items++ ); }
	public TextField( String id ) {
		this.id = id;
		this.target = new JTextField();
		this.canHaveChildren = false;
		this.real().setFont( GUIFactory.DEFAULT_FONT );
		this.setWidth( 15 );
		
		this.onWindowLoad( new GUIFactory.OnLoadEvent() {
			public void run() {
				((JTextField)this.target).addKeyListener( this.component );
				((JTextField)this.target).addFocusListener( this.component );
			}
		}.setTargets( this.target, this ) );
		this.runOnFocusLost = new TextFieldEvent() {
			@Override public void run() {
				this.target.enforceLimit();
			}
		};
	}
	
	private JTextField real() { return (JTextField)this.target; }
	
	public TextField enable() { this.real().setEditable( true ); return this.update(); }
	public TextField disable() { this.real().setEditable( false ); return this.update(); }
	
	public String value() { return this.real().getText(); }
	public String getText() { return this.value(); }
	public TextField setText( String text ) { if( text.equals( "" ) ) text = this.def; this.real().setText( text ); return this.update(); }
	public TextField setDefault( String text ) { this.def = text; if( this.getText() != text ) this.setText( text ); return this.update(); }
	public TextField setCharacterLimit( int limit ) { this.limit = limit; this.enforceLimit(); return this.update(); }
	public TextField setNumeric( boolean val ) { this.numeric = val; return this; }
	
	private void enforceLimit() {
		if( this.numeric ) {
			this.setText( this.getText().replaceAll( "[^0-9]", "" ) );
		}
		
		if( this.getText().length() > this.limit )
			this.setText( this.getText().substring( 0, this.limit ) );
		
		this.setText( this.getText() );
	}
	
	public TextField setWidth( int cols ) { this.real().setColumns( cols ); return this.update(); }
	
	private TextFieldEvent runOnEnter = null;
	public TextField onEnter( TextFieldEvent e ) {
		this.runOnEnter = e.<TextFieldEvent>setTarget( this );
		return this;
	}
	
	@Override public void keyReleased( KeyEvent e ) {
		if( e.getKeyCode() == KeyEvent.VK_ENTER ) {
			this.enforceLimit();
			if( this.runOnEnter != null ) {
				this.runOnEnter.value = this.value();
				this.runOnEnter.run();
			}
		}
	}
	
	@Override protected TextField destroy() {
		this.real().removeKeyListener( this );
		super.destroy();
		
		this.target = null;
		
		return null;
	}
}
