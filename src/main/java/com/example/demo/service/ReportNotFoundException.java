package com.example.demo.service;

public class ReportNotFoundException extends RuntimeException {
    public ReportNotFoundException(Long scenarioId) {
        super("시나리오 " + scenarioId + "에 대한 리포트가 없습니다.");
    }
}
