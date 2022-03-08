package atlantis.combat.missions;

import atlantis.game.A;
import atlantis.map.position.APosition;
import atlantis.units.AUnit;
import atlantis.units.actions.Actions;
import atlantis.util.We;

public abstract class MoveToFocusPoint {

    protected static final double MARGIN = 0.4;

    protected static double optimalDist;
    protected static double distUnitToFocus;
    protected static double distUnitToFromSide;
    protected static double distFocusToFromSide;
    protected static AUnit unit;
    protected static AFocusPoint focusPoint;
    protected static APosition fromSide;

    // =========================================================

    protected static boolean advance() {
        if (distUnitToFocus > (optimalDist + MARGIN)) {
            String dist = A.dist(distUnitToFocus);
            return unit.move(focusPoint, Actions.MOVE_FOCUS, "ToFocus" + dist, true);
        }

        return false;
    }

    /**
     * Unit is too far from its focus point and/or is on the wrong side of it (most evident on ramps).
     */
    protected static boolean wrongSideOfFocus() {
        if (!isAroundChoke() || fromSide == null) {
            return false;
        }

        if (distUnitToFromSide + 0.1 >= distFocusToFromSide) {
            return unit.move(fromSide, Actions.MOVE_FOCUS, "WithDraw" + A.dist(distUnitToFocus), true);
        }

//        if (fromSide != null && distUnitToFromSide < 12) {
//            if ((distUnitToFocus + distUnitToFromSide) > distFocusToFromSide * 1.1) {
//                if (distUnitToFromSide > distUnitToFocus) {
//                    String dist = A.dist(distUnitToFocus);
//                    return unit.move(fromSide, Actions.MOVE_FOCUS, "Withdraw" + dist, true);
//                }
//            }
//        }

        return false;
    }

    /**
     * Unit is too close to its focus point.
     */
    protected static boolean tooCloseToFocusPoint() {
        if (!isAroundChoke()) {
            return false;
        }

        if (unit.enemiesNear().inRadius(2, unit).isNotEmpty()) {
            return false;
        }

        if (unit.isZealot() && unit.enemiesNear().inRadius(6, unit).notEmpty()) {
            return false;
        }

        if (distUnitToFocus <= (optimalDist - MARGIN)) {
            String dist = A.dist(distUnitToFocus);

//            if (distUnitToFromSide > 3) {
//                return unit.move(fromSide, Actions.MOVE_FOCUS, "TooClose" + dist, true);
//            }

            return unit.moveAwayFrom(focusPoint, 0.15, "TooCloze" + dist, Actions.MOVE_FOCUS);
        }

        return false;
    }

    private static boolean isAroundChoke() {
        return focusPoint != null && focusPoint.isAroundChoke();
    }

}
