package com.github.paopaoyue.wljmod.stub;

import io.github.paopaoyue.mesh.rpc.api.CallOption;
import io.github.paopaoyue.mesh.rpc.RpcAutoConfiguration;
import io.github.paopaoyue.mesh.rpc.proto.Protocol;
import io.github.paopaoyue.mesh.rpc.stub.IClientStub;
import io.github.paopaoyue.mesh.rpc.stub.ServiceClientStub;
import io.github.paopaoyue.mesh.rpc.util.Context;
import io.github.paopaoyue.mesh.rpc.util.Flag;
import io.github.paopaoyue.mesh.rpc.util.RespBaseUtil;
import io.github.paopaoyue.mesh.rpc.util.TraceInfoUtil;
import com.github.paopaoyue.wljmod.proto.WljProto;
import com.google.protobuf.Any;
import com.google.protobuf.GeneratedMessage;

@ServiceClientStub(serviceName = "wlj-service")
public class WljClientStub implements IClientStub {

    private static final String SERVICE_NAME = "wlj-service";

    public <RESP extends GeneratedMessage, REQ extends GeneratedMessage> RESP process(Class<RESP> respClass, REQ request, CallOption option) {
        String handlerName;
        switch (request.getClass().getSimpleName()) {
            case "EchoRequest":
                handlerName = "echo";
                break;
            default:
                throw new IllegalArgumentException("Invalid request type: " + request.getClass().getSimpleName());
        }

        try {
            return respClass.cast(RpcAutoConfiguration.getRpcClient().getSender()
                    .send(SERVICE_NAME, handlerName, Any.pack(request), false, option).getBody().unpack(respClass));
        } catch (Exception e) {
            switch (handlerName) {
                case "echo":
                    return respClass.cast(WljProto.EchoResponse.newBuilder().setBase(RespBaseUtil.ErrorRespBase(e)).build());
                default:
                    throw new IllegalArgumentException("Invalid request type: " + request.getClass().getSimpleName());
            }
        }
    }
}
