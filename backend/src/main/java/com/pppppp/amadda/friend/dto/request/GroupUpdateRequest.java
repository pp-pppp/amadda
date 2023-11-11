package com.pppppp.amadda.friend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;

@Builder
public record GroupUpdateRequest(
    @NotNull(message = "그룹의 seq는 필수입니다. ") Long groupSeq,
    @NotBlank(message = "그룹 이름은 필수입니다. ") String groupName,
    @NotEmpty(message = "그룹은 한명 이상의 멤버가 필요합니다. ") List<Long> userSeqs) {

}
