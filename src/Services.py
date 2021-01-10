import traceback
import datetime
import time

class Services():
    

    def __init__(self,clientsCollection):
        self.clientsCollection=clientsCollection
        self.maxWithdrawalAmount=2000
    
    def validateCredentials(self, username, password):
        clientDocument=self.clientsCollection.find_one({"username":username})
        
        if clientDocument and clientDocument["password"]==password:
            self.logTransaction(username,"loginsLog")
            return True
        return False

    def getName(self,username):
        clientDocument=self.clientsCollection.find_one({"username":username})
        return clientDocument["firstName"]+" "+clientDocument["lastName"] if clientDocument else ""

    def getAccountBalance(self,username):
        clientDocument=self.clientsCollection.find_one({"username":username})
        return str(clientDocument["balance"]) if clientDocument else ""

    def deposit(self,username,amount):
        try:
            self.clientsCollection.find_one_and_update({'username': username},{'$inc': {'balance': amount}})
            self.logTransaction(username,"depositsLog",amount)
        except:
            #traceback.print_exc()
            return False
        return True
    
    def withdraw(self,username,amount):
        clientDocument=self.clientsCollection.find_one({"username":username})
        
        if amount>self.maxWithdrawalAmount or amount>clientDocument["balance"]:
            myLock.release()
            return False
        else:
            self.clientsCollection.find_one_and_update({'username': username},{'$inc': {'balance': -amount}})
            dateNow=datetime.datetime.now().date()
            logKey=f"{dateNow.day}{dateNow.month}{dateNow.year}"
            clientWithdrawalsToday=clientDocument["withdrawalsLog"].get(logKey)
            
            if not clientWithdrawalsToday:
                self.logTransaction(username,"withdrawalsLog",amount)
                return True
            else:
                total=sum(aSubDocument["amount"] for aSubDocument in clientWithdrawalsToday)
                if total+amount>self.maxWithdrawalAmount:
                    return False
                self.logTransaction(username,"withdrawalsLog",amount)
                return True

    def logTransaction(self,username,logName,amount=None):
        timeNow=datetime.datetime.now()
        dateNow=datetime.datetime.now().date()
        logKey=f"{dateNow.day}{dateNow.month}{dateNow.year}"
        if amount:
            self.clientsCollection.find_one_and_update({'username': username},{'$push': {f'{logName}.{logKey}': {"hour":timeNow.hour, "minute":timeNow.minute,"second":timeNow.second, "amount":amount}}})
        else:
            #logIns log -> no amount
            self.clientsCollection.find_one_and_update({'username': username},{'$push': {f'{logName}.{logKey}': {"hour":timeNow.hour, "minute":timeNow.minute,"second":timeNow.second}}})

if __name__ == '__main__':
    pass