package com.github.paopaoyue.wljmod.service;

import com.github.paopaoyue.wljmod.proto.WljProto;
import io.github.paopaoyue.mesh.rpc.service.RpcService;

@RpcService(serviceName = "wlj-service")
public class WljService implements IWljService {

    @Override
    public WljProto.EchoResponse echo(WljProto.EchoRequest request) {
        return WljProto.EchoResponse.newBuilder().setText(request.getText()).build();
    }
}
