package database;

import datatype.ICloneable;

import java.util.HashMap;

public class DataBase<T> {
    final HashMap<String, T> data;
    private final ICsvUpdater<T> csvUpdater;    // 데이터 타입 클래스를 csv 표의 한 행으로 출력 할 수 있다면 updataCSV()도 추상화 가능
    private final ISelector<T> selector;

    @SuppressWarnings("unchecked")
    DataBase(HashMap<String, T> data, ICsvUpdater<T> csvUpdater, boolean isCloneable) {
        this.data = data;
        this.csvUpdater = csvUpdater;

        // 직접 ICloneable을 implement 하여 clone을 정의한 클래스의 경우 깊은 복사, 이외에는 얕은 복사를 수행
        // selector 변수는 selectOrNull() 메소드에서 사용된다.
        if (isCloneable) {
            this.selector = (String uniqueKey) -> {
                if (data.containsKey(uniqueKey))
                    return ((ICloneable<T>) data.get(uniqueKey)).clone();
                return null;
            };
        } else {
            this.selector = (String uniqueKey) -> {
                if (data.containsKey(uniqueKey))
                    return data.get(uniqueKey);
                return null;
            };
        }
    }

    public T selectOrNull(String uniqueKey) {
        return selector.select(uniqueKey);
    }

    public boolean hasKey(String uniqueKey) {
        return data.containsKey(uniqueKey);
    }

//  public boolean updateElement(String uniqueKey, String elementCode, String element) {
//      return false;
//  }

    public boolean add(String uniqueKey, T t) {
        assert uniqueKey != null;
        assert t != null;

        if (data.containsKey(uniqueKey)) {
            return false;
        }

        data.put(uniqueKey, t);
        return true;
    }

    public boolean delete(String uniqueKey) {
        if (!data.containsKey(uniqueKey)) {
            return false;
        }

        data.remove(uniqueKey);
        csvUpdater.updateCSV(data);
        return true;
    }

    /* function pointer interface */
    private interface ISelector<T> {
        T select(String uniqueKey);
    }

    interface ICsvUpdater<T> {
        void updateCSV(HashMap<String, T> data);
    }
}
