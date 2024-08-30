syntax = "proto3";

package rpc;

option java_package = "io.github.paopaoyue.mesh.rpc.proto";
option java_outer_classname = "Base";

// include this in any rpc response
message RespBase {
  int32 code = 1;
  string message = 2;
}

// framework error code
enum StatusCode {
  OK = 0;
  // system error
  CLIENT_INTERNAL_ERROR = 101;

  NETWORK_ERROR = 201;
  SERVICE_UNAVAILABLE = 202;
  GATEWAY_TIMEOUT = 203;

  UNKNOWN_ERROR = 401;
  // service error
  INTERNAL_SERVER_ERROR = 90001;
  INVALID_PARAM_ERROR = 90002;
}