package hoxy.hLivv.controller;


import hoxy.hLivv.service.MessageTransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MessageTransferController {


    private final MessageTransferService messageTransferService;

    @GetMapping("/messageTransfer")
    public void transferMessage(
            @RequestParam String toNumber,
            @RequestParam String contents
    ) {
        messageTransferService.messageTransfer(toNumber,contents);
    }

}
