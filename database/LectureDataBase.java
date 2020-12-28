package database;

import datatype.Ajouin;
import datatype.ClassTime;
import datatype.EDay;
import datatype.Lecture;
import datatype.Subject;

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

public class LectureDataBase {
    private static DataBase<Lecture> instance;

    private LectureDataBase() { }

    private static HashMap<String, Lecture> initializeData() {
        String RESOURCE_PATH = "../resources/Lecture_List.csv";
        HashMap<String, Lecture> data = new HashMap<>();

        BufferedReader reader;
        try {
            InputStream iStream = LectureDataBase.class.getResourceAsStream(RESOURCE_PATH);
            InputStreamReader iReader = new InputStreamReader(iStream, "UTF-8");
            reader = new BufferedReader(iReader);

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
                assert (columns.length - 6) % 2 == 0: "Lecture dataBase initialize fail: Cannot initialize class time!";
                ArrayList<ClassTime> classTimes = new ArrayList<>();

                for (int i = 6; i < columns.length; i += 2) {
                    // 시작, 끝 요일이 동일한지 확인
                    assert columns[i].charAt(0) == columns[i+1].charAt(0): "Lecture dataBase initialize fail: Cannot initialize class time!";;

                    char day = columns[i].charAt(0);
                    int startOfClass = Integer.parseInt(columns[i].substring(1));
                    int endOfClass = Integer.parseInt(columns[i+1].substring(1));
                    classTimes.add(new ClassTime(EDay.getEnumFrom(day), startOfClass, endOfClass));
                }

                DataBase<Ajouin> ajouinDb = AjouinDataBase.getDB();
                String professorName = ajouinDb.selectOrNull(professorId).getName();
                DataBase<Subject> subjectDb = SubjectDataBase.getDB();
                Subject sb = subjectDb.selectOrNull(subjectCode);
                assert sb != null: subjectCode;

                Lecture lecture = new Lecture(uniqueKey, professorId, professorName, sb, credit, maxEnrolCount, classroom, classTimes);

                data.put(uniqueKey, lecture);

                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    public static DataBase<Lecture> getDB() {
        if (instance == null) {
            HashMap<String, Lecture> data = initializeData();
            instance = new DataBase<>(data, LectureDataBase::updateCSV, true);
        }
        return instance;
    }

    private static void updateCSV(HashMap<String, Lecture> data) {
        try {
            File file = new File("src/resources/Lecture_List.csv"); // TODO: 실행파일 제작 후 이 경로가 유효한지 확인 필요
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.getPath()), "UTF8"));

            output.write("LECTURE\n");
            output.write("강의고유번호,담당교수ID,과목코드,학점,여석,강의실,강의시간\n");

            for (Lecture lecture : data.values()) {
                output.write(lecture.getLectureId() + ',');
                output.write(lecture.getProfessorId() + ',');
                output.write(lecture.getSubject().getCode() + ',');
                output.write(Integer.toString(lecture.getCredit()) + ',');
                output.write(Integer.toString(lecture.getSeatsLimit()) + ',');
                output.write(lecture.getClassroom());

                ArrayList<ClassTime> classTimes = lecture.getClassTimes();
                for (ClassTime classTime : classTimes) {
                    output.write(',' + classTime.getDay().toString() + classTime.getStart() +',');
                    output.write(classTime.getDay().toString() + classTime.getEnd());
                }
                output.write('\n');
            }

            output.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    // 강의가 현재 등록된 강의들과 시간대와 장소가 겹치는 검증한다.
    public static boolean canAdd(Lecture lecture) {
        if (instance == null) {
            getDB();
        }

        for (Lecture lec : instance.data.values()) {
            if (lec.isOverlapClassroom(lecture)) {
                return false;
            }
        }
        return true;
    }
}
