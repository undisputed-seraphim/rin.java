package rin.engine.game.ability;

import java.util.ArrayList;
import java.util.Iterator;

public class AbilityList extends AbstractAbility implements Iterable<AbstractAbility> {

	ArrayList<AbstractAbility> abilities = new ArrayList<AbstractAbility>();
	public AbilityList add( AbstractAbility ability ) {
		abilities.add( ability );
		return this;
	}
	
	@Override
	public AbilityList setTarget( AbilityTarget target ) {
		for( AbstractAbility aa : this )
			aa.setTarget( target );
		return this;
	}

	@Override
	public AbilityList setEffect( AbstractAbilityEffect effect ) {
		for( AbstractAbility aa : this )
			aa.setEffect( effect );
		return this;
	}

	@Override
	public void execute() {
		for( AbstractAbility aa : this )
			aa.execute();
	}

	@Override
	public Iterator<AbstractAbility> iterator() {
		return abilities.iterator();
	}

}
