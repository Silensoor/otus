package ru.homework.otus;

import io.grpc.BindableService;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.homework.otus.service.RemoteDataService;

import java.io.IOException;

public class GrpcServer {

    private static final Logger logger = LoggerFactory.getLogger(GrpcServer.class);

    public static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {
        var service = new RemoteDataService();

        var server = ServerBuilder.forPort(SERVER_PORT).addService((BindableService) service).build();
        server.start();

        logger.atInfo().setMessage("server waiting client connection...").log();

        server.awaitTermination();
    }
}
