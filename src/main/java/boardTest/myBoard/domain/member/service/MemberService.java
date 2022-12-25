package boardTest.myBoard.domain.member.service;

import boardTest.myBoard.domain.member.dto.MemberSignUpDto;

public interface MemberService {

    Long signUp(MemberSignUpDto memberSignUpDto) throws Exception;


}
