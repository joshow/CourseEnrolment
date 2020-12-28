package database;

import datatype.Ajouin;
import datatype.EDepartment;
import datatype.Professor;
import datatype.Student;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;

public class AjouinDataBase {
    private static DataBase<Ajouin> instance;
    private static final HashMap<String, String> passwords = new HashMap<>();

    private AjouinDataBase() { }

    private static HashMap<String, Ajouin> initializeData() {
        String RESOURCE_PATH = "../resources/Ajouin_List.csv";
        HashMap<String, Ajouin> data = new HashMap<>();
        BufferedReader reader;
        try {
            InputStream iStream = AjouinDataBase.class.getResourceAsStream(RESOURCE_PATH);
            InputStreamReader iReader = new InputStreamReader(iStream, "UTF-8");
            reader = new BufferedReader(iReader);

            String line = reader.readLine();

            assert line != null: "Ajouin dataBase initialize fail: wrong file";
            assert line.equals("AJOUIN"): "Ajouin dataBase initialize fail: Invalid file signature";

            reader.readLine();          // just column signature
            line = reader.readLine();
            while (line != null) {
                String[] columns = line.split(",");

                String uniqueKey = columns[0];
                assert !passwords.containsKey(uniqueKey): "Ajouin dataBase initialize fail: Duplicated unique key";
                passwords.put(uniqueKey, columns[1]);
                String name = columns[2];
                String department = columns[3];

                Ajouin ajouin = null;
                if (uniqueKey.length() == 8) {         // professor key
                    ajouin = new Professor(uniqueKey, name, EDepartment.getEnumFrom(department));
                }
                else if (uniqueKey.length() == 9) {    // student key
                    ajouin = new Student(uniqueKey, name, EDepartment.getEnumFrom(department));;
                } else {
                    assert false: "Ajouin dataBase initialize fail: Invalid uniqueKey";
                }

                data.put(uniqueKey, ajouin);

                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    public static DataBase<Ajouin> getDB() {
        if (instance == null) {
            HashMap<String, Ajouin> data = initializeData();
            instance = new DataBase<>(data, AjouinDataBase::updateCSV, true);
        }
        return instance;
    }

    public static boolean isValidLoginInfo(String id, String pw) {
        if (passwords.containsKey(id)) {
            return passwords.get(id).equals(pw);
        }
        return false;
    }

    private static void updateCSV(HashMap<String, Ajouin> data) {
        try {
            File file = new File("src/resources/Ajouin_List.csv"); // TODO: 실행파일 제작 후 이 경로가 유효한지 확인 필요
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.getPath()), "UTF8"));

            output.write("AJOUIN\n");
            output.write("고유번호,비밀번호,이름,학과\n");

            for (Ajouin ajouin : data.values()) {
                output.write(ajouin.getId() + ',');
                output.write(passwords.get(ajouin.getId()) + ',');
                output.write(ajouin.getName() + ',');
                output.write(ajouin.getMajor().getCode());
                output.write('\n');
            }

            output.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
