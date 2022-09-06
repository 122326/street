package com.util;

import com.entity.Appeal;
import com.entity.Help;
import com.service.AppealService;
import com.service.HelpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ScheduleTask {

    @Autowired
    private HelpService helpService;
    @Autowired
    private AppealService appealService;


    public void updateHelpStatus(List<Help> helps) {

        Date now = new Date();
        for (Help help : helps) {
            Date endTime = help.getEndTime();
            if (endTime.before(now)) {
                if (help.getHelpStatus() == 0) {
                    help.setHelpStatus(3);
                    helpService.modifyHelp(help);
                } else if (help.getHelpStatus() == 1) {
                    help.setHelpStatus(2);
                    helpService.modifyHelp(help);
                }
            }
        }

    }

    public void updateAppealStatus(List<Appeal> appealList) {
        Date now = new Date();
        for (Appeal appeal : appealList) {
            Date endTime = appeal.getEndTime();
            if (endTime.before(now)) {
                if (appeal.getAppealStatus() == 0) {
                    appeal.setAppealStatus(3);
                    appealService.modifyAppeal(appeal);
                } else if (appeal.getAppealStatus() == 1) {
                    appeal.setAppealStatus(2);
                    appealService.modifyAppeal(appeal);
                }
            }
        }
    }

}
