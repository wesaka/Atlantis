package atlantis.combat.micro;

import atlantis.AGame;
import atlantis.debug.APainter;
import atlantis.units.AUnit;
import atlantis.units.AUnitType;
import atlantis.units.Select;
import atlantis.util.A;
import bwapi.Color;


public class AAvoidEnemyMeleeUnitsManager {
    
    private static AUnit nearestEnemy = null;

    // =========================================================

    /**
     * If unit is ranged unit like e.g. Marine, get away from very close melee units like e.g. Zealots.
     */
    public static boolean avoidCloseMeleeUnits(AUnit unit) {
        if (shouldNotAvoidMeleeUnits(unit)) {
            return false;
        }

        // =========================================================

        boolean isEnemyDangerouslyClose = shouldRunFromAnyEnemyMeleeUnit(unit);
        if (!isEnemyDangerouslyClose) {
            return false;
        }

        // === Run the fuck outta here ==============================

//            APainter.paintTextCentered(unit.getPosition().translateByPixels(0, -12), "RUN", Color.Red);

        if (unit.runFrom(nearestEnemy, 1.5)) {
            unit.setTooltip("MeleeRun");
            return true;
        }

        return handleErrorRun(unit);
    }

    public static double getCriticalDistance(AUnit unit) {
        nearestEnemy = nearestEnemy(unit);
        if (nearestEnemy == null) {
            return Double.NEGATIVE_INFINITY;
        }

        // If unit is much slower than enemy, don't run at all. It's better to shoot instead.
        double quicknessDifference = unit.getSpeed() - nearestEnemy.getSpeed();
        int beastNearby = Select.enemy().ofType(AUnitType.Protoss_Archon, AUnitType.Zerg_Ultralisk).inRadius(5, unit).count();

        double baseCriticalDistance = 0;
        double quicknessBonus = Math.min(0.5, (quicknessDifference > 0 ? -quicknessDifference / 3 : quicknessDifference / 1.5));
        double healthBonus = unit.getWoundPercent() / 38.0;
        double beastBonus = (beastNearby > 0 ? 1.2 : 0);
        double ourMovementBonus = unit.isMoving() ? (unit.lastStartedRunningAgo(12) ? 0.8 : 0) : 1.2;
        double enemyMovementBonus = (nearestEnemy != null && unit.isOtherUnitFacingThisUnit(nearestEnemy))
                ? (nearestEnemy.isMoving() ? 2.0 : 0.7) : 0;
//        APainter.paintTextCentered(unit.getPosition(), ourMovementBonus + " // " + + enemyMovementBonus, Color.White, 0, 3);

        double criticalDist = baseCriticalDistance + quicknessBonus + healthBonus + beastBonus + ourMovementBonus + enemyMovementBonus;
        return A.inRange(0.1, criticalDist, 4.8);
    }

    // =========================================================

    private static boolean handleErrorRun(AUnit unit) {
        System.err.println("ERROR_RUN for " + unit.getShortNamePlusId());

        AAttackEnemyUnit.handleAttackNearbyEnemyUnits(unit);
        unit.setTooltip("Cant run, fight");

        return true;
    }

    private static boolean shouldNotAvoidMeleeUnits(AUnit unit) {
        boolean shouldSkip = !unit.isWorker() && (unit.isAirUnit() || unit.isMeleeUnit());
        if (shouldSkip) {
            return true;
        }

        if (Select.enemyCombatUnits().inRadius(6, unit).count() <= 0) {
            return false;
        }

        // === Reaver should not avoid if has no cooldown ===============================

        if (AGame.isPlayingAsProtoss()) {
            if (
                Select.ourCombatUnits().inRadius(7, unit).count() >= 7
                && Select.ourCombatUnits().inRadius(4, unit).count() >= 3
            ) {
                return false;
            }

            if (unit.isType(AUnitType.Protoss_Reaver) && unit.getCooldownCurrent() <= 4) {
                return true;
            }
        }

        // =========================================================

        return false;
    }

    public static boolean shouldRunFromAnyEnemyMeleeUnit(AUnit unit) {
        return isEnemyCriticallyClose(unit);
    }

    private static AUnit nearestEnemy(AUnit unit) {
        if (nearestEnemy != null && nearestEnemy.isAlive()) {
            return nearestEnemy;
        }

        return nearestEnemy = Select.enemyCombatUnits().melee().inRadius(6, unit).nearestTo(unit);
    }

    public static boolean isEnemyCriticallyClose(AUnit unit) {
        nearestEnemy = nearestEnemy(unit);

        if (nearestEnemy == null) {
            return false;
        }

        double criticalDistance = getCriticalDistance(unit);
        double enemyDistance = nearestEnemy.distanceTo(unit);

        if (enemyDistance <= criticalDistance) {
            return true;
        }
        else {
            return false;
        }
    }

}
