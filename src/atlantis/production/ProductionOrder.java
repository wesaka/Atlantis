package atlantis.production;

import atlantis.position.APosition;
import atlantis.units.AUnitType;
import bwapi.TechType;
import bwapi.UpgradeType;

public class ProductionOrder {
    
    public static final String BASE_POSITION_NATURAL = "NATURAL";
    public static final String BASE_POSITION_MAIN = "MAIN";

    private static final int PRIORITY_LOWEST = 1;
    private static final int PRIORITY_NORMAL = 4;
    private static final int PRIORITY_HIGHEST = 8;

    // =========================================================
    private static int firstFreeId = 1;
    private int id = firstFreeId++;

    /**
     * AUnit type to be build. Can be null if this production order is for something else than upgrade.
     */
    private AUnitType unitOrBuilding = null;

    /**
     * Makes sense only for buildings.
     */
    private APosition position = null;

    /**
     * Upgrade type to research. Can be null if this production order is for something else than upgrade.
     */
    private UpgradeType upgrade;

    /**
     * Tech type to research. Can be null if this production order is for something else than upgrade.
     */
    private TechType tech;
    
    /**
     * Special modifier e.g. base position modifier. See ConstructionSpecialBuildPositionFinder constants.
     */
    private String modifier = null;
    
    /**
     * Contains first column 
     */
    private String rawFirstColumnInFile;
    
    /**
     * Number of row columns of line in build orders file.
     */
    private int numberOfColumnsInRow;

    /**
     * Metadata - used in APainter to show if we can afford it or not.
     */
    private boolean hasWhatRequired;

    /**
     *
     */
//    private int priority;

    /**
     * If true, no other order that comes after this order in the ProductionQueue can be started.
     */
//    private boolean blocking = false;

    // =========================================================
    
    public ProductionOrder(AUnitType unitOrBuilding, APosition position) {
        this.unitOrBuilding = unitOrBuilding;
        this.position = position;
    }
    
    public ProductionOrder(AUnitType unitOrBuilding) {
        this.unitOrBuilding = unitOrBuilding;
    }

    public ProductionOrder(UpgradeType upgrade) {
        this.upgrade = upgrade;
    }

    public ProductionOrder(TechType tech) {
        this.tech = tech;
    }

    private ProductionOrder() {
    }

    // =========================================================
    // Override
    
    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (!(object instanceof ProductionOrder)) {
            return false;
        }
        return ((ProductionOrder) object).id == id;
    }

    @Override
    public int hashCode() {
        return hashCode() * 7;
    }

    @Override
    public String toString() {
        if (unitOrBuilding != null) {
            return "Order: " + shortName();
        }
        else if (upgrade != null) {
            return "Order: " + shortName();
        }
        else if (tech != null) {
            return "Order: " + shortName();
        }
        else {
            return "InvalidEmptyOrder";
        }
    }

    public String shortName() {
        if (unitOrBuilding != null) {
            return unitOrBuilding.shortName();
        } else if (upgrade != null) {
            return upgrade.toString().replace("_", " ");
        } else if (tech != null) {
            return tech.toString().replace("_", " ");
        } else {
            return "Unknown - BUG";
        }
    }

    public ProductionOrder copy() {
        ProductionOrder clone = new ProductionOrder();
        
        clone.id = firstFreeId++;
        clone.modifier = this.modifier;
        clone.numberOfColumnsInRow = this.numberOfColumnsInRow;
        clone.rawFirstColumnInFile = this.rawFirstColumnInFile;
        clone.tech = this.tech;
        clone.unitOrBuilding = this.unitOrBuilding;
        clone.upgrade = this.upgrade;
        
        return clone;
    }
    
    // === Getters =============================================
    
    public int getGasRequired() {
        if (unitOrBuilding != null) {
            return unitOrBuilding.getGasPrice();
        }
        else if (upgrade != null) {
            return upgrade.gasPrice();
        }
        else if (tech != null) {
            return tech.gasPrice();
        }
        else {
            return 0;
        }
    }
    
    /**
     * If this production order concerns unit to be build (or building, AUnit class), it will return non-null
     * value being unit type.
     */
    public AUnitType getUnitOrBuilding() {
        return unitOrBuilding;
    }

    /**
     * If this production order concerns upgrade (UpgradeType class) to be researched, it will return non-null
     * value being unit type.
     */
    public UpgradeType getUpgrade() {
        return upgrade;
    }

    /**
     * If this production order concerns technology (TechType class) to be researched, it will return non-null
     * value being unit type.
     */
    public TechType getTech() {
        return tech;
    }

    /**
     * Special modifier e.g. base position modifier. See ConstructionSpecialBuildPositionFinder constants.
     */
    public String getModifier() {
        return modifier;
    }

    /**
     * Special modifier e.g. base position modifier. See ProductionOrder constants.
     */
    public void setModifier(String modifier) {
        if (modifier != null) {
            modifier = modifier.trim();
        }
        this.modifier = modifier;
    }

    public String getRawFirstColumnInFile() {
        return rawFirstColumnInFile;
    }

    public void setRawFirstColumnInFile(String rawFirstColumnInFile) {
        this.rawFirstColumnInFile = rawFirstColumnInFile;
    }

    public void setNumberOfColumnsInRow(int numberOfColumnsInRow) {
        this.numberOfColumnsInRow = numberOfColumnsInRow;
    }

    public void setHasWhatRequired(boolean hasWhatRequired) {
        this.hasWhatRequired = hasWhatRequired;
    }

    public boolean canHasWhatRequired() {
        return hasWhatRequired;
    }

    public APosition getPosition() {
        return position;
    }
}
