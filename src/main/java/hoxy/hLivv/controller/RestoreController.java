package hoxy.hLivv.controller;

import hoxy.hLivv.dto.restore.RestoreDto;
import hoxy.hLivv.dto.restore.RestoreEmailDto;
import hoxy.hLivv.dto.restore.RestoreRegisterDto;
import hoxy.hLivv.dto.restore.RestoreStatusDto;
import hoxy.hLivv.service.AmazonSMTPService;
import hoxy.hLivv.service.RestoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
//@Tag(name = "리스토어 API", description = "리스토어 관리와 관련된 작업들")
public class RestoreController {

    private final RestoreService restoreService;
    private final AmazonSMTPService amazonSMTPService;

    //@Operation(summary = "리스토어 등록")
    @PostMapping("/restore")
    public ResponseEntity<RestoreDto> restoreRegister(
            @Valid @RequestBody RestoreRegisterDto restoreRegisterDto
    ) {
        return ResponseEntity.ok(restoreService.restoreRegister(restoreRegisterDto));
    }

    //@Operation(summary = "신청한 리스토어 목록 조회", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/restore/list/{memberId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<RestoreDto>> getMyRestores(@PathVariable Long memberId) {
        return ResponseEntity.ok(restoreService.getRestores(memberId));
    }

    //@Operation(summary = "리스토어 1개 상세 불러오기", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/restore/{restoreId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<RestoreDto> getOneRestore(@PathVariable Long restoreId) {
        return ResponseEntity.ok(restoreService.getRestore(restoreId));
    }

    //@Operation(summary = "리스토어 업데이트", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/restore/{restoreId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<RestoreDto> updateRestore(@PathVariable Long restoreId, @Valid @RequestBody RestoreDto restoreDto) {
        return ResponseEntity.ok(restoreService.updateRestore(restoreId, restoreDto));
    }

    //@Operation(summary = "리스토어 포인트 지급 상태 업데이트", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/restore/rewarded/{restoreId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<RestoreDto> updateRestore(@PathVariable Long restoreId, @RequestParam Boolean rewarded) {
        return ResponseEntity.ok(restoreService.updateRestoreRewarded(restoreId, rewarded));
    }

    //@Operation(summary = "리스토어 완료 처리 이메일 전송")
    @PostMapping("/restore/email")
    public void transferRestoreEmail(
            @RequestBody RestoreEmailDto restoreEmailDto
    ) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("productName", restoreEmailDto.getProductName());
        variables.put("requestGrade", restoreEmailDto.getRequestGrade());
        variables.put("inspectedGrade", restoreEmailDto.getInspectedGrade());
        variables.put("payback", restoreEmailDto.getPayback());
        variables.put("rejectMsg", restoreEmailDto.getRejectMsg());
        amazonSMTPService.sendRestoreCompleteEmail(restoreEmailDto.getSubject(), variables, restoreEmailDto.getToEmail());
    }

    //@Operation(summary = "리스토어 상태 정보 조회", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/restore/status")
    public ResponseEntity<List<RestoreStatusDto>> getRestoreStatusInfo() {
        return ResponseEntity.ok(restoreService.getRestoreStatus());
    }
}
