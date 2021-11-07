package atlantis.combat.missions;

import atlantis.CameraManager;
import atlantis.enemy.AEnemyUnits;
import atlantis.information.AFoggedUnit;
import atlantis.map.*;
import atlantis.position.APosition;
import atlantis.units.select.Select;
import atlantis.util.Cache;
import atlantis.util.We;

public class MissionContainFocusPoint extends MissionFocusPoint {

    private Cache<APosition> cache = new Cache<>();

    @Override
    public APosition focusPoint() {
        return cache.get(
                "focusPoint",
                100,
                () -> {
                    if (We.terran()) {
                        AFoggedUnit enemyBuilding = AEnemyUnits.nearestEnemyBuilding();
                        if (enemyBuilding != null) {
                            return enemyBuilding.position();
                        }
                    }

                    AChoke naturalChoke = Chokes.enemyNaturalChoke();
                    if (naturalChoke != null && naturalChoke.getWidth() <= 4) {
                        return naturalChoke.position();
                    }

                    AChoke mainChoke = Chokes.enemyMainChoke();
                    if (mainChoke != null && mainChoke.getWidth() <= 4) {
                        return mainChoke.position();
                    }

                    AFoggedUnit enemyBuilding = AEnemyUnits.nearestEnemyBuilding();
                    if (enemyBuilding != null) {
                        return enemyBuilding.position();
                    }
////
//        AUnit nearestEnemy = Select.enemy().nearestTo(Select.our().first());
//        if (nearestEnemy != null) {
//            return nearestEnemy.position();
//        }

                    APosition enemyBase = AEnemyUnits.enemyBase();
                    if (enemyBase != null) {
                        return containPointIfEnemyBaseIsKnown(enemyBase);
                    }

                    // Try to go to some starting location, hoping to find enemy there.
                    return Chokes.nearestChoke(
                            BaseLocations.getNearestUnexploredStartingLocation(Select.mainBase().position())
                    );
                }
        );
    }

    // =========================================================

    private APosition containPointIfEnemyBaseIsKnown(APosition enemyBase) {
        AChoke chokepoint = Chokes.natural(enemyBase);
        if (chokepoint != null) {
            CameraManager.centerCameraOn(chokepoint.getCenter());
            return chokepoint.getCenter();
        }

        ABaseLocation natural = BaseLocations.natural(enemyBase.position());
        if (natural != null) {
            CameraManager.centerCameraOn(natural);
            return natural.position();
        }

        System.err.println("Shouldnt be here mate?");
        return null;
    }

//    private APosition containPointIfEnemyBaseNotKnown() {
//        AUnit nearestEnemy = Select.enemy().nearestTo(Select.our().first());
//        if (nearestEnemy != null) {
//            return nearestEnemy.getPosition();
//        }
//
//        return null;
//
////        AUnit mainBase = Select.mainBase();
////        if (mainBase == null) {
////            return null;
////        }
////
////        AChoke choke = AMap.getChokepointForNatural(mainBase.getPosition());
////        return choke == null ? null : choke.getCenter();
//    }

}
