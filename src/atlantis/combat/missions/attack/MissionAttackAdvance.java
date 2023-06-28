package atlantis.combat.missions.attack;

import atlantis.combat.micro.AAttackEnemyUnit;
import atlantis.combat.micro.managers.AdvanceUnitsManager;
import atlantis.combat.missions.WeDontKnowEnemyEnemyUnit;
import atlantis.combat.missions.focus.AFocusPoint;
import atlantis.combat.squad.ASquadCohesionManager;
import atlantis.units.AUnit;
import atlantis.units.actions.Actions;

public class MissionAttackAdvance {

    public static boolean advance(AUnit unit, MissionAttack mission) {
        AFocusPoint focusPoint = mission.focusPoint();

        // Invalid focus point, no enemy can be found, roam around map
//        if (focusPoint == null && (!unit.isAttackingOrMovingToAttack() || unit.isIdle())) {
//            return handleWeDontKnowWhereTheEnemyBaseIs(unit);
//        }

        if (ASquadCohesionManager.handle(unit)) {
            return true;
        }

        // Focus point is well known
//        if (focusPoint != null && unit.lastPositioningActionMoreThanAgo(40)) {
        if (focusPoint != null) {
            boolean looksIdle = !unit.isHoldingPosition() && (
                unit.looksIdle()
                || unit.lastActionMoreThanAgo(40)
                || (unit.isAttacking() && unit.target() == null)
            );

            if (
                unit.lastPositioningActionMoreThanAgo(40) || looksIdle
            ) {
                if (AdvanceUnitsManager.moveToFocusPoint(unit, focusPoint)) {
                    unit.setTooltipTactical("#MA:Advance" + AAttackEnemyUnit.canAttackEnemiesNowString(unit));
                    return true;
                }
            }

            unit.setTooltip("Advancing...");
//            if (!unit.isMoving() && !unit.isAccelerating() && unit.distTo(focusPoint) >= 7) {
            if (unit.looksIdle() && unit.distTo(focusPoint) >= 7) {
                if (unit.move(focusPoint, Actions.MOVE_FOCUS, "Hoorray", true)) {
                    return true;
                }
            }

            return false;
        }
        else {
            unit.setTooltipTactical("#MA-NoFocus");
            return WeDontKnowEnemyEnemyUnit.handleWeDontKnowWhereToFindEnemy(mission, unit);
        }
    }
}
