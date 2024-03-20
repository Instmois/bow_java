package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class GameInfo {
    public final CircleInfo bigTarget;
    public final CircleInfo smallTarget;
    public final List<PlayerInfo> playerList = new ArrayList<>();

    public GameInfo(final double height) {
        bigTarget = new CircleInfo(226.0, 0.5 * height, 44.0, 3);
        smallTarget = new CircleInfo(311.0, 0.5 * height, 22.0, 6);
    }
}
