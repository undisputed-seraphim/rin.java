package rin.engine.game.ability;

public class AbilityFactory {

	public static Ability createAbility( AbilityTarget target, AbilityEffect<?> effect ) {
		return new Ability() {
			private AbilityTarget tar;
			private AbilityEffect<?> eff;
			public Ability setTarget( AbilityTarget target ) { tar = target; return this; }
			public Ability setEffect( AbilityEffect<?> effect ) { eff = effect; return this; }
			public void execute() { tar.apply( eff ); }
		}.setTarget( target ).setEffect( effect );
	}
	
}
