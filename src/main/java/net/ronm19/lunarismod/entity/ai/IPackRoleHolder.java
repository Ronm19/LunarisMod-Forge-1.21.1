package net.ronm19.lunarismod.entity.ai;

public interface IPackRoleHolder {
    PackRole getPackRole();
    void setPackRole(PackRole role);

    default boolean isLeader() {
        PackRole role = getPackRole();
        return role == PackRole.LEADER;
    }
}
