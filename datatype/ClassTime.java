package datatype;/*
오전 8시 ~ 오후 10시 까지 30분 단위로 수업 시간을 지정할 수 있다.
유효 범위 [ 1~28 ]
    01 - 08:00 ~ 08:30
    02 - 08:30 ~ 09:00
    03 - 09:00 ~ 09:30
    04 - 09:30 ~ 10:00
    05 - 10:00 ~ 10:30
    06 - 10:30 ~ 11:00
    07 - 11:00 ~ 11:30
    08 - 11:30 ~ 12:00
    09 - 12:00 ~ 12:30
    10 - 12:30 ~ 13:00
    11 - 13:00 ~ 13:30
    12 - 13:30 ~ 14:00
    13 - 14:00 ~ 14:30
    14 - 14:30 ~ 15:00
    15 - 15:00 ~ 15:30
    16 - 15:30 ~ 16:00
    17 - 16:00 ~ 16:30
    18 - 16:30 ~ 17:00
    19 - 17:00 ~ 17:30
    20 - 17:30 ~ 18:00
    21 - 18:00 ~ 18:30
    22 - 18:30 ~ 19:00
    23 - 19:00 ~ 19:30
    24 - 19:30 ~ 20:00
    25 - 20:00 ~ 20:30
    26 - 20:30 ~ 21:00
    27 - 21:00 ~ 21:30
    28 - 21:30 ~ 22:00
*/
// ClassTime 은 immutable 클래스이다.
public class ClassTime {
    private EDay day;
    private int startOfClass;
    private int endOfClass;

    public ClassTime(EDay day, int startOfClass, int endOfClass) {
        assert 0 < startOfClass && startOfClass < endOfClass && endOfClass < 29 : "Invalid Class Time";
        this.day = day;
        this.startOfClass = startOfClass;
        this.endOfClass = endOfClass;
    }

    public boolean isOverlapTime(ClassTime other) {
        return this.startOfClass <= other.startOfClass && other.startOfClass < this.endOfClass
                || other.startOfClass <= this.startOfClass && this.startOfClass < other.endOfClass;
    }

    public EDay getDay() {
        return day;
    }

    public int getStart() {
        return startOfClass;
    }

    public int getEnd() {
        return endOfClass;
    }
}
