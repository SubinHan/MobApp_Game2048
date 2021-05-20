package kr.ac.jbnu.se.mobile.game2048;

public interface IGameListener {
    public void moved(SnapshotData snapshotData);
    public void gameOver(SnapshotData snapshotData);
}
