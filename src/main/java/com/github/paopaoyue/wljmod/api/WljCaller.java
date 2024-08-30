package com.github.paopaoyue.wljmod.api;

import io.github.paopaoyue.mesh.rpc.api.CallOption;
import io.github.paopaoyue.mesh.rpc.api.RpcCaller;
import io.github.paopaoyue.mesh.rpc.stub.IClientStub;
import com.github.paopaoyue.wljmod.proto.WljProto;

@RpcCaller(serviceName = "wlj-service")
public class WljCaller implements IWljCaller {

    IClientStub clientStub;

    @Override
    public WljProto.EchoResponse echo(WljProto.EchoRequest request, CallOption option) {
        return clientStub.process(WljProto.EchoResponse.class, request, option);
    }
}