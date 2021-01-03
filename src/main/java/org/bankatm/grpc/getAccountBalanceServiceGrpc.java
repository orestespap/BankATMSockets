package org.bankatm.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.15.0)",
    comments = "Source: server.proto")
public final class getAccountBalanceServiceGrpc {

  private getAccountBalanceServiceGrpc() {}

  public static final String SERVICE_NAME = "org.bankatm.grpc.getAccountBalanceService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<org.bankatm.grpc.getAccountBalanceRequest,
      org.bankatm.grpc.getAccountBalanceResponse> getGetAccountBalanceMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getAccountBalance",
      requestType = org.bankatm.grpc.getAccountBalanceRequest.class,
      responseType = org.bankatm.grpc.getAccountBalanceResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.bankatm.grpc.getAccountBalanceRequest,
      org.bankatm.grpc.getAccountBalanceResponse> getGetAccountBalanceMethod() {
    io.grpc.MethodDescriptor<org.bankatm.grpc.getAccountBalanceRequest, org.bankatm.grpc.getAccountBalanceResponse> getGetAccountBalanceMethod;
    if ((getGetAccountBalanceMethod = getAccountBalanceServiceGrpc.getGetAccountBalanceMethod) == null) {
      synchronized (getAccountBalanceServiceGrpc.class) {
        if ((getGetAccountBalanceMethod = getAccountBalanceServiceGrpc.getGetAccountBalanceMethod) == null) {
          getAccountBalanceServiceGrpc.getGetAccountBalanceMethod = getGetAccountBalanceMethod = 
              io.grpc.MethodDescriptor.<org.bankatm.grpc.getAccountBalanceRequest, org.bankatm.grpc.getAccountBalanceResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "org.bankatm.grpc.getAccountBalanceService", "getAccountBalance"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.bankatm.grpc.getAccountBalanceRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.bankatm.grpc.getAccountBalanceResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new getAccountBalanceServiceMethodDescriptorSupplier("getAccountBalance"))
                  .build();
          }
        }
     }
     return getGetAccountBalanceMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static getAccountBalanceServiceStub newStub(io.grpc.Channel channel) {
    return new getAccountBalanceServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static getAccountBalanceServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new getAccountBalanceServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static getAccountBalanceServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new getAccountBalanceServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class getAccountBalanceServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void getAccountBalance(org.bankatm.grpc.getAccountBalanceRequest request,
        io.grpc.stub.StreamObserver<org.bankatm.grpc.getAccountBalanceResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetAccountBalanceMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetAccountBalanceMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.bankatm.grpc.getAccountBalanceRequest,
                org.bankatm.grpc.getAccountBalanceResponse>(
                  this, METHODID_GET_ACCOUNT_BALANCE)))
          .build();
    }
  }

  /**
   */
  public static final class getAccountBalanceServiceStub extends io.grpc.stub.AbstractStub<getAccountBalanceServiceStub> {
    private getAccountBalanceServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private getAccountBalanceServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected getAccountBalanceServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new getAccountBalanceServiceStub(channel, callOptions);
    }

    /**
     */
    public void getAccountBalance(org.bankatm.grpc.getAccountBalanceRequest request,
        io.grpc.stub.StreamObserver<org.bankatm.grpc.getAccountBalanceResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetAccountBalanceMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class getAccountBalanceServiceBlockingStub extends io.grpc.stub.AbstractStub<getAccountBalanceServiceBlockingStub> {
    private getAccountBalanceServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private getAccountBalanceServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected getAccountBalanceServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new getAccountBalanceServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public org.bankatm.grpc.getAccountBalanceResponse getAccountBalance(org.bankatm.grpc.getAccountBalanceRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetAccountBalanceMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class getAccountBalanceServiceFutureStub extends io.grpc.stub.AbstractStub<getAccountBalanceServiceFutureStub> {
    private getAccountBalanceServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private getAccountBalanceServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected getAccountBalanceServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new getAccountBalanceServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.bankatm.grpc.getAccountBalanceResponse> getAccountBalance(
        org.bankatm.grpc.getAccountBalanceRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetAccountBalanceMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_ACCOUNT_BALANCE = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final getAccountBalanceServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(getAccountBalanceServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_ACCOUNT_BALANCE:
          serviceImpl.getAccountBalance((org.bankatm.grpc.getAccountBalanceRequest) request,
              (io.grpc.stub.StreamObserver<org.bankatm.grpc.getAccountBalanceResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class getAccountBalanceServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    getAccountBalanceServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return org.bankatm.grpc.Server.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("getAccountBalanceService");
    }
  }

  private static final class getAccountBalanceServiceFileDescriptorSupplier
      extends getAccountBalanceServiceBaseDescriptorSupplier {
    getAccountBalanceServiceFileDescriptorSupplier() {}
  }

  private static final class getAccountBalanceServiceMethodDescriptorSupplier
      extends getAccountBalanceServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    getAccountBalanceServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (getAccountBalanceServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new getAccountBalanceServiceFileDescriptorSupplier())
              .addMethod(getGetAccountBalanceMethod())
              .build();
        }
      }
    }
    return result;
  }
}
