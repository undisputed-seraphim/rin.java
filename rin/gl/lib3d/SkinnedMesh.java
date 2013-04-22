package rin.gl.lib3d;

import static org.lwjgl.opengl.GL20.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import rin.engine.game.entity.animated.Bone;
import rin.engine.game.entity.animated.Skeleton;
import rin.engine.math.Quaternion;
import rin.engine.util.ArrayUtils;
import rin.gl.GL;

public class SkinnedMesh extends Mesh {

	private String currentAnimation = "DEFAULT";
	private HashMap<String, ArrayList<Skeleton>> skeletons = new HashMap<String, ArrayList<Skeleton>>();
	
	public void setCurrentAnimation( String anim ) {
		if( skeletons.containsKey( anim ) )
			currentAnimation = anim;
	}
	
	public void addSkeleton( String animation, Skeleton skel ) {
		if( !skeletons.containsKey( animation ) )
			skeletons.put( animation, new ArrayList<Skeleton>() );
		skeletons.get( animation ).add( skel );
	}
	
	public Skeleton getSkeleton( String animation, int index ) {
		return skeletons.get( animation ).get( index );
	}
	
	public void setSkeleton( int index ) {
		Skeleton skel = skeletons.get( currentAnimation ).get( index );
		TreeMap<Integer, Bone> boneMap = skel.getBoneMap();
		for( Integer i : boneMap.keySet() ) {
			float[] tmp = boneMap.get( i ).getTransformation().toQuaternion().getArray();
			glUniform4f( GL.getUniform( "quats[" + i + "]" ), tmp[0], tmp[1], tmp[2], tmp[3] );
			tmp = boneMap.get( i ).getTransformation().getTranslation().getArray();
			glUniform3f( GL.getUniform( "trans[" + i + "]" ), tmp[0], tmp[1], tmp[2] );
		}
	}
	
	public void render( boolean unique ) {
		glUniformMatrix4( GL.getUniform( "mMatrix"), false, this.getMatrix().gl() );
		int count = 0;
		for( Poly p : this.getPolys() ) {
			if( count < skeletons.size() ) {
				setSkeleton( count );
			}
			p.render( unique );
			count++;
		}
	}
}
