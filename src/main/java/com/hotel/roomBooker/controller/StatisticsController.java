package com.hotel.roomBooker.controller;

import com.hotel.roomBooker.service.CsvExportService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/statistics")
@AllArgsConstructor
public class StatisticsController {
    private final CsvExportService csvExportService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/export")
    public ResponseEntity<Resource> exportStatistics() throws IOException {
        InputStreamResource resource = csvExportService.exportStatisticsToCsvAsResource();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=statistics.csv")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
