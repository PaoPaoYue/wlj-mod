package com.github.paopaoyue.wljmod.service;

import com.github.paopaoyue.wljmod.proto.WljProto;

public interface IWljService {

    WljProto.EchoResponse echo(WljProto.EchoRequest request);
}
