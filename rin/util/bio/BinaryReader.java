package rin.util.bio;

import java.nio.ByteBuffer;

public class BinaryReader {

    private ByteBuffer data;

    public BinaryReader( String file ) {}

    private <T> T[] fillArray( Class<T> cls, T[] arr ) {
    }

    public <T> T read( BinaryType<T> type ) { return type.getData( this.data ); }
    public <T> T[] read( BinaryType<T> type, int amount ) {
        T[] res = type.allocate( amount );
        for( int i = 0; i < res.length; i++ )
            res[i] = this.read( type );
        return res;
    }

    public byte readInt8() { return this.data.get(); }
    public byte[] readInt8( int amount ) {
        byte[] res = new byte[amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = this.readInt8();
        return res;
    }

    public short readUInt8() { return (short)(this.data.get() & 0xFF); }
    public short[] readUInt8( int amount ) {
        short[] res = new short[amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = this.readUInt8();
        return res;
    }

    public char readChar() { return new String( new byte[] { this.data.get() } ).charAt( 0 ); }
    public char[] readChar( int amount ) {
        char[] res = new char[amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = this.readChar();
        return res;
    }

    public short readInt16() { return this.data.getShort(); }
    public short[] readInt16( int amount ) {
        short[] res = new short[amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = this.readInt16();
        return res;
    }

    public int readUInt16() { return this.data.getShort() & 0xFFFF; }
    public int[] readUInt16( int amount ) {
        int[] res = new int[amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = this.readUInt16();
        return res;
    }

    public int readInt32() { return this.data.getInt(); }
    public int[] readInt32( int amount ) {
        int[] res = new int[amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = this.readInt32();
        return res;
    }

    public long readUInt32() { return this.data.getInt() & 0xFFFFFFFFL; }
    public long[] readUInt32( int amount ) {
        long[] res = new long[amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = this.readUInt32();
        return res;
    }

    public long readInt64() { return this.data.getLong(); }
    public long[] readInt64( int amount ) {
        long[] res = new long[amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = this.readInt64();
        return res;
    }

    public float readFloat32() { return this.data.getFloat(); }
    public float[] readFloat32( int amount ) {
        float[] res = new float[amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = this.readFloat32();
        return res;
    }

}
