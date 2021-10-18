package atlantis.combat.missions;

import atlantis.units.Select;

public class ProtossMissionChangerWhenDefend extends MissionChangerWhenContain {

    public static void changeMissionIfNeeded() {
        if (shouldChangeMissionToContain()) {
            changeMissionTo(Missions.CONTAIN);
        }
    }

    // === CONTAIN =============================================

    private static boolean shouldChangeMissionToContain() {
        if (Select.ourCombatUnits().count() >= 10) {
            return true;
        }

        return false;
    }

}
