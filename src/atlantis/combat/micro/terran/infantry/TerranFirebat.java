package atlantis.combat.micro.terran.infantry;

import atlantis.combat.micro.attack.AttackNearbyEnemies;
import atlantis.units.AUnit;
import atlantis.units.AUnitType;
import atlantis.units.actions.Actions;
import atlantis.units.managers.Manager;
import atlantis.units.select.Select;
import atlantis.util.Enemy;

public class TerranFirebat extends Manager {

    public TerranFirebat(AUnit unit) {
        super(unit);
    }

    public  Manager update( ) {
        if (!unit.isFirebat()) {
            return null;
        }

        if (!shouldContinueMeleeFighting()) {
            AUnit enemy = unit.nearestEnemy();
            boolean shouldRun = (enemy != null && unit.distTo(enemy) <= 1.8);
            if (shouldRun) {
                if (unit.runningManager().runFrom(
                    enemy, 1.0, Actions.RUN_ENEMY, false
                )) {
                    return usedManager(this);
                };
            }
        }

        if (
            unit.hp() >= 43
                && unit.cooldown() <= 3
                && unit.enemiesNear().melee().inRadius(1.6, unit).atMost(Enemy.protoss() ? 1 : 3)
                && unit.friendsNear().medics().inRadius(1.4, unit).notEmpty()
        ) {
            if (AttackNearbyEnemies.handleAttackNearEnemyUnits(unit)) {
                unit.setTooltip("Napalm");
                return usedManager(this);
            }
        }

        return null;
    }

    protected  boolean shouldContinueMeleeFighting( ) {
        if (unit.hp() <= 34 || unit.cooldown() >= 4) {
            return false;
        }

        if (unit.hp() >= 40) {
            return true;
        }

        int medics = Select.ourOfType(AUnitType.Terran_Medic)
            .havingEnergy(30)
            .inRadius(1.85, unit)
            .count();

        if (medics >= 1) {
            return true;
        }

        int enemies = Select.enemyCombatUnits().canAttack(unit, 0).count();
        int enemyModifier = Enemy.zerg() ? 25 : 40;

        return unit.hpPercent(Math.min(50, enemies * enemyModifier));
    }
}
