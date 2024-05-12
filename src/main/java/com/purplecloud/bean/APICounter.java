package com.purplecloud.bean;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 15517
 */
public class APICounter {
    private int limit;
    private final AtomicInteger COUNTER = new AtomicInteger(0);
    private final Timer TIMER = new Timer();
    public APICounter(long delay, int limit){
        this.limit=limit;
        startTimer(delay);
    }
    public boolean usedMoth(){
        if (getCounterValue()>=limit){
            return false;
        }else {
            incrementCounter();
            return true;
        }
    }
    /**
     * 访问接口时调用该方法，增加计数器
     */
    public void incrementCounter() {
        COUNTER.incrementAndGet();
    }
    public int getLimit() {
        return limit;
    }

    /**
     * 启动定时任务，在指定时间后重置计数器
     */
    public void startTimer(long delay) {
        TIMER.schedule(new TimerTask() {
            @Override
            public void run() {
                resetCounter();
            }
        }, delay);
    }

    /**
     * 重置计数器，用于统计特定时间段的访问次数
     */
    public void resetCounter() {
        COUNTER.set(0);
    }
    /**
     * 获取当前计数器的值
     */
    public int getCounterValue() {
        return COUNTER.get();
    }
}
