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
            return String.format( "0x%02x", bb.get() );
        }

    };

    public static final BinaryType<String> BIT8 = new BinaryType<String>() {

        @Override
        public String[] allocate( int amount ) { return new String[amount]; }

        @Override
        public String getData( ByteBuffer bb ) {
            byte b = bb.get();
            String res = "";

            for( int i = 0; i < 8; i++ )
                res += (b & i) == i ? "1" : "0";

            return res;
        }

    };

    public static final BinaryType<String> WORD = new BinaryType<String>() {

        @Override
        public String[] allocate( int amount ) { return new String[amount]; }

        @Override
        public String getData( ByteBuffer bb ) { return BIT8.getData( bb ); }

    };

}
