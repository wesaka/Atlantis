package atlantis.combat.micro;

import atlantis.units.AUnit;
import atlantis.units.AUnitType;
import atlantis.units.Select;

public class AAvoidDefensiveBuildings {

    public static boolean avoidCloseBuildings(AUnit unit) {
        AUnit enemyBuildingThatCanAttackThisUnit = defineNearestBuilding(unit);
        
        if (enemyBuildingThatCanAttackThisUnit == null) {
            return false;
        }

        int ourUnits = Select.ourCombatUnits().inRadius(10, unit).count();
        if (ourUnits >= 12) {
            System.out.println("Forget caannons, ourUnits = " + ourUnits);
            return false;
        }

        double enemyWeaponRange = enemyBuildingThatCanAttackThisUnit.getWeaponRangeAgainst(unit);
        double enemyDistance = enemyBuildingThatCanAttackThisUnit.distanceTo(unit);
//            System.out.println("weapon " + buildingTooClose.type().getShortName() + " // " + enemyWeaponRange + " // " + enemyDistance);
        double distanceMargin = enemyDistance - enemyWeaponRange;

        if (distanceMargin < 2.8 && (!unit.isMoving() && !unit.isHoldingPosition())) {
            unit.holdPosition();
            unit.setTooltip("AvoidHold (" + String.format("%.1f", distanceMargin) + ")");
            return true;
        }

        if (distanceMargin < 1.5 && !unit.isMoving()) {
            boolean result = unit.moveAwayFrom(enemyBuildingThatCanAttackThisUnit.getPosition(), 1);
            unit.setTooltip("AvoidMove (" + String.format("%.1f", distanceMargin) + ")");
            return result;
        }

        return false;
    }

    // =========================================================

    private static AUnit defineNearestBuilding(AUnit unit) {
        if (unit.isGroundUnit()) {
            return Select.enemyOfType(
                    AUnitType.Terran_Bunker,
                    AUnitType.Protoss_Photon_Cannon,
                    AUnitType.Zerg_Sunken_Colony
            ).canAttack(unit, 1).first();
        }
        else  {
            return Select.enemyOfType(
                    AUnitType.Terran_Bunker, AUnitType.Terran_Missile_Turret,
                    AUnitType.Protoss_Photon_Cannon,
                    AUnitType.Zerg_Sunken_Colony
            ).canAttack(unit, 1).first();
        }
    }

}
