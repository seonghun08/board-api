package boardTest.myBoard.domain.member.service;

import boardTest.myBoard.domain.member.dto.MemberSignUpDto;
import boardTest.myBoard.domain.member.entity.Member;
import boardTest.myBoard.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     */
    @Override
    public Long signUp(MemberSignUpDto memberSignUpDto) throws Exception {
        Member member = memberSignUpDto.createMember();
        member.encodePassword(passwordEncoder);

        if (memberRepository.findByUsername(member.getUsername()).isPresent()) {
            throw new IllegalStateException();
        }

        Member saveMember = memberRepository.save(member);
        return saveMember.getId();
    }
}
