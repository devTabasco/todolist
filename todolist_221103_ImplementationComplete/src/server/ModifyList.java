package server;

import java.util.ArrayList;

import server.beans.AccessHistoryBean;
import server.beans.TodoBean;

public class ModifyList {
	public ModifyList() {

	}

	public boolean modifyRequest(String clientData) { // serviceCode=1&id=changyong&password=1234
//		System.out.println(clientData);
		boolean result = false;
		ArrayList<TodoBean> todoBeans;

		// clientData 분리 후
		// serviceCode=14&originData=changyong,202211021100,202211051000,코딩하기,1,1,null&contents=정현우
		// serviceCode=14&originData=changyong,202211021100,202211051000,코딩하기,1,1,null&date=20221103,20221108
		String[] splitData = clientData.split("&");
		String[] origin = splitData[1].split("=")[1].split(","); //changyong,202211021100,202211051000,코딩하기,1,1,null
		String[] modifyMenu = splitData[2].split("=");
		TodoBean todoBean = new TodoBean();
		DataAccessObject dao = new DataAccessObject();
		todoBean.setAccessCode(origin[0]);
		todoBean.setStartDate(origin[1]);
		todoBean.setEndDate(origin[2]);
		todoBean.setContents(origin[3]);
		todoBean.setStatus(origin[4]);
		todoBean.setIsEnable(origin[5]);
		todoBean.setComment(origin[6]);
		todoBean.setFileIndex(2);

		todoBeans = dao.readTodoData(todoBean.getFileIndex());

		for (TodoBean listData : todoBeans) {
			if (!todoBean.getAccessCode().equals(listData.getAccessCode()))
				continue;
			if (!todoBean.getStartDate().equals(listData.getStartDate()))
				continue;
			if (!todoBean.getEndDate().equals(listData.getEndDate()))
				continue;
			if (!todoBean.getContents().equals(listData.getContents()))
				continue;
			if (!todoBean.getStatus().equals(listData.getStatus()))
				continue;
			if (!todoBean.getIsEnable().equals(listData.getIsEnable()))
				continue;
			if (!todoBean.getComment().equals(listData.getComment()))
				continue;

			if(modifyMenu[0].equals("contents")) {
				listData.setContents(modifyMenu[1]);
			}else if(modifyMenu[0].equals("date")) {
				listData.setStartDate(modifyMenu[1].split(",")[0] + "0000");
				listData.setEndDate(modifyMenu[1].split(",")[1] + "0000");			
			}else if(modifyMenu[0].equals("status")) {
				listData.setStatus(modifyMenu[1]);
			}else if(modifyMenu[0].equals("comments")) {
				listData.setComment(modifyMenu[1]);
			}else if(modifyMenu[0].equals("isEnAble")) {
				if(listData.getIsEnable().equals("1")) listData.setIsEnable("0");
				if(listData.getIsEnable().equals("0")) listData.setIsEnable("1");
			}
			// 내용이 모두 같으면!
			//
			// todoBeans에 수정된 내용의 새로운 데이터 추가
			// todoBeans에 기존 빈 삭제
			// 그리고 마지막에 todoBeans를 서버에 리턴
		}

//		result = dao.writeModitiedTodoList(todoBeans);

		// clientData에 원본 + 수정내역을 보내서 원본 비교 후 수정내역으로 해당 데이터를 수정해서 다시 write하기
		result = dao.writeModitiedTodoList(todoBeans);
		return result;
	}
}
