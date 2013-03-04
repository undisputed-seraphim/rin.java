package rin.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import rin.gui.GUIFactory.LabelEvent;

public class Label extends GUIComponent<Label, LabelEvent> {
	private static int items = 0;
	
	public Label() { this( "Label-" + Label.items++ ); }
	public Label( String id ) { this( id, id ); }
	public Label( String id, String text ) {
		this.id = id;
		this.canHaveChildren = false;
		this.target = new JLabel();
		
		if( text != null )
			this.setText( text );
	}
	
	private JLabel real() { return (JLabel)this.target; }
	
	public Label setText( String text ) { this.real().setText( text ); return this.update(); }
	public Label setImage( String file ) {
		try {
			BufferedImage img = ImageIO.read( new File( file ) );
			this.real().setIcon( new ImageIcon( img ) );
			return this.update();
		} catch (IOException e) {
			System.out.println( "IOException for Image " + file );
		}
		
		return this;
	}
	
	public Label setAlignmentX( GUIFactory.Alignment alignment ) {
		this.real().setHorizontalAlignment( GUIFactory.Alignment.getPosition( alignment ) );
		return this.update();
	}
	
	public Label setAlignmentY( GUIFactory.Alignment alignment ) {
		this.real().setVerticalAlignment( GUIFactory.Alignment.getPosition( alignment ) );
		return this.update();
	}
}
