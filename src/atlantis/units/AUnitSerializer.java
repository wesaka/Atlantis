package atlantis.units;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;

// TODO fix everything that is commented out :P
public class AUnitSerializer extends JsonSerializer<AUnit> {
    @Override
    public void serialize(AUnit aUnit, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // Custom serialization logic here
        //gen.writeObject(unit.game);
        // Getting game information from the unit is going to be kind of tricky

        gen.writeStartObject();

        // Let's get the values from the properties
//        gen.writeNumberField("_lastAttackOrder", aUnit._lastAttackOrder);
//        gen.writeNumberField("_lastAttackFrame", aUnit._lastAttackFrame);
//        gen.writeNumberField("_lastCooldown", aUnit._lastCooldown);
//        gen.writeNumberField("_lastFrameOfStartingAttack", aUnit._lastFrameOfStartingAttack);
//        gen.writeNumberField("_lastStartedRunning", aUnit._lastStartedRunning);
//        gen.writeNumberField("_lastStoppedRunning", aUnit._lastStoppedRunning);
//        gen.writeNumberField("_lastStartedAttack", aUnit._lastStartedAttack);
////        public AUnit _lastTargetToAttack;
//        gen.writeNumberField("_lastTargetToAttackAcquired", aUnit._lastTargetToAttackAcquired);
//        gen.writeStringField("_lastTech", aUnit._lastTech.toString());
//        gen.writeNumberField("_lastTechPosition_x", aUnit._lastTechPosition.x());
//        gen.writeNumberField("_lastTechPosition_y", aUnit._lastTechPosition.y());
////        public APosition _lastTechPosition;
//        gen.writeNumberField("_lastTechUnit_id", aUnit._lastTechUnit.id());
//        gen.writeNumberField("_lastUnderAttack", aUnit._lastUnderAttack);
//        gen.writeNumberField("_lastX", aUnit._lastX);
//        gen.writeNumberField("_lastY", aUnit._lastY);

        // Then the values from the getters
//        public Manager manager()
//        public AUnitType type()
//        public UnitType bwapiType()
        gen.writeNumberField("position_x", aUnit.position().x());
        gen.writeNumberField("position_y", aUnit.position().y());
        gen.writeNumberField("unit_id", aUnit.unit() == null ? -1 : aUnit.unit().id());
        gen.writeNumberField("int_hashCode", aUnit.hashCode());
        gen.writeStringField("toString", aUnit.toString());
        gen.writeBooleanField("isAlive", aUnit.isAlive());
        gen.writeBooleanField("isDead", aUnit.isDead());
        gen.writeBooleanField("canBeHealed", aUnit.canBeHealed());
        gen.writeBooleanField("isRepairableMechanically", aUnit.isRepairableMechanically());
        gen.writeBooleanField("isHealable", aUnit.isHealable());
        gen.writeBooleanField("isABuilding", aUnit.isABuilding());
        gen.writeBooleanField("isWorker", aUnit.isWorker());
        gen.writeBooleanField("isWraith", aUnit.isWraith());
        gen.writeBooleanField("isBunker", aUnit.isBunker());
        gen.writeBooleanField("isBase", aUnit.isBase());
        gen.writeBooleanField("isInfantry", aUnit.isInfantry());
        gen.writeBooleanField("isVehicle", aUnit.isVehicle());
        gen.writeBooleanField("isRanged", aUnit.isRanged());
        gen.writeBooleanField("isMelee", aUnit.isMelee());
        gen.writeBooleanField("isFullyHealthy", aUnit.isFullyHealthy());
        gen.writeNumberField("int_hpPercent", aUnit.hpPercent());
        gen.writeNumberField("double_woundPercent", aUnit.woundPercent());
        gen.writeBooleanField("isWounded", aUnit.isWounded());
        gen.writeBooleanField("isExists", aUnit.isExists());
        gen.writeNumberField("int_shields", aUnit.shields());
        gen.writeNumberField("int_maxShields", aUnit.maxShields());
        gen.writeNumberField("int_maxHP", aUnit.maxHP());
        gen.writeNumberField("int_minesCount", aUnit.minesCount());
        gen.writeStringField("name", aUnit.name());
        gen.writeStringField("nameWithId", aUnit.nameWithId());
        gen.writeNumberField("int_groundWeaponRange", aUnit.groundWeaponRange());
        gen.writeNumberField("double_groundWeaponMinRange", aUnit.groundWeaponMinRange());
        gen.writeNumberField("double_airWeaponRange", aUnit.airWeaponRange());
        gen.writeNumberField("int_getUnitIndexInBwapi", aUnit.getUnitIndexInBwapi());
        gen.writeStringField("tooltip", aUnit.tooltip());
        gen.writeBooleanField("hasTooltip", aUnit.hasTooltip());
        gen.writeBooleanField("isGroundUnit", aUnit.isGroundUnit());
        gen.writeBooleanField("isAir", aUnit.isAir());
        gen.writeBooleanField("isMine", aUnit.isMine());
        gen.writeBooleanField("isLarvaOrEgg", aUnit.isLarvaOrEgg());
        gen.writeBooleanField("isLarva", aUnit.isLarva());
        gen.writeBooleanField("isEgg", aUnit.isEgg());
        gen.writeBooleanField("isRealUnit", aUnit.isRealUnit());
        gen.writeBooleanField("isRealUnitOrBuilding", aUnit.isRealUnitOrBuilding());
        gen.writeBooleanField("isRunning", aUnit.isRunning());
        gen.writeBooleanField("isRetreating", aUnit.isRetreating());
//        public Squad squad()
//        public ARunningManager runningManager()
        gen.writeNumberField("int_lastUnitOrderTime", aUnit.lastUnitOrderTime());
        gen.writeNumberField("int_lastActionFramesAgo", aUnit.lastActionFramesAgo());
        gen.writeBooleanField("canAttackGroundUnits", aUnit.canAttackGroundUnits());
        gen.writeBooleanField("canAttackAirUnits", aUnit.canAttackAirUnits());
//        public WeaponType airWeapon()
//        public WeaponType groundWeapon()
        gen.writeNumberField("int_cooldownAbsolute", aUnit.cooldownAbsolute());
        gen.writeNumberField("int_cooldownRemaining", aUnit.cooldownRemaining());
        gen.writeBooleanField("isBuilder", aUnit.isBuilder());
//        public Construction construction()
        gen.writeBooleanField("isEnemy", aUnit.isEnemy());
        gen.writeBooleanField("isOur", aUnit.isOur());
        gen.writeBooleanField("isNeutral", aUnit.isNeutral());
        gen.writeBooleanField("canHaveAddon", aUnit.canHaveAddon());
        gen.writeNumberField("int_id", aUnit.id());
        gen.writeStringField("idWithHash", aUnit.idWithHash());
        gen.writeStringField("typeWithHash", aUnit.typeWithHash());
        gen.writeStringField("typeWithId", aUnit.typeWithId());
//        public APlayer player()
        gen.writeNumberField("int_x", aUnit.x());
        gen.writeNumberField("int_y", aUnit.y());
        gen.writeBooleanField("isCompleted", aUnit.isCompleted());
        gen.writeBooleanField("exists", aUnit.exists());
        gen.writeBooleanField("isConstructing", aUnit.isConstructing());
        gen.writeBooleanField("hasAddon", aUnit.hasAddon());
        gen.writeNumberField("int_hp", aUnit.hp());
        gen.writeNumberField("int_maxHp", aUnit.maxHp());
        gen.writeBooleanField("isResearching", aUnit.isResearching());
        gen.writeBooleanField("isUpgradingSomething", aUnit.isUpgradingSomething());
//        public TechType whatIsResearching()
//        public UpgradeType whatIsUpgrading()
        gen.writeBooleanField("isIdle", aUnit.isIdle());
        gen.writeBooleanField("isBusy", aUnit.isBusy());
        gen.writeBooleanField("isVisibleUnitOnMap", aUnit.isVisibleUnitOnMap());
        gen.writeBooleanField("effVisible", aUnit.effVisible());
        gen.writeBooleanField("effUndetected", aUnit.effUndetected());
        gen.writeBooleanField("isDetected", aUnit.isDetected());
        gen.writeBooleanField("notVisible", aUnit.notVisible());
        gen.writeBooleanField("isMiningOrExtractingGas", aUnit.isMiningOrExtractingGas());
        gen.writeBooleanField("isGatheringMinerals", aUnit.isGatheringMinerals());
        gen.writeBooleanField("isGatheringGas", aUnit.isGatheringGas());
        gen.writeBooleanField("isCarryingMinerals", aUnit.isCarryingMinerals());
        gen.writeBooleanField("isCarryingGas", aUnit.isCarryingGas());
        gen.writeBooleanField("isCloaked", aUnit.isCloaked());
        gen.writeBooleanField("isBurrowed", aUnit.isBurrowed());
        gen.writeBooleanField("isRepairing", aUnit.isRepairing());
        gen.writeNumberField("int_groundWeaponCooldown", aUnit.groundWeaponCooldown());
        gen.writeNumberField("int_cooldown", aUnit.cooldown());
        gen.writeNumberField("int_airWeaponCooldown", aUnit.airWeaponCooldown());
        gen.writeBooleanField("isAttackFrame", aUnit.isAttackFrame());
        gen.writeBooleanField("isStartingAttack", aUnit.isStartingAttack());
        gen.writeBooleanField("isStopped", aUnit.isStopped());
        gen.writeBooleanField("isStuck", aUnit.isStuck());
        gen.writeBooleanField("isHoldingPosition", aUnit.isHoldingPosition());
        gen.writeBooleanField("isPatrolling", aUnit.isPatrolling());
        gen.writeBooleanField("isSieged", aUnit.isSieged());
        gen.writeBooleanField("isUnsieged", aUnit.isUnsieged());
        gen.writeBooleanField("isUnderAttack", aUnit.isUnderAttack());

        // Write the training queue array
//        gen.writeStartArray();
//        aUnit.trainingQueue().forEach(listUnit -> {
//            try {
//                gen.writeNumber(listUnit.id());
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
//        gen.writeEndArray();

        gen.writeBooleanField("isUpgrading", aUnit.isUpgrading());

        // Write the larva array
//        gen.writeStartArray();
//        aUnit.getLarva().forEach(listUnit -> {
//            try {
//                gen.writeNumber(listUnit.id());
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
//        gen.writeEndArray();

        gen.writeNumberField("target_id", aUnit.target() == null ? -1 : aUnit.target().id());
        gen.writeBooleanField("hasTarget", aUnit.hasTarget());
        gen.writeBooleanField("hasTargetPosition", aUnit.hasTargetPosition());
        gen.writeNumberField("targetPosition_x", aUnit.targetPosition().x());
        gen.writeNumberField("targetPosition_y", aUnit.targetPosition().y());
        gen.writeNumberField("orderTarget_id", aUnit.orderTarget() == null ? -1 : aUnit.orderTarget().id());
        gen.writeNumberField("buildUnit_id",aUnit.buildUnit() == null ? -1 : aUnit.buildUnit().id());
        gen.writeStringField("buildTypeFullName", aUnit.buildType() == null ? "" : aUnit.buildType().fullName());
        gen.writeBooleanField("isVulture", aUnit.isVulture());
        gen.writeNumberField("double_maxSpeed", aUnit.maxSpeed());
        gen.writeBooleanField("isTank", aUnit.isTank());
        gen.writeBooleanField("isTankSieged", aUnit.isTankSieged());
        gen.writeBooleanField("isTankUnsieged", aUnit.isTankUnsieged());
        gen.writeBooleanField("isMorphing", aUnit.isMorphing());
        gen.writeBooleanField("isMoving", aUnit.isMoving());
        gen.writeBooleanField("isAttacking", aUnit.isAttacking());
        gen.writeBooleanField("isAttackingOrMovingToAttack", aUnit.isAttackingOrMovingToAttack());
        gen.writeBooleanField("hasValidTarget", aUnit.hasValidTarget());
        gen.writeBooleanField("isLifted", aUnit.isLifted());
        gen.writeBooleanField("isLoaded", aUnit.isLoaded());
        gen.writeBooleanField("isUnderDisruptionWeb", aUnit.isUnderDisruptionWeb());
        gen.writeBooleanField("isUnderDarkSwarm", aUnit.isUnderDarkSwarm());
        gen.writeBooleanField("isUnderStorm", aUnit.isUnderStorm());
        gen.writeNumberField("int_getRemainingBuildTime", aUnit.getRemainingBuildTime());
        gen.writeNumberField("int_remainingResearchTime", aUnit.remainingResearchTime());
        gen.writeNumberField("int_remainingTrainTime", aUnit.remainingTrainTime());
        gen.writeNumberField("int_getTotalTrainTime", aUnit.getTotalTrainTime());
        gen.writeNumberField("int_remainingUpgradeTime", aUnit.remainingUpgradeTime());
        gen.writeBooleanField("isTrainingAnyUnit", aUnit.isTrainingAnyUnit());
        gen.writeBooleanField("isBeingConstructed", aUnit.isBeingConstructed());
        gen.writeBooleanField("isInterruptible", aUnit.isInterruptible());
//        public UnitCommand getLastCommand()
//        public Action action()
        gen.writeBooleanField("isUnitActionAttack", aUnit.isUnitActionAttack());
        gen.writeBooleanField("isUnitActionRepair", aUnit.isUnitActionRepair());
        gen.writeBooleanField("noCooldown", aUnit.noCooldown());
        gen.writeBooleanField("hasCooldown", aUnit.hasCooldown());
        gen.writeNumberField("int_scarabCount", aUnit.scarabCount());
        gen.writeBooleanField("isRepairerOfAnyKind", aUnit.isRepairerOfAnyKind());
        gen.writeBooleanField("isScout", aUnit.isScout());
        gen.writeNumberField("int_getSpaceProvided", aUnit.getSpaceProvided());
        gen.writeNumberField("int_spaceRequired", aUnit.spaceRequired());
        gen.writeNumberField("int_spaceRemaining", aUnit.spaceRemaining());
        gen.writeNumberField("int_lastUnderAttackAgo", aUnit.lastUnderAttackAgo());
        gen.writeNumberField("int_lastAttackFrameAgo", aUnit.lastAttackFrameAgo());
        gen.writeNumberField("int_lastFrameOfStartingAttackAgo", aUnit.lastFrameOfStartingAttackAgo());
        gen.writeNumberField("int_lastStartedAttackAgo", aUnit.lastStartedAttackAgo());
        gen.writeNumberField("int_lastRetreatedAgo", aUnit.lastRetreatedAgo());
        gen.writeNumberField("int_lastStartedRunningAgo", aUnit.lastStartedRunningAgo());
        gen.writeBooleanField("hasNotMovedInAWhile", aUnit.hasNotMovedInAWhile());
        gen.writeBooleanField("isQuick", aUnit.isQuick());
        gen.writeBooleanField("isAccelerating", aUnit.isAccelerating());
        gen.writeBooleanField("isBraking", aUnit.isBraking());
        gen.writeNumberField("double_getAngle", aUnit.getAngle());
        gen.writeBooleanField("isFacingItsTarget", aUnit.isFacingItsTarget());
        gen.writeBooleanField("hasNoU", aUnit.hasNoU());
        gen.writeBooleanField("isFirstCombatUnit", aUnit.isFirstCombatUnit());
//        public Mission micro()
        gen.writeNumberField("int_squadSize", aUnit.squadSize());
//        public HasPosition squadCenter()
        gen.writeNumberField("int_energy", aUnit.energy());
        gen.writeNumberField("int_cooldownPercent", aUnit.cooldownPercent());
//        public Mission mission()
        gen.writeBooleanField("hasNothingInQueue", aUnit.hasNothingInQueue());
        gen.writeBooleanField("canCloak", aUnit.canCloak());
        gen.writeBooleanField("isArchon", aUnit.isArchon());
        gen.writeBooleanField("isUltralisk", aUnit.isUltralisk());

        // Write the loaded units array
//        gen.writeStartArray();
//        aUnit.loadedUnits().forEach(listUnit -> {
//            try {
//                gen.writeNumber(listUnit.id());
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
//        gen.writeEndArray();

        gen.writeNumberField("int_lastTechUsedAgo", aUnit.lastTechUsedAgo());
//        Optional.ofNullable(aUnit.lastTechUsed()).ifPresent(lastTech -> gen.writeStringField("lastTechName", lastTech.));
//        Optional.ofNullable(aUnit.lastTechPosition()).map(Position::x).ifPresent(x -> gen.writeNumberField("lastTechPosition_x", x));
//        Optional.ofNullable(aUnit.lastTechPosition()).map(Position::y).ifPresent(y -> gen.writeNumberField("lastTechPosition_y", y));
//        Optional.ofNullable(aUnit.lastTechUnit()).map(AUnit::id).ifPresent(id -> gen.writeNumberField("lastTechUnit_id", id));
        gen.writeBooleanField("hasCargo", aUnit.hasCargo());
        gen.writeBooleanField("hasAnyWeapon", aUnit.hasAnyWeapon());
        gen.writeBooleanField("hasNoWeaponAtAll", aUnit.hasNoWeaponAtAll());
        gen.writeBooleanField("recentlyAcquiredTargetToAttack", aUnit.recentlyAcquiredTargetToAttack());
        gen.writeNumberField("int_lastTargetToAttackAcquiredAgo", aUnit.lastTargetToAttackAcquiredAgo());
        gen.writeBooleanField("isAirUnitAntiAir", aUnit.isAirUnitAntiAir());
        gen.writeBooleanField("isSquadScout", aUnit.isSquadScout());
        gen.writeBooleanField("isNotAttackableByRangedDueToSpell", aUnit.isNotAttackableByRangedDueToSpell());
        gen.writeBooleanField("isStimmed", aUnit.isStimmed());
        gen.writeBooleanField("isStasised", aUnit.isStasised());
        gen.writeBooleanField("isLockedDown", aUnit.isLockedDown());
        gen.writeBooleanField("isDefenseMatrixed", aUnit.isDefenseMatrixed());
        gen.writeNumberField("int_stimTimer", aUnit.stimTimer());
        gen.writeNumberField("double_combatEvalAbsolute", aUnit.combatEvalAbsolute());
        gen.writeNumberField("double_combatEvalRelative", aUnit.combatEvalRelative());
        gen.writeBooleanField("isMedic", aUnit.isMedic());
        gen.writeBooleanField("isTerranInfantry", aUnit.isTerranInfantry());
        gen.writeBooleanField("isTerranInfantryWithoutMedics", aUnit.isTerranInfantryWithoutMedics());
        gen.writeBooleanField("isLurker", aUnit.isLurker());
        gen.writeBooleanField("isSunken", aUnit.isSunken());
        gen.writeNumberField("double_size", aUnit.size());
        gen.writeBooleanField("isMarine", aUnit.isMarine());
        gen.writeBooleanField("isGhost", aUnit.isGhost());
        gen.writeBooleanField("isFirebat", aUnit.isFirebat());
        gen.writeBooleanField("isRepairable", aUnit.isRepairable());
        gen.writeNumberField("int_totalCost", aUnit.totalCost());
        gen.writeNumberField("loadedInto_id", aUnit.loadedInto() == null ? -1 : aUnit.loadedInto().id());
        gen.writeBooleanField("isCombatBuilding", aUnit.isCombatBuilding());
        gen.writeBooleanField("isMutalisk", aUnit.isMutalisk());
        gen.writeBooleanField("isZealot", aUnit.isZealot());
        gen.writeBooleanField("isZergling", aUnit.isZergling());
        gen.writeBooleanField("isMissileTurret", aUnit.isMissileTurret());
        gen.writeBooleanField("isScv", aUnit.isScv());
        gen.writeBooleanField("isScienceVessel", aUnit.isScienceVessel());
        gen.writeBooleanField("isCombatUnit", aUnit.isCombatUnit());
        gen.writeNumberField("enemiesNear_count", aUnit.enemiesNear().count());
        gen.writeNumberField("meleeEnemiesNear_count", aUnit.meleeEnemiesNearCount());
        gen.writeNumberField("friendsNear_count", aUnit.friendsNearCount());
        gen.writeNumberField("allUnitsNear_count", aUnit.allUnitsNear().count());
        gen.writeBooleanField("hasMedicInRange", aUnit.hasMedicInRange());
        gen.writeBooleanField("isProtoss", aUnit.isProtoss());
        gen.writeBooleanField("isTerran", aUnit.isTerran());
        gen.writeBooleanField("isZerg", aUnit.isZerg());
        gen.writeBooleanField("isPowered", aUnit.isPowered());
        gen.writeBooleanField("medicInHealRange", aUnit.medicInHealRange());
        gen.writeBooleanField("isOverlord", aUnit.isOverlord());
        gen.writeBooleanField("isDragoon", aUnit.isDragoon());
        gen.writeBooleanField("isGoliath", aUnit.isGoliath());
        gen.writeBooleanField("isHydralisk", aUnit.isHydralisk());
        gen.writeBooleanField("isCommandCenter", aUnit.isCommandCenter());
        gen.writeBooleanField("isDT", aUnit.isDT());
        gen.writeBooleanField("isObserver", aUnit.isObserver());
        gen.writeBooleanField("isBeingHealed", aUnit.isBeingHealed());
        gen.writeBooleanField("isFoggedUnitWithUnknownPosition", aUnit.isFoggedUnitWithUnknownPosition());
        gen.writeBooleanField("isFoggedUnitWithKnownPosition", aUnit.isFoggedUnitWithKnownPosition());
        gen.writeBooleanField("isHealthy", aUnit.isHealthy());
        gen.writeBooleanField("isNearEnemyBuilding", aUnit.isNearEnemyBuilding());
        gen.writeBooleanField("isMissionSparta", aUnit.isMissionSparta());
        gen.writeBooleanField("isMissionDefend", aUnit.isMissionDefend());
        gen.writeBooleanField("isMissionDefendOrSparta", aUnit.isMissionDefendOrSparta());
        gen.writeBooleanField("isMissionAttack", aUnit.isMissionAttack());
        gen.writeBooleanField("isMissionContain", aUnit.isMissionContain());
        gen.writeBooleanField("isReaver", aUnit.isReaver());
        gen.writeBooleanField("recentlyMoved", aUnit.recentlyMoved());
        gen.writeBooleanField("idIsEven", aUnit.idIsEven());
        gen.writeBooleanField("idIsOdd", aUnit.idIsOdd());
        gen.writeNumberField("nearestEnemy_id", aUnit.nearestEnemy() == null ? -1 : aUnit.nearestEnemy().id());
        gen.writeNumberField("double_nearestEnemyDist", aUnit.nearestEnemyDist());
        gen.writeNumberField("double_distToLeader", aUnit.distToLeader());
        gen.writeBooleanField("hasSquad", aUnit.hasSquad());
        gen.writeNumberField("double_squadRadius", aUnit.squadRadius());
        gen.writeBooleanField("outsideSquadRadius", aUnit.outsideSquadRadius());
        gen.writeBooleanField("isProtector", aUnit.isProtector());
        gen.writeBooleanField("kitingUnit", aUnit.kitingUnit());
        gen.writeNumberField("double_distToFocusPoint", aUnit.distToFocusPoint());
        gen.writeBooleanField("shouldRetreat", aUnit.shouldRetreat());
        gen.writeBooleanField("isMechanical", aUnit.isMechanical());
        gen.writeBooleanField("isValid", aUnit.isValid());
        gen.writeBooleanField("notImmobilized", aUnit.notImmobilized());
        gen.writeBooleanField("debug", aUnit.debug());
        gen.writeNumberField("repairer_id", aUnit.repairer() == null ? -1 : aUnit.repairer().id());
        gen.writeBooleanField("isBeingRepaired", aUnit.isBeingRepaired());
        gen.writeBooleanField("isFlying", aUnit.isFlying());
        gen.writeBooleanField("looksIdle", aUnit.looksIdle());
        gen.writeBooleanField("canLift", aUnit.canLift());

        gen.writeEndObject();
    }
}