package atlantis.dedicated.protoss;

import atlantis.combat.micro.generic.MobileDetector;
import atlantis.combat.squad.Squad;
import atlantis.position.APosition;
import atlantis.units.AUnit;
import atlantis.units.AUnitType;
import atlantis.units.select.Select;
import atlantis.units.select.Selection;
import atlantis.units.actions.UnitActions;

public class ProtossObserver extends MobileDetector {

    protected static AUnitType type = AUnitType.Protoss_Observer;

    // =========================================================

    public static boolean update(AUnit scienceVessel) {
        return MobileDetector.update(scienceVessel);
    }

}
