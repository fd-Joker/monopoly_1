package monopoly;

import javax.swing.*;

/**
 * Created by Joker on 6/17/16.
 */
public class PlayerHead {
    public enum PlayerHeadType {
        head1, head2, head3, head4
    }

    public static final int totalNumberOfHeads = 4;
    public static final ImageIcon[] playerHeadList;

    static {
        playerHeadList = new ImageIcon[totalNumberOfHeads];
        for (int i = 0; i < totalNumberOfHeads; i++)
            playerHeadList[i] = new ImageIcon("./images/head" + (i+1) + "_normal.jpg");
    }

    private PlayerHeadType headIndex;

    public PlayerHead(PlayerHeadType headIndex) {
        this.headIndex = headIndex;
    }

    public ImageIcon getHeadImage() {
        return playerHeadList[headIndex.ordinal()];
    }
}
