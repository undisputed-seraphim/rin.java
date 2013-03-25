package rin.util.dcode.dds;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import rin.util.RinUtils;
import rin.util.bio.BIOFile;

public class DDSFile extends BIOFile {
	public DDSFile( String file ) { super( file ); }
	
	public class DDSHeader {
		
		public DDSHeader() {
			System.out.println( readString( 4 ) );
		}
	}
	
	@Override public void read() {
		new DDSHeader();
	}
	
	@Override public void write() {}
}
