package atlantis.protoss;

import atlantis.units.AUnit;
import atlantis.units.AUnitType;
import atlantis.units.Select;

public class ProtossSpecificBuildingsCommander {

    public static void update() {
        for (AUnit building : Select.ourBuildings().listUnits()) {

            if (building.isType(AUnitType.Protoss_Shield_Battery)) {
                ProtossShieldBattery.handle(building);
            }

        }
    }

}