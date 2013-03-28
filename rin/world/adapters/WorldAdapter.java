package rin.world.adapters;

import java.util.Map;

import rin.world.WorldManager;
import rin.world.entity.interfaces.Entity;
import rin.world.interfaces.World;

public class WorldAdapter implements World {

	private WorldManager manager = new WorldManager();
	
	@Override
	public WorldManager getManager() { return this.manager; }
	
	@Override
	public Map<String, Entity> getEntities() { return this.manager.getManagedItems(); }
	
}
