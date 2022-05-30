package rpc.tomstillcoding.com.core.codec;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
// 调用编码
public class RpcRequestBody implements Serializable {
    private String interfaceName;
    private String methodName;
    private Object[] parameters;
    private Class<?>[] paramTypes;
}
