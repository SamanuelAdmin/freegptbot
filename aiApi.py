'''

 Created by: Samanuel Admin

 NOTE: you will be able to create your own AI API server using
 'Server' class. But your own class need to has 'generate' func.

'''


import g4f
from datetime import datetime
import socket
import threading
import re


SERVER_IP = '127.0.0.1' # you can also use 0.0.0.0
SERVER_CHATGPT_PORT = 63131 

# DONT FORGET TO CHANGE IT IN JAVA CODE IF YOU TOUCHED THESE


PROVIDERS = [
	g4f.Provider.FreeGpt,
	g4f.Provider.GptForLove,
	g4f.Provider.GptGo,
	g4f.Provider.Hashnode,
	g4f.Provider.GPTalk,
]


class Server(socket.socket):
	def __init__(self, SERVER_IP, SERVER_PORT):
		self.SERVER_IP = SERVER_IP
		self.SERVER_PORT = SERVER_PORT

		self.mainSocket = socket.socket()
		self.mainSocket.bind((self.SERVER_IP, self.SERVER_PORT))
		self.mainSocket.listen()

		threading.Thread(target=self.gettingClientThread, daemon=True).start()


	def __del__(self):
		self.mainSocket.close()

	def gettingClientThread(self):
		print(f'Handing clients on server {self.SERVER_IP}:{self.SERVER_PORT}')

		while True:
			client, clientIp = self.mainSocket.accept()

			threading.Thread(
				target=self.clientFunc, 
				args=(client, clientIp),
				daemon=True
			).start()


	def clientFunc(self, client, clientIp):
		print(f'[{self.SERVER_IP}:{self.SERVER_PORT}] Client {clientIp} has been connect.')

		try: 
			request = client.recv(102400).decode('utf-8')

			if request != '' and request: 
				print(f'[{self.SERVER_IP}:{self.SERVER_PORT}] Gotten from {clientIp} message [{len(request)}].')

				message = self.generate(request)
				client.send(message.encode())
				print(f'[{self.SERVER_IP}:{self.SERVER_PORT}] Sended an answer to {clientIp} [{len(message)}]')
		except Exception as error:
			print(f'{clientIp}: {error}')
			return 0
		else: 
			client.close()

	def request(self, message): return "Pass"



class ChatGPTServer(Server):
	currentProvider = None

	def __init__(self):
		self.setActiveChatGPTProvider()
		Server.__init__(self, SERVER_IP, SERVER_CHATGPT_PORT)

		print(f'ChatGPT server has been started on {self.SERVER_IP}:{self.SERVER_PORT}')


	def setActiveChatGPTProvider(self):
		for provider in PROVIDERS:
			try:
				g4f.ChatCompletion.create(
					model="gpt-3.5-turbo",
					messages=[{'role': 'user', 'content': 'hi'}],
					provider=provider,
					stream=True,
				)

				self.currentProvider = provider
			except: 
				pass

	def generate(self, request):
		response = g4f.ChatCompletion.create(
			model="gpt-3.5-turbo",
			messages=[
				{'role': 'user', 'content': request}
			],
			# stream=True,
			provider=g4f.Provider.ChatBase
		)

		return response


def main():
	chatGPT = ChatGPTServer()

	while True: pass


if __name__ == '__main__':
	try: main()
	except KeyboardInterrupt: pass