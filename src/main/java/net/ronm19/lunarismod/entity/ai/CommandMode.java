package net.ronm19.lunarismod.entity.ai;

public enum CommandMode {
    FOLLOW,
    HOLD,
    PATROL;

    public CommandMode next() {
        return switch(this) {
            case FOLLOW -> HOLD;
            case HOLD -> PATROL;
            case PATROL -> FOLLOW;
        };
    }
}
