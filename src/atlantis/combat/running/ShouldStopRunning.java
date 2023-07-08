package atlantis.combat.running;

import atlantis.combat.micro.avoid.AvoidEnemies;
import atlantis.units.AUnit;
import atlantis.units.AUnitType;
import atlantis.units.actions.Actions;
import atlantis.units.managers.Manager;
import atlantis.units.select.Select;
import atlantis.util.We;

public class ShouldStopRunning extends Manager {

    public ShouldStopRunning(AUnit unit) {
        super(unit);
    }

    @Override
    public Manager handle() {
        if (check()) {
            return usedManager(this);
        }

        return null;
    }

    public boolean check() {
//        System.out.println(unit.id() + " // " + unit.isRunning()
//                + " // " + AAvoidUnits.shouldNotAvoidAnyUnit());
//        System.out.println(unit.isRunning() + " // " + unit.runningManager().isRunning() + " // " + unit.action().isRunning());
        if (!unit.isRunning()) {
            return decisionStopRunning();
        }

        if (unit.isFlying() && unit.enemiesNearInRadius(8.2) == 0) {
            unit.setTooltipTactical("SafeEnough");
            return decisionStopRunning();
        }

        if (
            unit.isAction(Actions.RUN_IN_ANY_DIRECTION)
                && unit.lastActionLessThanAgo(20)
        ) {
            unit.setTooltipTactical("InAnyDir");
            return false;
        }

        if (
            unit.hp() > 30
                && unit.lastStartedRunningMoreThanAgo(150)
                && unit.nearestEnemyDist() >= 3.5
        ) {
            unit.setTooltipTactical("RanTooLong");
            return decisionStopRunning();
        }

        if (We.terran() && unit.isHealthy() && unit.lastUnderAttackLessThanAgo(30)) {
            unit.setTooltipTactical("HealthyNow");
            return decisionStopRunning();
        }

        if (
            unit.noCooldown()
                && unit.lastStartedRunningMoreThanAgo(15)
                && !AvoidEnemies.shouldNotAvoidAnyUnit()) {
            unit.setTooltip("StopMan", false);
            return decisionStopRunning();
        }

//        if (unit.isWounded() && unit.nearestEnemyDist() >= 3) {
//            return false;
//        }

        if (
            unit.lastStoppedRunningMoreThanAgo(ARunningManager.STOP_RUNNING_IF_STOPPED_MORE_THAN_AGO)
                && unit.lastStartedRunningMoreThanAgo(ARunningManager.STOP_RUNNING_IF_STARTED_RUNNING_MORE_THAN_AGO)
                && !unit.isUnderAttack(unit.isFlying() ? 250 : 5)
                //                && AAvoidUnits.shouldNotAvoidAnyUnit()
                || AvoidEnemies.shouldNotAvoidAnyUnit()
        ) {
            unit.setTooltip("StopRun");
            return decisionStopRunning();
        }

        return false;
    }

    private boolean decisionStopRunning() {
        if (unit.hp() <= 20 && unit.isTerranInfantry()) {
            AUnit nearestMedic = Select.ourOfType(AUnitType.Terran_Medic).havingEnergy(30).nearestTo();
            if (nearestMedic != null) {
                unit.move(nearestMedic, Actions.MOVE_HEAL, "Lazaret");
                return true;
            }
        }

        unit.runningManager().stopRunning();
        return false;
    }
}