package us.corenetwork.core.claims;

import us.corenetwork.core.CLog;

public abstract class BlockWorker  {
    private long progress = 0;

    public long runFor(long work) {
        long runFor = ClaimsSettings.WORKER_SPEED.integer();
        try {
            long worked = work(runFor);
            progress += worked;
            return worked;
        } catch (Exception e) {
            CLog.severe("Exception while running worker");
            e.printStackTrace();
            return 0;
        }
    }

    public abstract void init();

    public abstract void onDone();

    public abstract long getTaskSize();

    public long getProgress() {
        return progress;
    }

    public abstract long work(long items);

    public boolean isDone() {
        return getProgress() >= getTaskSize();
    }
}
