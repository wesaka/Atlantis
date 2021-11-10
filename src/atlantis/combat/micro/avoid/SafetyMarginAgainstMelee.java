package atlantis.combat.micro.avoid;

import atlantis.units.AUnit;
import atlantis.units.AUnitType;
import atlantis.units.select.Select;
import atlantis.units.select.Selection;

public class SafetyMarginAgainstMelee extends SafetyMargin {

    public static double calculate(AUnit attacker, AUnit defender) {
        double criticalDist;

        if (defender.isInfantry()) {
            criticalDist = 2.1 + defender.woundPercent() / 26
                    + ourMovementBonus(defender) / 2
                    + enemyMovementBonus(defender, attacker) / 2;
        }
        else {
            criticalDist = enemyWeaponRangeBonus(defender, attacker)
                    + woundedAgainstMeleeBonus(defender)
                    + beastBonus(defender)
                    + ourUnitsNearbyBonus(defender)
                    + workerBonus(defender, attacker)
                    + ourMovementBonus(defender)
                    + quicknessBonus(defender, attacker)
                    + enemyMovementBonus(defender, attacker);
        }

        criticalDist = Math.min(criticalDist, 3.7);
//        System.out.println("criticalDist = " + criticalDist + " // " + ourUnitsNearbyBonus(defender));

        return attacker.distTo(defender) - criticalDist;
    }

    // =========================================================

    protected static double beastBonus(AUnit defender) {
        int beastNearby = Select.enemy()
                .ofType(
                        AUnitType.Protoss_Archon,
                        AUnitType.Protoss_Dark_Templar,
                        AUnitType.Zerg_Ultralisk
                )
                .inRadius(5, defender)
                .count();

        return beastNearby > 0 ? 1.6 : 0;
    }

    protected static double woundedAgainstMeleeBonus(AUnit defender) {
        if (defender.isAirUnit()) {
            return defender.woundPercent() / 10;
        }

        boolean applyExtraModifier = defender.isTank();
        return (defender.woundPercent() * (applyExtraModifier ? 2 : 1)) / 32.0;
    }

}
