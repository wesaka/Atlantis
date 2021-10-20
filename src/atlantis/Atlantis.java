package atlantis;

import atlantis.combat.squad.ASquadManager;
import atlantis.constructing.*;
import atlantis.enemy.AEnemyUnits;
import atlantis.information.AOurUnitsExtraInfo;
import atlantis.production.orders.AProductionQueueManager;
import atlantis.repair.ARepairAssignments;
import atlantis.ums.UmsSpecialActions;
import atlantis.units.AUnit;
import atlantis.util.ProcessHelper;
import bwapi.*;

/**
 * Main bridge between the game and your code, ported to BWMirror.
 */
public class Atlantis implements BWEventListener {

    /**
     * Singleton instance.
     */
    private static Atlantis instance;

    /**
     * JBWAPI core class.
     */
    private static BWClient bwClient;

    /**
     * JBWAPI's game object, contains low-level methods.
     */
    private Game game;

    /**
     * Class controlling game speed.
     */
    private static AGameSpeed gameSpeed;

    /**
     * Top abstraction-level class that governs all units, buildings etc.
     */
    private AGameCommander gameCommander;

    // =========================================================
    // Other variables
    private boolean _isStarted = false; // Has game been started
    private boolean _isPaused = false; // Is game currently paused
    private final boolean _initialActionsExecuted = false; // Have executed one-time actions at match start?

    // =========================================================
    // Counters
    /**
     * How many units we have killed.
     */
    public static int KILLED = 0;

    /**
     * How many units we have lost.
     */
    public static int LOST = 0;

    /**
     * How many resources (minerals+gas) units we have killed were worth.
     */
    public static int KILLED_RESOURCES = 0;

    /**
     * How many resources (minerals+gas) units we have lost were worth.
     */
    public static int LOST_RESOURCES = 0;

    // =========================================================
    /**
     * It's executed only once, before the first game frame happens.
     */
    @Override
    public void onStart() {

        // Initialize game object - JBWAPI's representation of game and its state.
        game = bwClient.getGame();

        // Initialize Game Commander, a class to rule them all
        gameCommander = new AGameCommander();

        // Allow user input etc
        setBwapiFlags();

        // =========================================================

        OnStart.execute();
    }

    private void setBwapiFlags() {
//        game.setLocalSpeed(AtlantisConfig.GAME_SPEED);  // Change in-game speed (0 - fastest, 20 - normal)
//        game.setFrameSkip(AtlantisConfig.FRAME_SKIP);   // Number of GUI frames to skip
//        game.setGUI(false);                           // Turn off GUI - will speed up game considerably
        game.enableFlag(Flag.UserInput);                // Without this flag you can't control units with mouse
//        game.enableFlag(Flag.CompleteMapInformation); // See entire map - must be disabled for real games
    }

    /**
     * It's single time frame, entire logic goes in here. It's executed approximately 25 times per second.
     */
    @Override
    public void onFrame() {

        // === Handle PAUSE ================================================
        // If game is paused wait 100ms - pause is handled by PauseBreak button
        while (AGameSpeed.isPaused()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) { }
        }

        // === All game actions that take place every frame ==================================================
        
        try {

            // If game is running (not paused), proceed with all actions.
            if (gameCommander != null) {
                gameCommander.update();
            }
            else {
                System.err.println("Game Commander is null, totally screwed.");
            }
        }

