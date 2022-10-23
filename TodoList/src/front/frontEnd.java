package front;

import java.util.Scanner;

public class frontEnd {

	public frontEnd() {
		controller();
	}
	
	public void controller() {
		Scanner sc = new Scanner(System.in);
		String loginInfo = new String();
		String id = new String();
		String pw = new String();
		
		String title = makeTitle();
		System.out.println(title);
		System.out.println(makeSignin());
		
		loginInfo = sc.nextLine();
		id = loginInfo.split("\t")[0];
		pw = loginInfo.split("\t")[1];
		connecting();
		
		System.out.println("아이디는 " + id);
		System.out.println("비밀번호는 " + pw);
		
	}
	
	private String makeTitle() {
		StringBuffer title = new StringBuffer();
		title.append("------------------------------------------------------\n\n");
		title.append("\t[현우네 To-do List]\n\n");
		title.append("\t\t\t\t2022. 10. 21\n");
		title.append("\t\t\t\tdesigned by Changyong\n\n");        
		title.append("------------------------------------------------------");
		return title.toString();
	}
	
	private String makeSignin() {
		StringBuffer signin = new StringBuffer();
		signin.append("++++++++++++++++++++++++++++++++++++++++++++++++++++++\n\n");
		signin.append("[아이디]\t\t[비밀번호]\n");
		return signin.toString();
	}
	
	private void connecting() {
		StringBuffer str = new StringBuffer();
		str.append("*");
		str.append("connecting...");
		for(int i = 0; i<20; i++) {
			str.insert(1, "*");
			System.out.println(str);
			
			try {
	            Thread.sleep(200);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
		}
	}
	
//	
//
//	[아이디]		[비밀번호]
//
//	_hyunwoo	_zzang
//
//++++++++++++++++++++++++++++connecting...
//>>>>>>>>>>>>>>>>>>>>>>Succesful Connection
//>>>>>>>>>>>>>>>>>>>>>>Connection failed
//___________________________________Retry(y / n) ?

}
