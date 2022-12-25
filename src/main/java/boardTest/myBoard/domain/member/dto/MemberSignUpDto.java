package boardTest.myBoard.domain.member.dto;

import boardTest.myBoard.domain.member.entity.Role;
import boardTest.myBoard.domain.member.entity.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

public record MemberSignUpDto (

    @NotBlank(message = "아이디를 입력하세요.")
    @Size(min = 4, max = 25, message = "아이디는 4~25자 내외로 입력하세요.")
    String username,

    @NotBlank(message = "비밀번호를 입력하세요.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,30}$",
            message = "비밀번호는 8~30 자리이면서 1개 이상의 알파벳, 숫자, 특수문자를 포함해야합니다."
    )
    String password,

    @NotBlank(message = "이름을 입력해주세요")
    @Size(min = 2, message = "사용자의 이름이 너무 짧습니다.")
    @Pattern(
            regexp = "^[A-Za-z가-힣]+$",
            message = "사용자 이름은 한글 또는 영어만 입력하세요."
    )
    String name,

    @NotBlank(message = "닉네임을 입력하세요.")
    @Size(min = 2, message = "닉네임이 너무 짧습니다.")
    String nickname,

    @NotNull(message = "나이를 입력해주세요")
    @Range(min = 0, max = 150)
    Integer age
) {
    public Member createMember() {
        return Member.builder()
                .username(username)
                .password(password)
                .name(name)
                .nickname(nickname)
                .age(age)
                .role(Role.USER)
                .build();

    }
}
