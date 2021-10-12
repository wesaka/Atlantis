package atlantis.combat.missions;

import atlantis.AGame;
import atlantis.units.Count;

public class ProtossMissionChangerWhenContain extends MissionChangerWhenContain {

    public static void changeMissionIfNeeded() {
        if (shouldChangeMissionToDefend()) {
            changeMissionTo(Missions.DEFEND);
        } else if (shouldChangeMissionToAttack()) {
            changeMissionTo(Missions.ATTACK);
        }
    }

    // === ATTACK ==============================================

    private static boolean shouldChangeMissionToAttack() {
        int ourCombatUnits = Count.ourCombatUnits();

        if (ourCombatUnits >= 35) {
            return true;
        }

        if (AGame.getTimeSeconds() <= 400 && AGame.killsLossesResourceBalance() >= 900) {
            return true;
        }

        return false;
    }

    // === DEFEND ==============================================

    private static boolean shouldChangeMissionToDefend() {
        if (isFirstMission()) {
            return false;
        }

        int ourCombatUnits = Count.ourCombatUnits();

        return ourCombatUnits <= 6;
    }

}