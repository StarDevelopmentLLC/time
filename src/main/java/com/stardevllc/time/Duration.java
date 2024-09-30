package com.stardevllc.time;

public class Duration {
    private long time;
    
    public Duration() {}
    
    public long get() {
        return time;
    }
    
    public double get(TimeUnit unit) {
        return unit.fromMillis(time);
    }
    
    public Duration(TimeUnit unit, long time) {
        this.time = unit.toMillis(time);
    }
    
    public Duration add(TimeUnit unit, long time) {
        this.time += unit.toMillis(time);
        return this;
    }
    
    public Duration subtract(TimeUnit unit, long time) {
        this.time -= unit.toMillis(time);
        return this;
    }
    
    public Duration add(Duration duration) {
        this.time += duration.get();
        return this;
    }
    
    public Duration subtract(Duration duration) {
        this.time -= duration.get();
        return this;
    }
    
    public Duration abs() {
        this.time = Math.abs(this.time);
        return this;
    }
}