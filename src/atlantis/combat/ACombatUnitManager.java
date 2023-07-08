package atlantis.combat;

import atlantis.combat.managers.CombatManagerTopPriority;
import atlantis.combat.managers.ImproveCombatManagerPerformance;
import atlantis.combat.managers.SkipCombatManager;
import atlantis.combat.micro.attack.AttackNearbyEnemies;
import atlantis.combat.micro.avoid.special.AvoidSpellsAndMines;
import atlantis.combat.micro.avoid.special.AvoidCriticalUnits;
import atlantis.combat.micro.avoid.AvoidEnemies;
import atlantis.combat.micro.managers.DanceAfterShoot;
import atlantis.combat.micro.managers.StopAndShoot;
import atlantis.combat.micro.transport.TransportUnits;
import atlantis.combat.missions.Mission;
import atlantis.combat.retreating.RetreatManager;
import atlantis.combat.running.ShouldStopRunning;
import atlantis.game.A;
import atlantis.terran.repair.UnitBeingReparedManager;
import atlantis.units.SpecialUnitsManager;
import atlantis.units.AUnit;
import atlantis.units.interrupt.DontDisturbInterrupt;
import atlantis.units.managers.Manager;

public class ACombatUnitManager extends Manager {

    private static RetreatManager retreatManager;
    private static AvoidEnemies avoidEnemies;
    private static UnitBeingReparedManager unitBeingReparedManager;

    public ACombatUnitManager(AUnit unit) {
        super(unit);
        retreatManager = new RetreatManager(unit);
        unitBeingReparedManager = new UnitBeingReparedManager(unit);
    }

    @Override
    protected Class<? extends Manager>[] managers() {
        return new Class[]{
            SkipCombatManager.class,
            SpecialUnitsManager.class,
            CombatManagerTopPriority.class,
            ImproveCombatManagerPerformance.class,
        };
    }

    @Override
    public Manager handle() {
        //if (true)System.out.println("A0 " + unit + " at " + A.now());

//        if (preActions()) {
//            return true;
//        }

        //if (unit.debug())System.out.println("A " + unit + " at " + A.now());

        // =========================================================
        // === SPECIAL units =======================================
        // =========================================================

        // Medics are using dedicated managers.
        // These are stopping standard top priority managers
//        if (SpecialUnitsManager.handle()) {
//            return true;
//        }

//        if (unit.enemiesNear().empty() && A.notNthGameFrame(7)) {
//            return false;
//        }

        //if (unit.debug())System.out.println("B " + unit);

        // =========================================================
        // === TOP priority ========================================
        // =========================================================

//        if (handledTopPriority()) {
//            return lastManager();
//        }

        //if (unit.debug())System.out.println("C " + unit);

        // =========================================================
        // === SPECIAL units =======================================
        // =========================================================

        // Terran infantry has own managers, but these allow higher
        // level managers to take control.
//        if (SpecialUnitsManager.updateAndAllowTopManagers()) {
////            System.out.println("@@ Special - " + unit);
////            unit.addLog("SpecialManager");
//            return true;
//        }

//        if (unit.debug())System.out.println("D " + unit);

        // =========================================================
        // === MEDIUM priority - TACTICAL level ====================
        // =========================================================

        if (handledMediumPriority()) {
            return true;
        }

//        if (unit.debug())System.out.println("E " + unit);

        // =========================================================
        // === LOW priority - MISSION level =======================
        // =========================================================

        return handleLowPriority();
    }

    // =========================================================

//    private boolean handledTopPriority() {
//        if (AvoidSpellsAndMines.avoidSpellsAndMines()) {
//            return true;
//        }
//
//        if (AvoidCriticalUnits.update()) {
//            return true;
//        }
//
//        if (DanceAfterShoot.update()) {
//            return true;
//        }
//
//        if (StopAndShoot.update()) {
//            return true;
//        }
//
//        if (DontDisturbInterrupt.dontInterruptImportantActions()) {
//            return true;
//        }
//
//        if ((unit.isMoving() && !unit.isAttackingOrMovingToAttack()) && TransportUnits.handleLoad()) {
//            return true;
//        }
//
//        if (unit.isLoaded() && TransportUnits.unloadFromTransport()) {
//            return true;
//        }
//
//        // Handle units getting bugged by Starcraft
////        if (Unfreezer.handleUnfreeze()) {
////            return true;
////        }
//
//        if (unit.isRunning()) {
//            if (ShouldStopRunning.shouldStopRunning()) {
//                unit.runningManager().stopRunning();
//            }
//    //        if (unit.isRunning() && unit.lastStartedRunningLessThanAgo(2)) {
//            else if (A.everyNthGameFrame(3)) {
//    //            unit.setTooltip("Running(" + A.digit(unit.distTo(unit.getTargetPosition())) + ")");
//    //            return A.everyNthGameFrame(2) ? AAvoidUnits.avoidEnemiesIfNeeded() : true;
//                return AvoidEnemies.avoidEnemiesIfNeeded();
//            }
//        }
//
//        // Useful for testing and debugging of shooting/running
////        if (testUnitBehaviorShootAtOwnUnit()) { return true; };
//
//        return false;
//    }

    private boolean handledMediumPriority() {

        // Avoid:
        // - invisible units (Dark Templars)
        // - close melee units (Zealots)
        // - ranged units that can shoot at us (Dragoons)
        // - defensive buildings (Cannons)
        if (avoidEnemies.avoidEnemiesIfNeeded()) {
            return true;
        }

        // If Near enemies would likely defeat us, retreat
//        if (RetreatManager.shouldRetreat(unit)) {
//            return true;
//        }

        // Handle repair of mechanical units
        if (unitBeingReparedManager.handleUnitShouldBeRepaired(unit)) {
            return true;
        }

        if (retreatManager.handleRetreat()) {
            return true;
        }

        if (AttackNearbyEnemies.handleAttackNearEnemyUnits(unit)) {
            return true;
        }
    }

    /**
     * If we're here, mission manager is allowed to take control over this unit.
     * Meaning no action was needed on *tactical* level - stick to *strategic* level.
     */
    private boolean handleLowPriority() {
//        if (AvoidEdgesWhenMoving.handle()) {
//            return true;
//        }

        Mission mission = unit.mission();
        if (mission != null) {
//            if (unit.debug())System.out.println("F " + unit);

            unit.setTooltipTactical(mission.name());
            return mission.update();
        }

//        if (unit.debug())System.out.println("G " + unit);
        return false;
    }

    // =========================================================

    /**
     * Can be used for testing.
     */
//    private static boolean testUnitBehaviorShootAtOwnUnit(AUnit unit) {
//        if (Select.our().first().id() != unit.id()) {
//            unit.attackUnit(Select.our().first());
//        }
//        return true;
//    }

}
