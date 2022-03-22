package atlantis.production.dynamic.terran;

import atlantis.game.A;
import atlantis.game.AGame;
import atlantis.information.decisions.terran.ShouldMakeTerranBio;
import atlantis.information.generic.TerranArmyComposition;
import atlantis.information.decisions.Decisions;
import atlantis.units.AUnit;
import atlantis.units.AUnitType;
import atlantis.units.select.Count;
import atlantis.units.select.Select;
import atlantis.units.select.Selection;
import atlantis.util.Enemy;

public class TerranDynamicInfantry extends TerranDynamicUnitsManager {

    private static boolean DEBUG = false;
//    private static boolean DEBUG = true;

    protected static boolean saveForFactory() {
        if (!A.hasMinerals(200)) {
            if (
                    Count.existingOrInProduction(AUnitType.Terran_Factory) == 0
                    && Count.inQueue(AUnitType.Terran_Factory, 2) > 0
            ) {
                return true;
            }
        }

        return false;
    }

    protected static boolean ghosts() {
        if (Enemy.zerg()) {
            return false;
        }

        int ghosts = Count.ofType(AUnitType.Terran_Ghost);
        if (Count.ofType(AUnitType.Terran_Covert_Ops) == 0 || ghosts >= 14) {
            return false;
        }

        if (ghosts >= 4 && !AGame.canAffordWithReserved(60, 200)) {
            return false;
        }

        return addToQueueToMaxAtATime(AUnitType.Terran_Ghost, 4);
    }

    protected static boolean medics() {
        if (!Decisions.shouldMakeTerranBio() || Count.ofType(AUnitType.Terran_Academy) == 0) {
            return false;
        }

        if (A.hasGas(25) && A.hasMinerals(50) && Count.medics() == 0 && Count.marines() > 0) {
            return true;
        }

        if (saveForFactory()) {
            return false;
        }

        if (!AGame.canAffordWithReserved(60, 30)) {
            return false;
        }

        Selection barracks = Select.ourOfType(AUnitType.Terran_Barracks).free();
        if (barracks.isNotEmpty()) {

            // Firebats - disabled, too problematic
//            if (Count.medics() >= 4 && Count.ourOfTypeWithUnfinished(AUnitType.Terran_Firebat) < minFirebats()) {
//                produceUnit(barracks.first(), AUnitType.Terran_Firebat);
//                return;
//            }

            // Medics
            if (TerranArmyComposition.medicsToInfantryRatioTooLow()) {
//                produceUnit(barracks.first(), AUnitType.Terran_Medic);
                return addToQueueToMaxAtATime(AUnitType.Terran_Medic, 4);
            }
        }

        return false;
    }

    private static int minFirebats() {
        if (Enemy.terran()) {
            return 0;
        }
        if (Enemy.protoss()) {
            return Math.max(1, Count.medics() / 5);
        }

        // Zerg
        return (int) Math.max(2, Count.medics() / 4);
    }

    protected static boolean marines() {
        if (Count.ofType(AUnitType.Terran_Barracks) == 0) {
            return false;
        }

        if (!Decisions.shouldMakeTerranBio()) {
            if (DEBUG) System.out.println("Marines - Dont - shouldNOTMakeTerranBio - " + ShouldMakeTerranBio.reason);
            return false;
        }

        if (saveForFactory()) {
            if (DEBUG) System.out.println("Marines - Dont - saveForFactory");
            return false;
        }

        if (A.supplyUsed() >= 32 && !AGame.canAffordWithReserved(80, 0)) {
            if (DEBUG) System.out.println("Marines - Dont - cantNOTAffordWithReserved");
            return false;
        }

        Selection barracks = Select.ourOfType(AUnitType.Terran_Barracks).free();
        if (barracks.isNotEmpty()) {
            return addToQueueToMaxAtATime(AUnitType.Terran_Marine, 1);
//            AbstractDynamicUnits.addToQueue(AUnitType.Terran_Marine);
//            return;
        }

        return trainMarinesForBunkersIfNeeded();
    }

    protected static boolean trainMarinesForBunkersIfNeeded() {
        int bunkers = Select.countOurOfTypeWithUnfinished(AUnitType.Terran_Bunker);
        if (bunkers > 0) {
            int marines = Select.countOurOfType(AUnitType.Terran_Marine);
            int shouldHaveMarines = defineOptimalNumberOfMarines(bunkers);

            // Force at least one marine per bunker
            if (marines < shouldHaveMarines) {
                for (int i = 0; i < shouldHaveMarines - marines; i++) {
                    AUnit idleBarrack = Select.ourOneNotTrainingUnits(AUnitType.Terran_Barracks);
                    if (idleBarrack != null) {
//                        return AbstractDynamicUnits.addToQueue(AUnitType.Terran_Marine);
                        return addToQueueToMaxAtATime(AUnitType.Terran_Marine, 4);
                    }
                    else {
                        break;
                    }
                }
            }
        }
        return false;
    }

    protected static int defineOptimalNumberOfMarines(int bunkers) {
        if (bunkers <= 0) {
            return 0;
        }
        else if (bunkers <= 1) {
            return 1;
        }
        else {
            return 4 * bunkers;
        }
    }

    // =========================================================

    private static void produceUnit(AUnit building, AUnitType type) {
        building.train(type);
    }
}
