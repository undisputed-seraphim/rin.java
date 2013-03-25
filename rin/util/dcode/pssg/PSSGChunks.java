package rin.util.dcode.pssg;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import rin.util.bio.BIOBuffer;
import rin.util.bio.BIOChunks.Chunk;
import rin.util.dcode.pssg.PSSGFile.DataOnlyChunks;
import static rin.util.bio.BIOTypes.*;
import static rin.util.dcode.pssg.PSSGTypes.*;
import static rin.util.dcode.pssg.PSSGFile.*;

public class PSSGChunks {	
	public static final Chunk PSSGHEADER = new Chunk( "PSSGHEADER" ) {
		@Override public void define( Chunk c ) {
			String magic = c.readString( 4 );
			
			if( !magic.toUpperCase().equals( "PSSG" ) )
				System.out.println( "HUGE ERROR TO BE REPLACED WITH PSSGHeaderFormatException" );
			
			PSSG.size = c.readUInt32();
			PSSG.propertyTypes = c.readUInt32();
			PSSG.chunkTypes = c.readUInt32();
		}
	};
	
	public static final Chunk INFOLIST = new Chunk( "INFOLIST" ) {
		@Override public void define( Chunk c ) {
			
			PSSGChunkInfo cinfo = new PSSGChunkInfo();
			cinfo.index = readUInt32();
			cinfo.name = read( PSSGSTRING );
			cinfo.props = readUInt32();
			cinfo.properties = new PSSGPropertyInfo[ (int)cinfo.props ];
			
			for( int i = 0; i < cinfo.props; i++ ) {
				PSSGPropertyInfo pinfo = new PSSGPropertyInfo();
				pinfo.index = readUInt32();
				pinfo.name = read( PSSGSTRING );
				
				cinfo.properties[i] = pinfo;
				PSSG.propInfoMap.put( (int)pinfo.index, pinfo );
			}
			
			PSSG.chunkInfoMap.put( (int)cinfo.index, cinfo );
		}
	};
	
