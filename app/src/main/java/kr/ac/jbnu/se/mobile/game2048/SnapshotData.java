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

    public byte[] serialize() {
        return (VERSION_NUMBER + "," + score).getBytes();
    }

//    @Nullable
//    public static SnapshotData deserialize(byte[] bytes) {
//        List<String> data = Arrays.asList(new String(bytes).split(","));
//        if (!data.get(0).equals(Long.toString(VERSION_NUMBER))) {
//            return null;
//        }
//        return newSnapshotData(Long.parseLong(data.get(1)));
//    }
//
//    @Nullable
//    public static SnapshotData deserialize(Snapshot snapshot) {
//        try {
//            byte[] bytes = snapshot.getSnapshotContents().readFully();
//            return SnapshotData.deserialize(bytes);
//        } catch (IOException ignored) {
//            return null;
//        }
//    }
}
