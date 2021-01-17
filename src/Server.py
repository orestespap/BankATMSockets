#import traceback
import datetime
import time
import pymongo
from spyne import Application, ServiceBase, Iterable, Integer, Unicode
from spyne.decorator import srpc

from spyne.protocol.soap import Soap11
from spyne.server.wsgi import WsgiApplication
import logging

from wsgiref.simple_server import make_server

class Services(ServiceBase):
    uri=""    
    try:
        mongoClient=pymongo.MongoClient(uri)
        clientsCollection=mongoClient.bankDB.clients
    except:
        traceback.print_exc()

    maxWithdrawalAmount=2000
    
    @srpc(Unicode, Unicode, _returns=bool)
    def validateCredentials(username, password):
        clientDocument=Services.clientsCollection.find_one({"username":username})
        
        if clientDocument and clientDocument["password"]==password:
            Services.logTransaction(username,"loginsLog")
            return True
        return False

    @srpc(Unicode, _returns=Unicode)
    def getName(username):
        clientDocument=Services.clientsCollection.find_one({"username":username})
        return clientDocument["firstName"]+" "+clientDocument["lastName"] if clientDocument else ""

    @srpc(Unicode, _returns=Unicode)
    def getAccountBalance(username):
        clientDocument=Services.clientsCollection.find_one({"username":username})
        return str(clientDocument["balance"]) if clientDocument else ""

    @srpc(Unicode, Integer, _returns=bool)
    def deposit(username,amount):
        try:
            Services.clientsCollection.find_one_and_update({'username': username},{'$inc': {'balance': amount}})
            Services.logTransaction(username,"depositsLog",amount)
        except:
            #traceback.print_exc()
            return False
        return True
    
    @srpc(Unicode, Integer, _returns=bool)
    def withdraw(username,amount):
        clientDocument=Services.clientsCollection.find_one({"username":username})
        if not clientDocument:
            return False

        try:
        
            if amount>Services.maxWithdrawalAmount or amount>clientDocument["balance"]:
                return False
            else:
                Services.clientsCollection.find_one_and_update({'username': username},{'$inc': {'balance': -amount}})
                dateNow=datetime.datetime.now().date()
                logKey=f"{dateNow.day}{dateNow.month}{dateNow.year}"
                clientWithdrawalsToday=clientDocument["withdrawalsLog"].get(logKey)
                
                if not clientWithdrawalsToday:
                    Services.logTransaction(username,"withdrawalsLog",amount)
                    return True
                else:
                    total=sum(aSubDocument["amount"] for aSubDocument in clientWithdrawalsToday)
                    if total+amount>Services.maxWithdrawalAmount:
                        return False
                    Services.logTransaction(username,"withdrawalsLog",amount)
                    return True
        except:
            #traceback.print_exc()
            return False

    def logTransaction(username,logName,amount=None):
        timeNow=datetime.datetime.now()
        dateNow=timeNow.date()
        logKey=f"{dateNow.day}{dateNow.month}{dateNow.year}"
        if amount:
            Services.clientsCollection.find_one_and_update({'username': username},{'$push': {f'{logName}.{logKey}': {"hour":timeNow.hour, "minute":timeNow.minute,"second":timeNow.second, "amount":amount}}})
        else:
            #logIns log -> no amount parameter
            Services.clientsCollection.find_one_and_update({'username': username},{'$push': {f'{logName}.{logKey}': {"hour":timeNow.hour, "minute":timeNow.minute,"second":timeNow.second}}})


application = Application([Services], 'bankservices.soap',
                          in_protocol=Soap11(validator='lxml'),
                          out_protocol=Soap11())
wsgi_application = WsgiApplication(application) #wsgi webserver, single threaded no need for locks in the withdraw service


if __name__ == '__main__':
    # logging.basicConfig(level=logging.DEBUG)
    # logging.getLogger('spyne.protocol.xml').setLevel(logging.DEBUG)

    # logging.info("listening to http://127.0.0.1:8000")
    # logging.info("wsdl is at: http://localhost:8000/?wsdl") #for testing purposes

    server = make_server('127.0.0.1', 8000, wsgi_application)
    server.serve_forever()