package rin.gl.model;

import java.util.ArrayList;

import rin.engine.Engine;
import rin.engine.util.ArrayUtils;
import rin.engine.util.PSSGExtractor;
import rin.gl.lib3d.Actor;
import rin.gl.lib3d.Poly;
import rin.util.Buffer;

public class ModelISM2 implements Model {

	@Override
	public Actor fromFile(String file) {
		PSSGExtractor ims2 = new PSSGExtractor( Engine.MAINDIR + "packs/rin/002.ism2" );
		System.out.println( "IMS2: " );
		for( int i = 0; i < ims2.vmap.size(); i++ )
			System.out.println( "vmap " + (i+1) + " " + ims2.vmap.get( i ).length );
		
		for( int i = 0; i < ims2.imap.size(); i++ ) {
			//System.out.println( ArrayUtils.asString( ims2.imap.get( i ) ) );
			System.out.println( "imap " + (i+1) + " " + ims2.imap.get( i ).length );
		}
		
		ArrayList<Float> v = new ArrayList<Float>();
		for( int i = 0; i < ims2.imap.get( 0 ).length; i++ ) {
			v.add( ims2.vmap.get( 2 )[ims2.imap.get( 0 )[i]*3]);
			v.add( ims2.vmap.get( 2 )[ims2.imap.get( 0 )[i]*3+1]);
			v.add( ims2.vmap.get( 2 )[ims2.imap.get( 0 )[i]*3+2]);
		}
		Poly poly = new Poly();
		poly.setBound( false );
		poly.build( Buffer.toArrayf( v ), new float[0], new float[0], new float[0] );
		return poly;
	}

}
