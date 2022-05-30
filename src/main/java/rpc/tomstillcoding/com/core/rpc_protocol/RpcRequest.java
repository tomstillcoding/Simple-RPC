package rpc.tomstillcoding.com.core.rpc_protocol;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
// Serializable：对象变成可传输的字节序列
public class RpcRequest implements Serializable {
    // 协议头部分
    private String header;
    // 协议体部分
    private byte[] body;


}

