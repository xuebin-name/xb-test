package com.example.demo.controller;

import com.example.demo.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private RedisUtils redisUtils;

    @GetMapping("/lock")
    public void testGet(){
        BlockingQueue workQueue = new SynchronousQueue<Runnable>();
        ThreadPoolExecutor executor;
        executor = new ThreadPoolExecutor(100, 150, 20, TimeUnit.SECONDS, workQueue);
        for (int i = 0; i < 100; i++) {
            executor.execute(() -> redisUtils.tryLock("lock-key",200));
        }

    }
}
