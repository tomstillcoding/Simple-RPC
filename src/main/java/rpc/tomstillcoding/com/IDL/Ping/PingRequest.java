package rpc.tomstillcoding.com.IDL.Ping;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class PingRequest implements Serializable {
    private String name;
}
