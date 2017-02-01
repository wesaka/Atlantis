package atlantis.buildings.managers;

import atlantis.AtlantisConfig;
import atlantis.AtlantisGame;
import atlantis.constructing.AtlantisConstructionManager;
import static atlantis.constructing.AtlantisConstructionManager.requestConstructionOf;
import atlantis.constructing.ConstructionOrderStatus;
import atlantis.information.AtlantisMap;
import atlantis.production.ProductionOrder;
import atlantis.production.orders.AtlantisBuildOrdersManager;
import atlantis.units.Select;
import java.util.ArrayList;

/**
 *
 * @author Rafal Poniatowski <ravaelles@gmail.com>
 */
public class AtlantisExpansionManager {

    public static void requestNewBaseIfNeeded() {
//        if (1 < 2) {
//            return;
//        }
        
        int minMinerals = 100 + (AtlantisGame.playsAsZerg() ? 268 : 356);

        // It makes sense to think about expansion only if we have a lot of minerals.
        if (!AtlantisGame.hasMinerals(minMinerals)) {
            return;
        }

        // If there're still things to produce, don't auto-expand.
        ArrayList<ProductionOrder> nextOrders
                = AtlantisBuildOrdersManager.getBuildOrders().getProductionQueueNext(5);
        if (nextOrders.size() >= 3 && !AtlantisGame.hasMinerals(minMinerals + 50)) {
            return;
        }
        
        // === Force decent army before 3rd base =========================================
        
        int numberOfBases = Select.ourBases().count();
        
        // Enforce too have a lot of tanks before expansion
        if (AtlantisGame.playsAsTerran() && numberOfBases >= 2) {
            if (Select.ourTanks().count() <= 8) {
                return;
            }
        }
        
        // === Check if we have almost as many bases as base locations; if so, exit ======
        
        
        if (numberOfBases >= AtlantisMap.getBaseLocations().size() - 2) {
            return;
        }

        // ===============================================================================
        int numberOfUnfinishedBases
                = AtlantisConstructionManager.countNotFinishedConstructionsOfType(AtlantisConfig.BASE);

        boolean haveEnoughMinerals = AtlantisGame.hasMinerals(minMinerals);
        boolean haveEnoughBases = numberOfBases >= 7
                && AtlantisGame.playsAsZerg() && Select.ourLarva().count() >= 2;
        boolean noBaseToConstruct = numberOfUnfinishedBases == 0;
        boolean allowExtraExpansion = AtlantisGame.hasMinerals(minMinerals + 200)
                && numberOfUnfinishedBases <= 1;

        // Check if it makes sense to request new base
        if (haveEnoughMinerals && !haveEnoughBases && (noBaseToConstruct || allowExtraExpansion)) {
            
            // ZERG case
            if (AtlantisGame.playsAsZerg() && AtlantisGame.hasMinerals(minMinerals)) {
                ProductionOrder fakeProductionOrder = new ProductionOrder(AtlantisConfig.BASE);
                fakeProductionOrder.setModifier(ProductionOrder.BASE_POSITION_MAIN);
                
                requestConstructionOf(
                        AtlantisConfig.BASE, 
                        fakeProductionOrder,
                        Select.secondBaseOrMainIfNoSecond().getPosition()
                );
            } 
            
            // TERRAN + PROTOSS
            else {
                requestConstructionOf(AtlantisConfig.BASE);
            }
        }
    }

}