	public static final Chunk PSSGDATABASE = new Chunk( "PSSGDATABASE" ) {
		
		public <T> PSSGProperty<T> createProperty( Type<T> type, int amount, long index, long size ) {
			PSSGProperty<T> prop = new PSSGProperty<T>( type, index, size );
			prop.index = index;
			prop.size = size;
			int start = position();
			
			prop.name = PSSG.propInfoMap.get( (int)prop.index ).name;
			//prop.data = read( type, amount );
			//System.out.println( BIOBuffer.asString( prop.data ) );
			
			/*if( position() != start + prop.size ) {
				System.out.println( "Property [" + prop.name + "] did not take up required space." );
				position( start + prop.size );
			}*/
			
			if( prop.size == 4 ) {
				prop.data = ""+readUInt32();
			} else if( prop.size > 4 ) {
				long length = readUInt32();
				if( prop.size - 4 == length ) {
					prop.data = readString( length );
				} else {
					rewind( 4 );
					prop.data = BIOBuffer.asString( readUInt8s( prop.size ) );
				}
			} else {
				prop.data = BIOBuffer.asString( readUInt8s( prop.size ) );
			}

			return prop;
		}
		
		public Type<?> findType( long index ) {
			DataOnlyChunks c;
			if( PSSG.chunkInfoMap.containsKey( (int)index ) )
				if( ( c =  DataOnlyChunks.find( PSSG.chunkInfoMap.get( (int)index ).name ) ) != null )
					if( c.type != null )
						return c.type;
			
			return UINT8;
		}
		
		public <T> PSSGChunk<T> create( PSSGChunk<?> parent, Type<T> type ) {
			PSSGChunk<T> chunk = new PSSGChunk<T>();
			
			if( parent != null )
				chunk.parent = parent;
			
			chunk.index = readUInt32();
			PSSGChunkInfo cinfo = PSSG.chunkInfoMap.get( (int)chunk.index );
			if( cinfo == null )
				return chunk;
			
			chunk.name = cinfo.name;
			
			chunk.chunksize = readUInt32();
			long chunkStop = position() + chunk.chunksize;
			
			chunk.propsize = readUInt32();
			long propStop = position() + chunk.propsize;
			
			while( position() < propStop ) {
				long index = readUInt32();
				long size = readUInt32();
				
				PropertyMap p;
				if( PSSG.propInfoMap.containsKey( (int)index ) ) {
					if( (p = PropertyMap.find( PSSG.propInfoMap.get( (int)index ).name ) ) != null ) {
						/*if( chunk.name.equals( "RENDERINSTANCESTREAM" ) ) {
							if( parent.parent.name.equals( "SKINNODE" ) ) {
								System.out.println( "here" + " " + p.toString() + " " + size + " " + BIOBuffer.asString( preview( CHAR, 4 ) ) );
							}
						}*/
						chunk.properties.add( this.createProperty( p.type, p.amount, index, size ) );
					} else advance( size );
				} else advance( size );
			}
			
			if( position() != propStop ) {
				System.out.println( "Something has caused the reader to be ahead of schedule..." );
				position( propStop );
			}

			if( chunk.name.equals( "RENDERSTREAMINSTANCE" ) || chunk.name.equals( "MODIFIERNETWORKINSTANCE" ) ) {
				PSSG.addToStream( chunk.parent, chunk );
			} else if( chunk.name.equals( "RISTREAM" ) ) {
				PSSG.addToStream( chunk.parent.parent, chunk );
			} else if( chunk.name.equals( "RENDERINSTANCESOURCE" ) ) {
				PSSG.addToStream( chunk.parent.parent, chunk );
			} else if( chunk.name.equals( "RENDERINSTANCESTREAM" ) ) {
				if( parent.parent.name.equals( "SKINNODE" ) ) {
					
				}
				PSSG.addToStream( chunk.parent.parent, chunk );
			} else if( chunk.name.equals( "DATABLOCK" ) ) {
				PSSG.addNode( chunk, chunk );
			} else if( chunk.name.equals( "DATABLOCKSTREAM" ) ) {
				PSSG.addNode( chunk.parent, chunk );
			} else if( chunk.name.equals( "RENDERINDEXSOURCE" ) ) {
				PSSG.addNode( chunk.parent, chunk );
			} else if( chunk.name.equals( "RENDERDATASOURCE" ) ) {
				PSSG.addNode( chunk, chunk );
			} else if( chunk.name.equals( "RENDERSTREAM" ) ) {
				PSSG.addNode( chunk.parent, chunk );
			}
			
			DataOnlyChunks c;
			if( ( c = DataOnlyChunks.find( chunk.name ) ) != null ) {
				chunk.hasData = true;
				switch( c ) {
				
				case INVERSEBINDMATRIX:
					chunk.data = read( type, (chunkStop - position() ) / type.sizeof() );
					PSSG.skeleton.addMatrix( chunk.parent, chunk );
					break;
					
				case SKINJOINT:
					PSSG.addNode( chunk.parent, chunk );
					break;
					
				case TRANSFORM:
					chunk.data = read( type, (chunkStop - position() ) / type.sizeof() );
					PSSG.addNode( chunk.parent, chunk );
					break;
					
				case BOUNDINGBOX:
					chunk.data = read( type, (chunkStop - position() ) / type.sizeof() );
					PSSG.addNode( chunk.parent, chunk );
					break;
					
				case TEXTUREIMAGEBLOCKDATA:
					//chunk.data = read( , (chunkStop - position() ) / type.sizeof() );
					Texture tex = new Texture();
					tex.id = chunk.parent.parent.getProperty( "id" ).data;
					tex.format = chunk.parent.parent.getProperty( "texelFormat" ).data;
					tex.width = Integer.parseInt( chunk.parent.parent.getProperty( "width" ).data );
					tex.height = Integer.parseInt( chunk.parent.parent.getProperty( "height" ).data );
					tex.data = readInt8s( (chunkStop - position() ) / type.sizeof() );
					PSSG.master.textures.add( tex );
					//System.out.println( tex.id );
					//chunk.parent.parent.info();
					//BIOBuffer buf = new BIOBuffer( data );
					//System.out.println( BIOBuffer.asString( buf.readUInt8s( 4 ) ) );
					/*int width = Integer.parseInt( chunk.parent.parent.getProperty( "width" ).data );
					int height = Integer.parseInt( chunk.parent.parent.getProperty( "height" ).data );
					System.out.println( width + "x" + height + " " + data.length + " " + position() + " " + chunkStop );
					BufferedImage img = new BufferedImage( width, height, BufferedImage.TYPE_4BYTE_ABGR );
					int count = 0;
					for(int h=0;h<height;h++){
		                for(int w=0;w<width;w++){
		                    img.setRGB(w, h, data[count] & data[count] & data[count] );
		                    count++;
		                }
		            }
					
					rin.gui.GUIFactory.createWindow()
						.add( rin.gui.GUIFactory.createLabel().setImage( img ) )
						.show();*/
					break;
					
				case DATABLOCKDATA:
					PSSG.addNode( chunk.parent, chunk );
					break;
					
				case INDEXSOURCEDATA:
					PSSG.addNode( chunk.parent, chunk );
					break;
					
				default:
					chunk.data = read( type, (chunkStop - position()) / type.sizeof() );
					break;
					
				}
				
				//if( type != UINT8 )
				//	System.out.println( chunk.name + " " + BIOBuffer.asString( chunk.data ) );
				
				if( position() != chunkStop ) {
					System.out.println( "did not make it on " + chunk.name + " " + position() + " " + chunkStop );
					position( chunkStop );
				}
			} else {
				while( position() < chunkStop ) {
					Type<?> t = this.findType( previewUInt32() );
					chunk.children.add( this.create( chunk, t ) );
				}
			}
			
			return chunk;
		}
		
		@Override public void define( Chunk c ) {
			long index = previewUInt32();
			if( PSSG.chunkInfoMap.containsKey( (int)index ) )
				if( !PSSG.chunkInfoMap.get( (int)index ).name.toUpperCase().equals( "PSSGDATABASE" ) )
					System.out.println( "MASSIVE ERROR TO BE REPLACED WITH EXCEPTION PSSGDatabaseNotFoundException" );
			//TODO: instead of exceptioning, allow searching for the pssgdatabase node, then exception if it does not exist

			PSSG.root = this.create( null, this.findType( previewUInt32() ) );
		}
	};
}
