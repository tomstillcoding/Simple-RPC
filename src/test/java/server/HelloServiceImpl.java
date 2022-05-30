package server;

import rpc.tomstillcoding.com.IDL.Hello.HelloRequest;
import rpc.tomstillcoding.com.IDL.Hello.HelloResponse;
import rpc.tomstillcoding.com.IDL.Hello.HelloService;

public class HelloServiceImpl implements HelloService {
    @Override
    public HelloResponse hello(HelloRequest request) {
        String name = request.getName();
        String retMsg = "hello: " + name;
        HelloResponse response = new HelloResponse(retMsg);
        return response;
    }

    @Override
    public HelloResponse hi(HelloRequest request) {
        String name = request.getName();
        String retMsg = "hi: " + name;
        HelloResponse response = new HelloResponse(retMsg);
        return response;
    }
}
