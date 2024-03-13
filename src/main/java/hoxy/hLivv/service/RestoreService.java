package hoxy.hLivv.service;

import hoxy.hLivv.dto.restore.RestoreDto;
import hoxy.hLivv.dto.restore.RestoreRegisterDto;
import hoxy.hLivv.dto.restore.RestoreStatusDto;
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
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestoreService {

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final RestoreRepository restoreRepository;
    private final RestoreImageRepository restoreImageRepository;
    private final S3Service s3Service;
    @PersistenceContext
    private final EntityManager em;

    @Transactional
    public RestoreDto restoreRegister(RestoreRegisterDto restoreRegisterDto) {

        Product product = productRepository.findById(restoreRegisterDto.getProductId())
                .orElseThrow(() -> new NotFoundProductException("Product not found"));

        if (!product.isRestore()) {
            throw new NotFoundProductException("Re-store unavailable product");
        }
        //upload restore image

        Restore restore = Restore.builder()
                .member(SecurityUtil.getCurrentUsername()
                        .flatMap(memberRepository::findOneWithAuthoritiesByLoginId)
                        .orElseThrow(() -> new NotFoundMemberException("Member not found")))
                .product(product)
                .regDate(new Date())
                .pickUpDate(restoreRegisterDto.getPickUpDate())
                .requestGrade(restoreRegisterDto.getRequestGrade())
                .restoreDesc(restoreRegisterDto.getRestoreDesc())
                .rewarded(false)
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
    public List<RestoreDto> getRestores(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundMemberException("Member not found"));
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

        updateRestoreFromDto(restoreDto, restore);
        return RestoreDto.from(restoreRepository.save(restore));
    }

    @Transactional
    public RestoreDto updateRestore(Long restoreId,RestoreDto restoreDto) {
        Restore restore = restoreRepository.findById(restoreId).orElseThrow(() -> new NotFoundRestoreException("Re-store not found"));
        updateRestoreFromDto(restoreDto, restore);
        return RestoreDto.from(restoreRepository.save(restore));
    }

    @Transactional
    public RestoreDto updateRestoreRewarded(Long restoreId,Boolean rewarded) {
        Restore restore = restoreRepository.findById(restoreId).orElseThrow(() -> new NotFoundRestoreException("Re-store not found"));
        restore.setRewarded(rewarded);
        return RestoreDto.from(restoreRepository.save(restore));
    }

    private static void updateRestoreFromDto(RestoreDto restoreDto, Restore restore) {
        restore.setInspectedGrade(restoreDto.getInspectedGrade());
        if (restoreDto.getRestoreDesc() != null) {
            restore.setRestoreDesc(restoreDto.getRestoreDesc());
        }
        if (restoreDto.getRewarded() != null) {
            restore.setRewarded(restoreDto.getRewarded());
        }
        if (restoreDto.getRejectMsg() != null) {
            restore.setRejectMsg(restoreDto.getRejectMsg());
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

    @Transactional
    public void callUpdateRestoreCompleteAndPointsProcedure() {
        StoredProcedureQuery query = em.createNamedStoredProcedureQuery("UpdateRestoreCompleteAndPoints");
        query.execute();
    }
    @Transactional
    public void callUpdateRestoreCompleteAndPointsProcedure(Long restoreId) {
        StoredProcedureQuery query = em.createNamedStoredProcedureQuery("UpdateRestoreCompleteAndPointsByRestoreId");
        query.setParameter("p_restore_id", restoreId);
        query.execute();
    }

    @Transactional
    public Integer getRestoreByRestoreStatus(RestoreStatus restoreStatus) {
        return restoreRepository.findByRestoreStatus(restoreStatus).size();
    }

    @Transactional
    public List<RestoreStatusDto> getRestoreStatus() {
        return restoreRepository.findRestoreGroupByRestoreStatus();
    }
}
