package atlantis.strategy;

import atlantis.combat.missions.MissionChanger;
import atlantis.combat.missions.Missions;
import atlantis.enemy.EnemyInformation;
import atlantis.map.Chokes;
import atlantis.production.orders.CurrentBuildOrder;
import atlantis.production.requests.ARequests;
import atlantis.strategy.decisions.OurStrategicBuildings;
import atlantis.units.AUnit;
import atlantis.units.AUnitType;
import atlantis.units.select.Select;

public class EnemyUnitDiscoveredResponse {

    public static void updateEnemyUnitDiscovered(AUnit enemyUnit) {

        // HIDDEN units
        if (enemyUnit.effCloaked()) {
            updateHiddenUnitDetected(enemyUnit);
        }

        // COMBAT buildings
        if (enemyUnit.type().isCombatBuildingOrCreepColony()) {
            if (GamePhase.isEarlyGame()) {
                EnemyInformation.enemyStartedWithDefensiveBuilding = true;
            }
            if (Missions.isFirstMission()) {
                MissionChanger.forceMissionContain();
//                CurrentBuildOrder.set(TerranStrategies.TERRAN_Mech.buildOrder());
            }
        }
    }

    // =========================================================

    private static void updateHiddenUnitDetected(AUnit enemyUnit) {
        if (enemyUnit.effVisible()) {
            return;
        }

//        if (enemyUnit.isType(AUnitType.Protoss_Dark_Templar, AUnitType.Zerg_Lurker)) {
        if (enemyUnit.isType(AUnitType.Protoss_Dark_Templar)) {
            OurStrategicBuildings.setDetectorsNeeded(1);
//            ARequests.getInstance().requestDetectorQuick(
//                    Chokes.mainChoke().getCenter()
//            );
//            ARequests.getInstance().requestDetectorQuick(
//                    Chokes.natural(Select.mainBase().position()).getCenter()
//            );
        }
    }

}