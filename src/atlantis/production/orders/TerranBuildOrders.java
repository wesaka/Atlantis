package atlantis.production.orders;

import atlantis.AtlantisConfig;
import atlantis.production.AtlantisWorkerProductionManager;
import atlantis.units.AUnit;
import atlantis.units.AUnitType;
import atlantis.units.Select;
import atlantis.workers.AtlantisWorkerCommander;
import java.util.ArrayList;

public class TerranBuildOrders extends AtlantisBuildOrdersManager {

    @Override
    public void produceWorker() {
        AtlantisWorkerProductionManager.produceWorker();
    }

    @Override
    public void produceUnit(AUnitType unitType) {
        AUnitType whatBuildsIt = unitType.getWhatBuildsIt();
        AUnit unitThatWillProduce = Select.ourOneIdle(whatBuildsIt);
        if (unitThatWillProduce != null) {
            unitThatWillProduce.train(unitType);
        } else {
//            System.err.println("Can't find " + whatBuildsIt + " to produce " + unitType);
        }
    }

    @Override
    public ArrayList<AUnitType> produceWhenNoProductionOrders() {
        ArrayList<AUnitType> units = new ArrayList<>();
        
        System.out.println("Notice: No production orders, auto-produce.");

        if (AtlantisWorkerCommander.shouldTrainWorkers(true)) {
            units.add(AtlantisConfig.WORKER);
        }
        
        if (Select.ourTanks().count() < 30) {
            units.add(AUnitType.Terran_Siege_Tank_Tank_Mode);
            units.add(AUnitType.Terran_Siege_Tank_Tank_Mode);
        }

//        if (Select.ourBuildings().ofType(AUnitType.Terran_Academy).count() == 0) {
//            units.add(AUnitType.Terran_Marine);
//        } else {
//            int marines = Select.our().countUnitsOfType(AUnitType.Terran_Marine);
//            int medics = Select.our().countUnitsOfType(AUnitType.Terran_Medic);
//
//            if ((double) marines / medics < 3) {
//                units.add(AUnitType.Terran_Marine);
//            } else {
//                units.add(AUnitType.Terran_Medic);
//                units.add(AUnitType.Terran_Marine);
//            }
//        }
        
        if (Select.ourTanks().count() < 30) {
            units.add(AUnitType.Terran_Siege_Tank_Tank_Mode);
            units.add(AUnitType.Terran_Siege_Tank_Tank_Mode);
        }
        
        return units;
    }

}