        // === Catch any exception that occur not to "kill" the bot with one trivial error ===================
        catch (Exception e) {
            System.err.println("### AN ERROR HAS OCCURRED ###");
            e.printStackTrace();
        }
    }

    /**
     * This is only valid to our units. We have started training a new unit. It exists in the memory, but its
     * unit.isComplete() is false and issuing orders to it has no effect. It's executed only once per unit.
     *
     * @see AUnit::unitCreate()
     */
    @Override
    public void onUnitCreate(Unit u) {
        AUnit unit = AUnit.createFrom(u);
        if (unit != null) {
            unit.removeTooltip();

            // Our unit
            if (unit.isOur()) {
                AProductionQueueManager.rebuildQueue();

                // Apply construction fix: detect new Protoss buildings and remove them from queue.
                if (AGame.isPlayingAsProtoss() && unit.type().isBuilding()) {
                    ProtossConstructionManager.handleWarpingNewBuilding(unit);
                }
            }
        }
    }

    /**
     * This is only valid to our units. New unit has been completed, it's existing on map. It's executed only
     * once per unit.
     */
    @Override
    public void onUnitComplete(Unit u) {
        AUnit unit = AUnit.createFrom(u);
        if (unit != null) {
            unit.refreshType();

            ourNewUnit(unit);
        }
        else {
            System.err.println("onUnitComplete null for " + u);
        }
    }

    /**
     * A unit has been destroyed. It was either our unit or enemy unit.
     */
    @Override
    public void onUnitDestroy(Unit u) {
        AUnit unit = AUnit.createFrom(u);

//        Unit theUnit = AtlantisUnitInformationManager.getUnitDataByID(unit.getID()).getUnit();
        if (unit != null) {
            if (unit.isEnemyUnit()) {
                AEnemyUnits.unitDestroyed(unit);
            }
            else if (unit.isOur()) {
                AOurUnitsExtraInfo.idsOfOurDestroyedUnits.add(unit.getID());
            }

            // Our unit
            if (unit.isOur()) {
                AProductionQueueManager.rebuildQueue();
                ASquadManager.battleUnitDestroyed(unit);
                ARepairAssignments.removeRepairerOrProtector(unit);
                if (!unit.type().isGasBuilding()) {
                    LOST++;
                    LOST_RESOURCES += unit.type().getTotalResources();
                }
            } else {
                if (!unit.type().isGeyser()) {
                    KILLED++;
                    KILLED_RESOURCES += unit.type().getTotalResources();
                }
            }
        }
    }

    /**
     * For the first time we have discovered non-our unit. It may be enemy unit, but also a <b>mineral</b> or
     * a <b>critter</b>.
     */
    @Override
    public void onUnitDiscover(Unit u) {
        AUnit unit = AUnit.createFrom(u);
        if (unit != null) {

            // Enemy unit
            if (unit.isEnemyUnit()) {
                enemyNewUnit(unit);
            }

            else if (unit.isOur()) {
            }

            else {
                if (!unit.isNotActualUnit()) {
                    System.out.println("Neutral unit discovered! " + unit.shortName());
                    UmsSpecialActions.NEW_NEUTRAL_THAT_WILL_RENEGADE_TO_US = unit;
                }
            }
        }
    }

    /**
     * Called when unit is hidden by a fog war and it becomes inaccessible by the BWAPI.
     */
    @Override
    public void onUnitEvade(Unit u) {
//        AUnit unit = AUnit.createFrom(u);
    }

    /**
     * Called just as a visible unit is becoming invisible.
     */
    @Override
    public void onUnitHide(Unit u) {
//        AUnit unit = AUnit.createFrom(u);
    }

    /**
     * Called when a unit changes its AUnitType.
     *
     * For example, when a Drone transforms into a Hatchery, a Siege Tank uses Siege Mode, or a Vespene Geyser
     * receives a Refinery.
     */
    @Override
    public void onUnitMorph(Unit u) {
        AUnit unit = AUnit.createFrom(u);

        // A bit of safe approach: forget the unit and remember it again.
        // =========================================================
        // Forget unit
        if (unit != null) {
            if (unit.isOur()) {
                ASquadManager.battleUnitDestroyed(unit);
            } else {
                AEnemyUnits.unitDestroyed(unit);
            }
        }

        // =========================================================
        // Remember the unit
        if (unit != null) {
            unit.refreshType();

            // Our unit
            if (unit.isOur()) {

                // === Fix for Zerg Extractor ========================================
                // Detect morphed gas building meaning construction has just started
                if (unit.type().isGasBuilding()) {
                    for (ConstructionOrder order : AConstructionRequests.getAllConstructionOrders()) {
                        if (order.getBuildingType().equals(AtlantisConfig.GAS_BUILDING)
                                && order.getStatus().equals(ConstructionOrderStatus.CONSTRUCTION_NOT_STARTED)) {
                            order.setConstruction(unit);
                            break;
                        }
                    }
                }

                // =========================================================
                AProductionQueueManager.rebuildQueue();

                // Add to combat squad if it's military unit
                if (unit.isActualUnit()) {
                    ASquadManager.possibleCombatUnitCreated(unit);
                }
            }

            // Enemy unit
            else {
                AEnemyUnits.refreshEnemyUnit(unit);
            }
        }
    }

    /**
     * Called when a previously invisible unit becomes visible.
     */
    @Override
    public void onUnitShow(Unit u) {
        AUnit unit = AUnit.createFrom(u);
        if (unit.isEnemyUnit()) {
            AEnemyUnits.updateEnemyUnitPosition(unit);
        }
    }

    /**
     * Unit has been converted and joined the enemy (by Dark Archon).
     */
    @Override
    public void onUnitRenegade(Unit u) {
        onUnitDestroy(u);
        AUnit.forgetUnitEntirely(u);
        AUnit newUnit = AUnit.createFrom(u);
        if (newUnit.type().isGasBuilding()) {
            return;
        }

        // New unit taken from us
        if (u.getPlayer().equals(AGame.getPlayerUs())) {
            ourNewUnit(newUnit);
            System.out.println("NEW RENEGADE FOR US " + newUnit.shortName());
            UmsSpecialActions.NEW_NEUTRAL_THAT_WILL_RENEGADE_TO_US = newUnit;
        }

        // New unit for us e.g. some UMS maps give units
        else {
            enemyNewUnit(newUnit);
            System.out.println("NEW RENEGADE FOR ENEMY " + newUnit.shortName());
        }
    }

    private void enemyNewUnit(AUnit unit) {
        AEnemyUnits.discoveredEnemyUnit(unit);
    }

    private void ourNewUnit(AUnit unit) {
        AProductionQueueManager.rebuildQueue();

        // Our unit
        if (unit.isOur()) {
            ASquadManager.possibleCombatUnitCreated(unit);
        }
    }

    /**
     * Any key has been pressed. Can be used to define key shortcuts.
     *
     * @Override public void keyPressed(int keyCode) { //
     * System.err.println("########################################"); // System.err.println("############KEY
     * = " + keyCode + "############################"); //
     * System.err.println("########################################");
     *
     * // 27 (Esc) - pause/unpause game if (keyCode == 27) { pauseOrUnpause(); } // 107 (+) - increase game
     * speed else if (keyCode == 107) { AtlantisConfig.GAME_SPEED -= 2; if (AtlantisConfig.GAME_SPEED < 0) {
     * AtlantisConfig.GAME_SPEED = 0; } AGame.changeSpeed(AtlantisConfig.GAME_SPEED); } // 109 (-) -
     * decrease game speed else if (keyCode == 109) { AtlantisConfig.GAME_SPEED += 2;
     * AGame.changeSpeed(AtlantisConfig.GAME_SPEED); }
     *
     * // =========================================================
     * // 107 (+) - increase game speed
     * if (AtlantisConfig.GAME_SPEED > 2) { AtlantisConfig.GAME_SPEED -= 10; } else { }
     *
     *
     * // ========================================================= // 109 (-) - decrease game speed if
     * (AtlantisConfig.GAME_SPEED > 2) { AtlantisConfig.GAME_SPEED += 10; } else { } }
     */
    /**
     * Match has ended. Shortly after that the game will go to the menu.
     */
    @Override
    public void onEnd(boolean winner) {
        System.out.println();
        if (winner) {
            System.out.println("You were VICTORIOUS!");
        } else {
            System.out.println("DEFEAT...");
        }

        exitGame();
    }

    public void exitGame() {
        gameSummary();
        killProcesses();
    }

    private void killProcesses() {
        System.out.print("Killing StarCraft process... ");
        ProcessHelper.killStarcraftProcess();

        System.out.print("Killing Chaoslauncher process... ");
        ProcessHelper.killChaosLauncherProcess();

        System.out.println("Exit...");
        System.exit(0);
    }

    private void gameSummary() {
        if (Atlantis.game() == null) {
            return;
        }
        System.out.print("Total time: " + AGame.getTimeSeconds() + " seconds. Killed: "
                + AGame.getPlayerUs().getKillScore() + ", Lost: " + AGame.getEnemy().getKillScore() + ". ");
        System.out.println("Resource killed/lost balance: " + AGame.killsLossesResourceBalance());
    }

    /**
     * Send text using in game chat. Can be used to type in cheats.
     */
    @Override
    public void onSendText(String text) {
    }

    /**
     * The other bot or observer has sent a message using game chat.
     */
    @Override
    public void onReceiveText(Player player, String s) {

    }

    /**
     * "Nuclear launch detected".
     */
    @Override
    public void onNukeDetect(Position p) {
    }

    /**
     * "Nuclear launch detected".
     * <b>Guess: I guess in this case we don't know the exact point of it.</b>
     *
     * @Override public void nukeDetect() { }
     */
    /**
     * Other player has left the game.
     */
    @Override
    public void onPlayerLeft(Player player) {
    }

    /**
     * <b>Not sure, haven't used it, sorry</b>.
     */
    @Override
    public void onSaveGame(String gameName) {
    }

    /**
     * Other player has been thrown out from the game.
     */
    @Override
    public void onPlayerDropped(Player player) {
    }

    // =========================================================
    // Constructors
    /**
     * You have to pass AtlantisConfig object to initialize Atlantis.
     */
    public Atlantis() {
        instance = this; // Save static reference to this instance, act like a singleton.
    }

    /**
     * Returns the current Atlantis instance, useful for retrieving game information
     *
     * @return
     */
    public static Atlantis getInstance() {
        if (instance == null) {
            instance = new Atlantis();
        }
        return instance;
    }

    // =========================================================
    // Start / Pause / Unpause
    /**
     * Starts the bot.
     */
    public void run() {
        if (!_isStarted) {
            _isPaused = false;
            _isStarted = true;

            bwClient = new BWClient(this);
            bwClient.startGame();
        }
    }

    /**
     * Forces all calculations to be stopped. CPU usage should be minimal. Or resumes the game after pause.
     */
    public void pauseOrUnpause() {
        _isPaused = !_isPaused;
    }

    // =========================================================
    /**
     * This method returns bridge connector between Atlantis and Starcraft, which is a BWMirror object. It
     * provides low-level functionality for functions like canBuildHere etc. For more details, see BWMirror
     * project documentation.
     */
    public static Game game() {
        return getInstance().game;
    }

    // =========================================================
    // Utility / Axuliary methods
    /**
     * This is convenience that takes any number of arguments and displays them in one line.
     */
    public static void debug(Object... args) {
        for (int i = 0; i < args.length - 1; i++) {
            System.out.print(args[i] + " / ");
        }
        System.out.println(args[args.length - 1]);
    }

}
