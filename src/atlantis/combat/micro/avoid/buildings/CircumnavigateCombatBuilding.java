package atlantis.combat.micro.avoid.buildings;

import atlantis.debug.painter.APainter;
import atlantis.map.position.APosition;
import atlantis.units.AUnit;
import atlantis.units.actions.Actions;
import bwapi.Color;

public class CircumnavigateCombatBuilding {

    /**
     * Try to go around a defensive building by not running back-and-forth, but sideways.
     */
    public static boolean handle(AUnit unit, AUnit combatBuilding) {
        APosition goTo = findPositionAround(unit, combatBuilding);

        APainter.paintLine(unit, goTo, Color.Orange);
        APainter.paintCircle(goTo, 4, Color.Orange);

        if (unit.move(goTo, Actions.MOVE_MACRO, "Around!", false)) {
            unit.setTooltip("SmartAround", false);
            return true;
        }
        
        return false;
    }

    public static APosition findPositionAround(AUnit unit, AUnit combatBuilding) {
        int roamingRange = 3;

        APosition raw = unit.translateTilesTowards(-roamingRange - 0.2, combatBuilding);

        // Now we randomize the position to implement "circling around" the combat building
        return raw.randomizePosition(roamingRange);
    }
}