package net.ronm19.lunarismod.entity.ai;

public abstract class PackRoleHolderBase implements IPackRoleHolder {
    private PackRole packRole = PackRole.FOLLOWER;

    @Override
    public PackRole getPackRole() {
        return packRole != null ? packRole : PackRole.FOLLOWER;
    }

    @Override
    public void setPackRole(PackRole role) {
        this.packRole = (role != null) ? role : PackRole.FOLLOWER;
    }
}

