package rin.util.dcode.n64;

import rin.util.bio.BinaryDecoder;
import static rin.util.bio.BinaryTypes.*;

/*0000h              (1 byte): initial PI_BSB_DOM1_LAT_REG value (0x80)
0001h              (1 byte): initial PI_BSB_DOM1_PGS_REG value (0x37)
0002h              (1 byte): initial PI_BSB_DOM1_PWD_REG value (0x12)
0003h              (1 byte): initial PI_BSB_DOM1_PGS_REG value (0x40)
0004h - 0007h     (1 dword): ClockRate
0008h - 000Bh     (1 dword): Program Counter (PC)
000Ch - 000Fh     (1 dword): Release
0010h - 0013h     (1 dword): CRC1
0014h - 0017h     (1 dword): CRC2
0018h - 001Fh    (2 dwords): Unknown (0x0000000000000000)
0020h - 0033h    (20 bytes): Image name
                             Padded with 0x00 or spaces (0x20)
0034h - 0037h     (1 dword): Unknown (0x00000000)
0038h - 003Bh     (1 dword): Manufacturer ID
                             0x0000004E = Nintendo ('N')
003Ch - 003Dh      (1 word): Cartridge ID
003Eh - 003Fh      (1 word): Country code
                             0x4400 = Germany ('D')
                             0x4500 = USA ('E')
                             0x4A00 = Japan ('J')
                             0x5000 = Europe ('P')
                             0x5500 = Australia ('U')
0040h - 0FFFh (1008 dwords): Boot code*/

public class N64Reader extends BinaryDecoder {

    public N64Reader( String file ) { super( file ); }

    public void read() {

        this.read( N64Chunks.HEADER );
        
    }

}
