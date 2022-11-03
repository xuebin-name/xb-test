package com.example.demo;

import com.example.demo.util.RedisUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private RedisUtils redisUtils;


    @Test
    void contextLoads() {
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        int k = scanner.nextInt();
        int n = s.length();
        Map<Integer, String> map = new HashMap<>();
        String[] array = s.split("");
        for (int i = 0; i < array.length; i++) {
            StringBuilder str = new StringBuilder();
            for (int j = i+1; j < array.length-1; j++) {

                if(array[i].compareTo(array[j])<=0){
                    str.append(array[i]);
                    str.append(array[j]);
                }
                if(str.length()==k){
                    Integer assic = Integer.valueOf(str.toString());
                    map.put(assic,str.toString());
                }

            }

        }

        List<Integer> collect = map.keySet().stream().sorted(Integer::compareTo).collect(Collectors.toList());
        System.out.println(collect.get(0));
        System.out.println(k);
    }


    @Test
    public void test02(){
        BlockingQueue workQueue = new SynchronousQueue<Runnable>();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(100, 150, 20, TimeUnit.SECONDS,workQueue);
        for (int i = 0; i < 100; i++) {
           executor.execute(() -> redisUtils.tryLock("lock-key",30));
        }

    }

}
