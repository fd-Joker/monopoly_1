package monopoly;

/**
 * Created by Joker on 4/22/16.
 */
public class Dice {
    private int control_number;
    private int cur_number;

    public Dice() {
        resetControl_number();
    }

    public int throwIt() {
        if (control_number == -1) {
            // FIXME: debugging
            cur_number = (int) (Math.random() * 6) + 1;
            return cur_number;
        }
        else {
            cur_number = control_number;
            resetControl_number();
            return cur_number;
        }
    }

    public boolean isControlled() {
        return control_number != -1;
    }

    public void setControl_number(int control_number) {
        this.control_number = control_number;
    }

    public void resetControl_number() {
        this.control_number = -1;
    }

    public int getCur_number() {
        return cur_number;
    }
}
