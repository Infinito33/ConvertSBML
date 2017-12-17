package convertsbml.model.entities.matlab;

/**
 *
 * @author Magda
 */
public class EquationM {

    private String full;
    private String leftSide;
    private String rightSide;
    private String comment;

    public EquationM(String full) {
        this.full = full;
    }
    
    public EquationM(String full, String leftSide, String rightSide, String comment) {
        this.full = full;
        this.leftSide = leftSide;
        this.rightSide = rightSide;
        this.comment = comment;
    }

    public String getFull() {
        return full;
    }

    public void setFull(String full) {
        this.full = full;
    }

    public String getLeftSide() {
        return leftSide;
    }

    public void setLeftSide(String leftSide) {
        this.leftSide = leftSide;
    }

    public String getRightSide() {
        return rightSide;
    }

    public void setRightSide(String rightSide) {
        this.rightSide = rightSide;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
