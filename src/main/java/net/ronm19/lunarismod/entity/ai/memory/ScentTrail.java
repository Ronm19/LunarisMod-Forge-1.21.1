package net.ronm19.lunarismod.entity.ai.memory;

import net.minecraft.core.BlockPos;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ScentTrail {
    private final LinkedList<TimedPos> trail = new LinkedList<>();
    private final long trailDuration;

    public ScentTrail() {
        this(12000L); // Default: 10 minutes in ticks
    }

    public ScentTrail(long customDuration) {
        this.trailDuration = customDuration;
    }

    public void addPosition(BlockPos pos, long gameTime) {
        trail.addLast(new TimedPos(pos, gameTime));
        cleanupOldPositions(gameTime);
    }

    public List<BlockPos> getPositions() {
        return trail.stream()
                .map(tp -> tp.pos)
                .collect(Collectors.toList()); // Or .toList() on Java 16+
    }

    public List<BlockPos> getRecentPositions(int limit) {
        return trail.stream()
                .skip(Math.max(0, trail.size() - limit))
                .map(tp -> tp.pos)
                .collect(Collectors.toList());
    }

    public void merge(ScentTrail other, long gameTime) {
        for (TimedPos tp : other.trail) {
            trail.addLast(tp);
        }
        cleanupOldPositions(gameTime);
    }

    private void cleanupOldPositions(long currentTime) {
        Iterator<TimedPos> iter = trail.iterator();
        while (iter.hasNext()) {
            TimedPos tp = iter.next();
            if (currentTime - tp.time > trailDuration) {
                iter.remove();
            } else {
                break; // ordered by time — early exit
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