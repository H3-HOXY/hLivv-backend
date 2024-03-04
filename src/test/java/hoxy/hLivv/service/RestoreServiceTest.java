package hoxy.hLivv.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class RestoreServiceTest {

    @Autowired
    RestoreService restoreService;


    @Test
    void procedureExec() {
        restoreService.callUpdateRestoreCompleteAndPointsProcedure();
//        Assertions.assertThat(restoreService.getRestore(21L).getRewarded()).isEqualTo(true);
//        if (!restoreService.getRestore(21L).getRewarded()) {
//
//        }
    }

}