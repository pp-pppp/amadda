package com.pppppp.amadda.friend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;

@Builder
public record GroupCreateRequest(
    @NotNull(message = "유효하지 않은 요청입니다.") Long ownerSeq,
    @NotBlank(message = "그룹 이름을 입력해 주세요.") String groupName,
    @NotEmpty(message = "그룹은 한명 이상의 멤버가 필요합니다.") List<Long> userSeqs) {

}
