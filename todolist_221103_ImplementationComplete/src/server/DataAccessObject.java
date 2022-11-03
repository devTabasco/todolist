package server;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

import server.beans.AccessHistoryBean;
import server.beans.MemberBean;
import server.beans.TodoBean;

//To access database
public class DataAccessObject {
	private String[] fileInfo = { "C:\\java\\data\\todolist\\src\\database\\MEMBERS.txt",
			"C:\\java\\data\\todolist\\src\\database\\ACCESSHISTORY.txt",
			"C:\\java\\data\\todolist\\src\\database\\TODO.txt" };

	public DataAccessObject() {

	}
	
	public ArrayList<TodoBean> readTodoData(int fileIndex) {
		TodoBean todoBean;
		ArrayList<TodoBean> todoList = null;
		BufferedReader buffer = null;
		String line = null;
		
		try {
			buffer = new BufferedReader(new FileReader(new File(fileInfo[fileIndex])));
			todoList = new ArrayList<TodoBean>();

			while ((line = buffer.readLine()) != null) {
				//changyong,202210271100,202210271100,코딩하기,1,1,null
				// split
				String[] tmp = line.split(",");
				todoBean = new TodoBean();

				// bean가져와서 데이터 넣기
				todoBean.setAccessCode(tmp[0]);
				todoBean.setStartDate(tmp[1]);
				todoBean.setEndDate(tmp[2]);
				todoBean.setContents(tmp[3]);
				todoBean.setStatus(tmp[4]);
				todoBean.setIsEnable(tmp[5]);
				todoBean.setComment(tmp[6]);

				// ArrayList에 new MemberBean()의 주소를 저장
				todoList.add(todoBean);

			}
			// heap에 참조선이 연결된 부분은 예외처리 시 null로 초기화시켜 heap데이터를 제거해주어야 함.
		} catch (FileNotFoundException e) {
//		System.out.println("파일이 존재하지 않습니다.");
			e.printStackTrace();
		} catch (IOException e) {
//		System.out.println("파일을 읽을 수 없습니다.");
			todoList = null; // 참조선 제거
			e.printStackTrace();
		} finally {
			try {
				buffer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return todoList;
	}

	// member파일 전달
	public ArrayList<MemberBean> readDatabase(int fileIndex) {
		MemberBean memberBean;
		ArrayList<MemberBean> memberList = null;
		BufferedReader buffer = null;
		String line = null;

		if (fileIndex == 0) {
			try {
				// csv 데이터 파일

				buffer = new BufferedReader(new FileReader(new File(fileInfo[fileIndex])));
				memberList = new ArrayList<MemberBean>();

				while ((line = buffer.readLine()) != null) {
					// changyong,1234,창용,01012345678,1
					// split
					String[] tmp = line.split(",");
					memberBean = new MemberBean();

					// bean가져와서 데이터 넣기
					memberBean.setAccessCode(tmp[0]);
					memberBean.setSecretCode(tmp[1]);
					memberBean.setName(tmp[2]);
					memberBean.setPhoneNumber(tmp[3]);
					memberBean.setActivation(Integer.parseInt(tmp[4]));

					// ArrayList에 new MemberBean()의 주소를 저장
					memberList.add(memberBean);

				}
				// heap에 참조선이 연결된 부분은 예외처리 시 null로 초기화시켜 heap데이터를 제거해주어야 함.
			} catch (FileNotFoundException e) {
//			System.out.println("파일이 존재하지 않습니다.");
				e.printStackTrace();
			} catch (IOException e) {
//			System.out.println("파일을 읽을 수 없습니다.");
				memberList = null; // 참조선 제거
				e.printStackTrace();
			} finally {
				try {
					buffer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else if (fileIndex == 2) {
			// read todo list
		}

		return memberList;
	}

	// getTodoList
	public ArrayList<TodoBean> getToDoList(TodoBean searchInfo) { // fileIndex, accessCode, StartDate(날짜 전부 계산해서) 넘겨받음
		ArrayList<TodoBean> dayList = null;
		TodoBean toDo = null;
		String line;
		BufferedReader buffer = null;
		int date, recordCount = 1;
		int[] dateRange = new int[2];

		LocalDate userDate = LocalDate.of(Integer.parseInt(searchInfo.getStartDate().substring(0, 4)),
				Integer.parseInt(searchInfo.getStartDate().substring(4)), 1);

		try {
			buffer = new BufferedReader(new FileReader(new File(fileInfo[searchInfo.getFileIndex()])));
			while ((line = buffer.readLine()) != null) {
				if (recordCount == 1)
					dayList = new ArrayList<TodoBean>();

				String[] record = line.split(",");
				/* 계정별로 추려오기 */
				if (!searchInfo.getAccessCode().equals(record[0]))
					continue;

//				System.out.println(searchInfo.isAll());
//				System.out.println(!searchInfo.getIsEnable().equals("0"));

//				if(!searchInfo.getIsEnable().equals(record[5])) continue;
//				if(!searchInfo.getIsEnable().equals("0")) {
//					if(!searchInfo.isAll()) { // All condition 확인
//						if(!searchInfo.getStatus().equals(record[4])) continue;
//					}
//				}

				if (searchInfo.getServiceCode().equals("9")) {
					switch (searchInfo.getIsEnable()) {
					case "0": // 휴지통
						if (!record[5].equals("0"))
							continue;
						break;

					case "1": // 활성
						if (record[5].equals("0"))
							continue;
						if (!(searchInfo.isAll())) {
							if (!searchInfo.getStatus().equals(record[4]))
								continue;
						}
					}
				}

				/* 계정별로 추려오기 */
				date = Integer.parseInt(searchInfo.getStartDate());
				dateRange[0] = Integer.parseInt(record[1].substring(0, 8));
				dateRange[1] = Integer.parseInt(record[2].substring(0, 8));

				if (date > dateRange[0] / 100)
					dateRange[0] = Integer.parseInt(date + "01");
				if (date < dateRange[1] / 100)
					dateRange[1] = Integer.parseInt(date + "" + userDate.lengthOfMonth());

				for (int idx = dateRange[0]; idx <= dateRange[1]; idx++) {

					toDo = new TodoBean();
					toDo.setStartDate(idx + "");
					dayList.add(toDo);
				}
				recordCount++;

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (buffer != null)
					buffer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return dayList;
	}

	// 특정 기간의 할일 목록 가져오기
	public ArrayList<TodoBean> getList(TodoBean searchInfo) {
		// 수정(13)의 경우
		// serviceCode=13&id=changyong&startDate=202211010000&endDate=202211120000
		ArrayList<TodoBean> todoList = null;
		String line;
		String[] record;
		BufferedReader reader = null;
		TodoBean todoBean;
		int count = 1;
		try {
			reader = new BufferedReader(new FileReader(new File(fileInfo[searchInfo.getFileIndex()])));

			while ((line = reader.readLine()) != null) {
				if (count == 1)
					todoList = new ArrayList<TodoBean>();
				record = line.split(",");

				// 조건 적용 1. accessCode 2.startDate & endDate 3.visibleType
				if (!searchInfo.getAccessCode().equals(record[0])) {
					continue;
				}
				if (!this.isCheckRange(record[1].substring(0, 8), record[2].substring(0, 8), searchInfo.getStartDate(),
						searchInfo.getEndDate())) {
					continue;
				}

				if (searchInfo.getServiceCode().equals("12")) {
					switch (searchInfo.getIsEnable()) {
					case "0": // 휴지통
						if (!record[5].equals("0"))
							continue;
						break;

					case "1": // 활성
						if (record[5].equals("0"))
							continue;
						if (!(searchInfo.isAll())) {
							if (!searchInfo.getStatus().equals(record[4]))
								continue;
						}
					default:

						break;
					}
				}
				// serviceCode=13&id=changyong&startDate=202211010000&endDate=202211120000
				// 데이터 수집
				todoBean = new TodoBean();
				todoBean.setStartDate(record[1]);
				todoBean.setEndDate(record[2]);
				todoBean.setContents(record[3]);
				todoBean.setStatus(record[4]);
				todoBean.setIsEnable(record[5]);
				todoBean.setComment(record[6]);

				todoList.add(todoBean);

				count++;
			}

		} catch (FileNotFoundException e) {
			System.out.println("파일 없음");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("입출력 오류");
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return todoList;
	}

	private boolean isCheckRange(String startDay, String endDay, String compareStartDay, String compareEndDay) {
		int check = 0;

		for (int i = Integer.parseInt(startDay); i <= Integer.parseInt(endDay); i++) {
			if (Integer.parseInt(compareStartDay) == i) {
				check++;
			}
			if (Integer.parseInt(compareEndDay) == i) {
				check++;
			}
			if (Integer.parseInt(compareStartDay) < Integer.parseInt(startDay)
					&& Integer.parseInt(compareEndDay) > Integer.parseInt(endDay)) {
				check++;
			}

		}

		return check >= 1;
	}

	// 접속기록 작성
	public boolean writeAccessHistory(AccessHistoryBean accessInfo) {
		boolean result = false;
		BufferedWriter bufferedWriter = null;

		try {
			bufferedWriter = new BufferedWriter(new FileWriter(new File(fileInfo[accessInfo.getFileIndex()]), true));// changyong,20221026151037,1
			bufferedWriter.write(accessInfo.getAccessCode() + "," + accessInfo.getAccessDate() + ","
					+ accessInfo.getAccessType() + "\n");
			bufferedWriter.flush(); // write로 담은 내용 출력 후, 버퍼를 비움.

			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bufferedWriter.close(); // bufferedWriter close.
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	public boolean writeTodoList(TodoBean accessInfo) {
		boolean result = false;
		BufferedWriter bufferedWriter = null;

		try {
			bufferedWriter = new BufferedWriter(new FileWriter(new File(fileInfo[accessInfo.getFileIndex()]), true));// changyong,20221026151037,1
			bufferedWriter.write(accessInfo.getAccessCode() + "," + accessInfo.getStartDate() + ","
					+ accessInfo.getEndDate() + "," + accessInfo.getContents() + "," + accessInfo.getStatus() + ","
					+ accessInfo.getIsEnable() + "," + accessInfo.getComment() + "\n");
			bufferedWriter.flush(); // write로 담은 내용 출력 후, 버퍼를 비움.

			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bufferedWriter.close(); // bufferedWriter close.
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return result;
	}
	
	public boolean writeModitiedTodoList(ArrayList<TodoBean> todoBeans) {
		boolean result = false;
		BufferedWriter bufferedWriter = null;

		try {
			bufferedWriter = new BufferedWriter(new FileWriter(new File(fileInfo[2])));
			
			for(TodoBean todoBean : todoBeans) {
				bufferedWriter.write(todoBean.getAccessCode() + "," + todoBean.getStartDate() + ","
						+ todoBean.getEndDate() + "," + todoBean.getContents() + "," + todoBean.getStatus() + ","
						+ todoBean.getIsEnable() + "," + todoBean.getComment() + "\n");
			}
			bufferedWriter.flush(); // write로 담은 내용 출력 후, 버퍼를 비움.

			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bufferedWriter.close(); // bufferedWriter close.
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

}