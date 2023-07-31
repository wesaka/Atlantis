package atlantis.combat.squad.positioning;

import atlantis.architecture.Manager;
import atlantis.combat.micro.terran.tank.TerranTank;
import atlantis.game.A;
import atlantis.map.choke.Chokes;
import atlantis.units.AUnit;
import atlantis.units.actions.Actions;
import atlantis.units.select.Select;

public class MakeSpaceForNearbyWorkers extends Manager {
    public MakeSpaceForNearbyWorkers(AUnit unit) {
        super(unit);
    }

    @Override
    public boolean applies() {
        if (unit.enemiesNearInRadius(12) > 0) {
            return false;
        }

        if (unit.isMissionAttack()) return false;

        if (unit.isTank() && A.seconds() % 4 <= 2) return false;

        if (Chokes.nearestChoke(unit).distTo(unit) >= 7) return false;

        return true;
    }

    public Manager handle() {
        AUnit nearWorker = Select.ourWorkers().inRadius(1.5, unit).first();

        if (nearWorker != null) {
            if (unit.isTankSieged()) {
                if (TerranTank.wantsToUnsiege(unit)) {
                    return usedManager(this);
                }
            }
            else {
                AUnit main = Select.main();
                if (main != null && main.distToMoreThan(unit, 5)) {
                    unit.move(main, Actions.MOVE_SPACE, "Space4Worker");
                    return usedManager(this);
                }
            }
        }

        return null;
    }
}
