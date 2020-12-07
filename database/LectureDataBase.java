package database;

import datatype.Ajouin;
import datatype.ClassTime;
import datatype.EDay;
import datatype.EDepartment;
import datatype.Lecture;
import datatype.Professor;
import datatype.Student;
import datatype.Subject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class LectureDataBase extends DataBase {
    private final String RESOURCE_PATH = "../resources/Lecture_List.csv";
    private static LectureDataBase instance;

    private HashMap<String, Lecture> data = new HashMap<>();
    private HashMap<String, String> passWords = new HashMap<>();

    private LectureDataBase() {
        super();

        BufferedReader reader;
        try {
            InputStream iStream = LectureDataBase.class.getResourceAsStream(RESOURCE_PATH);
            InputStreamReader iReader = new InputStreamReader(iStream, "UTF-16");
            reader = new BufferedReader(iReader);//new FileReader(RESOURCE_PATH));

            String line = reader.readLine();

            assert line != null: "Lecture dataBase initialize fail: wrong file";
            assert line.equals("LECTURE"): "Lecture dataBase initialize fail: Invalid file signature";

            reader.readLine();          // just column signature
            line = reader.readLine();
            while (line != null) {
                String[] columns = line.split(",");

                String uniqueKey = columns[0];
                String professorId = columns[1];
                String subjectCode = columns[2];
                int credit = Integer.parseInt(columns[3]);
                int maxEnrolCount = Integer.parseInt(columns[4]);
                String classroom = columns[5];

                // 수업 시간은 반드시 (시작,끝) 짝수개로 나타나야 한다.
                assert (columns.length - 5) % 2 == 0: "Lecture dataBase initialize fail: Cannot initialize class time!";
                ArrayList<ClassTime> classTimes = new ArrayList<>();

                for (int i = 6; i < columns.length; i += 2) {
                    // 시작, 끝 요일이 동일한지 확인
                    assert columns[i].charAt(0) == columns[i+1].charAt(0): "Lecture dataBase initialize fail: Cannot initialize class time!";;

                    char day = columns[i].charAt(0);
                    int startOfClass = Integer.parseInt(columns[i].substring(1));
                    int endOfClass = Integer.parseInt(columns[i+1].substring(1));
                    classTimes.add(new ClassTime(EDay.getEnumFrom(day), startOfClass, endOfClass));
                }


                AjouinDataBase personDb = AjouinDataBase.getInstance();
                String professorName = personDb.selectOrNull(professorId).getName();
                //SubjectDataBase subjectDb = SubjectDataBase.getInstance();
                // Subject sb = subjectDb.selectOrNull(subjectCode);

                Lecture lecture = new Lecture(uniqueKey, professorId, professorName, new Subject(), credit, maxEnrolCount, classroom, classTimes);

                data.put(uniqueKey, lecture);

                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static LectureDataBase getInstance() {
        if (instance == null) {
            instance = new LectureDataBase();
        }
        return instance;
    }

    public Lecture selectOrNull(String uniqueKey) {
        if (data.containsKey(uniqueKey)) {
            return (Lecture) data.get(uniqueKey).clone();
        }
        return null;
    }

    @Override
    public boolean hasKey(String uniqueKey) {
        return data.containsKey(uniqueKey);
    }

    @Override
    public boolean delete(String uniqueKey) {
        // TODO: 실제 csv 파일에서도 삭제

        if (!data.containsKey(uniqueKey)) {
            return false;
        }

        data.remove(uniqueKey);
        return true;
    }

}
