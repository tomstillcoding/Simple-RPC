package rpc.tomstillcoding.com.core.client;

import rpc.tomstillcoding.com.core.codec.RpcRequestBody;
import rpc.tomstillcoding.com.core.codec.RpcResponseBody;
import rpc.tomstillcoding.com.core.rpc_protocol.RpcRequest;
import rpc.tomstillcoding.com.core.rpc_protocol.RpcResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RpcClientProxy implements InvocationHandler {
    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class<?>[]{clazz},
                this
        );
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // 1、将调用所需信息编码成bytes[]，即有了调用编码【codec层】
        RpcRequestBody rpcRequestBody = RpcRequestBody.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .paramTypes(method.getParameterTypes())
                .parameters(args)
                .build();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(rpcRequestBody);
        byte[] bytes = baos.toByteArray();

        // 2、创建RPC协议，将Header、Body的内容设置好（Body中存放调用编码）【protocol层】
        RpcRequest rpcRequest = RpcRequest.builder()
                .header("version=1")
                .body(bytes)
                .build();

        // 3、发送RpcRequest，获得RpcResponse
        RpcClientTransfer rpcClient = new RpcClientTransfer();
        RpcResponse rpcResponse = rpcClient.sendRequest(rpcRequest);

        // 4、解析RpcResponse，也就是在解析rpc协议【protocol层】
        String header = rpcResponse.getHeader();
        byte[] body = rpcResponse.getBody();
        if (header.equals("version=1")) {
            // 将RpcResponse的body中的返回编码，解码成我们需要的对象Object并返回【codec层】
            ByteArrayInputStream bais = new ByteArrayInputStream(body);
            ObjectInputStream ois = new ObjectInputStream(bais);
            RpcResponseBody rpcResponseBody = (RpcResponseBody) ois.readObject();
            Object retObject = rpcResponseBody.getRetObject();
            return retObject;
        }
        return null;
    }
}
