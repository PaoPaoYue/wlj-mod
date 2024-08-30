package com.github.paopaoyue.wljmod.api;

import io.github.paopaoyue.mesh.rpc.api.CallOption;
import com.github.paopaoyue.wljmod.proto.WljProto;

public interface IWljCaller {

    WljProto.EchoResponse echo(WljProto.EchoRequest request, CallOption option);
}
