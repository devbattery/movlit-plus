package movlit.be.image.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.chat_room.application.service.dto.ProfileImageUpdatedEvent;
import movlit.be.common.util.ids.MemberId;
import movlit.be.image.application.convertor.ImageConverter;
import movlit.be.image.domain.entity.ImageEntity;
import movlit.be.image.domain.repository.ImageRepository;
import movlit.be.image.presentation.dto.response.ImageResponse;
import movlit.be.member.application.service.MemberReadService;
import movlit.be.member.application.service.MemberWriteService;
import movlit.be.member.domain.entity.MemberEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ImageService {

    private final ImageRepository imageRepository;
    private final S3Service s3Service;
    private final MemberReadService memberReadService;
    private final MemberWriteService memberWriteService;
    private final ApplicationEventPublisher eventPublisher;

    @Value("${aws.s3.bucket.folderName}")
    private String folderName;

    public ImageResponse uploadProfileImage(MemberId memberId, MultipartFile file) {
        // 1. 기존 이미지가 있다면 삭제
        deleteExistingProfileImageIfPresent(memberId);

        // 2. S3에 업로드 후 엔티티 생성
        String imageUrl = s3Service.uploadImage(file, folderName);
        ImageEntity imageEntity = ImageConverter.toImageEntity(imageUrl, memberId);
        ImageEntity savedImageEntity = imageRepository.upload(imageEntity);

        // 3. 회원 프로필 이미지 URL 업데이트
        updateMemberProfileImageUrl(memberId, savedImageEntity.getUrl());

        // 4. 프로필 업데이트 이벤트 발행
        eventPublisher.publishEvent(new ProfileImageUpdatedEvent(memberId));

        return new ImageResponse(savedImageEntity.getImageId(), savedImageEntity.getUrl());
    }

    private void deleteExistingProfileImageIfPresent(MemberId memberId) {
        if (imageRepository.existsByMemberId(memberId)) {
            imageRepository.deleteByMemberId(memberId);
        }
    }

    private void updateMemberProfileImageUrl(MemberId memberId, String imageUrl) {
        MemberEntity member = memberReadService.fetchEntityByMemberId(memberId);
        member.updateProfileImgUrl(imageUrl);
        memberWriteService.save(member);
    }

    public ImageResponse fetchProfileImage(MemberId memberId) {
        return imageRepository.fetchProfileImageByMemberId(memberId);
    }

}
