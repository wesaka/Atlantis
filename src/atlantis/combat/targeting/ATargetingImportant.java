package atlantis.combat.targeting;

import atlantis.units.AUnit;
import atlantis.units.AUnitType;
import atlantis.units.select.Select;

public class ATargetingImportant extends ATargeting {

    public static AUnit target(AUnit unit) {

        // =========================================================
        // =========================================================
        // =========== REMEMBER, AT THIS POINT =====================
        // ======== ENEMY IS AT MOST 15 TILES AWAY =================
        // =========================================================
        // =========================================================

        AUnit target;

        if (unit.isAir() && (target = ATargetingForAir.targetForAirUnits(unit)) != null) {
            if (ATargeting.debug(unit)) System.out.println("CA = " + target);
            return target;
        }

        if ((target = targetInShootingRange(unit)) != null) {
            return target;
        }

        if ((target = targetOutsideShootingRange(unit)) != null) {
            return target;
        }

        return null;
    }

    // =========================================================

    private static AUnit targetInShootingRange(AUnit unit) {
        AUnit target;

        // =========================================================
        // Target AIR UNITS IN RANGE

        target = enemyUnits.clone()
                .air()
                .excludeTypes(AUnitType.Zerg_Overlord)
                .inShootRangeOf(unit)
                .nearestTo(unit);
        if (target != null) {
            if (ATargeting.debug(unit)) System.out.println("C1a = " + target);
            return target;
        }

        // =========================================================
        // Defensive buildings IN RANGE

        target = Select.enemy()
                .ofType(
                        AUnitType.Protoss_Photon_Cannon,
                        AUnitType.Terran_Bunker,
                        AUnitType.Zerg_Sunken_Colony
                )
                .canBeAttackedBy(unit, 1.5)
                .nearestTo(unit);
        if (target != null) {
            return defensiveBuildingOrScvRepairingIt(target);
        }

        target = Select.enemy()
                .ofType(
                        AUnitType.Protoss_Photon_Cannon,
                        AUnitType.Terran_Bunker,
                        AUnitType.Zerg_Sunken_Colony
                )
                .inRadius(11, unit)
                .nearestTo(unit);
        if (target != null) {
            return defensiveBuildingOrScvRepairingIt(target);
        }

        // =========================================================
        // Target COMBAT UNITS IN RANGE

        // Ignore MEDICS
        target = enemyUnits.clone()
                .combatUnits()
                .excludeTypes(AUnitType.Terran_Medic)
                .inShootRangeOf(unit)
                .nearestTo(unit);
        if (target != null) {
            if (ATargeting.debug(unit)) System.out.println("C1b = " + target);
            return target;
        }

        // Take into account excluded units above e.g. MEDICS
        target = enemyUnits.clone()
                .ofType(AUnitType.Terran_Medic)
                .inShootRangeOf(unit)
                .nearestTo(unit);
        if (target != null) {
            if (ATargeting.debug(unit)) System.out.println("C1c = " + target);
            return target;
        }

        return null;
    }

    private static AUnit defensiveBuildingOrScvRepairingIt(AUnit unit) {
        if (!unit.isBunker()) {
            if (ATargeting.debug(unit)) System.out.println("C0c = " + unit);
            return unit;
        }

        // Target repairers
        AUnit repairer = Select.enemy().workers().notGathering().inRadius(2, unit)
                .canBeAttackedBy(unit, 1.7).nearestTo(unit);
        if (repairer != null) {
            if (ATargeting.debug(unit)) System.out.println("C0a = " + repairer);
            return repairer;
        }

        if (ATargeting.debug(unit)) System.out.println("C0b = " + unit);
        return unit;
    }

    private static AUnit targetOutsideShootingRange(AUnit unit) {
        AUnit target;

        // =========================================================
        // Target COMBAT UNITS IN RANGE

        target = enemyUnits.clone()
                .combatUnits()
                .inShootRangeOf(unit)
//                .inRadius(13, unit)
                .nearestTo(unit);
        if (target != null) {
            if (ATargeting.debug(unit)) System.out.println("C4 = " + target);
            return target;
        }

        // =========================================================
        // Including unfinished defensive buildings

        target = Select.enemy()
                .ofType(
                        AUnitType.Protoss_Photon_Cannon,
                        AUnitType.Zerg_Sunken_Colony,
                        AUnitType.Zerg_Creep_Colony,
                        AUnitType.Zerg_Spore_Colony,
                        AUnitType.Terran_Bunker,
                        AUnitType.Terran_Missile_Turret
                )
                .inRadius(12, unit)
                .canBeAttackedBy(unit, 4)
                .nearestTo(unit);

        if (ATargeting.debug(unit)) System.out.println("C5 = " + target);
        return target;
    }

}
