package rin.util.dcode;

import static rin.gui.GUIFactory.*;
import rin.engine.Engine;
import rin.util.bio.BIOBuffer;
import rin.util.bio.BIOFile;
import rin.util.bio.BIOFileAdapter;
import rin.util.bio.BIOTypes;
import rin.util.dcode.ttf.TTFFile;
import rin.util.dcode.ttf.TTFTypes;
import static rin.util.bio.BIOTypes.*;

public class DCode {
	protected static BIOFile bio;
	
	public static void main( String args[] ) {
		/*DCode.bio = new BIOFileAdapter( Engine.FONT_DIR + "arial.ttf" );
		DCode.bio.read( TTFTypes.FIXED, SHORT, SHORT, SHORT, SHORT );
		
		DCode.bio.read( UBYTE, 32 );
		System.out.println( DCode.bio.getBuffer().position() );
		DCode.bio.read( TTFTypes.TAG, UINT32, UINT32, UINT32 );*/
		DCode.bio = new TTFFile( Engine.FONT_DIR + "arial.ttf" );
		//DCode.bio.read( BIOTypes.HUINT8 );
		DCode.bio.read();
		/*DCode.bio.addChunk( new BIOChunk( "header" )
				.addPart( TTFTypes.FIXED, 1, "version" )
				.addPart( BIOTypes.SHORT, 1, "numTables" )
				.addPart( BIOTypes.SHORT, 1, "searchRange" )
				.addPart( BIOTypes.SHORT, 1, "entrySelector" )
				.addPart( BIOTypes.SHORT, 1, "rangeShift" )
		, true );

		DCode.bio.addChunk( new BIOChunk( "table_" )
				.addPart( TTFTypes.TAG, 1, "tag_" )
				.addPart( BIOTypes.INT, 1, "table_0_version" )
				.addPart( BIOTypes.INT, 1, "short" )
				.addPart( BIOTypes.INT, 1, "short" )
		, true );



		DCode.bio.addChunk( new BIOChunk( "table_" )
				.addPart( TTFTypes.TAG, 1, "tag_" )
		, true );
		
		for( int i = 0; i < DCode.bio.getShort( "numTables" ); i++ ) {
			DCode.bio.addChunk( new BIOChunk( "table_"+i )
					.addPart( TTFTypes.TAG, 1, "tag_"+i )
					.addPart( BIOTypes.UINT32, 1, "table_"+i+"_version" )
					.addPart( BIOTypes.UINT32, 1, "short" )
			, true );
			
			if( DCode.bio.getString( "tag_" + i ) != null )
				if( !DCode.bio.getString( "tag_" + i ).equals( "cmap" ) ) {
					DCode.bio.getChunk( "table_" + i ).addPart( BIOTypes.UINT32, 1, "short", true );
				} else {
					DCode.bio.getChunk( "table_" + i ).addPart( BIOTypes.BYTE, 3, "short", true );
				}
		}*/
		
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
														getTextField( "pByte" ).setText( BIOBuffer.asString( DCode.bio.getBuffer().previewBytes(
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
