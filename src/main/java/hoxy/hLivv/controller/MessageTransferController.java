package hoxy.hLivv.controller;


import hoxy.hLivv.dto.restore.RestoreEmailDto;
import hoxy.hLivv.service.AmazonSMTPService;
import hoxy.hLivv.service.MessageTransferService;
import hoxy.hLivv.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MessageTransferController {


    private final MessageTransferService messageTransferService;
    @PostMapping("/messageTransfer")
    public void transferRestoreMessage(
            @RequestParam String toNumber,
            @RequestParam String contents
    ) {
        messageTransferService.messageTransfer(toNumber,contents);
    }



}
