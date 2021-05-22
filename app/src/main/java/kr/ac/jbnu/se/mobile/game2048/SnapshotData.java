package kr.ac.jbnu.se.mobile.game2048;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * The data in a snapshot.
 */
public class SnapshotData {
    // TODO: Change it to Game Snapshot.

    private static long VERSION_NUMBER = 1;

    private long score;

    public SnapshotData(long score) {
        this.score = score;
    }

    public long getScore() {
        return score;
    }
}
