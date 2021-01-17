from time import sleep
from suds.client import Client
#import traceback

def withdraw(client,username):
	while True:
		try:
			amount=int(input("Type in the amount you wish to withdraw or 0 to cancel: "))
		except ValueError:
			continue

		if amount==0:
			break

		if amount>0 and (amount%20==0 or amount%50==0):
			try:
				response=client.service.withdraw(username,amount)
			except Exception as e:
				print("Bank server is down. Please try again later.")
				#traceback.print_exc() //for testing purposes
				break

			if response:
				print("Amount withdrawn successfully ...")
				viewBalace(client,username)
			else:
				print("Withdrawal failed. Possible reasons for failure:\n1.)Bank server is down\n2.)The amount you've requested is greater than your current account balance\n3.)You've exceeded your daily withdrawal limit\n4.)This ATM's funds aren't enough\nPlease try again later.")
				sleep(5)
			break
		
		print("Invalid input. Input must either be 0 or a positive integer that is a multiple of 20 or 50.")	

def deposit(client,username):
	
	while True:
		try:
			amount=int(input("Type in the amount you wish to deposit or 0 to cancel: "))
		except ValueError:
			continue

		if amount==0:
			break

		if amount%5==0:
			try:
				response=client.service.deposit(username,amount)
			except Exception as e:
				print("Bank server is down. Please try again later.")
				#traceback.print_exc() //for testing purposes
				break

			if response:
				print("Amount deposited successfully ...")
				viewBalace(client,username)
			else:
				print("Deposit failed. Please try again later.")
				sleep(5)
			break
		print("Invalid input. Input must either be 0 or a positive integer that is a multiple of 5.")


def viewBalace(client,username):
	try:
		response=client.service.getAccountBalance(username)
		print(f"Account balance: {response}€")
		sleep(5)
	except Exception as e:
		print("Bank server is down. Please try again later.")
		#traceback.print_exc() #for testing purposes


def logOut():
	print("Logging you out ... Bye!")
	return 0

def homepage(client,username):
	 optionsDict={1:viewBalace,2:deposit,3:withdraw}
	 name=client.service.getName(username)
	 print(f"Welcome {name}")
	 menu="---OPTIONS---\n1.)Account balance\n2.)Deposit\n3.)Withdraw\n4.)Log out"

	 while True:
	 	print(menu)
	 	try:
	 		ans=int(input("Input (1-4): "))
	 	except ValueError:
	 		continue;

	 	if ans>4 or ans<1:
	 		continue;

	 	if ans==4:
	 		return logOut()
	 	
	 	optionsDict[ans](client,username) #calls appropriate menu function

def logIn():
	client = Client('http://localhost:8000/?wsdl')

	maxAttempts, attempts = 3,0
	
	while attempts<maxAttempts:
		print(f"ATM login page. Attempts remaining {maxAttempts-attempts}\nType in EXIT to remove card")
		username=input("Username: ")
		
		if username.lower()=="exit":
			print("Bye!")
			exit()
		
		password=input("Password: ")

		try:
			response=client.service.validateCredentials(username,password)
		except:
			print("Bank server is down. Please try again later. Exiting.")
			exit()

		if response:
			attempts=homepage(client,username)
		else:
			attempts+=1
			print("Invalid credentials. Please try again.")

if __name__ == '__main__':
	logIn()