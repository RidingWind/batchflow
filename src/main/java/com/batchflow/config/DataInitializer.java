package com.batchflow.config;

import com.batchflow.model.Job;
import com.batchflow.model.SystemInfo;
import com.batchflow.model.enums.JobType;
import com.batchflow.repository.JobRepository;
import com.batchflow.repository.SystemInfoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;

@Component
public class DataInitializer implements CommandLineRunner {

    private final SystemInfoRepository systemInfoRepository;
    private final JobRepository jobRepository;

    public DataInitializer(SystemInfoRepository systemInfoRepository, JobRepository jobRepository) {
        this.systemInfoRepository = systemInfoRepository;
        this.jobRepository = jobRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Create Systems
        SystemInfo crm = new SystemInfo(null, "CRM", "Customer Relationship Management", false);
        SystemInfo billing = new SystemInfo(null, "BILLING", "Billing System", true);
        SystemInfo reporting = new SystemInfo(null, "REPORTING", "Reporting Warehouse", true);

        systemInfoRepository.saveAll(Arrays.asList(crm, billing, reporting));

        // Create Jobs
        Job extractCrm = new Job();
        extractCrm.setJobName("EXTRACT_CRM_CUSTOMERS");
        extractCrm.setJobType(JobType.START_JOB);
        extractCrm.setDescription("Extracts customer data from the legacy CRM system.");
        extractCrm.setSystemInfo(crm);

        Job transformData = new Job();
        transformData.setJobName("TRANSFORM_CUSTOMER_DATA");
        transformData.setJobType(JobType.CONVERSION);
        transformData.setDescription("Transforms legacy customer data to the new format.");
        transformData.setSystemInfo(crm);

        Job loadToBilling = new Job();
        loadToBilling.setJobName("LOAD_CUSTOMERS_TO_BILLING");
        loadToBilling.setJobType(JobType.CONVERSION);
        loadToBilling.setDescription("Loads transformed customer data into the new Billing system.");
        loadToBilling.setSystemInfo(billing);

        Job validateBilling = new Job();
        validateBilling.setJobName("VALIDATE_BILLING_DATA");
        validateBilling.setJobType(JobType.VALIDATION);
        validateBilling.setDescription("Validates the loaded data in the Billing system.");
        validateBilling.setSystemInfo(billing);

        Job generateReport = new Job();
        generateReport.setJobName("GENERATE_MIGRATION_REPORT");
        generateReport.setJobType(JobType.REPORT);
        generateReport.setDescription("Generates a final report on the migration status.");
        generateReport.setSystemInfo(reporting);

        // Save jobs first to get IDs
        jobRepository.saveAll(Arrays.asList(extractCrm, transformData, loadToBilling, validateBilling, generateReport));

        // Set Dependencies
        extractCrm.getSuccessors().add(transformData);
        transformData.getSuccessors().add(loadToBilling);
        loadToBilling.getSuccessors().add(validateBilling);
        validateBilling.getSuccessors().add(generateReport);

        jobRepository.saveAll(Arrays.asList(extractCrm, transformData, loadToBilling, validateBilling));
    }
}
