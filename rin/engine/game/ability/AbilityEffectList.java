package rin.engine.game.ability;

import java.util.ArrayList;
import java.util.Iterator;

public class AbilityEffectList extends AbstractAbilityEffect implements Iterable<AbstractAbilityEffect> {

	ArrayList<AbstractAbilityEffect> effects = new ArrayList<AbstractAbilityEffect>();
	public AbilityEffectList add( AbstractAbilityEffect effect ) {
		effects.add( effect );
		return this;
	}
	
	@Override
	public AbilityTarget getTarget() { return target; }
	
	@Override
	public void process() {
		for( AbstractAbilityEffect eff : effects )
			eff.setTarget( getTarget() ).process();
	}

	@Override
	public Iterator<AbstractAbilityEffect> iterator() {
		return effects.iterator();
	}
	
}
