package database;

import datatype.EClassification;
import datatype.Subject;
import datatype.EDepartment;

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

public class SubjectDataBase extends DataBase {
    private final String RESOURCE_PATH = "../resources/Subject_List.csv";
    private static SubjectDataBase instance;

    private HashMap<String, Subject> data = new HashMap<>();

    private SubjectDataBase() {
        super();

        BufferedReader reader;
        try {
            InputStream iStream = SubjectDataBase.class.getResourceAsStream(RESOURCE_PATH);
            InputStreamReader iReader = new InputStreamReader(iStream, "UTF-8");
            reader = new BufferedReader(iReader);

            String line = reader.readLine();

            assert line != null: "Subject dataBase initialize fail: wrong file";
            assert line.equals("SUBJECT"): "Subject dataBase initialize fail: Invalid file signature";

            reader.readLine();          // just column signature
            line = reader.readLine();
            while (line != null) {
                String[] columns = line.split(",");
                String subjectName = columns[0];
                String uniqueKey = columns[1];
                EClassification classification = EClassification.getEnumFrom(columns[2]);

                EDepartment[] majorDepartments = new EDepartment[columns.length - 3];
                for (int i = 3; i < columns.length; ++i) {
                    majorDepartments[i - 3] = EDepartment.getEnumFrom(columns[i]);
                }

                Subject subject = null;
                if (classification == EClassification.ELECTIVE) {
                    subject = new Subject(subjectName, uniqueKey, classification);
                } else if (classification == EClassification.CORE) {
                    subject = new Subject(subjectName, uniqueKey, classification, majorDepartments);
                } else {
                    assert false: "Subject dataBase initialize fail: Invalid classification";;
                }

                data.put(uniqueKey, subject);

                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SubjectDataBase getInstance() {
        if (instance == null) {
            instance = new SubjectDataBase();
        }
        return instance;
    }

    // 템플릿 프로그래밍으로 추상화 할 수 없까?
    public Subject selectOrNull(String uniqueKey) {
        if (data.containsKey(uniqueKey)) {
            return (Subject) data.get(uniqueKey).clone();
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
            File file = new File("src/resources/Subject_List_Update.csv"); // TODO: 실행파일 제작 후 이 경로가 유효한지 확인 필요
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.getPath()), "UTF8"));

            output.write("SUBJECT\n");
            output.write("과목명,과목코드,교과구분,해당학과\n");

            for (Subject subject : data.values()) {
                output.write(subject.getName() + ',');
                output.write(subject.getCode() + ',');
                output.write(subject.getClassification().toString() + ',');

                ArrayList<EDepartment> majors = subject.getMajorDepartments();
                for (EDepartment major : majors) {
                    output.write(major.getCode());
                    if (major != majors.get(majors.size() - 1)) {
                        output.write(',');
                    }
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
