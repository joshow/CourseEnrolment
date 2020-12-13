package enrolment;

public enum EEnrolmentState {
    SUCCESS,                       // 성공
    FAIL_NO_MORE_CREDIT,           // 학점 부족 (학생)
    FAIL_NO_MORE_REMAIN_SEAT,      // 여석 부족 (학생)
    FAIL_ENROLLED_LECTURE,         // 이미 신청/등록된 강의 (학생/교수)
    FAIL_NONE_ENROLLED_LECTURE,    // 신청/동록하지 않은 강의 (학생/교수)
    FAIL_OVERLAP_CLASS_TIME,       // 본인의 다른 강의 시간 겹침 (학생/교수)
    FAIL_OVERLAP_CLASSROOM,        // 동일한 시간에 강의 장소가 겹침 (교수)
    FAIL_INVALID_ID,               // 잘못된 ID를 사용 (학생/교수)
    FAIL_WRONG_IDENTITY_ID,        // 잘못된 신분의 학적 ID를 사용 (학생/교수)

    FAIL
    // 이외 필요에 따라 적당한 FAIL 코드를 추가하여 사용하기
}
