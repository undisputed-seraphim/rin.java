package rin.engine.resource;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

public class Directory extends ResourcePointer {
	
	private static final FileFilter ONLY_DIRS = new FileFilter() {
		@Override public boolean accept( File file ) {
			return file.isDirectory() && !( file.getPath().equals( file.getParent() ) );
		}
	};
	
	private static final FileFilter ONLY_FILES = new FileFilter() {
		@Override public boolean accept( File file ) {
			return !file.isDirectory();
		}
	};
	
	private FileFilter getDirectoryNameFilter( final String name, final boolean partial ) {
		return new FileFilter() {
			@Override public boolean accept( File file ) {
				String n = file.getName();
				if( !partial )
					return n.equalsIgnoreCase( name ) && file.isDirectory();
				return n.toLowerCase().contains( name.toLowerCase() ) && file.isDirectory();
			}
		};
	}
	
	private FileFilter getResourceExtensionFilter( final String ext ) {
		return new FileFilter() {
			@Override public boolean accept( File file ) {
				return file.getName().toLowerCase().endsWith( ext.toLowerCase() ) && !file.isDirectory();
			}
		};
	}
	
	private FileFilter getResourceNameFilter( final String name, final boolean partial ) {
		return new FileFilter() {
			@Override public boolean accept( File file ) {
				String n = file.getName();
				if( !partial )
					return n.equalsIgnoreCase( name ) && !file.isDirectory();
				return n.toLowerCase().contains( name.toLowerCase() ) && !file.isDirectory();
			}
		};
	}
	
	public Directory( File file ) {
		super( file );
	}
	
	public boolean empty() {
		return target.listFiles().length == 0;
	}
	
	public boolean containsDirectory( String name ) {
		if( name == null )
			return target.listFiles( ONLY_DIRS ).length == 0;
		
		return target.listFiles( getDirectoryNameFilter( name, false ) ).length == 1;
	}
	
	public boolean containsDirectories( String pattern ) {
		return target.listFiles( getDirectoryNameFilter( pattern, true ) ).length >= 1;
	}
	
	public Directory getDirectory( String name ) {
		if( !containsDirectory( name ) )
			throw new ResourceManager.DirectoryNotFoundException();

		return new Directory( target.listFiles( getDirectoryNameFilter( name, false ) )[0] );
	}
	
	public Directory[] getDirectories() {
		return getDirectories( ONLY_DIRS );
	}
	
	public Directory[] getDirectories( FileFilter filter ) {
		File[] tmp = target.listFiles( filter );
		
		Directory[] res = new Directory[tmp.length];
		for( int i = 0; i < tmp.length; i++ )
			res[i] = new Directory( tmp[i] );
		
		return res;
	}
	
	public Directory[] getDirectories( String pattern ) {
		return getDirectories( getDirectoryNameFilter( pattern, true ) );
	}
	
	public Directory createDirectory( String name ) {
		if( containsDirectory( name ) )
			return getDirectory( name );
		
		File res = new File( target.getPath() + File.separator + name );
		if( res.mkdir() )
			return new Directory( res );
		
		throw new IllegalArgumentException();
	}
	
	public boolean containsResource( String name ) {
		if( name == null )
			return target.listFiles( ONLY_FILES ).length == 0;
		
		return target.listFiles( getResourceNameFilter( name, false ) ).length == 1;
	}
	
	public boolean containsResources( String pattern ) {
		return target.listFiles( getResourceNameFilter( pattern, true ) ).length >= 1;
	}
	
	public Resource getResource( String name ) {
		if( !containsResource( name ) )
			throw new ResourceManager.ResourceNotFoundException();

		return new Resource( target.listFiles( getResourceNameFilter( name, false ) )[0] );
	}
	
	public Resource findResource( String ... resource ) {
		if( resource.length == 0 )
			throw new IllegalArgumentException( "Directory#findResource() requires a resource string." );
		
		Directory res = this;
		for( int i = 0; i < resource.length - 1; i++ )
			res = res.getDirectory( resource[i] );

		return res.getResource( resource[resource.length-1] );
	}
	
	public Resource[] getResources() {
		return getResources( ONLY_FILES );
	}
	
	public Resource[] getResources( FileFilter filter ) {
		File[] tmp = target.listFiles( filter );
		
		Resource[] res = new Resource[tmp.length];
		for( int i = 0; i < tmp.length; i++ )
			res[i] = new Resource( tmp[i] );
		
		return res;
	}
	
	public Resource[] getResources( String pattern ) {
		return getResources( getResourceNameFilter( pattern, true ) );
	}
	
	public Resource[] getResourcesByExtension( String ext ) {
		if( ext.indexOf( "." ) != 0 ) ext = "."+ext;
		return getResources( getResourceExtensionFilter( ext ) );
	}
	
	public Resource createResource( String name ) {
		return createResource( name, false );
	}
	
	public Resource createResource( String name, boolean overwrite ) {
		if( containsResource( name ) ) {
			if( !overwrite )
				throw new ResourceManager.ResourceExistsException();
			
			return getResource( name );
		} else {
			File res = new File( target.getPath() + File.separator + name );
			try {
				if( res.createNewFile() ) {
					return new Resource( res );
				}
			} catch( IOException ex ) {
				// TODO Auto-generated catch block
			}
		}
		
		throw new IllegalArgumentException();
	}
	
}
