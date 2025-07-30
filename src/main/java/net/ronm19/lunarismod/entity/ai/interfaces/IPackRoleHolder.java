package net.ronm19.lunarismod.entity.ai.interfaces;

import net.ronm19.lunarismod.entity.ai.PackRole;

public interface IPackRoleHolder {
    PackRole getPackRole();
    void setPackRole(PackRole role);

    default boolean isLeader() {
        PackRole role = getPackRole();
        return role == PackRole.LEADER;
    }
}
