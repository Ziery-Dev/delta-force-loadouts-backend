package com.ziery.DeltaForceLoadouts.rateLimit;


import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleRateLimiter {

    private final Map<String, Deque<Long>> userRequests = new ConcurrentHashMap<>();

    private final int maxRequests;
    private final long windowMs;

    public SimpleRateLimiter(int maxRequests, long windowMs) {
        this.maxRequests = maxRequests;
        this.windowMs = windowMs;
    }

    /**
     * @return true se pode prosseguir; false se deve bloquear.
     */
    public boolean allow(String userKey) {
        long now = System.currentTimeMillis();
        long cutoff = now - windowMs;

        Deque<Long> queue = userRequests.computeIfAbsent(userKey, k -> new ArrayDeque<>());

        synchronized (queue) {
            // remove chamadas antigas fora da janela
            while (!queue.isEmpty() && queue.peekFirst() < cutoff) {
                queue.removeFirst();
            }

            // se já bateu o limite, bloqueia
            if (queue.size() >= maxRequests) {
                return false;
            }

            // registra essa chamada e permite
            queue.addLast(now);
            return true;
        }
    }
}
