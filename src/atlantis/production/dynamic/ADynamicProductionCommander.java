package atlantis.production.dynamic;


import atlantis.AGame;

public class ADynamicProductionCommander {

    public static void update() {
        if (AGame.isUms()) {
            return;
        }

        ADynamicUnitProductionManager.update();
        ADynamicBuildingsManager.update();
    }
    
}