package atlantis.combat.retreating;

import atlantis.AGame;
import atlantis.combat.ACombatEvaluator;
import atlantis.combat.missions.MissionChanger;
import atlantis.units.AUnit;
import atlantis.units.Select;

import java.util.Objects;

public class RetreatManager {

    public static boolean shouldNotRetreat(AUnit unit) {
        return shouldRetreat(unit);
    }

    /**
     * If chances to win the skirmish with the nearby enemy units aren't favorable, avoid fight and retreat.
     */
    public static boolean shouldRetreat(AUnit unit) {
        if (shouldNotConsiderRetreatingNow(unit)) {
            return false;
        }

        boolean isNewFight = (unit.getUnitAction() != null && !unit.getUnitAction().isRunningOrRetreating());
        boolean isSituationFavorable = ACombatEvaluator.isSituationFavorable(unit, isNewFight);

        // If situation is unfavorable, retreat
        if (!isSituationFavorable) {
            unit._lastRetreat = AGame.now();
            unit.setTooltip("Retreat");
            MissionChanger.notifyThatUnitRetreated(unit);
            return unit.runningManager().runFromHere();
        }

        if (Objects.equals(unit.getTooltip(), "Retreat")) {
            unit.removeTooltip();
        }

        return false;
    }

    // =========================================================

    protected static boolean shouldNotConsiderRetreatingNow(AUnit unit) {
        if (unit.type().isReaver()) {
            return Select.enemyRealUnits().inRadius(12, unit).isEmpty() && unit.getCooldownCurrent() <= 7;
        }

        return false;
    }

}