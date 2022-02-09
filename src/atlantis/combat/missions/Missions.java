package atlantis.combat.missions;

import atlantis.combat.missions.attack.MissionAttack;
import atlantis.combat.missions.contain.MissionContain;
import atlantis.combat.missions.defend.MissionDefend;
import atlantis.game.A;
import atlantis.game.AGame;
import atlantis.units.select.Select;

/**
 * Handles the global mission that is mission that affects the battle squad Alpha.
 */
public class Missions {

    /**
     * This is the mission for main battle squad forces. E.g. initially it will be DEFEND, then it should be
     * PREPARE (go near enemy) and then ATTACK.
     */
    private static Mission currentGlobalMission = null;
    private static int lastMissionChanged = 0;

    public static final Mission ATTACK = new MissionAttack();
    public static final Mission CONTAIN = new MissionContain();
    public static final Mission DEFEND = new MissionDefend();
//    public static final Mission UMS = new MissionUms();

    // =========================================================

    /**
     * Global mission is the military stance that all non-special battle squads should follow and it should
     * always correspond to the mission of our main Alpha battle squad.
     */
    public static Mission globalMission() {
        if (A.isUms()) {
            return ATTACK;
        }

        if (currentGlobalMission == null) {
            setGlobalMissionTo(initialMission());
        }

        return currentGlobalMission;
    }

    public static boolean isGlobalMissionDefend() {
        return globalMission().isMissionDefend();
    }

    public static boolean isGlobalMissionContain() {
        return globalMission().isMissionContain();
    }

    public static boolean isGlobalMissionAttack() {
        if (AGame.isUms()) {
            return true;
        }

        return globalMission().isMissionAttack();
    }

    public static void setGlobalMissionAttack() {
        setGlobalMissionTo(ATTACK);
    }

    public static void setGlobalMissionDefend() {
        setGlobalMissionTo(DEFEND);
    }

    public static void setGlobalMissionContain() {
        setGlobalMissionTo(CONTAIN);
    }

    public static Mission initialMission() {

        // === Handle UMS ==========================================

        if (AGame.isUms() || Select.main() == null) {
//            return Missions.UMS;
            return Missions.ATTACK;
        }

        // =========================================================

//        if (Enemy.zerg()) {
//            return Missions.DEFEND;
//        }

        return Missions.DEFEND;
//        return Missions.ATTACK;
//        return Missions.CONTAIN;
    }

    public static Mission fromString(String mission) {
        mission = mission.toUpperCase().replace("MISSION=", "");
        switch (mission) {
            case "ATTACK" : return ATTACK;
            case "CONTAIN" : return CONTAIN;
            case "DEFEND" : return DEFEND;
//            default : AGame.exit("Invalid mission: " + mission); return null;
            default : return null;
        }
    }

    public static void setGlobalMissionTo(Mission mission) {
        if (mission.equals(currentGlobalMission)) {
            return;
        }

        currentGlobalMission = mission;
        lastMissionChanged = A.now();

        if (A.now() > 50) {
//            if (mission.isMissionDefend()) {
//                throw new RuntimeException("DEF?!?");
//            }
//            if (mission.isMissionContain()) {
//                throw new RuntimeException("CHange to contain?!?");
//            }

            if (MissionChanger.DEBUG) {
                System.err.println("CHANGED MISSION TO: " + mission.name() + ", reason: " + MissionChanger.debugReason);
            }
            MissionChanger.missionHistory.add(currentGlobalMission != null ? currentGlobalMission : mission);
        }
    }

    public static int lastMissionChangedAgo() {
        return A.ago(lastMissionChanged);
    }

    public static boolean recentlyChangedMission() {
        return lastMissionChangedAgo() <= 30 * 5;
    }

    public static int counter() {
        return MissionChanger.missionHistory.size();
    }

    public static Mission prevMission() {
        if (MissionChanger.missionHistory.size() >= 2) {
            return MissionChanger.missionHistory.get(MissionChanger.missionHistory.size() - 2);
        } else {
            return null;
        }
    }

    public static boolean isFirstMission() {
        return MissionChanger.missionHistory.size() == 1;
    }
}
