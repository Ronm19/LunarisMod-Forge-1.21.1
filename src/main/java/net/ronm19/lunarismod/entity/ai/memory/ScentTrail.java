package net.ronm19.lunarismod.entity.ai.memory;

import net.minecraft.core.BlockPos;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ScentTrail {
    private final LinkedList<TimedPos> trail = new LinkedList<>();
    private static final long TRAIL_DURATION = 12000L; // 10 minutes in ticks

    public void addPosition(BlockPos pos, long gameTime) {
        trail.addLast(new TimedPos(pos, gameTime));
        cleanupOldPositions(gameTime);
    }

    public List<BlockPos> getPositions() {
        List<BlockPos> positions = new LinkedList<>();
        for (TimedPos tp : trail) {
            positions.add(tp.pos);
        }
        return positions;
    }

    private void cleanupOldPositions(long currentTime) {
        Iterator<TimedPos> iter = trail.iterator();
        while (iter.hasNext()) {
            TimedPos tp = iter.next();
            if (currentTime - tp.time > TRAIL_DURATION) {
                iter.remove();
            } else {
                break; // list is ordered by time, so can break early
            }
        }
    }

    private static class TimedPos {
        final BlockPos pos;
        final long time;

        TimedPos(BlockPos pos, long time) {
            this.pos = pos;
            this.time = time;
        }
    }


}