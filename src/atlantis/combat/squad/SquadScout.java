package atlantis.combat.squad;

import atlantis.combat.micro.AAttackEnemyUnit;
import atlantis.combat.micro.avoid.AAvoidUnits;
import atlantis.combat.missions.MissionChanger;
import atlantis.combat.missions.Missions;
import atlantis.debug.APainter;
import atlantis.enemy.EnemyUnits;
import atlantis.information.AFoggedUnit;
import atlantis.log.ALog;
import atlantis.position.APosition;
import atlantis.units.AUnit;
import atlantis.units.actions.UnitActions;
import atlantis.units.select.Select;
import atlantis.util.A;
import bwapi.Color;

public class SquadScout {

    public static boolean handle(AUnit unit) {
//        if (We.terran()) {
//            return false;
//        }

        if (unit.equals(unit.squad().getSquadScout())) {
            return handleSquadScout(unit);
        }

        return false;
    }

    // =========================================================

    private static boolean handleSquadScout(AUnit squadScout) {
        if (AAvoidUnits.avoidEnemiesIfNeeded(squadScout)) {
            return true;
        }

        APosition positionToEngage = positionToEngageEarlyOn(squadScout);

        if (positionToEngage != null) {
            APainter.paintCircle(positionToEngage, 25, Color.Orange);
            APainter.paintTextCentered(positionToEngage, "Squad Scout", Color.Orange);
            String dist = A.dist(squadScout, positionToEngage);

            if (positionToEngage.distTo(squadScout) > 2.2) {
                return squadScout.move(positionToEngage, UnitActions.MOVE_TO_ENGAGE, "Pioneer" + dist);
            }
            else {
                engageWorkersNow(squadScout);
                return true;
            }
        }

        return false;
    }

    private static void engageWorkersNow(AUnit squadScout) {
        AAttackEnemyUnit.handleAttackNearbyEnemyUnits(squadScout);
        squadScout.setTooltip("MadeContact");

        if (Select.enemyCombatUnits().atMost(2)) {
            ALog.addMessage("Squad scout forced GLOBAL ATTACK");
            if (!Missions.isGlobalMissionAttack()) {
                MissionChanger.forceMissionAttack();
            }
        }
    }

    private static APosition positionToEngageEarlyOn(AUnit squadScout) {
        APosition positionToEngage = null;

        APosition enemyBase = EnemyUnits.enemyBase();
        if (enemyBase != null) {
            positionToEngage = enemyBase;
        }

        if (positionToEngage == null) {
            AFoggedUnit nearestEnemyBuilding = EnemyUnits.nearestEnemyBuilding();
            if (nearestEnemyBuilding != null) {
                positionToEngage = nearestEnemyBuilding.position();
            }
        }

        return positionToEngage;
    }

}
