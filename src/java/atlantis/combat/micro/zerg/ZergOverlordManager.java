package atlantis.combat.micro.zerg;

import atlantis.information.AtlantisEnemyInformationManager;
import atlantis.information.AtlantisMap;
import atlantis.scout.AtlantisScoutManager;
import atlantis.wrappers.SelectUnits;
import jnibwapi.Position;
import jnibwapi.Unit;

/**
 *
 * @author Rafal Poniatowski <ravaelles@gmail.com>
 */
public class ZergOverlordManager {

    public static void update(Unit unit) {

        // We know enemy building
        if (AtlantisEnemyInformationManager.hasDiscoveredEnemyBuilding()) {
            actWhenWeKnowEnemy(unit);
        } // We don't know any enemy building
        else {
            actWhenDontKnowEnemyLocation(unit);
        }
    }

    // --------------------------------------------------------------------
    /**
     * We know at least one enemy building location.
     */
    private static void actWhenWeKnowEnemy(Unit unit) {
        Position goTo = AtlantisMap.getMainBaseChokepoint();
        if (goTo == null) {
            goTo = SelectUnits.mainBase();
        }

        unit.setTooltip("Retreat");
        if (goTo != null && goTo.distanceTo(unit) > 3) {
            unit.setTooltip("--> Retreat");
            unit.move(goTo, false);
        }
    }

    /**
     * We don't know at any enemy building location.
     */
    private static void actWhenDontKnowEnemyLocation(Unit unit) {
        AtlantisScoutManager.tryToFindEnemy(unit);
        unit.setTooltip("Find enemy");
    }

}
