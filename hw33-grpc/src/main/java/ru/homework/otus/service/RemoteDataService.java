package ru.homework.otus.service;

import io.grpc.stub.StreamObserver;
import ru.homework.otus.protobuf.BoundsMessage;
import ru.homework.otus.protobuf.DataMessage;
import ru.homework.otus.protobuf.RemoteDataServiceGrpc;

public class RemoteDataService extends RemoteDataServiceGrpc.RemoteDataServiceImplBase {

    @Override
    public void getData(BoundsMessage request, StreamObserver<DataMessage> responseObserver) {

        long number = request.getFirstValue();
        long last = request.getLastValue();

        while (number <= last) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }

            var message = DataMessage.newBuilder().setValue(number).build();

            responseObserver.onNext(message);

            number++;
        }

        responseObserver.onCompleted();
    }
}
