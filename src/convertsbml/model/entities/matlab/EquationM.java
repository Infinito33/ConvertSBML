package convertsbml.model.entities.matlab;

/**
 * Równanie Matlab.
 *
 * @author Magda
 */
public class EquationM {

    private String full;
    private String leftSide;
    private String rightSide;
    private String comment;

    public EquationM(String full) {
        this.full = full.trim();
    }

    public EquationM(String full, String leftSide, String rightSide, String comment) {
        this.full = full.trim();
        this.leftSide = leftSide.trim();
        this.rightSide = rightSide.trim();
        this.comment = comment.trim();
    }

    public String getFull() {
        return full;
    }

    public void setFull(String full) {
        this.full = full.trim();
    }

    public String getLeftSide() {
        return leftSide;
    }

    public void setLeftSide(String leftSide) {
        this.leftSide = leftSide.trim();
    }

    public String getRightSide() {
        return rightSide;
    }

    public void setRightSide(String rightSide) {
        this.rightSide = rightSide.trim();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment.trim();
    }
}
