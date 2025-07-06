package net.ronm19.lunarismod.entity.ai;

public enum PackRole {
    LEADER,
    SCOUT,
    GUARDIAN,
    FOLLOWER;

    public boolean isLeader() {
        return false;
    }
}
