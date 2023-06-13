package atlantis.combat.missions.attack;

import atlantis.combat.missions.focus.AFocusPoint;
import atlantis.combat.missions.focus.MissionFocusPoint;
import atlantis.combat.squad.alpha.Alpha;
import atlantis.game.A;
import atlantis.information.enemy.EnemyUnits;
import atlantis.information.strategy.GamePhase;
import atlantis.map.AChoke;
import atlantis.map.AMap;
import atlantis.map.Bases;
import atlantis.map.Chokes;
import atlantis.map.position.APosition;
import atlantis.map.position.HasPosition;
import atlantis.units.AUnit;
import atlantis.units.fogged.AbstractFoggedUnit;
import atlantis.units.select.Count;
import atlantis.units.select.Select;
import atlantis.util.cache.Cache;

public class MissionAttackFocusPoint extends MissionFocusPoint {

    private Cache<AFocusPoint> cache = new Cache<>();

    private static APosition _temporaryTarget = null;

    public AFocusPoint focusPoint() {
        return cache.getIfValid(
                "focusPoint",
                60,
                () -> defineFocusPoint()
        );
    }

    private AFocusPoint defineFocusPoint() {
        AUnit our = Select.our().first();
        if (A.supplyUsed() <= 1) {
            AUnit enemy = Select.enemy().first();

            if (our == null) {
                return null;
            }

            if (enemy == null) {
                return null;
            }

            return new AFocusPoint(
                    enemy,
                    our,
                "FirstEnemy"
            );
        }

        // Try going near any enemy building
        AbstractFoggedUnit enemyBuilding = EnemyUnits.nearestEnemyBuilding();
        if (
            enemyBuilding != null
                && enemyBuilding.position() != null
                && (enemyBuilding.isAlive() || !enemyBuilding.isVisibleUnitOnMap())
            ) {
            return new AFocusPoint(
                    enemyBuilding,
                    Select.main(),
                "EnemyBuilding"
            );
        }

        // Prevent switching bases across entire map
        if (GamePhase.isEarlyGame() || Select.enemy().buildings().atMost(2)) {

            // Try going near enemy base
            APosition enemyBase = EnemyUnits.enemyBase();
            if (enemyBase != null) {
                return new AFocusPoint(
                        enemyBase,
                        Select.main(),
                    "EnemyBase"
                );
            }
        }

        // Try going near any enemy building
        AUnit visibleEnemyBuilding = EnemyUnits.discovered().buildings().last();
        if (visibleEnemyBuilding != null) {
            return new AFocusPoint(
                    visibleEnemyBuilding,
                    Select.main(),
                "AnyEnemyBuilding"
            );
        }

        // Try going to any known enemy unit
        HasPosition alphaCenter = Alpha.alphaCenter();
        AUnit anyEnemyLandUnit = EnemyUnits.discovered().groundUnits().effVisible().realUnits().nearestTo(
            alphaCenter != null ? alphaCenter : Select.our().first()
        );
//        AUnit anyEnemyLandUnit = EnemyUnits.visibleAndFogged().combatUnits().groundUnits().first();
        if (anyEnemyLandUnit != null) {
            return new AFocusPoint(
                    anyEnemyLandUnit,
                    Select.main(),
                "AnyEnemyLandUnit"
            );
        }

        AUnit anyEnemyAirUnit = EnemyUnits.discovered().air().effVisible().nearestTo(
            alphaCenter != null ? alphaCenter : Select.our().first()
        );
        if (anyEnemyAirUnit != null) {
            return new AFocusPoint(
                    anyEnemyAirUnit,
                    Select.main(),
                "AnyEnemyAirUnit"
            );
        }

        if (Count.ourCombatUnits() <= 40) {
            AChoke mainChoke = Chokes.enemyMainChoke();
            if (mainChoke != null) {
                return new AFocusPoint(
                        mainChoke,
                        Select.main(),
                    "EnemyMainChoke"
                );
            }
        }

        // Try to go to some starting location, hoping to find enemy there.
        if (Select.main() != null) {
            APosition startLocation = Bases.nearestUnexploredStartingLocation(Select.main());

            if (startLocation != null) {
                return new AFocusPoint(
                        startLocation,
                        Select.main(),
                    "NearStartLocation"
                );
            }
        }

        if (isTemporaryTargetStillValid()) {
            return new AFocusPoint(
                _temporaryTarget,
                our,
                "RandPosition"
            );
        }

//        System.out.println("New temp target @ " + A.now());

        if (our == null) {
            return null;
        }

        // Go to random UNEXPLORED
        _temporaryTarget = AMap.randomUnexploredPosition(our);
        if (_temporaryTarget != null && _temporaryTarget.hasPathTo(our.position())) {
            return new AFocusPoint(
                _temporaryTarget,
                our,
                "RandomUnexplored"
            );
        }

        // Go to random INVISIBLE
        _temporaryTarget = AMap.randomInvisiblePosition(our);
        if (_temporaryTarget != null && _temporaryTarget.hasPathTo(our.position())) {
            return new AFocusPoint(
                _temporaryTarget,
                our,
                "RandomInvisible"
            );
        }

//        System.err.println("No MissionAttack FocusPoint");
        return null;
    }

    private boolean isTemporaryTargetStillValid() {
        return _temporaryTarget != null && !_temporaryTarget.isPositionVisible();
    }

}