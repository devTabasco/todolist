package client;

import java.time.LocalDate;

import java.time.format.DateTimeFormatter;

import server.ServerController;

public class TaskManagement {

	private ServerController serverController;
	
	public TaskManagement() {

	}
	
	public Object taskController(String clientData) {
		System.out.println(clientData);
		Object result = null;
		switch (clientData.split("&")[0].split("=")[1]) {
		case "12":
			result = this.getTaskListCtl(clientData);
			break;
		case "13":
			result = this.getTaskListCtl(clientData);
		}
		
		return result;
	}

	public Object taskController(int selection, String accessCode, int addMonth, String menuSelection) {
		Object result = null;
		switch (selection) {
		case 11:
			result = this.makeTaskCalendarCtl(accessCode, addMonth, menuSelection);
			break;
		}
		
		return result;
	}

	/* 특정 달의 Task Calendar 생성하기 */
	private Object makeTaskCalendarCtl(String accessCode, int addMonth, String menuSelection) {
		// getTaskData
		serverController = new ServerController();
		String pattern = "yyyyMM";
		LocalDate today = LocalDate.now().plusMonths(addMonth);
		String[] taskDays = this.getTaskDays(serverController.controller("serviceCode=9&accessCode=" + accessCode
				+ "&date=" + today.format(DateTimeFormatter.ofPattern(pattern)) + "&" + menuSelection));

//		System.out.println(serverController.controller("serviceCode=9&accessCode=" + accessCode + "&date="
//				+ LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern))));

		return this.makeCalendar(taskDays, today);
	}

	/* 등록된 모든 할일 리스트 가져오기 */
	private Object getTaskListCtl(String clientData) {
		//clientData : serviceCode=12&accessCode=changyong&startDate=20221012&endDate=20221012&status=0&isEnable=0&isAll=1
		serverController = new ServerController();
		if(clientData.split("&")[0].split("=")[1].equals("12")) {
			return this.makeListData(serverController.controller(clientData).split(";"),clientData);
		}else if(clientData.split("&")[0].split("=")[1].equals("13")) {
			//serviceCode=13&id=changyong&startDate=202211010000&endDate=202211120000
			return this.makeListData(serverController.controller(clientData).split(";"),clientData);
		}else {
			return null;
		}
	}
	
	private String makeListData(String[] record, String clientData) {
			StringBuffer todoList = new StringBuffer();
			String tmp;
			/*
			 * ----------------------------------------------
					22.10.21	-  22.10.30
				
				  1. [in progress][날짜]
				      할일 1
				  2. [done][날짜]
				      할일 2
				  3. [preparing][날짜]
				      할일 3
				  4. [in progress][날짜]
				      할일 4
				  0. 돌아가기
				----------------------------------------------
			 * */
			
			for(int i = 0; i<record.length; i++) {
				if(i != record.length-1) {
					for(int j=i+1;j<record.length;j++) {
						if(Integer.parseInt(record[i].substring(0, 8)) > Integer.parseInt(record[j].substring(0, 8))) {
							tmp = record[i];
							record[i] = record[j];
							record[j] = tmp;
						}
					}
				}
			}
			//startDate,endDate,contents,status,isEnable,comment
			todoList.append("----------------------------------------------\n");
			todoList.append("\t\t"+clientData.split("&")[2].split("=")[1]+"  -  "+clientData.split("&")[3].split("=")[1]+"\n\n");
			for(int i = 0;i<record.length;i++) {
				String[] todo = record[i].split(",");
				todoList.append("\t"+(i+1)+". "+"["+convertStatus(todo[3])+"]"+" ["+ todo[0].substring(0,8) + " - " + todo[1].substring(0,8) +"]\n");
				todoList.append("\t\t"+todo[2]+"\n");
			}
			todoList.append("\t0.돌아가기\n");
			todoList.append("-------------------------수정할 일정 번호를 선택해주세요 : ");
			for(int i=0;i<record.length;i++) {
				todoList.append(";");
				todoList.append(record[i]);
			}
			return todoList.toString();
	}
	
	private String convertStatus(String status) {
		//0 : 진행예정, 1 : 진행중, -1 : 완료
		if(status.equals("0")) {
			return "Preparing";
		}else if(status.equals("1")) {
			return "In progress";
		}else {
			return "Done";
		}
	}

	/* 할일 등록 하기 */
	private void TaskRegistrationCtl(String accessCode) {

	}

	/* 등록된 할일 수정하기 */
	private void setModifyTaskCtl(String accessCode) {

	}

	/* 할일에 대한 통계 만들기 */
	private void getTaskStatCtl(String accessCode) {

	}

	/* 특정 달의 할일이 등록되어있는 날짜가져오기 */
	private String[] getTaskDays(String str) {
		String[] taskDays = null;
		taskDays = str.split(":");

		return taskDays;
	}

	/* 특정 달의 할일이 등록되어있는 날짜를 특정 달의 달력에 표시하기 */
	private String makeCalendar(String[] days, LocalDate today) {
		StringBuffer calendar = new StringBuffer();
		int firstCheck = 1;
		int dayOfWeek = LocalDate.of(today.getYear(), today.getMonthValue(), 1).getDayOfWeek().getValue();
		int lastDay = today.lengthOfMonth();
		String pattern = "yyyy. MM";

		dayOfWeek = (dayOfWeek == 7) ? 1 : dayOfWeek + 1;

		calendar.append("\n\t+++++++++++ Previous  [ " + today.format(DateTimeFormatter.ofPattern(pattern))
				+ " ]  Next +++++++++++\n");
		calendar.append("\tSUN\tMON\tTUE\tWED\tTHU\tFRI\tSAT\n");
		
		for (int dayIdx = 1; dayIdx <= lastDay; dayIdx++) {

				if (firstCheck == 1) {
					for (int i = 0; i < dayOfWeek-1; i++) {
						calendar.append("\t");
					}
				} 
				calendar.append("\t");
				calendar.append((dayIdx < 10 ? " " : "") + dayIdx);
				try {
					if(days[0] != "") {
						for(int i = 0;i<days.length;i++) {
							if(dayIdx == Integer.parseInt(days[i])) {
								calendar.append("+");
							}
							
						}
						
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			calendar.append(dayOfWeek % 7 == 0 ? "\n" : "");

			if (firstCheck == lastDay) {
				calendar.append("\n");
			}

			dayOfWeek++;
			firstCheck++;

		}

//		calendar.append("\t+++++++++++++++++++++++++++++++++++++ Start Date : ");

		return calendar.toString();
	}
}