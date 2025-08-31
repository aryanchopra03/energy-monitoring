package com.mycompany.reportservice.service;

import com.mycompany.reportservice.model.Report;
import com.mycompany.reportservice.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public Report createReports(Report report) {
        return reportRepository.save(report);
    }
}
