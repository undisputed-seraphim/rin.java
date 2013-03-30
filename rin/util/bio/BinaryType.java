package rin.util.bio;

import java.nio.ByteBuffer;

public abstract class BinaryType<T> {

    public abstract T[] allocate( int amount );
    public abstract T getData( ByteBuffer bb );

    /* ------------ predefined common types for convenience ----------------- */

    public static final BinaryType<String> HEX8 = new BinaryType<String>() {

        @Override
        public String[] allocate( int amount ) { return new String[amount]; }

        @Override
        public String getData( ByteBuffer bb ) {
            return String.format( "%08x", Integer.parseInt( Integer.toBinaryString( bb.get() ) ) );
        }

    };

    public static final BinaryType<String> BIT8 = new BinaryType<String>() {

        @Override
        public String[] allocate( int amount ) { return new String[amount]; }

        @Override
        public String getData( ByteBuffer bb ) {
            return String.format( "%08d", Integer.parseInt( Integer.toBinaryString( bb.get() ) ) );
        }

    };

}
