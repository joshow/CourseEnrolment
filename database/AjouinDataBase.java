package database;

import datatype.Ajouin;
import datatype.EDepartment;
import datatype.Professor;
import datatype.Student;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class AjouinDataBase extends DataBase {
    private final String RESOURCE_PATH = "../resources/Ajouin_List.csv";
    private static AjouinDataBase instance;

    private HashMap<String, Ajouin> data = new HashMap<>();
    private HashMap<String, String> passwords = new HashMap<>();

    private AjouinDataBase() {
        super();

        BufferedReader reader;
        try {
            InputStream iStream = AjouinDataBase.class.getResourceAsStream(RESOURCE_PATH);
            InputStreamReader iReader = new InputStreamReader(iStream, "UTF-8");
            reader = new BufferedReader(iReader);//new FileReader(RESOURCE_PATH));

            String line = reader.readLine();

            assert line != null: "Ajouin dataBase initialize fail: wrong file";
            System.out.println(line);
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
    }

    public static AjouinDataBase getInstance() {
        if (instance == null) {
            instance = new AjouinDataBase();
        }
        return instance;
    }

    // 템플릿 프로그래밍으로 추상화 할 수 없까?
    public Ajouin selectOrNull(String uniqueKey) {
        if (data.containsKey(uniqueKey)) {
            return (Ajouin) data.get(uniqueKey).clone();
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

    public boolean isValidLoginInfo(String id, String pw) {
        if (passwords.containsKey(id)) {
            if (passwords.get(id).equals(pw)) {
                return true;
            }
            return false;
        }
        return false;
    }
}
