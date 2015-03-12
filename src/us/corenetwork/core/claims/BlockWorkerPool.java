package us.corenetwork.core.claims;

import us.corenetwork.core.CLog;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class BlockWorkerPool implements Runnable {
    private Set<BlockWorker> working = new HashSet<BlockWorker>();
    private Queue<BlockWorker> waiting = new LinkedBlockingQueue<BlockWorker>();

    public void addWorker(BlockWorker worker) {
        if (working.size() >= ClaimsSettings.WORKER_COUNT.integer()) {
            waiting.add(worker);
        } else {
            worker.init();
            working.add(worker);
        }
    }

    @Override
    public void run() {
        long workLoad = ClaimsSettings.WORKER_SPEED.integer();
        int remaining = working.size();

        Set<BlockWorker> toRemove = new HashSet<BlockWorker>();
        for (BlockWorker worker : working) {
            long workLoadThis = workLoad / remaining;
            long worked = worker.runFor(workLoadThis);
            remaining --;
            workLoad -= worked;

            CLog.debug("Worker " + worker.getClass().getSimpleName() + " progress: " + (int) ((double) worker.getProgress() / (double) worker.getTaskSize() * 100.0) + "%");

            if (worker.isDone()) {
                toRemove.add(worker);
                worker.onDone();
            }
        }

        if (!toRemove.isEmpty()) {
            CLog.debug(toRemove.size() + " workers are done.");
            working.removeAll(toRemove);
        }

        remaining = ClaimsSettings.WORKER_COUNT.integer() - working.size();

        while (!waiting.isEmpty() && remaining > 0) {
            BlockWorker worker = waiting.poll();
            worker.init();
            working.add(worker);
            remaining --;
        }

    }
}
