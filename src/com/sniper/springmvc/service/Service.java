package com.sniper.springmvc.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import com.sun.security.ntlm.Server;

public class Service {

	public void Server() throws UnknownHostException, IOException {
		ServerSocket serverSocket = new ServerSocket(7777);

		while (true) {
			Socket socket = serverSocket.accept();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			System.out.println("you input is : " + br.readLine());
		}
	}

	public static void main(String[] args) {
		try {
			Service service = new Service();
			service.Server();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
