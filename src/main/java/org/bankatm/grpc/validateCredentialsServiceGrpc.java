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
public final class validateCredentialsServiceGrpc {

  private validateCredentialsServiceGrpc() {}

  public static final String SERVICE_NAME = "org.bankatm.grpc.validateCredentialsService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<org.bankatm.grpc.validateCredentialsRequest,
      org.bankatm.grpc.validateCredentialsResponse> getValidateCredentialsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "validateCredentials",
      requestType = org.bankatm.grpc.validateCredentialsRequest.class,
      responseType = org.bankatm.grpc.validateCredentialsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.bankatm.grpc.validateCredentialsRequest,
      org.bankatm.grpc.validateCredentialsResponse> getValidateCredentialsMethod() {
    io.grpc.MethodDescriptor<org.bankatm.grpc.validateCredentialsRequest, org.bankatm.grpc.validateCredentialsResponse> getValidateCredentialsMethod;
    if ((getValidateCredentialsMethod = validateCredentialsServiceGrpc.getValidateCredentialsMethod) == null) {
      synchronized (validateCredentialsServiceGrpc.class) {
        if ((getValidateCredentialsMethod = validateCredentialsServiceGrpc.getValidateCredentialsMethod) == null) {
          validateCredentialsServiceGrpc.getValidateCredentialsMethod = getValidateCredentialsMethod = 
              io.grpc.MethodDescriptor.<org.bankatm.grpc.validateCredentialsRequest, org.bankatm.grpc.validateCredentialsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "org.bankatm.grpc.validateCredentialsService", "validateCredentials"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.bankatm.grpc.validateCredentialsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.bankatm.grpc.validateCredentialsResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new validateCredentialsServiceMethodDescriptorSupplier("validateCredentials"))
                  .build();
          }
        }
     }
     return getValidateCredentialsMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static validateCredentialsServiceStub newStub(io.grpc.Channel channel) {
    return new validateCredentialsServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static validateCredentialsServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new validateCredentialsServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static validateCredentialsServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new validateCredentialsServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class validateCredentialsServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void validateCredentials(org.bankatm.grpc.validateCredentialsRequest request,
        io.grpc.stub.StreamObserver<org.bankatm.grpc.validateCredentialsResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getValidateCredentialsMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getValidateCredentialsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.bankatm.grpc.validateCredentialsRequest,
                org.bankatm.grpc.validateCredentialsResponse>(
                  this, METHODID_VALIDATE_CREDENTIALS)))
          .build();
    }
  }

  /**
   */
  public static final class validateCredentialsServiceStub extends io.grpc.stub.AbstractStub<validateCredentialsServiceStub> {
    private validateCredentialsServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private validateCredentialsServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected validateCredentialsServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new validateCredentialsServiceStub(channel, callOptions);
    }

    /**
     */
    public void validateCredentials(org.bankatm.grpc.validateCredentialsRequest request,
        io.grpc.stub.StreamObserver<org.bankatm.grpc.validateCredentialsResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getValidateCredentialsMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class validateCredentialsServiceBlockingStub extends io.grpc.stub.AbstractStub<validateCredentialsServiceBlockingStub> {
    private validateCredentialsServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private validateCredentialsServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected validateCredentialsServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new validateCredentialsServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public org.bankatm.grpc.validateCredentialsResponse validateCredentials(org.bankatm.grpc.validateCredentialsRequest request) {
      return blockingUnaryCall(
          getChannel(), getValidateCredentialsMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class validateCredentialsServiceFutureStub extends io.grpc.stub.AbstractStub<validateCredentialsServiceFutureStub> {
    private validateCredentialsServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private validateCredentialsServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected validateCredentialsServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new validateCredentialsServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.bankatm.grpc.validateCredentialsResponse> validateCredentials(
        org.bankatm.grpc.validateCredentialsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getValidateCredentialsMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_VALIDATE_CREDENTIALS = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final validateCredentialsServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(validateCredentialsServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_VALIDATE_CREDENTIALS:
          serviceImpl.validateCredentials((org.bankatm.grpc.validateCredentialsRequest) request,
              (io.grpc.stub.StreamObserver<org.bankatm.grpc.validateCredentialsResponse>) responseObserver);
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

  private static abstract class validateCredentialsServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    validateCredentialsServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return org.bankatm.grpc.Server.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("validateCredentialsService");
    }
  }

  private static final class validateCredentialsServiceFileDescriptorSupplier
      extends validateCredentialsServiceBaseDescriptorSupplier {
    validateCredentialsServiceFileDescriptorSupplier() {}
  }

  private static final class validateCredentialsServiceMethodDescriptorSupplier
      extends validateCredentialsServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    validateCredentialsServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (validateCredentialsServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new validateCredentialsServiceFileDescriptorSupplier())
              .addMethod(getValidateCredentialsMethod())
              .build();
        }
      }
    }
    return result;
  }
}
