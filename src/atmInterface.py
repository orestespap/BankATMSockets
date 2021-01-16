from time import sleep
import requests
import traceback

def withdraw(username,authHeader):
	while True:
		try:
			amount=int(input("Type in the amount you wish to withdraw or 0 to cancel: "))
		except ValueError:
			continue

		if amount==0:
			break

		if amount>0 and (amount%20==0 or amount%50==0):
			try:
				response=postRequest(base+"withdraw",{"username":username,"amount":amount},headers=authHeader)
			except Exception as e:
				print("Bank server is down. Please try again later.")
				#traceback.print_exc() //for testing purposes
				return -1

			if response.status_code==200:
				print("Amount withdrawn successfully ...")
				viewBalace(username,authHeader)
				return 1
			elif response.status_code==400:
				print("Withdrawal failed. Possible reasons for failure:\n1.)Bank server is down\n2.)The amount you've requested is greater than your current account balance\n3.)You've exceeded your daily withdrawal limit\n4.)This ATM's funds aren't enough\nPlease try again later.")
				return -1
				sleep(5)
			elif response.status_code==401:
				return 0
			else:
				print("Bank server is down. Please try again later.")
				return -1
			break
		
		print("Invalid input. Input must either be 0 or a positive integer that is a multiple of 20 or 50.")	

def deposit(username,authHeader):
	
	while True:
		try:
			amount=int(input("Type in the amount you wish to deposit or 0 to cancel: "))
		except ValueError:
			continue

		if amount==0:
			break

		if amount%5==0:
			try:
				response=postRequest(base+"deposit",{"username":username,"amount":amount},headers=authHeader)
			except Exception as e:
				print("Bank server is down. Please try again later.")
				#traceback.print_exc() //for testing purposes
				return -1

			if response.status_code==200:
				print("Amount deposited successfully ...")
				viewBalace(username,authHeader)
				return 1
			elif response.status_code==401:
				return 0
			else:
				print("Bank server is down. Please try again later.") 
				return -1
			break
		print("Invalid input. Input must either be 0 or a positive integer that is a multiple of 5.")


def viewBalace(username,authHeader):
	try:
		response=getRequest(base+f"accountBalance/{username}",headers=authHeader)
		if response.status_code==401:
			return 0
		print(f"Account balance: {response.json()['message']}€")
		sleep(5)
		return 1
	except Exception as e:
		print("Bank server is down. Please try again later.")
		return -1
		#traceback.print_exc() #for testing purposes

def homepage(username,token):
	try:
		authHeader={"auth":token}
		response=getRequest(base+f"fullName/{username}",headers=authHeader)
		if response.status_code==401:
			print("Session expired. Ejecting card ...")
			return 0
	except Exception as e:
		print("Bank server is down. Please try again later.")
		#traceback.print_exc() #for testing purposes
		return -1

	optionsDict={1:viewBalace,2:deposit,3:withdraw}
	print(f"Welcome {response.json()['message']}")
	menu="---OPTIONS---\n1.)Account balance\n2.)Deposit\n3.)Withdraw\n4.)Log out"

	while True :
		print(menu)
		try:
			ans=int(input("Input (1-4): "))
		except ValueError:
			continue;

		if ans>4 or ans<1:
			continue;

		if ans==4:
			return logOut()

		if not optionsDict[ans](username,authHeader): #calls appropriate menu function
			print("Session expired. Ejecting card ...")
			return 0

def logIn():
	
	maxAttempts, attempts = 3,0
	
	while attempts<maxAttempts:
		print(f"ATM login page. Attempts remaining {maxAttempts-attempts}\nType in EXIT to remove card")
		username=input("Username: ")
		
		if username.lower()=="exit":
			print("Bye!")
			exit()
		
		password=input("Password: ")

		try:
			response=postRequest(base+"validation",{"username":username,"password":password})
		except Exception as e:
			print("Bank server is down. Please try again later.")
			traceback.print_exc() #for testing purposes

		if response.status_code==200:
			token=response.json()["message"]
			attempts=homepage(username,token)
		else:
			attempts+=1
			print("Invalid credentials. Please try again.")

def logOut():
	print("Logging you out ... Bye!")
	return 0

if __name__ == '__main__':
	postRequest=requests.post
	getRequest=requests.get
	base="http://127.0.0.1:5000/"
	logIn()