package server.nettyserver;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import server.nettyserver.config.NettyConfig;

@SpringBootApplication
public class NettyServerApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(NettyServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        new NettyConfig().startServer();
    }
}
