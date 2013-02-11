package rin.gl.lib3d;

import java.util.Arrays;

import rin.gl.lib3d.Actor;

public class ActorList {
	private Actor[] actors;
	public int length = 0;
	
	public ActorList() {
		this.actors = new Actor[0];
	}
	
	public void add( Actor a ) {
		Actor[] tmp = this.actors.clone();
		this.actors = new Actor[ this.length+1 ];
		for( int i = 0; i < tmp.length; i++ )
			this.actors[i] = tmp[i];
		this.actors[ this.length ] = a;
		this.length++;
	}
	
	public Actor get( int index ) {
		return this.actors[ index ];
	}

	public void sort() {
		Arrays.sort( this.actors, new DistanceComparator() );
	}
}
