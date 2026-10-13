package com.tugas.utsbayangan1.service;

import java.util.Map;
import java.util.UUID;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.springframework.stereotype.Service;

@Service
public class PengajuanIzinService {
    private static final String PROCESS_KEY = "pengajuanIzin";

    private final RuntimeService runtimeService;
    private final HistoryService historyService;

    public PengajuanIzinService(RuntimeService runtimeService, HistoryService historyService) {
        this.runtimeService = runtimeService;
        this.historyService = historyService;
    }

    public Decision submit(String studentName, String reason, int days) {
        String requestId = UUID.randomUUID().toString();
        var processInstance =
                runtimeService.startProcessInstanceByKey(
                        PROCESS_KEY,
                        requestId,
                        Map.of(
                                "studentName", studentName,
                                "reason", reason,
                                "days", days));

        HistoricVariableInstance statusVariable =
                historyService
                        .createHistoricVariableInstanceQuery()
                        .processInstanceId(processInstance.getProcessInstanceId())
                        .variableName("status")
                        .singleResult();
        if (statusVariable == null) {
            throw new IllegalStateException("Process pengajuan izin tidak menghasilkan status.");
        }

        String status = String.valueOf(statusVariable.getValue());
        String message =
                "APPROVED".equals(status)
                        ? "Pengajuan izin disetujui karena maksimal 3 hari."
                        : "Pengajuan izin ditolak karena lebih dari 3 hari.";
        return new Decision(status, message);
    }

    public record Decision(String status, String message) {}
}
