package atlantis.combat.targeting;

import atlantis.units.AUnit;
import atlantis.units.Select;

public class ATargetingForSpecificUnits {

    public static AUnit target(AUnit unit) {
        if (unit.isArchon() || unit.isUltralisk()) {
            return furthestTargetInRange(unit);
        }

        return null;
    }

    // =========================================================

    private static AUnit furthestTargetInRange(AUnit unit) {
        return Select.enemyRealUnits().canBeAttackedBy(unit, true, true).mostDistantTo(unit);
    }

}