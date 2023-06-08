package atlantis.combat.micro.terran;

import atlantis.config.env.Env;
import atlantis.debug.painter.AAdvancedPainter;
import atlantis.game.A;
import atlantis.game.AGame;
import atlantis.game.CameraManager;
import atlantis.map.ABaseLocation;
import atlantis.map.Bases;
import atlantis.map.position.APosition;
import atlantis.units.AUnit;
import atlantis.units.AUnitType;
import atlantis.units.actions.Actions;
import atlantis.units.select.Select;
import atlantis.units.select.Selection;
import bwapi.Color;

import java.util.List;

public class TerranCommandCenter {

    public static boolean update(AUnit building) {
        if (AGame.notNthGameFrame(46)) {
            return false;
        }

        if (building.isLifted() || baseMinedOut(building)) {
            return flyToNewMineralPatches(building);
        }

        return false;
    }

    // =========================================================

    private static boolean baseMinedOut(AUnit building) {
        return Select.minerals().inRadius(12, building).isEmpty();
    }

    private static boolean flyToNewMineralPatches(AUnit building) {
        if (Env.isTesting()) {
            return false;
        }

        List<AUnit> minerals = Select.minerals().sortDataByDistanceTo(building, true);
        Selection bases = Select.ourBuildingsWithUnfinished().ofType(AUnitType.Terran_Command_Center);
        ABaseLocation baseLocation = Bases.expansionFreeBaseLocationNearestTo(building);

        if (baseLocation == null && !Env.isTesting()) {
            System.err.println("No expansionFreeBaseLocationNearestTo for rebasing");
            return false;
        }

        APosition rebaseTo = baseLocation.isPositionVisible()
            ? baseLocation.makeLandableFor(building)
            : baseLocation.position();

        if (rebaseTo == null) {
            rebaseTo = minerals.get(0) != null ? minerals.get(0).position() : null;
        }

        if (rebaseTo == null) {
            System.err.println("Null rebaseTo");
            return false;
        }

//        System.out.println(rebaseTo + " // " + A.dist(Select.main(), rebaseTo));

        AAdvancedPainter.paintBase(rebaseTo, "REBASE HERE", Color.Green, -0.5);

//        System.out.println("baseLocation.isExplored() = " + baseLocation.isExplored());
//        System.out.println("minerals = " + Select.minerals().inRadius(10, rebaseTo).notEmpty());

            if (
                !rebaseTo.isExplored()
                || Select.minerals().inRadius(10, rebaseTo).notEmpty()
            ) {
                if (!building.isLifted() && rebaseTo.distToMoreThan(building, 3)) {
//                    System.err.println("# Lift");
                    building.lift();
                }
                else {
//                    if (A.everyNthGameFrame(31)) {
                    double dist = rebaseTo.distTo(building);
                    building.setTooltip("Rebase" + A.dist(dist), true);
                    if (dist <= 5) {
                        rebaseTo = baseLocation.makeLandableFor(building);
//                        System.err.println("# Land at " + rebaseTo.toTilePosition());
                        if (rebaseTo != null) {
                            building.land(rebaseTo.toTilePosition());
                            return true;
                        }
                    } else {
//                        System.err.println("# Fly to " + rebaseTo + " // " + dist);
                        building.move(rebaseTo, Actions.MOVE_SPECIAL, "FlyToRebase", true);
                    }
                }
                return true;
            }
//        }

        return false;
    }

}
