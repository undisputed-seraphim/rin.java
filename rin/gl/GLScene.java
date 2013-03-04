package rin.gl;

import java.util.ArrayList;

import rin.gl.lib3d.Actor;

public class GLScene {
	private static volatile ArrayList<Actor> actors = new ArrayList<Actor>();
	public static ArrayList<Actor> getActors() { return GLScene.actors; }
}
