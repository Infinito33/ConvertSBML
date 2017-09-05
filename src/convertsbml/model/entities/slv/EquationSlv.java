package convertsbml.model.entities.slv;

/**
 * Klasa reprezentuje pojedyncze równanie, które można spotkać w modelu SLV.
 *
 * @author Magda
 */
public class EquationSlv {

    private String full;
    private String leftSide;
    private String rightSide;
    private String leftSideName;

    public EquationSlv() {

    }

    public EquationSlv(String full, String leftSide, String rightSide) {
        this.full = full;
        this.leftSide = leftSide;
        this.rightSide = rightSide;

        //Wyciągnięcie nazwy parametru po lewej stronie bez znaku '.
        Integer leftSideLength = leftSide.length();
        leftSideName = leftSide.substring(0, leftSideLength - 1);
    }

    public String getFull() {
        return full;
    }

    public String getLeftSide() {
        return leftSide;
    }

    public String getRightSide() {
        return rightSide;
    }

    public String getLeftSideName() {
        return leftSideName;
    }

    public void setLeftSideName(String leftSideName) {
        this.leftSideName = leftSideName;
    }

    @Override
    public String toString() {
        return full;
    }

}
