package hoxy.hLivv.backoffice.controller;

import hoxy.hLivv.dto.MemberDto;
import hoxy.hLivv.dto.product.ProductDto;
import hoxy.hLivv.dto.product.ProductImageDto;
import hoxy.hLivv.jwt.TokenProvider;
import hoxy.hLivv.service.MemberService;
import hoxy.hLivv.service.ProductService;
import hoxy.hLivv.service.RestoreService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/backoffice/api")
@Slf4j
@RequiredArgsConstructor
public class BackofficeApiController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberService memberService;
    private final RestoreService restoreService;
    private final ProductService productService;

    @PostMapping("/updateMember")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<MemberDto> updateMember(@RequestBody MemberDto memberDto) {
        return ResponseEntity.ok(MemberDto.from(memberService.updateMember(memberDto)));
    }


    @PostMapping("/updateProduct")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto) {
        return ResponseEntity.ok(ProductDto.from(productService.updateProduct(productDto)));
    }

    @GetMapping("/getProductImageUrls")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<ProductImageDto>> getProductImages(@RequestParam Long id) {
        return ResponseEntity.ok(productService.getProductWith(id).getProductImages());
    }

    @GetMapping("/member/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<MemberDto> getMemberById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getMemberById(id));
    }

}
