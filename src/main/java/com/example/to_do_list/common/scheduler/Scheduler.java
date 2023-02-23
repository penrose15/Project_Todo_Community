package com.example.to_do_list.common.scheduler;

import com.example.to_do_list.service.AttendService;
import com.example.to_do_list.service.TeamManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class Scheduler {
    private final AttendService attendService;
    private final TeamManagementService teamManagementService;

    @Async
    @Scheduled(cron = "0 59 23 * * *")
    public void saveAttend(){
        attendService.saveAll(); //매일 저장
        teamManagementService.deleteUser();//매일 체크리스트 몇일 연속 퍼센테지 0퍼인 사람 확인 후 삭제
    }


}
