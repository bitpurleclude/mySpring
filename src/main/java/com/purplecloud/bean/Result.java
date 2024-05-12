package com.purplecloud.bean;

public class Result {
    private boolean processed;
    private Object returnObject;

    public Result() {
    }

    public Result(boolean processed, Object returnObject) {
        this.processed = processed;
        this.returnObject = returnObject;
    }

    /**
     * 获取
     * @return processed
     */
    public boolean isProcessed() {
        return processed;
    }

    /**
     * 设置
     * @param processed
     */
    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    /**
     * 获取
     * @return returnObject
     */
    public Object getReturnObject() {
        return returnObject;
    }

    /**
     * 设置
     * @param returnObject
     */
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    public String toString() {
        return "Result{processed = " + processed + ", returnObject = " + returnObject + "}";
    }
}
