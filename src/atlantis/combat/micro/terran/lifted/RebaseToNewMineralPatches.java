package atlantis.combat.micro.terran.lifted;

import atlantis.architecture.Manager;
import atlantis.config.env.Env;
import atlantis.game.AGame;
import atlantis.map.base.ABaseLocation;
import atlantis.map.base.Bases;
import atlantis.map.position.APosition;
import atlantis.units.AUnit;
import atlantis.units.actions.Actions;
import atlantis.units.select.Select;
import atlantis.util.log.ErrorLog;

public class RebaseToNewMineralPatches extends Manager {
    public RebaseToNewMineralPatches(AUnit unit) {
        super(unit);
    }

    @Override
    public boolean applies() {
        if (AGame.notNthGameFrame(37)) {
            return false;
        }

        return unit.isCommandCenter() && (unit.isLifted() || isBaseMinedOut());
    }

    @Override
    public Manager handle() {
        if (flyToNewMineralPatches()) {
            return usedManager(this);
        }

        return null;
    }

    private boolean flyToNewMineralPatches() {
        if (Env.isTesting()) {
            return false;
        }

        ABaseLocation newBase = defineNewBaseLocation();

        if (newBase == null) {
            ErrorLog.printErrorOnce("Null newBase");
            return false;
        }

        if (flyToRebase(newBase)) return true;

        return false;
    }

    private ABaseLocation defineNewBaseLocation() {
        ABaseLocation baseLocation = Bases.expansionFreeBaseLocationNearestTo(unit);

        if (baseLocation == null && !Env.isTesting()) {
            ErrorLog.printErrorOnce("No expansionFreeBaseLocationNearestTo for rebasing");
            return null;
        }

        return baseLocation;
    }

    private boolean flyToRebase(ABaseLocation baseLocation) {
        if (liftToFlyFirst(baseLocation)) return true;

        double dist = baseLocation.distTo(unit);
//        System.out.println("Rebase" + A.dist(dist));

        if (dist >= 7) {
            return moveToRebase(baseLocation);
        }
        else {
            issueLandOrder(baseLocation);
            return true;
        }
    }

    private boolean moveToRebase(ABaseLocation rebaseTo) {
        unit.setTooltipAndLog("MoveToRebase");
        unit.move(rebaseTo, Actions.MOVE_SPECIAL, "FlyToRebase", true);
        return true;
    }

    private boolean issueLandOrder(ABaseLocation baseLocation) {
        APosition rebaseExactLocation = baseLocation.makeLandableFor(unit);
        if (rebaseExactLocation != null) {
            unit.setTooltipAndLog("LandInNewHome");
            unit.land(rebaseExactLocation.toTilePosition());
            return true;
        }
        return false;
    }

    private boolean liftToFlyFirst(ABaseLocation rebaseTo) {
        if (!unit.isLifted() && rebaseTo.distToMoreThan(unit, 5)) {
            unit.setTooltipAndLog("LiftAndRebase");
            unit.lift();
            return true;
        }
        return false;
    }

    protected boolean isBaseMinedOut() {
        return Select.minerals().inRadius(10, unit).isEmpty();
    }
}
