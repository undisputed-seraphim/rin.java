package rin.gl.model;

import java.util.ArrayList;

import rin.engine.Engine;
import rin.engine.math.Matrix4x4;
import rin.engine.math.Quaternion;
import rin.engine.math.Vector3;
import rin.engine.resource.Resource;
import rin.engine.resource.ResourceManager;
import rin.engine.resource.ResourceManager.ResourceNotFoundException;
import rin.engine.resource.formats.pssg.PSSGDecoder;
import rin.engine.resource.formats.pssg.PSSGDecoder.ActualAnimation;
import rin.engine.resource.model.ModelContainer;
import rin.engine.util.ArrayUtils;
import rin.gl.lib3d.Actor;
import rin.gl.lib3d.Mesh;
import rin.gl.lib3d.ModelScene;
import rin.util.dcode.pssg.PSSGFile;

public class ModelPSSG implements Model {

	@Override public Actor fromResource( Resource resource ) {
		//System.out.println( file );
		String path = resource.getDirectory().getPath();
		String name = resource.getName();
		
		Mesh mesh = new Mesh();
		PSSGFile pssg = new PSSGFile( resource.getPath() );
		pssg.read();
		
		PSSGFile.PSSGData data = pssg.getData();
		//mesh.setSkeleton( data.skel.skel );
		//System.out.println( "Skeleton size: " + mesh.getSkeleton().getBoneMap().size() + " submeshes: " + data.sources.length );
		
		/*ArrayList<PSSGResource> anims = new ArrayList<PSSGResource>();
		for( int i = 1; ; i++ ) {
			try {
				//ResourceIdentifier tmp = ResourceManager.getPackResource( "meruru", "models", "meruru", "meruru_anim" + i + ".pssg" );
				//System.out.println( "got anim " + i );
				//anims.add( ResourceManager.getDecoder( PSSGDecoder.class ).decode( tmp ) );
				//for( String s : res.getAnimationMap().keySet() ) {
					//ActualAnimation a = res.getAnimation( "PC22_F_WAIT" );
					//System.out.println( "FIRST: " + s  + " ("+a.boneMap.size() + " bones)" );
					//for( String ss : a.boneMap.keySet() ) {
					//	if( PSSGFile.PSSG.rootNode.find( ss ) == null ) {
							//System.out.println( ss + " not found!!" );
						//}
					//}
					//break;
				//}
			} catch( ResourceNotFoundException ex ) {
				break;
			}
		}*/
		
		for( PSSGFile.PSSGDataSource ds : data.sources ) {
			if( ds.skindices.length != 0 ) {
				//System.out.println( ArrayUtils.asString( ds.weights ) );
				//System.out.println( ArrayUtils.asString( ds.skindices ) );
				
				float[] v = new float[ds.indices.length*3];
				float[] t = new float[ds.indices.length*2];
				float[] b = new float[ds.indices.length*4];
				float[] w = new float[ds.indices.length*4];
				
				//System.out.println( ds.skindices.length + " " + ds.indices.length + " " + ds.weights.length );
				for( int i = 0; i < ds.indices.length; i++ ) {
					
					v[i*3] = ds.v[ ds.indices[i] * 3 ];
					v[i*3+1] = ds.v[ ds.indices[i] * 3 + 1 ];
					v[i*3+2] = ds.v[ ds.indices[i] * 3 + 2 ];

					//System.out.println( "bone: " + ds.skindices[ ds.indices[i] ] );
					
					t[i*2] = ds.t[ ds.indices[i] * 3 ];
					t[i*2+1] = ds.t[ ds.indices[i] * 3 + 1 ];
					
					for( int j = 0; j < 4; j++ ) {
						if( ds.weights[ ds.indices[i]*4+j ] != 0.0f ) {
							b[i*4+j] = ds.skindices[ ds.indices[i]*4+j ];
							w[i*4+j] = ds.weights[ ds.indices[i]*4+j ];
						} else {
							b[i*4+j] = -1;
							w[i*4+j] = 0;
						}
					}
				}

				//System.out.println( "Max for this set: " + ds.skindices[(max*4)+3] + " " + ds.joints.size() + " " + ds.indices.length + " " + ds.skindices.length );
				String tex = path + "textures" + Engine.LS + ds.texture.id.substring( 0, ds.texture.id.lastIndexOf( "." ) ) + ".png";
				mesh.addPoly( v, new float[0], t, tex, b, w );
				
				/*Skeleton skel = new Skeleton();
				for( int i = 0; i < ds.joints.size(); i++ ) {
					skel.addBone( ds.joints.get( i ), ds.jmats.get( i ) );
				}
				mesh.addSkeleton( "DEFAULT", skel );*/
				
				/*for( PSSGResource res : anims ) {
					for( String s : res.getAnimationMap().keySet() ) {
						ActualAnimation a = res.getAnimation( s );
						Skeleton tmp = skel.clone();
						for( String ss : ds.joints ) {
							if( a.boneMap.containsKey( ss ) ) {
								//System.out.println( "Animation " + s + " contains bone " + ss + " " + a.boneMap.get( ss ).getChannel( "Time" ).data.length );
								Matrix4x4 mat = new Matrix4x4();
								Quaternion rot;
								Vector3 trans;
								
								if( a.boneMap.get( ss ).getChannel( "Translation" ).data.length != 0 ) {
									System.out.println( "HAS TRANSLATION" );
									trans = new Vector3( a.boneMap.get( ss ).getChannel( "Translation" ).data );
								} else {
									trans = new Vector3();
								}
								
								if( a.boneMap.get( ss ).getChannel( "Rotation" ).data.length != 0 ) {
									System.out.println( "HAS ROTATION" );
									rot = new Quaternion( a.boneMap.get( ss ).getChannel( "Rotation" ).data );
								} else {
									rot = new Quaternion();
								}
								tmp.getBone( ss ).setTransformation( Matrix4x4.multiply( rot.toRotationMatrix(), mat.translate( trans ) ) );
							}
						}
						mesh.addSkeleton( s , tmp );
					}
				}*/
			}
		}
		
		mesh.setPosition( 100.0f, 0.0f, 0.0f );
		mesh.setScale( 0.01f, 0.01f, 0.01f );
		mesh.setRotation( 90.0f, 0.0f, 0.0f );
		
		return mesh;
	}

	@Override
	public Actor fromContainer( ModelContainer container ) {
		ModelScene pssg = container.getScene();
		
		return pssg;
	}

}
