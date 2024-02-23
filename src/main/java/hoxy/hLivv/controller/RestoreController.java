package hoxy.hLivv.controller;


import hoxy.hLivv.dto.MemberDto;
import hoxy.hLivv.dto.restore.RestoreDto;
import hoxy.hLivv.dto.restore.RestoreRegisterDto;
import hoxy.hLivv.entity.Member;
import hoxy.hLivv.entity.Restore;
import hoxy.hLivv.service.RestoreService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class RestoreController {

    private final RestoreService restoreService;

    //리스토어 등록
    @PostMapping("/restore")
    public ResponseEntity<RestoreDto> restoreRegister(
            @Valid @RequestBody RestoreRegisterDto restoreRegisterDto
    ) {
        return ResponseEntity.ok(restoreService.restoreRegister(restoreRegisterDto));
    }

    // 내가 신청한 리스토어들 불러오기
    @GetMapping("/restore/list")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<RestoreDto>> getRestores() {
        return ResponseEntity.ok(restoreService.getRestores());
    }

    // 리스토어 1개 상세 불러오기
    @GetMapping("/restore/{restoreId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<RestoreDto> getOneRestore(@PathVariable Long restoreId) {
        return ResponseEntity.ok(restoreService.getRestore(restoreId));
    }

    @PutMapping("/restore/{restoreId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<RestoreDto> getOneRestore(@PathVariable Long restoreId,@Valid @RequestBody RestoreDto restoreDto) {
        return ResponseEntity.ok(restoreService.updateRestore(restoreId, restoreDto));
    }



}
