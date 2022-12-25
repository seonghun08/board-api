package boardTest.myBoard.domain.member.service;

import boardTest.myBoard.domain.member.dto.MemberSignUpDto;
import boardTest.myBoard.domain.member.entity.Member;
import boardTest.myBoard.domain.member.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberService memberService;
    @PersistenceContext
    EntityManager em;

    private MemberSignUpDto newMemberSignUpDto() {
        return new MemberSignUpDto("username", "1234567890", "name", "nickname", 25);
    }

    /**
     * 회원가입
     */
    @DisplayName("회원가입 성공")
    @Test
    public void signUp_success() throws Exception {
        //given
        MemberSignUpDto memberSignUpDto = newMemberSignUpDto();

        //when
        Long memberId = memberService.signUp(memberSignUpDto);

        //then
        Member findMember = memberRepository.findById(memberId).orElseThrow();
        assertThat(findMember.getUsername()).isEqualTo("username");
    }

    @DisplayName("회원가입 실패 - 아이디 중복")
    @Test
    public void signUp_fail1() throws Exception {
        //given
        MemberSignUpDto memberSignUpDto1 = newMemberSignUpDto();
        MemberSignUpDto memberSignUpDto2 = newMemberSignUpDto();
        memberService.signUp(memberSignUpDto1);
        em.flush();
        em.clear();

        //when, then
        assertThrows(Exception.class, () ->
                memberService.signUp(memberSignUpDto2));
    }

//    @Test
//    public void signUp_fail2() throws Exception {
//        //given
//        String password = "1234567890";
//        MemberSignUpDto memberSignUpDto1 = new MemberSignUpDto(null, password, "name", "nickNAme", 22);
//        MemberSignUpDto memberSignUpDto2 = new MemberSignUpDto("username", null, "name", "nickNAme", 22);
//        MemberSignUpDto memberSignUpDto3 = new MemberSignUpDto("username", password, null, "nickNAme", 22);
//        MemberSignUpDto memberSignUpDto4 = new MemberSignUpDto("username", password, "name", null, 22);
//        MemberSignUpDto memberSignUpDto5 = new MemberSignUpDto("username", password, "name", "nickNAme", null);
//
//        //when, then
//        assertThrows(Exception.class, () -> memberService.signUp(memberSignUpDto1));
//        assertThrows(Exception.class, () -> memberService.signUp(memberSignUpDto2));
//        assertThrows(Exception.class, () -> memberService.signUp(memberSignUpDto3));
//        assertThrows(Exception.class, () -> memberService.signUp(memberSignUpDto4));
//        assertThrows(Exception.class, () -> memberService.signUp(memberSignUpDto5));
//    }
}