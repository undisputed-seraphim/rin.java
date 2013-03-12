package rin.util.dcode;

import static rin.gui.GUIFactory.*;
import rin.engine.Engine;
import rin.util.BIO;
import rin.util.Buffer;

public class DCode {
	protected static BIO bio;
	
	public static void main( String args[] ) {
		DCode.bio = BIO.fromFile( Engine.FONT_DIR + "arial.ttf" );
		DCode.createGUI();
		waitForBuild( 0 );
		DCode.load();
	}
	
	public static void createGUI() {
		new GUI() {
			@Override public void build() {
				createWindow()
						.add( createContainer()
								.setAlignment( Alignment.CENTER )
								.add( createScrollPane()
										.add( createTextArea() )
								)
								.add( createPanel()
										.add( createButton() )
										.add( createTextField() )
								)
								.add( createColumns( 2 )
										.add( 1, createLabel( "Preview the next " ) )
										.add( 2, createTextField( "p" )
												.setDefault( "1" )
												.setNumeric( true )
												.setCharacterLimit( 3 )
												.onEnter( new TextFieldEvent() {
													@Override public void run() {
														getTextField( "pByte" ).setText( BIO.asString( DCode.bio.previewBytes(
																Integer.parseInt( getTextField( "p" ).getText() ) ) ) );
														
														getTextField( "pChar" ).setText( BIO.asString( DCode.bio.previewChars(
																Integer.parseInt( getTextField( "p" ).getText() ) ) ) );
														
														getTextField( "pShort" ).setText( BIO.asString( DCode.bio.previewShorts(
																Integer.parseInt( getTextField( "p" ).getText() ) ) ) );
														
														getTextField( "pInt" ).setText( BIO.asString( DCode.bio.previewInts(
																Integer.parseInt( getTextField( "p" ).getText() ) ) ) );
														
														getTextField( "pFloat" ).setText( BIO.asString( DCode.bio.previewFloats(
																Integer.parseInt( getTextField( "p" ).getText() ) ) ) );
														
														getTextField( "pDouble" ).setText( BIO.asString( DCode.bio.previewDoubles(
																Integer.parseInt( getTextField( "p" ).getText() ) ) ) );
														
														getTextField( "pLong" ).setText( BIO.asString( DCode.bio.previewLongs(
																Integer.parseInt( getTextField( "p" ).getText() ) ) ) );
													}
												})
										)										
										.add( 1, createLabel( "as Bytes " ) )
										.add( 2, createTextField( "pByte" ).disable() )
										
										.add( 1, createLabel( "as Chars " ) )
										.add( 2, createTextField( "pChar" ).disable() )
										
										.add( 1, createLabel( "as Shorts" ) )
										.add( 2, createTextField( "pShort" ).disable() )
										
										.add( 1, createLabel( "as Ints " ) )
										.add( 2, createTextField( "pInt" ).disable() )
										
										.add( 1, createLabel( "as Floats " ) )
										.add( 2, createTextField( "pFloat" ).disable() )
										
										.add( 1, createLabel( "as Doubles " ) )
										.add( 2, createTextField( "pDouble" ).disable() )
										
										.add( 1, createLabel( "as Longs " ) )
										.add( 2, createTextField( "pLong" ).disable() )
								)
								.add( createPanel()
										.setAlignment( Alignment.CENTER )
										.add( createButton()
												.setText( "Save" )
												.onClick( new ButtonEvent() {
													@Override public void run() {
														DCode.save();
													}
												})
										)
								)
								.add( createScrollPane()
										.add( createTextArea() )
								)
						)
						.show();
			}
		};
	}
	
	/* load chunk data from a file and insert data into application */
	public static void load() {
	}
	
	/* save current chunks and application state to a file */
	public static void save() {
	}
}
