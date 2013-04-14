package rin.util.bio;

import java.io.DataInputStream;
import java.io.IOException;

public abstract class BinaryStreamReader {
	
	public abstract DataInputStream getStream();
    
    public String readString( int length ) throws IOException {
        String res = "";
        for( int i = 0; i < length; i++ )
            res += this.readChar();
        return res;
    }
    
    public byte readInt8() throws IOException { return this.getStream().readByte(); }
    public byte[] readInt8( int amount ) throws IOException {
        byte[] res = new byte[amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = this.readInt8();
        return res;
    }
    
    public short readUInt8() throws IOException { return (short)(this.getStream().readByte() & 0xFF); }
    public short[] readUInt8( int amount ) throws IOException {
        short[] res = new short[amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = this.readUInt8();
        return res;
    }
    
    public char readChar() throws IOException { return new String( new byte[] { this.getStream().readByte() } ).charAt( 0 ); }
    public char[] readChar( int amount ) throws IOException {
        char[] res = new char[amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = this.readChar();
        return res;
    }
    
    public short readInt16() throws IOException { return this.getStream().readShort(); }
    public short[] readInt16( int amount ) throws IOException {
        short[] res = new short[amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = this.readInt16();
        return res;
    }
    
    public int readUInt16() throws IOException { return this.getStream().readShort() & 0xFFFF; }
    public int[] readUInt16( int amount ) throws IOException {
        int[] res = new int[amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = this.readUInt16();
        return res;
    }
    
    public int readInt32() throws IOException { return this.getStream().readInt(); }
    public int[] readInt32( int amount ) throws IOException {
        int[] res = new int[amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = this.readInt32();
        return res;
    }
    
    public long readUInt32() throws IOException { return this.getStream().readInt() & 0xFFFFFFFFL; }
    public long[] readUInt32( int amount ) throws IOException {
        long[] res = new long[amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = this.readUInt32();
        return res;
    }

    public long readInt64() throws IOException { return this.getStream().readLong(); }
    public long[] readInt64( int amount ) throws IOException {
        long[] res = new long[amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = this.readInt64();
        return res;
    }

    public float readFloat32() throws IOException { return this.getStream().readFloat(); }
    public float[] readFloat32( int amount ) throws IOException {
        float[] res = new float[amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = this.readFloat32();
        return res;
    }
    
    public double readFloat64() throws IOException { return this.getStream().readDouble(); }
    public double[] readFloat64( int amount ) throws IOException {
        double[] res = new double[amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = this.readFloat64();
        return res;
    }
    
}
