package server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import server.beans.TodoBean;

public class TaskManager {

	public TaskManager() {

	}

	/* 특정 계정의 특정 월의 할일이 등록되어 있는 날짜 리스트 가져오기 */
	public String getTodoDateCtl(String clientData) {
		DataAccessObject dao = new DataAccessObject();
//		System.out.println(clientData);
		//serviceCode=9&accessCode=changyong&date=202211&status=null&isEnable=null&isAll=1
		// 1. clientData : serviceCode=9&accessCode=hoonzzang&date=202210 --> Bean Data
		// 2. todo --> Dao.getToDoList --> ArrayList<ToDoBean>
//		System.out.println(this.convertServerData(dao.getToDoList((TodoBean) this.setBean(clientData))));
		return this.convertServerData(dao.getToDoList((TodoBean) this.setBean(clientData)));
	}
	
	public String getTodoListCtl(String clientData) {
		DataAccessObject dao = new DataAccessObject();
		// 1. clientData : serviceCode=12&accessCode=changyong&startDate=12&endDate=12&status=0&isEnable=0&isAll=1
		// 2. todo --> Dao.getToDoList --> ArrayList<ToDoBean>
		
//		System.out.println(clientData);
//		System.out.println(dao.getList((TodoBean)this.setBean(clientData)).size());
		return this.convertListData(dao.getList((TodoBean)this.setBean(clientData)));
	}
	
	public String getModifyListCtl(String clientData) {
		DataAccessObject dao = new DataAccessObject();
		//1. clientData : serviceCode=13&id=changyong&startDate=202211010000&endDate=202211120000
		
		//202211011100,202211301000,쓰레기통테스트,1,0,null;202211021100,202211051000,코딩하기,1,1,null;
		return this.convertListData(dao.getList((TodoBean)this.setBean(clientData)));
	}
	
	private String convertListData(ArrayList<TodoBean> list) {
		StringBuffer serverData = new StringBuffer();
		
		for(TodoBean todo : list) {
			serverData.append(todo.getStartDate() + ",");
			serverData.append(todo.getEndDate() + ",");
			serverData.append(todo.getContents() + ",");
			serverData.append(todo.getStatus() + ",");
			serverData.append(todo.getIsEnable() + ",");
			serverData.append(todo.getComment() + ";");
		}
		
		/* 마지막으로 추가된 항목 삭제 */
		if(serverData.length() != 0){
			serverData.charAt((serverData.length()-1));
		}
		
//		System.out.println(serverData.toString());
		return serverData.toString();
	}

	private String convertServerData(ArrayList<TodoBean> list) {
		int count = 0;
		StringBuffer serverData = new StringBuffer();
		/* 1:10:15:16:20: --> 1:10:15:16:20 */
		for (TodoBean todo : list) {
			if(count != 0) serverData.append(":");
			serverData.append(todo.getStartDate().substring(6, 8));
			count++;
		}

		/* 마지막으로 추가된 항목 삭제 */
		if(serverData.length() != 0){
			serverData.charAt((serverData.length()-1));
		}else {
			
		}
		
		
		/* 중복값 제거 */
		// 배열 준비
		String[] arr = serverData.toString().split(":");       
		HashSet <String> hashSet = new HashSet<>(Arrays.asList(arr));         
		// HashSet을 배열로 변환        
		String[] resultArr = hashSet.toArray(new String[0]);
		StringBuffer str = new StringBuffer();
		
		for(int i=0;i<resultArr.length;i++) {
			if(i!=0) str.append(":");
			str.append(resultArr[i]);
		}
		
		return str.toString();
	}

	private Object setBean(String clientData) {
//		System.out.println(clientData);
		Object object = null;
		String[] splitData = clientData.split("&");
		switch (splitData[0].split("=")[1]) {
		case "9":
			/* serviceCode=9&accessCode=changyong&date=202210 */
			//serviceCode=9&accessCode=changyong&date=202211&status=null&isEnable=null&isAll=1
			object = new TodoBean();
			((TodoBean) object).setFileIndex(2);
			((TodoBean) object).setServiceCode("9");
			((TodoBean) object).setAccessCode(splitData[1].split("=")[1]);
			((TodoBean) object).setStartDate(splitData[2].split("=")[1]);
			((TodoBean) object).setStatus(splitData[3].split("=")[1]);
			((TodoBean) object).setIsEnable(splitData[4].split("=")[1]);
			((TodoBean) object).setIsAll(splitData[5].split("=")[1].equals("1")?true:false);
			
//			System.out.println(((TodoBean) object).isAll());
			break;
			
		case "12":
			//serviceCode=12&accessCode=changyong&startDate=2022101&endDate=20221031&status=1&isEnable=1&isAll=1
			object = new TodoBean();
			((TodoBean) object).setFileIndex(2);
			((TodoBean) object).setServiceCode("12");
			((TodoBean) object).setAccessCode(splitData[1].split("=")[1]);
			((TodoBean) object).setStartDate(splitData[2].split("=")[1]);
			((TodoBean) object).setEndDate(splitData[3].split("=")[1]);
			((TodoBean) object).setStatus(splitData[4].split("=")[1]);
			((TodoBean) object).setIsEnable(splitData[5].split("=")[1]);
			((TodoBean) object).setIsAll((splitData[6].split("=")[1].equals("1")?true:false));
			break;
			
		case "13":
			//serviceCode=13&id=changyong&startDate=202211010000&endDate=202211120000
			object = new TodoBean();
			((TodoBean) object).setFileIndex(2);
			((TodoBean) object).setServiceCode("13");
			((TodoBean) object).setAccessCode(splitData[1].split("=")[1]);
			((TodoBean) object).setStartDate(splitData[2].split("=")[1].substring(0, 8));
			((TodoBean) object).setEndDate(splitData[3].split("=")[1].substring(0, 8));
		}

		return object;
	}
}