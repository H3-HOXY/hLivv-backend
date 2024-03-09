package hoxy.hLivv.controller;

import hoxy.hLivv.dto.AddressDto;
import hoxy.hLivv.dto.address.AddressReqDto;
import hoxy.hLivv.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Tag(name = "주소록 API", description = "주소록 관리와 관련된 작업들")
public class AddressController {
    private final AddressService addressService;

    @Operation(summary = "주소록 생성")
    @PostMapping("/address")
    public ResponseEntity<AddressDto> createAddress(@RequestBody AddressReqDto addressReqDto) {
        AddressDto addressDto = addressService.saveAddress(addressReqDto);
        return ResponseEntity.ok(addressDto);
    }

    @Operation(summary = "전체 주소록 조회")
    @GetMapping("/address")
    public ResponseEntity<List<AddressDto>> getAddress(
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
            @RequestParam(required = false, defaultValue = "20", value = "pageSize") int pageSize) {
        return ResponseEntity.ok(addressService.getAllAddress(pageNo, pageSize));
    }

    @Operation(summary = "내 주소록 조회")
    @GetMapping("/address/my")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<AddressDto>> getMyAddress() {
        return ResponseEntity.ok(addressService.getMyAddress());
    }

    @Operation(summary = "상세 주소록 조회")
    @GetMapping("/address/{addressId}")
    public ResponseEntity<AddressDto> getAddress(@PathVariable Long addressId) {
        var address = addressService.getAddressWith(addressId);
        return ResponseEntity.ok(address);
    }

//	@Operation(summary = "주문 주소록 조회")
//	@GetMapping("/address/{orderId}")
//	@PreAuthorize("hasAnyRole('USER','ADMIN')")
//	public ResponseEntity<List<AddressDto>> getOrderAddress(@PathVariable Long orderId) {
//		return ResponseEntity.ok(addressService.getOrderAddress(orderId));
//	}
}