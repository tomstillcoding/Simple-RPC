package client;

import rpc.tomstillcoding.com.IDL.Hello.HelloRequest;
import rpc.tomstillcoding.com.IDL.Hello.HelloResponse;
import rpc.tomstillcoding.com.IDL.Hello.HelloService;
import rpc.tomstillcoding.com.core.client.RpcClientProxy;

public class TestClient {
    public static void main(String[] args) {
        // 获取RpcService
        RpcClientProxy proxy = new RpcClientProxy();
        HelloService helloService = proxy.getService(HelloService.class);
        // 构造出请求对象HelloRequest
        HelloRequest helloRequest = new HelloRequest("tom");
        // rpc调用并返回结果对象HelloResponse
        HelloResponse helloResponse = helloService.hello(helloRequest);
        // 从HelloResponse中获取msg
        String helloMsg = helloResponse.getMsg();
        // 打印msg
        System.out.println(helloMsg);

        // 调用hi方法
        HelloResponse hiResponse = helloService.hi(helloRequest);
        String hiMsg = hiResponse.getMsg();
        System.out.println(hiMsg);

        // 调用ping方法
//        PingService pingService = proxy.getService(PingService.class);
//        PingRequest pingRequest = new PingRequest("tom");
//        PingResponse pingResponse = pingService.ping(pingRequest);
//        String pingMsg = pingResponse.getMsg();
//        System.out.println(pingMsg);
    }
}
