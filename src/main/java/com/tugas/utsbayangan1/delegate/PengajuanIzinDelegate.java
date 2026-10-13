package com.tugas.utsbayangan1.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("pengajuanIzinDelegate")
public class PengajuanIzinDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        Integer days = (Integer) execution.getVariable("days");
        if (days == null || days <= 0) {
            throw new IllegalArgumentException("days harus berupa bilangan bulat positif");
        }
        execution.setVariable("status", days <= 3 ? "APPROVED" : "REJECTED");
    }
}
