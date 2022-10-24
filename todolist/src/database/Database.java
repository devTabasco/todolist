package database;

import java.io.*;

public class Database {
	
	public Database() {
		
	}

	private void readCsv() {
		float[][] database = new float[2880][6];
		// CSV 파일을 읽고 저장할 배열 선언 , arraylist나 벡터 등의 다른 곳에 저장해도 상관없음

		try
		{
			// csv 데이터 파일
			File csv = new File("C:\\폴더 1\\폴더 2\\파일이름.csv");
			BufferedReader br = new BufferedReader(new FileReader(csv));
			String line = "";
			int row = 0, i;

			while ((line = br.readLine()) != null) {
				// -1 옵션은 마지막 "," 이후 빈 공백도 읽기 위한 옵션
				String[] token = line.split(",", -1);
				for (i = 0; i < 6; i++) {
					database[row][i] = Float.parseFloat(token[i]);
				}

				// CSV에서 읽어 배열에 옮긴 자료 확인하기 위한 출력
				for (i = 0; i < 6; i++) {
					System.out.print(database[row][i] + ",");
				}
				System.out.println("");

				row++;
			}
			br.close();

		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		
	    catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	

}
