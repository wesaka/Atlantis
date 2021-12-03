package atlantis.combat.missions;

import atlantis.position.APosition;
import atlantis.units.AUnit;
import atlantis.units.actions.UnitActions;
import atlantis.util.A;

public abstract class MoveToFocusPoint {

    protected static final double MARGIN = 0.6;

    protected static double optimalDist;
    protected static double distUnitToFocus;
    protected static double distUnitToFromSide;
    protected static double distFocusToFromSide;
    protected static AUnit unit;
    protected static AFocusPoint focusPoint;
    protected static APosition fromSide;

    // =========================================================

    /**
     * Unit is too far from its focus point and/or is on the wrong side of it (most evident on ramps).
     */
    protected static boolean tooFar() {
        if (fromSide != null) {
            if ((distUnitToFocus + distUnitToFromSide) > distFocusToFromSide * 1.1) {
                if (distUnitToFromSide > distUnitToFocus) {
                    String dist = A.dist(distUnitToFocus);
                    return unit.move(fromSide, UnitActions.MOVE_TO_FOCUS, "Withdraw" + dist);
                }
            }
        }

        if (distUnitToFocus > (optimalDist + MARGIN)) {
            String dist = A.dist(distUnitToFocus);
            return unit.move(focusPoint, UnitActions.MOVE_TO_FOCUS, "GoToFocus" + dist);
        }

        return false;
    }

    /**
     * Unit is too close to its focus point.
     */
    protected static boolean tooClose() {
        if (distUnitToFocus <= (optimalDist - MARGIN)) {
            String dist = A.dist(distUnitToFocus);

            if (distUnitToFromSide > 2) {
                return unit.move(fromSide, UnitActions.MOVE_TO_FOCUS, "TooClose" + dist);
            }
            return unit.moveAwayFrom(focusPoint, 0.5, "TooClose" + dist);
        }

        return false;
    }

}