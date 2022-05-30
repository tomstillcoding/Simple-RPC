package rpc.tomstillcoding.com.core.server;

import rpc.tomstillcoding.com.core.rpc_protocol.RpcRequest;
import rpc.tomstillcoding.com.core.codec.RpcRequestBody;
import rpc.tomstillcoding.com.core.rpc_protocol.RpcResponse;
import rpc.tomstillcoding.com.core.codec.RpcResponseBody;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.HashMap;

public class RpcServerWorker implements Runnable{

    private Socket socket;
    private HashMap<String, Object> registeredService;

    public RpcServerWorker(Socket socket, HashMap<String, Object> registeredService) {
        this.socket = socket;
        this.registeredService = registeredService;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

            // 1、Transfer层获取到RpcRequest消息【transfer层】
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();

            // 2、解析版本号，并判断【protocol层】
            if (rpcRequest.getHeader().equals("version=1")) {

                // 3、将rpcRequest中的body部分解码出来变成RpcRequestBody【codec层】
                byte[] body = rpcRequest.getBody();
                ByteArrayInputStream bais = new ByteArrayInputStream(body);
                ObjectInputStream ois = new ObjectInputStream(bais);
                RpcRequestBody rpcRequestBody = (RpcRequestBody) ois.readObject();

                // 调用服务
                Object service = registeredService.get(rpcRequestBody.getInterfaceName());
                Method method = service.getClass().getMethod(rpcRequestBody.getMethodName(), rpcRequestBody.getParamTypes());
                Object returnObject = method.invoke(service, rpcRequestBody.getParameters());

                // 1、将returnObject编码成bytes[]即变成了返回编码【codec层】
                RpcResponseBody rpcResponseBody = RpcResponseBody.builder()
                        .retObject(returnObject)
                        .build();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(rpcResponseBody);
                byte[] bytes = baos.toByteArray();

                // 2、将返回编码作为body，加上header，生成RpcResponse协议【protocol层】
                RpcResponse rpcResponse = RpcResponse.builder()
                        .header("version=1")
                        .body(bytes)
                        .build();
                // 3、发送【transfer层】
                objectOutputStream.writeObject(rpcResponse);
                objectOutputStream.flush();
            }

        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
