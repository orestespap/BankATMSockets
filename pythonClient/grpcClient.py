import grpc
import server_pb2_grpc as pb2_grpc
import server_pb2 as pb2


class Client():

    def __init__(self):
        self.host = 'localhost'
        self.server_port = 9999

        # instantiate a channel
        self.channel = grpc.insecure_channel(f'{self.host}:{self.server_port}')
    
    def validateCredentials(self, username, password):
        # bind the client and the server
        self.stub = pb2_grpc.validateCredentialsServiceStub(self.channel)
        
        request = pb2.validateCredentialsRequest(username=username, password=password)
        return self.stub.validateCredentials(request)

    def getAccountBalance(self,username):
        self.stub = pb2_grpc.getAccountBalanceServiceStub(self.channel)

        request = pb2.getAccountBalanceRequest(username=username)
        return self.stub.getAccountBalance(request)

    def deposit(self,username,amount):
        self.stub = pb2_grpc.depositServiceStub(self.channel)

        request = pb2.depositRequest(username=username,amount=amount)
        return self.stub.deposit(request)
    
    def withdraw(self,username,amount):
        self.stub = pb2_grpc.withdrawServiceStub(self.channel)

        request = pb2.withdrawRequest(username=username,amount=amount)
        return self.stub.withdraw(request)


if __name__ == '__main__':
    #python -m grpc_tools.protoc -I. --python_out=. --grpc_python_out=. server.proto
    print("grpcClient.py module")