package rin.world;

import java.util.concurrent.ConcurrentHashMap;

import rin.system.interfaces.Manager;
import rin.world.entity.interfaces.Entity;

public class WorldManager implements Manager<Entity> {

	private ConcurrentHashMap<String, Entity> entities = new ConcurrentHashMap<String, Entity>();
	
	@Override
	public ConcurrentHashMap<String, Entity> getManagedItems() { return this.entities; }
	
}
