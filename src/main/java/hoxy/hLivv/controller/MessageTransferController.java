package hoxy.hLivv.controller;

import hoxy.hLivv.dto.restore.RestoreEmailDto;
import hoxy.hLivv.service.AmazonSMTPService;
import hoxy.hLivv.service.MessageTransferService;
import hoxy.hLivv.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "메시지 전송 API", description = "메시지 전송과 관련된 작업들")
public class MessageTransferController {

    private final MessageTransferService messageTransferService;

    @Operation(summary = "문자 메시지 전송")
    @PostMapping("/messageTransfer")
    public void transferRestoreMessage(
            @RequestParam String toNumber,
            @RequestParam String contents
    ) {
        messageTransferService.messageTransfer(toNumber, contents);
    }
}