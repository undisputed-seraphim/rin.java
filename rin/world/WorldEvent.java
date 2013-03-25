package rin.world;

import rin.system.interfaces.Event;
import rin.world.interfaces.World;

public class WorldEvent extends Event<World> {

	@Override
	public void handle() {}
	
	@Override
	public final World result() { return this.target; }

}
