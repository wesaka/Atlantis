package atlantis.strategy.response;

import atlantis.AGame;
import atlantis.combat.missions.Missions;
import atlantis.production.requests.AAntiAirBuildingRequests;
import atlantis.production.requests.AAntiLandBuildingRequests;
import atlantis.scout.AScoutManager;
import atlantis.strategy.EnemyStrategy;
import atlantis.strategy.AStrategy;
import atlantis.strategy.decisions.OurStrategicBuildings;
import atlantis.util.Enemy;

public abstract class AStrategyResponse {

    public boolean update() {
        if (AGame.notNthGameFrame(10)) {
            return false;
        }

        // Anti-LAND
        if (AAntiLandBuildingRequests.handle()) {
            return true;
        }

        // Anti-AIR
        if (AAntiAirBuildingRequests.handle()) {
            return true;
        }

        return false;
    }

    public static AStrategyResponse get() {
        return AStrategyResponseFactory.forOurRace();
    }
    
    // =========================================================

    public void updateEnemyStrategyChanged() {
        AStrategy enemyStrategy = EnemyStrategy.get();

        // === Rush ========================================

        if (enemyStrategy.isRushOrCheese()) {
            rushDefence(enemyStrategy);
        }

        // === Expansion ===================================

        if (enemyStrategy.isExpansion()) {
            Missions.setGlobalMissionContain();
        }

        // === Tech ========================================

        if (enemyStrategy.isHiddenUnits()) {
            if (!Enemy.terran()) {
                Missions.setGlobalMissionDefend();
            }

            OurStrategicBuildings.setAntiLandBuildingsNeeded(1);
//            ADetectorRequest.requestDetectorImmediately(null);
        }

        if (enemyStrategy.isAirUnits()) {
            if (!Enemy.protoss()) {
                Missions.setGlobalMissionDefend();
            }

            handleAirUnitsDefence();
        }
    }

    // =========================================================

    protected boolean rushDefence(AStrategy enemyStrategy) {
        System.out.println("GENERIC RUSH - shouldn't be called");
        Missions.setGlobalMissionDefend();

        if (shouldSkipAntiRushCombatBuilding(enemyStrategy)) {
            return false;
        }
        
        OurStrategicBuildings.setAntiLandBuildingsNeeded(rushDefenseCombatBuildingsNeeded(enemyStrategy));
        return true;
    }

    protected int rushDefenseCombatBuildingsNeeded(AStrategy enemyStrategy) {
        return enemyStrategy.isGoingCheese() ? 3 : 2;
    }

    protected void handleAirUnitsDefence() {
//        OurStrategicBuildings.setAntiAirBuildingsNeeded(5);
//        AAntiAirBuildingRequests.requestAntiAirQuick(null);
    }

    // =========================================================

    protected boolean shouldSkipAntiRushCombatBuilding(AStrategy enemyStrategy) {
        if (enemyStrategy == null) {
            return false;
        }
        
        if (AScoutManager.hasAnyScoutBeenKilled()) {
            return false;
        }
        
        // =========================================================

        return !enemyStrategy.isRush() && !enemyStrategy.isGoingCheese();
    }

}
