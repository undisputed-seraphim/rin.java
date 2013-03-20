package rin.util.dcode;

import static rin.gui.GUIFactory.*;
import rin.engine.Engine;
import rin.util.bio.BIOBuffer;
import rin.util.bio.BIOFile;
import rin.util.bio.BIOFileAdapter;
import rin.util.bio.BIOTypes;
import rin.util.dcode.pssg.PSSGFile;
import rin.util.dcode.ttf.TTFFile;
import rin.util.dcode.ttf.TTFTypes;
import static rin.util.bio.BIOTypes.*;

public class DCode {
	protected static BIOFile bio;
	
	public static void main( String args[] ) {
		DCode.bio = new PSSGFile( Engine.MODEL_DIR + "test.pssg" );
		
		DCode.bio.read();
		//DCode.bio.previewChunks();
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
														/*getTextField( "pByte" ).setText( BIOBuffer.asString( DCode.bio.getBuffer().previewBytes(
																Integer.parseInt( getTextField( "p" ).getText() ) ) ) );
														
														getTextField( "pChar" ).setText( BIOBuffer.asString( DCode.bio.getBuffer().previewChars(
																Integer.parseInt( getTextField( "p" ).getText() ) ) ) );
														
														getTextField( "pShort" ).setText( BIOBuffer.asString( DCode.bio.getBuffer().previewShorts(
																Integer.parseInt( getTextField( "p" ).getText() ) ) ) );
														
														getTextField( "pInt" ).setText( BIOBuffer.asString( DCode.bio.getBuffer().previewInts(
																Integer.parseInt( getTextField( "p" ).getText() ) ) ) );
														
														getTextField( "pFloat" ).setText( BIOBuffer.asString( DCode.bio.getBuffer().previewFloats(
																Integer.parseInt( getTextField( "p" ).getText() ) ) ) );
														
														getTextField( "pDouble" ).setText( BIOBuffer.asString( DCode.bio.getBuffer().previewDoubles(
																Integer.parseInt( getTextField( "p" ).getText() ) ) ) );
														
														getTextField( "pLong" ).setText( BIOBuffer.asString( DCode.bio.getBuffer().previewLongs(
																Integer.parseInt( getTextField( "p" ).getText() ) ) ) );*/
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
