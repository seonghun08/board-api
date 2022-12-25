package boardTest.myBoard.domain.member.repository;

import boardTest.myBoard.domain.member.dto.MemberSignUpDto;
import boardTest.myBoard.domain.member.entity.Member;
import boardTest.myBoard.domain.member.entity.Role;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired EntityManager em;

    @AfterEach
    protected void afterEach() {
        em.clear();
    }

    private Member newMember() {
        return new MemberSignUpDto("username", "1234567890", "name", "nickname", 25).createMember();
    }

    @DisplayName("회원가입 성공")
    @Test
    public void success_save() throws Exception {
        //given
        Member member = newMember();

        //when
        Member saveMember = memberRepository.save(member);

        //then
        Member findMember = memberRepository.findById(saveMember.getId())
                .orElseThrow(() ->
                        new RuntimeException("저장된 회원이 없습니다"));

        assertThat(findMember).isEqualTo(saveMember);
        assertThat(findMember).isEqualTo(member);
    }

    @DisplayName("아이디 없이 회원가입시 실패")
    @Test
    public void fail_username() throws Exception {
        //given
        Member member = Member.builder()
                .password("1234567890")
                .name("name")
                .nickname("nickname")
                .role(Role.USER)
                .age(25)
                .build();

        //when, then
        assertThrows(Exception.class, () -> memberRepository.save(member));
    }

    @DisplayName("이름 없이 회원가입시 실패")
    @Test
    public void fail_name() throws Exception {
        //given
        Member member = Member.builder()
                .username("username")
                .password("1234567890")
                .nickname("nickname")
                .role(Role.USER)
                .age(25)
                .build();

        //when, then
        assertThrows(Exception.class, () -> memberRepository.save(member));
    }

    @DisplayName("닉네임 없이 회원가입시 실패")
    @Test
    public void fail_nickname() throws Exception {
        //given
        Member member = Member.builder()
                .username("username")
                .password("1234567890")
                .name("name")
                .role(Role.USER)
                .age(25)
                .build();

        //when, then
        assertThrows(Exception.class, () -> memberRepository.save(member));
    }

    @DisplayName("나이 없이 회원가입시 실패")
    @Test
    public void fail_age() throws Exception {
        //given
        Member member = Member.builder()
                .username("username")
                .password("1234567890")
                .name("name")
                .nickname("nickname")
                .role(Role.USER)
                .build();

        //when, then
        assertThrows(Exception.class, () -> memberRepository.save(member));
    }

    @DisplayName("회원가입시 중복된 아이디가 있으면 실패")
    @Test
    public void fail_duplication() throws Exception {
        //given
        Member member1 = newMember();
        Member member2 = newMember();
        memberRepository.save(member1);
        em.flush();
        em.clear();

        //when, then
        assertThrows(Exception.class, () -> memberRepository.save(member2));
    }

    @DisplayName("회원정보 수정 성공")
    @Test
    public void success_updateMember() throws Exception {
        //given
        Member member = newMember();
        memberRepository.save(member);
        em.flush();
        em.clear();

        String newPassword = "updatePassword";
        String newName = "updateName";
        String newNickname = "updateNickName";
        int newAge = 33;

        //when
        Member findMember = memberRepository.findById(member.getId()).orElseThrow(() -> new Exception());
        findMember.updateAge(newAge);
        findMember.updateName(newName);
        findMember.updateNickname(newNickname);
        findMember.updatePassword(passwordEncoder, newPassword);
        em.flush();
        em.clear();

        //then
        Member findUpdateMember = memberRepository.findById(findMember.getId()).orElseThrow(() -> new Exception());
        assertThat(findUpdateMember.getId()).isEqualTo(findMember.getId());
        assertThat(passwordEncoder.matches(newPassword, findUpdateMember.getPassword())).isTrue();
        assertThat(findUpdateMember.getName()).isEqualTo(newName);
        assertThat(findUpdateMember.getName()).isNotEqualTo(member.getName());
    }

    @DisplayName("회원 삭제")
    @Test
    public void success_deleteMember() throws Exception {
        //given
        Member member = newMember();
        memberRepository.save(member);
        em.flush();
        em.clear();

        //when
        Member m = memberRepository.findById(member.getId()).orElseThrow();
        memberRepository.delete(m);
        em.flush();
        em.clear();

        //then
        assertThrows(Exception.class,
                () -> memberRepository.findById(member.getId())
                        .orElseThrow(() -> new Exception()));
    }

    @DisplayName("findByUsername")
    @Test
    public void success_findByUsername() throws Exception {
        //given
        Member member = newMember();
        String username = member.getUsername();
        memberRepository.save(member);
        em.flush();
        em.clear();

        //when, then
        assertThat(memberRepository.findByUsername(username).get().getUsername()).isEqualTo(member.getUsername());
        assertThat(memberRepository.findByUsername(username).get().getName()).isEqualTo(member.getName());
        assertThat(memberRepository.findByUsername(username).get().getId()).isEqualTo(member.getId());
        assertThrows(Exception.class,
                () -> memberRepository.findByUsername(username + "xxx")
                        .orElseThrow(() -> new Exception()));
    }

    @DisplayName("회원 가입시 생성시간 등록")
    @Test
    public void success_notNull_createdDateAndLastModifiedDate() throws Exception {
        //given
        Member member = newMember();
        memberRepository.save(member);
        em.flush();
        em.clear();

        //when
        Member findMember = memberRepository.findById(member.getId()).orElseThrow(() -> new Exception());

        //then
        assertThat(findMember.getCreatedDate()).isNotNull();
        assertThat(findMember.getLastModifiedDate()).isNotNull();
    }
}