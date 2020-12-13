package database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class EnrolmentDataBase {
    private static final DataBase<ArrayList<String>>[] instances = new DataBase[3];
    private static final String[] csvPaths = new String[] {
            "../resources/Enrolment_Student_List.csv",
            "../resources/Enrolment_Professor_List.csv",
            "../resources/Enrolment_Lecture_List.csv"
    };
    private static final String[] csvSignatures = new String[] {
            "ENROLMENT_STUDENT",
            "ENROLMENT_PROFESSOR",
            "ENROLMENT_LECTURE"
    };
    private static final String[] csvRowTitles = new String[] {
            "학생고유번호,수강신청한강의코드...",
            "교수고유번호,등록한강의코드...",
            "강의고유번호,수강신청한학생고유번호..."
    };

    private static HashMap<String, ArrayList<String>> initializeData(int index) {
        String RESOURCE_PATH = csvPaths[index];
        HashMap<String, ArrayList<String>> data = new HashMap<>();
        BufferedReader reader;
        try {
            InputStream iStream = AjouinDataBase.class.getResourceAsStream(RESOURCE_PATH);
            InputStreamReader iReader = new InputStreamReader(iStream, "UTF-8");
            reader = new BufferedReader(iReader);

            String line = reader.readLine();

            assert line != null: "Enrolment dataBase initialize fail: wrong file";
            assert line.equals(csvSignatures[index]): "Enrolment dataBase initialize fail: Invalid file signature";

            reader.readLine();          // just column signature
            line = reader.readLine();
            while (line != null) {
                String[] columns = line.split(",");

                String uniqueKey = columns[0];
                ArrayList<String> members = new ArrayList<>();

                for (int i = 1; i < columns.length; ++i) {
                    members.add(columns[i]);
                }

                data.put(uniqueKey, members);

                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    static DataBase<ArrayList<String>> getDB(int index) {
        assert 0 <= index && index < instances.length;
        if (instances[index] == null) {
            HashMap<String, ArrayList<String>> data = initializeData(index);
            instances[index] = new DataBase<ArrayList<String>>(data, EnrolmentDataBase::updateCSV, false);
        }

        return instances[index];
    }

    private static void updateCSV(HashMap<String, ArrayList<String>> data) {
        assert data != null;

        int index = 0;
        for (; index < instances.length; ++index) {
            if (data == instances[index].data) {
                break;
            }
        }

        if (index >= instances.length) {
            return;
        }

        try {
            String path = "src" + csvPaths[index].substring(2, csvPaths[index].length() - 4) + "_Update.csv";
            File file = new File(path); // TODO: 실행파일 제작 후에도 경로가 유효한지 확인 필요
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.getPath()), "UTF8"));

            output.write(csvSignatures[index] + '\n');
            output.write(csvRowTitles[index] + '\n');

            for (String key : data.keySet()) {
                output.write(key);

                ArrayList<String> members = data.get(key);
                for (String member : members) {
                    output.write(',' + member);
                }

                output.write('\n');
            }

            output.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
