package monopoly;

/**
 * Created by Joker on 6/20/16.
 */
public class PlayerStatus {
    public enum Status {
        IN_HOSPITAL
    }

    private Status status;
    private int round;

    public PlayerStatus(Status status, int round) {
        this.status = status;
        this.round = round;
    }

    public Status getStatus() {
        return status;
    }

    public int getRound() {
        return round;
    }

    public void setStatus(Status status, int round) {
        this.status = status;
        this.round = round;
    }

    public void updateStatus() {
        round--;
        if (round == 0)
            status = null;
    }
}
