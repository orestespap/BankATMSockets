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
public final class depositServiceGrpc {

  private depositServiceGrpc() {}

  public static final String SERVICE_NAME = "org.bankatm.grpc.depositService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<org.bankatm.grpc.depositRequest,
      org.bankatm.grpc.depositResponse> getDepositMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "deposit",
      requestType = org.bankatm.grpc.depositRequest.class,
      responseType = org.bankatm.grpc.depositResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.bankatm.grpc.depositRequest,
      org.bankatm.grpc.depositResponse> getDepositMethod() {
    io.grpc.MethodDescriptor<org.bankatm.grpc.depositRequest, org.bankatm.grpc.depositResponse> getDepositMethod;
    if ((getDepositMethod = depositServiceGrpc.getDepositMethod) == null) {
      synchronized (depositServiceGrpc.class) {
        if ((getDepositMethod = depositServiceGrpc.getDepositMethod) == null) {
          depositServiceGrpc.getDepositMethod = getDepositMethod = 
              io.grpc.MethodDescriptor.<org.bankatm.grpc.depositRequest, org.bankatm.grpc.depositResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "org.bankatm.grpc.depositService", "deposit"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.bankatm.grpc.depositRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.bankatm.grpc.depositResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new depositServiceMethodDescriptorSupplier("deposit"))
                  .build();
          }
        }
     }
     return getDepositMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static depositServiceStub newStub(io.grpc.Channel channel) {
    return new depositServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static depositServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new depositServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static depositServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new depositServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class depositServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void deposit(org.bankatm.grpc.depositRequest request,
        io.grpc.stub.StreamObserver<org.bankatm.grpc.depositResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getDepositMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getDepositMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.bankatm.grpc.depositRequest,
                org.bankatm.grpc.depositResponse>(
                  this, METHODID_DEPOSIT)))
          .build();
    }
  }

  /**
   */
  public static final class depositServiceStub extends io.grpc.stub.AbstractStub<depositServiceStub> {
    private depositServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private depositServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected depositServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new depositServiceStub(channel, callOptions);
    }

    /**
     */
    public void deposit(org.bankatm.grpc.depositRequest request,
        io.grpc.stub.StreamObserver<org.bankatm.grpc.depositResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getDepositMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class depositServiceBlockingStub extends io.grpc.stub.AbstractStub<depositServiceBlockingStub> {
    private depositServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private depositServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected depositServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new depositServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public org.bankatm.grpc.depositResponse deposit(org.bankatm.grpc.depositRequest request) {
      return blockingUnaryCall(
          getChannel(), getDepositMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class depositServiceFutureStub extends io.grpc.stub.AbstractStub<depositServiceFutureStub> {
    private depositServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private depositServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected depositServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new depositServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.bankatm.grpc.depositResponse> deposit(
        org.bankatm.grpc.depositRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getDepositMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_DEPOSIT = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final depositServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(depositServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_DEPOSIT:
          serviceImpl.deposit((org.bankatm.grpc.depositRequest) request,
              (io.grpc.stub.StreamObserver<org.bankatm.grpc.depositResponse>) responseObserver);
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

  private static abstract class depositServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    depositServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return org.bankatm.grpc.Server.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("depositService");
    }
  }

  private static final class depositServiceFileDescriptorSupplier
      extends depositServiceBaseDescriptorSupplier {
    depositServiceFileDescriptorSupplier() {}
  }

  private static final class depositServiceMethodDescriptorSupplier
      extends depositServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    depositServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (depositServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new depositServiceFileDescriptorSupplier())
              .addMethod(getDepositMethod())
              .build();
        }
      }
    }
    return result;
  }
}
