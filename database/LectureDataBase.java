package database;

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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

public class LectureDataBase extends DataBase {
    private final String RESOURCE_PATH = "../resources/Lecture_List.csv";
    private static LectureDataBase instance;

    private HashMap<String, Lecture> data = new HashMap<>();

    private LectureDataBase() {
        super();

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


                AjouinDataBase personDb = AjouinDataBase.getInstance();
                String professorName = personDb.selectOrNull(professorId).getName();
                SubjectDataBase subjectDb = SubjectDataBase.getInstance();
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
        if (!data.containsKey(uniqueKey)) {
            return false;
        }

        data.remove(uniqueKey);
        updateCSV();
        return true;
    }

    private void updateCSV() {
        try {
            File file = new File("src/resources/Lecture_List_Update.csv"); // TODO: 실행파일 제작 후 이 경로가 유효한지 확인 필요
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.getPath()), "UTF8"));

            output.write("LECTURE\n");
            output.write("강의고유번호,담당교수ID,과목코드,학점,여석,강의실,강의시간\n");

            for (Lecture lecture : data.values()) {
                output.write(lecture.getLectureId() + ',');
                output.write(lecture.getProfessorId() + ',');
                output.write(lecture.getSubject().getCode() + ',');
                output.write(Integer.toString(lecture.getCredit()) + ',');
                output.write(Integer.toString(lecture.getMaxEnrolCount()) + ',');
                output.write(lecture.getClassroom());

                ArrayList<ClassTime> classTimes = lecture.getClassTimes();
                for (ClassTime classTime : classTimes) {
                    output.write(',' + classTime.getDay().toString() + classTime.getStart() +',');
                    output.write(classTime.getDay().toString() + classTime.getEnd());
                }
                output.write('\n');
            }

            output.close();
        } catch(UnsupportedEncodingException uee) {
            uee.printStackTrace();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
