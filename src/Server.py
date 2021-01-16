import pymongo
import traceback
from Services import Services
from flask import Flask, request, redirect
from flask_restful import Api, Resource, reqparse, abort
from Services import Services
import time
import jwt
import datetime
from functools import wraps

base="http://127.0.0.1:5000/"
uri=""    
try:
    mongoClient=pymongo.MongoClient(uri)
    clientsCollection=mongoClient.bankDB.clients
except:
    traceback.print_exc()
print("Successfully connected to database ...\nLaunching flask restful server ...")

bankServices=Services(clientsCollection)

app= Flask(__name__)
api=Api(app)
app.config["KEY"]="tokenKey"


def tokenVerification(func):
    @wraps(func)
    def inner(*args,**kwargs):
        try:
            jwt.decode(request.headers["auth"],app.config["KEY"],algorithms=["HS256"],verify_exp=True)
        except Exception as e:
            #PyJWT raised token expired exception, or something else has occured
            abort(401)

        return func(*args,**kwargs)
    return inner


class Validation(Resource):
    myService=bankServices.validateCredentials
    parser=reqparse.RequestParser()
    parser.add_argument("username",type=str, help="Client's username value needed",required=True)
    parser.add_argument("password",type=str, help="Client's password value needed",required=True)
    
    def post(self):
        requestArgs=Validation.parser.parse_args()
        result=Validation.myService(requestArgs["username"],requestArgs["password"])
        if not result:
            abort(401) #authentication failed
        else:
            token = jwt.encode({"user": requestArgs["username"], "exp":datetime.datetime.utcnow()+datetime.timedelta(minutes=4)}, app.config["KEY"], algorithm="HS256")
            return {"message":token}


class Fullname(Resource):
    myService=bankServices.getName

    @tokenVerification
    def get(self,username):
        result=Fullname.myService(username)
        if not result:
            abort(404)
        return {"message":result}

class AccountBalance(Resource):
    myService=bankServices.getAccountBalance
    @tokenVerification
    def get(self,username):
        result=AccountBalance.myService(username)
        if not result:
            abort(404)
        return {"message":result}

class Deposit(Resource):
    myService=bankServices.deposit
    parser=reqparse.RequestParser()
    parser.add_argument("username",type=str, help="Client's username value needed",required=True)
    parser.add_argument("amount",type=int, help="Client's withdrawal amount value needed",required=True)

    @tokenVerification
    def post(self):
        requestArgs=Deposit.parser.parse_args()
        result=Deposit.myService(requestArgs["username"],requestArgs["amount"])
        if not result[0]:
            abort(result[1])
      
class Withdraw(Resource):
    myService=bankServices.withdraw
    parser=reqparse.RequestParser()
    parser.add_argument("username",type=str, help="Client's username value needed",required=True)
    parser.add_argument("amount",type=int, help="Client's withdrawal amount value needed",required=True)

    @tokenVerification
    def post(self):
        requestArgs=Withdraw.parser.parse_args()
        result=Withdraw.myService(requestArgs["username"],requestArgs["amount"])
        if not result[0]:
            abort(result[1])


if __name__ == '__main__':
    api.add_resource(Validation,"/validation")
    api.add_resource(Fullname,"/fullName/<string:username>")
    api.add_resource(AccountBalance,"/accountBalance/<string:username>")
    api.add_resource(Deposit,"/deposit")
    api.add_resource(Withdraw,"/withdraw")

    app.run(debug=True)
    print("HTTP Server has been shut down ... Shutting down mongo connection and exiting ...")
    mongoClient.close()