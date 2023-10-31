package com.pppppp.amadda.friend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
public record GroupCreateRequest (
    @NotNull(message = "주인 유저의 seq는 필수입니다. ") Long ownerSeq,
    @NotBlank(message = "그룹 이름은 필수입니다. ") String groupName,
    @NotEmpty(message = "유저 seq 리스트는 필수입니다. ") List<Long> userSeqs){

}
