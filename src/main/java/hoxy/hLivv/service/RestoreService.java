package hoxy.hLivv.service;

import hoxy.hLivv.dto.product.ProductDto;
import hoxy.hLivv.dto.restore.RestoreDto;
import hoxy.hLivv.dto.restore.RestoreRegisterDto;
import hoxy.hLivv.entity.Member;
import hoxy.hLivv.entity.Product;
import hoxy.hLivv.entity.Restore;
import hoxy.hLivv.entity.RestoreImage;
import hoxy.hLivv.entity.enums.RestoreStatus;
import hoxy.hLivv.exception.AccessDeniedMemberException;
import hoxy.hLivv.exception.NotFoundMemberException;
import hoxy.hLivv.exception.NotFoundProductException;
import hoxy.hLivv.exception.NotFoundRestoreException;
import hoxy.hLivv.repository.MemberRepository;
import hoxy.hLivv.repository.ProductRepository;
import hoxy.hLivv.repository.RestoreImageRepository;
import hoxy.hLivv.repository.RestoreRepository;
import hoxy.hLivv.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestoreService {

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final RestoreRepository restoreRepository;
    private final RestoreImageRepository restoreImageRepository;

    @Transactional
    public RestoreDto restoreRegister(RestoreRegisterDto restoreRegisterDto) {

        Product product = productRepository.findById(restoreRegisterDto.getProductId())
                .orElseThrow(() -> new NotFoundProductException("Product not found"));

        if (!product.isRestore()) {
            throw new NotFoundProductException("Re-store unavailable product");
        }
        Restore restore = Restore.builder()
                .member(SecurityUtil.getCurrentUsername()
                        .flatMap(memberRepository::findOneWithAuthoritiesByLoginId)
                        .orElseThrow(() -> new NotFoundMemberException("Member not found")))
                .product(product)
                .regDate(new Date())
                .pickUpDate(restoreRegisterDto.getPickUpDate())
                .requestGrade(restoreRegisterDto.getRequestGrade())
                .restoreDesc(restoreRegisterDto.getRestoreDesc())
                .requestMsg(restoreRegisterDto.getRequestMsg())
                .whenRejected(restoreRegisterDto.getWhenRejected())
                .restoreStatus(RestoreStatus.접수완료)
                .build();

        restoreImageSetting(restoreRegisterDto, restore);

        return RestoreDto.from(restoreRepository.save(restore));
    }

    // 나의 리스토어 목록 가져오기
    @Transactional
    public List<RestoreDto> getRestores() {
        Member member =  SecurityUtil.getCurrentUsername()
                .flatMap(memberRepository::findOneWithAuthoritiesByLoginId)
                .orElseThrow(() -> new NotFoundMemberException("Member not found"));
        return restoreRepository.findByMember(member).stream().map(RestoreDto::from).toList();
    }

    @Transactional
    public Page<RestoreDto> getAllProductsWithPagination(Pageable pageable) {
        return restoreRepository.findAll(pageable).map(RestoreDto::from);
//        return restoreRepository.findAll(pageable).stream().map(RestoreDto::from).toList();
    }

    @Transactional
    public List<RestoreDto> getAllRestores(int pageNo, int pageSize) {
        pageSize = Math.min(pageSize, 100);
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return restoreRepository.findAll(pageable).stream().map(RestoreDto::from).toList();
    }

    @Transactional
    public RestoreDto getRestore(Long restoreId) {
        Restore restore = restoreRepository.findById(restoreId).orElseThrow(() -> new NotFoundRestoreException("Re-store not found"));
        Member member =  SecurityUtil.getCurrentUsername()
                .flatMap(memberRepository::findOneWithAuthoritiesByLoginId)
                .orElseThrow(() -> new NotFoundMemberException("Member not found"));

        if (member.getAuthorities().stream().map(item -> item.getAuthority().getAuthorityName()).toList().contains("ROLE_ADMIN")) {
            return RestoreDto.from(restore);
        }

        if (restore.getMember() != member){
            throw new AccessDeniedMemberException("Re-store Access Denied");
        }
        return RestoreDto.from(restore);
    }

    @Transactional
    public RestoreDto updateRestore(RestoreDto restoreDto) {
        Restore restore = restoreRepository.findById(restoreDto.getRestoreId()).orElseThrow(() -> new NotFoundRestoreException("Re-store not found"));

        restore.setInspectedGrade(restoreDto.getInspectedGrade());
        if (restoreDto.getRestoreDesc() != null) {
            restore.setRestoreDesc(restoreDto.getRestoreDesc());
        }
        if (restoreDto.getRequestMsg() != null) {
            restore.setRestoreDesc(restoreDto.getRequestMsg());
        }
        if (restoreDto.getRejectMsg() != null) {
            restore.setRestoreDesc(restoreDto.getRejectMsg());
        }
        restore.setPayback(restoreDto.getPayback());
        restore.setWhenRejected(restoreDto.getWhenRejected());
        restore.setRestoreStatus(restoreDto.getRestoreStatus());

        // restoreImages 업데이트
        List<String> newImageUrls = restoreDto.getRestoreImageUrls();
        if (newImageUrls != null) {
            List<RestoreImage> existingImages = restore.getRestoreImages();

            // 기존 이미지 URL 목록
            List<String> originImageUrls = existingImages.stream()
                    .map(RestoreImage::getRiUrl)
                    .toList();

            if (!originImageUrls.equals(newImageUrls)) {
                existingImages.clear();
                for (String imageUrl : newImageUrls) {
                    RestoreImage newImage = RestoreImage.builder()
                            .restore(restore)
                            .riUrl(imageUrl)
                            .build();
                    existingImages.add(newImage);
                }
            }
        }
        return RestoreDto.from(restoreRepository.save(restore));
    }

    private static void restoreImageSetting(RestoreDto restoreDto, Restore restore) {
        List<RestoreImage> restoreImages = new ArrayList<>();
        for (String restoreImageUrl : restoreDto.getRestoreImageUrls()) {
            RestoreImage restoreImage = RestoreImage.builder()
                    .restore(restore)
                    .riUrl(restoreImageUrl)
                    .build();
            restoreImages.add(restoreImage);
        }
        restore.setRestoreImages(restoreImages);
    }
    private static void restoreImageSetting(RestoreRegisterDto restoreRegisterDto, Restore restore) {
        List<RestoreImage> restoreImages = new ArrayList<>();
        for (String restoreImageUrl : restoreRegisterDto.getRestoreImageUrls()) {
            RestoreImage restoreImage = RestoreImage.builder()
                    .restore(restore)
                    .riUrl(restoreImageUrl)
                    .build();
            restoreImages.add(restoreImage);
        }
        restore.setRestoreImages(restoreImages);
    }
}
