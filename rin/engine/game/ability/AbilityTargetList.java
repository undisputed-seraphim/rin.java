package rin.engine.game.ability;

import java.util.ArrayList;
import java.util.Iterator;

public class AbilityTargetList implements AbilityTarget, Iterable<AbilityTarget> {

	ArrayList<AbilityTarget> targets = new ArrayList<AbilityTarget>();
	public AbilityTargetList add( AbilityTarget target ) {
		targets.add( target );
		return this;
	}
	
	@Override
	public void apply( AbstractAbilityEffect effect ) {
		for( AbilityTarget ab : targets )
			ab.apply( effect.setTarget( ab ) );
	}
	
	@Override
	public Iterator<AbilityTarget> iterator() {
		return targets.iterator();
	}
	
}
