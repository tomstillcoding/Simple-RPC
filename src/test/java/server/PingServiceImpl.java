package server;

import rpc.tomstillcoding.com.IDL.Hello.HelloRequest;
import rpc.tomstillcoding.com.IDL.Hello.HelloResponse;
import rpc.tomstillcoding.com.IDL.Hello.HelloService;
import rpc.tomstillcoding.com.IDL.Ping.PingRequest;
import rpc.tomstillcoding.com.IDL.Ping.PingResponse;
import rpc.tomstillcoding.com.IDL.Ping.PingService;

public class PingServiceImpl implements PingService {

    @Override
    public PingResponse ping(PingRequest request) {
        String name = request.getName();
        String retMsg = "pong: " + name;
        PingResponse response = new PingResponse(retMsg);
        return response;
    }
}
