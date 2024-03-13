package hoxy.hLivv.backoffice.controller;

import hoxy.hLivv.dto.member.MemberDto;
import hoxy.hLivv.dto.product.ProductDto;
import hoxy.hLivv.dto.product.ProductImageDto;
import hoxy.hLivv.dto.restore.RestoreDto;
import hoxy.hLivv.jwt.TokenProvider;
import hoxy.hLivv.service.MemberService;
import hoxy.hLivv.service.ProductService;
import hoxy.hLivv.service.RestoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author 이상원
 */
@RestController
@RequestMapping("/backoffice/api")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "백오피스 API", description = "백오피스 구현에 필요한 API 목록")
public class BackofficeApiController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberService memberService;
    private final RestoreService restoreService;
    private final ProductService productService;


    @Operation(summary = "회원 정보 업데이트")
    @PostMapping("/updateMember")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<MemberDto> updateMember(@RequestBody MemberDto memberDto) {
        return ResponseEntity.ok(MemberDto.from(memberService.updateMember(memberDto)));
    }

    @Operation(summary = "상품 정보 업데이트")
    @PostMapping("/updateProduct")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto) {
        return ResponseEntity.ok(ProductDto.from(productService.updateProduct(productDto)));
    }

    @Operation(summary = "상품 이미지 URL 가져오기")
    @GetMapping("/getProductImageUrls")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<ProductImageDto>> getProductImages(@RequestParam Long id) {
        return ResponseEntity.ok(productService.getProductWith(id).getProductImages());
    }


    @Operation(summary = "memberId로 멤버 조회")
    @GetMapping("/member/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<MemberDto> getMemberById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getMemberById(id));
    }


    @Operation(summary = "restoreId로 리스토어 조회")
    @GetMapping("/getRestore")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<RestoreDto> getRestoreById(@RequestParam Long id) {
        return ResponseEntity.ok(restoreService.getRestore(id));
    }

    @Operation(summary = "restoreId로 리스토어 이미지 조회")
    @GetMapping("/getRestoreImageUrls")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<String>> getRestoreImages(@RequestParam Long id) {
        return ResponseEntity.ok(restoreService.getRestore(id).getRestoreImageUrls());
    }


    @Operation(summary = "리스토어 정보 수정")
    @PostMapping("/updateRestore")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<RestoreDto> updateRestore(@RequestBody RestoreDto restoreDto) {
        return ResponseEntity.ok(restoreService.updateRestore(restoreDto));
    }

    @Operation(summary = "검수완료 된 모든 리스토어 완료 처리, 멤버에게 포인트 지급")
    @PostMapping("/restore/rewarded")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<String> restoreRewarded() {
        restoreService.callUpdateRestoreCompleteAndPointsProcedure();
        return ResponseEntity.ok("리스토어 완료, 포인트 지급 완료.");
    }


    @Operation(summary = "restoreId로 리스토어 완료 처리, 멤버에게 포인트 지급")
    @PostMapping("/restore/rewarded/{restoreId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<String> restoreRewarded(@PathVariable Long restoreId) {
        restoreService.callUpdateRestoreCompleteAndPointsProcedure(restoreId);
        return ResponseEntity.ok("리스토어 완료, 포인트 지급 완료.");
    }
}
