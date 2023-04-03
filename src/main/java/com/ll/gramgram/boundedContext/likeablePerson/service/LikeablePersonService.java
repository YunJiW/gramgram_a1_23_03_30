package com.ll.gramgram.boundedContext.likeablePerson.service;

import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.instaMember.serivce.InstaMemberService;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.repository.LikeablePersonRepository;
import com.ll.gramgram.boundedContext.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeablePersonService {

    private final LikeablePersonRepository likeablePersonRepository;

    private final InstaMemberService instaMemberService;

    //username이 상대방 이름
    @Transactional
    public RsData<LikeablePerson> create(Member member, String username, int attractiveTypeCode) {
        //인스타에 저장된 사람 넣기
        InstaMember byUsername = instaMemberService.findbyUsernameOrCreate(username);

        if(member.getInstaMember().getUsername().equals(username)){
            return RsData.of("F-1","본인을 호감상대로 등록 불가능");
        }

        LikeablePerson likeablePerson = LikeablePerson
                .builder()
                .fromInstaMember(member.getInstaMember())
                .fromInstaMemberUsername(member.getInstaMember().getUsername())
                .toInstaMember(byUsername)
                .toInstaMemberUsername(byUsername.getUsername())
                .build();

        likeablePersonRepository.save(likeablePerson);

        return RsData.of("S-1","호감상대(%s)로 등록되었습니다.".formatted(username),likeablePerson);
    }
}
