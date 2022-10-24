package client;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import server.BackEnd;

public class UserApp {

	public UserApp() {
		frontController();
	}

	private void frontController() {
		LocalDate now = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
		String today = now.format(formatter);

		BackEnd backEnd = new BackEnd();

		Scanner sc = new Scanner(System.in);
		String title = mainTitle(today);
		String loginInfo = new String();
		String id = new String();
		String pw = new String();
		String[][] menu = {
				{"Menu Selection","1. Task List","2. Task Settings","3. Modify Task","4. Task Stats","0. Disconnect"},
				{"Select List Menu","1. all condition","2. preparing","3. in progress","4. done","5. temporary deletion","0. 돌아가기"}
				};

		System.out.println(title);
		System.out.println(makeSignin());

		loginInfo = sc.nextLine();
		id = loginInfo.split("\t")[0];
		pw = loginInfo.split("\t")[1];
		connecting();
		display(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Successful Connection!\n");

		display(title);
		display("\n1. Task List		2. Task Settings\r\n" + "3. Modify Task		4. Task Stats\r\n" + "0. Disconnect\n");

//		System.out.println("아이디는 " + id);
//		System.out.println("비밀번호는 " + pw);

//		backEnd.makeCalendar(2022, 10);

		sc.close();

	}

	private String userInput(Scanner sc) {
		return sc.nextLine();
	}

	private void display(String str) {
		System.out.print(str);
	}
	
	//ID, PW 존재여부를 받아 text 출력
	private String loginResult(boolean loginCk) {
		if(loginCk)
			return ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Successful Connection!\n";
		else
			return ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Connection failed!\n";
	}

	private String mainTitle(String today) {
		StringBuffer title = new StringBuffer();
		title.append("------------------------------------------------------\n\n");
		title.append("\t[현우네 To-do List]\n\n");
		title.append("\t\t\t\t");
		title.append(today+"\n");
		title.append("\t\t\t\tdesigned by Changyong\n\n");
		title.append("------------------------------------------------------\n");
		return title.toString();
	}

	private String makeSignin() {
		StringBuffer signin = new StringBuffer();
//		signin.append("------------------------------------------------------\n");
		signin.append("[아이디]\t\t[비밀번호]\n");
		return signin.toString();
	}

	private void connecting() {
		System.out.print("connecting");
		for (int i = 0; i < 15; i++) {
			System.out.print("...");
			
			try {
				Thread.sleep(390);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println();
	}
}