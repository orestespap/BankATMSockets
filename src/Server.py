import pymongo
import traceback
import zerorpc
from Services import Services

if __name__ == '__main__':
    uri=""    
    try:
        mongoClient=pymongo.MongoClient(uri)
        clientsCollection=mongoClient.bankDB.clients
    except:
        traceback.print_exc()

    print("Successfully connected to database ...\nLaunching zeroRPC server ...")
    bankServices=Services(clientsCollection)
    server = zerorpc.Server(bankServices)
    server.bind("tcp://127.0.0.1:9999")
    server.run()
    mongoClient.close()