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
import java.util.ArrayList;
import java.util.HashMap;

public class SubjectDataBase {
    private static DataBase<Subject> instance;

    private SubjectDataBase() { }  // hide

    private static HashMap<String, Subject> initializeData() {
        String RESOURCE_PATH = "../resources/Subject_List.csv";
        HashMap<String, Subject> data = new HashMap<>();

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

        return data;
    }

    public static DataBase<Subject> getDB() {
        if (instance == null) {
            HashMap<String, Subject> data = initializeData();
            instance = new DataBase<>(data, SubjectDataBase::updateCSV, true);
        }
        return instance;
    }

    private static void updateCSV(HashMap<String, Subject> data) {
        try {
            File file = new File("src/resources/Subject_List.csv");
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
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
