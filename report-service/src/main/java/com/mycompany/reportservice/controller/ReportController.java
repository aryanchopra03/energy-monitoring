package com.mycompany.reportservice.controller;

import com.mycompany.reportservice.model.Report;
import com.mycompany.reportservice.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor public class ReportController {

    private final ReportService reportService;

    @GetMapping
    public ResponseEntity<List<Report>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @PostMapping
    public ResponseEntity<Report> createReports (@RequestBody Report report) {
        return ResponseEntity.ok(reportService.createReports(report));
    }

}
