package rin.engine.util.binary;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BinaryChunk {

	private ByteBuffer data;
	
	public BinaryChunk( byte[] b ) { data = ByteBuffer.wrap( b ); }
	
	public void setBigEndian() { data.order( ByteOrder.BIG_ENDIAN ); }
	public void setLittleEndian() { data.order( ByteOrder.LITTLE_ENDIAN ); }
	
	public float toFloat32() { return data.getFloat(); }
	public float[] toFloat32s() {
		int length = (int) Math.floor( data.capacity() / 4 );
		float[] res = new float[ length ];
		for( int i = 0; i < length; i++ )
			res[i] = toFloat32();
		return res;
	}
}
