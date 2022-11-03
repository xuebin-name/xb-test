package client.nettyclient;

import client.nettyclient.config.Client;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NettyClientApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(NettyClientApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Client client = new Client("127.0.0.1",9001);
        try {
            client.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("客户端发生异常");
        }
    }

}
