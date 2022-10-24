package client;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class UserApp {
	
	public UserApp() {
		frontController();
	}
	
	/* 클라이언트 화면 및 데이터 흐름 제어 */
	private void frontController() {
		Scanner scanner = new Scanner(System.in);
		String mainTitle = this.mainTitle(this.getToday(true));
		String mainMenu = this.getMainMenu();
		boolean isLoop = true;
		String[] accessInfo = new String[2];
		boolean accessResult;
		
		while(isLoop) {
			
			for(int idx=0;idx<accessInfo.length; idx++) {
				this.display(mainTitle);
				this.display(this.getAccessLayer(true, accessInfo[0]));
				accessInfo[idx] = this.userInput(scanner);
			}
			this.display(this.getAccessLayer(false, null)); //print connecting...
			
			/* 서버에 로그인 정보 전달 */
			
			/* 서버로부터 받은 로그인 결과에 따른 화면 출력 */
			accessResult = true; //server에서 true를 받았다고 가정
			this.display(this.accessResult(accessResult));
			if(!accessResult) {
				/* 로그인 실패 */
				//n 누르면 종료
				if(this.userInput(scanner).toUpperCase().equals("N")) {
					isLoop = false;
				}else { //n이 아닌 다른 문자 누르면 로그인정보 리셋
					accessInfo[0] = null;
					accessInfo[1] = null;
				}	
			}else {
				/* 로그인 성공 */
				while(isLoop) {
					String menuSelection = new String();
					this.display(mainTitle);
					this.display(mainMenu);
					menuSelection = this.userInput(scanner);
				
				   /* 0번 선택시 서버에 로그아웃 통보 후 프로그램 종료 */
					if(menuSelection.toUpperCase().equals("0")) { 
						isLoop = false;
					}
				}
			}
		}
		
		this.display("\n\n  x-x-x-x-x-x-x-x-x-x- 프로그램을 종료합니다 -x-x-x-x-x-x-x-x-x-x");	
		scanner.close();
	}
	
	/* 프로그램 메인 타이틀 제작 */
	private String mainTitle(String date) {
		StringBuffer title = new StringBuffer();
		
		title.append("\n\n\n");
		title.append("  __________________________________________________________\n\n");
		title.append("     ◀▷◀▷◀▷◀▷◀▷◀▷◀▷◀▷◀▷\n");
		title.append("          T A S K\n"); 
		title.append("          M A N A G E R                      " + date + "\n");
		title.append("     ◀▷◀▷◀▷◀▷◀▷◀▷◀▷◀▷◀▷        Designed by HoonZzang\n");
		title.append("  __________________________________________________________\n");

		return title.toString();
	}
	
	
	private String getAccessLayer(boolean isItem, String accessCode) {
		StringBuffer accessLayer = new StringBuffer();
		
		if(isItem) {
			accessLayer.append("\n");
			accessLayer.append("     Access ++++++++++++++++++++++++++++++++++++++++++\n");
			accessLayer.append("     +        AccessCode          SecretCode\n");
			accessLayer.append("     + --------------------------------------------\n");
			accessLayer.append("     +         " + ((accessCode!=null)? accessCode+"\t\t": ""));
		}else {
			accessLayer.append("     +++++++++++++++++++++++++++++++++++ Connecting...");
		}
	    return accessLayer.toString();
	}
	
	/* 서버응답에 따른 로그인 결과 출력 */
	private String accessResult(boolean isAccess) {
		StringBuffer accessResult = new StringBuffer();
		
		accessResult.append("\n     >>>>>>>>>>>>>>>>>>>>>>>>> ");
		if(isAccess) {
			accessResult.append("Successful Connection\n"); 
		    accessResult.append("     Move after 2 sceonds...");
		}else {
			accessResult.append("Connection Failed\n");
			accessResult.append("     _______________________________ Retry(y/n) ? ");
		}
		
		return accessResult.toString(); 
	}
	
	/* 메인페이지 출력 */
	private String getMainMenu() {
		StringBuffer mainPage = new StringBuffer();
		
		mainPage.append("\n");
		mainPage.append("     [ MENU SELECTION ] __________________________________\n\n");
		mainPage.append("       1. TASK LIST		2. TASK REGISTRATION\n");
		mainPage.append("       3. MODIFY TASK		4. TASK STATS\n");
		mainPage.append("       0. DISCONNECT    \n");
		mainPage.append("     ________________________________________________ : ");
		   
		return mainPage.toString();
	}
	
	/* 날짜시간 출력 : LocalDateTime Class + DateTimeFormatter Class */
	private String getToday(boolean isDate) {
		String pattern = (isDate)? "yyyy. MM. dd.": "yyyy-MM-dd HH:mm:ss";
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern)); 
	}
	/* 사용자 입력 */
	private String userInput(Scanner scanner) {
		return scanner.next();
	}
	/* 화면 출력 */
	private void display(String text) {
		System.out.print(text);
	}
}
