package hoxy.hLivv.controller;


import hoxy.hLivv.dto.restore.RestoreDto;
import hoxy.hLivv.dto.restore.RestoreEmailDto;
import hoxy.hLivv.dto.restore.RestoreRegisterDto;
import hoxy.hLivv.dto.restore.RestoreStatusDto;
import hoxy.hLivv.service.AmazonSMTPService;
import hoxy.hLivv.service.RestoreService;
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
public class RestoreController {

    private final RestoreService restoreService;
    private final AmazonSMTPService amazonSMTPService;

    //리스토어 등록
    @PostMapping("/restore")
    public ResponseEntity<RestoreDto> restoreRegister(
            @Valid @RequestBody RestoreRegisterDto restoreRegisterDto
    ) {
        return ResponseEntity.ok(restoreService.restoreRegister(restoreRegisterDto));
    }

    // 내가 신청한 리스토어들 불러오기
//    @GetMapping("/restore/list")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<RestoreDto>> getRestores() {
        return ResponseEntity.ok(restoreService.getRestores());
    }

    @GetMapping("/restore/list/{memberId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<RestoreDto>> getMyRestores(@PathVariable Long memberId) {
        return ResponseEntity.ok(restoreService.getRestores(memberId));
    }

    // 리스토어 1개 상세 불러오기
    @GetMapping("/restore/{restoreId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<RestoreDto> getOneRestore(@PathVariable Long restoreId) {
        return ResponseEntity.ok(restoreService.getRestore(restoreId));
    }

    @PutMapping("/restore/{restoreId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<RestoreDto> updateRestore(@PathVariable Long restoreId, @Valid @RequestBody RestoreDto restoreDto) {
        return ResponseEntity.ok(restoreService.updateRestore(restoreId, restoreDto));
    }

    @PostMapping("/restore/rewarded/{restoreId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<RestoreDto> updateRestore(@PathVariable Long restoreId, @RequestParam Boolean rewarded) {
        return ResponseEntity.ok(restoreService.updateRestoreRewarded(restoreId, rewarded));
    }

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

    @GetMapping("/restore/status")
    public ResponseEntity<List<RestoreStatusDto>> getRestoreStatusInfo() {
        return ResponseEntity.ok(restoreService.getRestoreStatus());
    }
}
