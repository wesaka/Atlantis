package atlantis.combat.micro.avoid;

import atlantis.combat.micro.AAttackEnemyUnit;
import atlantis.debug.APainter;
import atlantis.position.APosition;
import atlantis.position.HasPosition;
import atlantis.units.AUnit;
import atlantis.units.Units;
import atlantis.util.A;
import bwapi.Color;

public class Avoid {

    public static boolean unit(AUnit unit, AUnit enemy) {
        APainter.paintCircle(enemy, 16, Color.Orange);

        if (unit.runningManager().runFrom(enemy, getRunDistance(unit, enemy))) {
            unit.setTooltip(getTooltip(unit, enemy));
            return true;
        }

        return handleErrorRun(unit);
    }

    public static boolean groupOfUnits(AUnit unit, Units enemiesDangerouslyClose) {
        for (AUnit enemy : enemiesDangerouslyClose.list()) {
            APainter.paintCircle(enemy, 16, Color.Orange);
        }

        APosition enemiesCenter = enemiesDangerouslyClose.median();
        APainter.paintCircle(enemiesCenter, 6, Color.Orange);
        APainter.paintCircle(enemiesCenter, 4, Color.Orange);
        APainter.paintCircle(enemiesCenter, 2, Color.Orange);

        if (unit.runningManager().runFrom(enemiesCenter, getRunDistance(unit, enemiesCenter))) {
            unit.setTooltip("GroupAvoid(" + A.digit(unit.distTo(enemiesCenter)) + ")");
            return true;
        }

        return handleErrorRun(unit);
    }

    // =========================================================

    protected static double getRunDistance(AUnit unit, HasPosition enemies) {
        return 1.8;
    }

    protected static double getRunDistance(AUnit unit, AUnit enemy) {
        if (unit.isInfantry()) {
            return 2.5;
        }

        return 1.8 + (enemy.isQuick() ? 2 : 0);
    }

    protected static String getTooltip(AUnit unit, AUnit enemy) {
        String dist = "(" + A.digit(unit.distTo(enemy)) + ")";

        if (enemy.isMelee()) {
            return "MeleeRun" + dist;
        }
        else if (enemy.isRanged()) {
            return "RangedRun" + dist;
        }
        else {
            return "MeleeRun" + dist;
        }
    }

    protected static boolean handleErrorRun(AUnit unit) {
        System.err.println("ERROR_RUN for " + unit.shortNamePlusId());

        AAttackEnemyUnit.handleAttackNearbyEnemyUnits(unit);
        unit.setTooltip("Cant run, fight");

        return true;
    }

}
