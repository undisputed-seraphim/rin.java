package rin.engine.game.ability;

import java.util.ArrayList;
import java.util.Iterator;

public class AbilityEffectList implements AbilityEffect<AbilityTarget>, Iterable<AbilityEffect<AbilityTarget>> {

	ArrayList<AbilityEffect<AbilityTarget>> effects = new ArrayList<AbilityEffect<AbilityTarget>>();
	public AbilityEffectList add( AbilityEffect<AbilityTarget> effect ) {
		effects.add( effect );
		return this;
	}
	
	@Override
	public void process( AbilityTarget target ) {
		for( AbilityEffect<AbilityTarget> eff : effects )
			eff.process( target );
	}

	@Override
	public Iterator<AbilityEffect<AbilityTarget>> iterator() {
		return effects.iterator();
	}
	
}
