package com.github.paopaoyue.wljmod.stub;

import io.github.paopaoyue.mesh.rpc.exception.HandlerException;
import io.github.paopaoyue.mesh.rpc.exception.HandlerNotFoundException;
import io.github.paopaoyue.mesh.rpc.proto.Protocol;
import io.github.paopaoyue.mesh.rpc.stub.IServerStub;
import io.github.paopaoyue.mesh.rpc.stub.ServiceServerStub;
import io.github.paopaoyue.mesh.rpc.util.Context;
import com.github.paopaoyue.wljmod.proto.WljProto;
import com.github.paopaoyue.wljmod.service.IWljService;
import com.google.protobuf.Any;

@ServiceServerStub(serviceName = "wlj-service")
public class WljServerStub implements IServerStub {

    private static final String SERVICE_NAME = "wlj-service";

    private IWljService service;

    @Override
    public Protocol.Packet process(Protocol.Packet packet) throws HandlerException, HandlerNotFoundException {
        Context context = new Context(packet);
        Context.setContext(context);

        if (!context.getService().equals(SERVICE_NAME)) {
            throw new HandlerNotFoundException(context.getService(), context.getHandler());
        }

        Any responseBody;
        try {
            switch (context.getHandler()) {
                case "echo":
                    responseBody = Any.pack(service.echo(packet.getBody().unpack(WljProto.EchoRequest.class)));
                    break;
                default:
                    throw new HandlerNotFoundException(context.getService(), context.getHandler());
            }
        } catch (Exception e) {
            throw new HandlerException("Handler error", e);
        }

        Protocol.Packet out = Protocol.Packet.newBuilder()
                .setHeader(packet.getHeader())
                .setTraceInfo(packet.getTraceInfo())
                .setBody(responseBody)
                .build();

        Context.removeContext();
        return out;
    }
}
