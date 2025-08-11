import React, { useState, useEffect } from 'react';
import { Modal, Form, Input, Select, Button, message } from 'antd';
import api from '../services/api';

const { Option } = Select;

const JobFormModal = ({ open, onCancel, onFinish, job }) => {
    const [form] = Form.useForm();
    const [systems, setSystems] = useState([]);
    const [jobs, setJobs] = useState([]);

    useEffect(() => {
        if (open) {
            if (job) {
                // If editing, populate form fields
                form.setFieldsValue({
                    ...job,
                    systemInfoId: job.systemInfo?.id,
                    successorJobNames: job.successorJobNames || [],
                });
            } else {
                // If creating, reset form
                form.resetFields();
            }

            // Fetch systems and jobs for the select dropdowns
            const fetchPrerequisites = async () => {
                try {
                    const [systemsResponse, jobsResponse] = await Promise.all([
                        api.getSystems(),
                        api.getJobs()
                    ]);
                    setSystems(systemsResponse.data);
                    setJobs(jobsResponse.data);
                } catch (error) {
                    message.error("Failed to fetch data for the form.");
                }
            };
            fetchPrerequisites();
        }
    }, [open, job]);

    const handleOk = () => {
        form.validateFields()
            .then(values => {
                onFinish(values);
            })
            .catch(info => {
                console.log('Validate Failed:', info);
            });
    };

    return (
        <Modal
            open={open}
            title={job ? "Edit Job" : "Create a new Job"}
            okText={job ? "Update" : "Create"}
            cancelText="Cancel"
            onCancel={onCancel}
            onOk={handleOk}
        >
            <Form form={form} layout="vertical" name="job_form">
                <Form.Item name="jobName" label="Job Name" rules={[{ required: true, message: 'Please input the job name!' }]}>
                    <Input />
                </Form.Item>
                <Form.Item name="description" label="Description">
                    <Input.TextArea />
                </Form.Item>
                <Form.Item name="jobType" label="Job Type" rules={[{ required: true, message: 'Please select a job type!' }]}>
                    <Select placeholder="Select a type">
                        <Option value="START_JOB">Start Job</Option>
                        <Option value="CONVERSION">Conversion</Option>
                        <Option value="VALIDATION">Validation</Option>
                        <Option value="REPORT">Report</Option>
                        <Option value="DISPATCH_NODE">Dispatch Node</Option>
                    </Select>
                </Form.Item>
                <Form.Item name="systemInfoId" label="System" rules={[{ required: true, message: 'Please select a system!' }]}>
                    <Select placeholder="Select a system">
                        {systems.map(system => (
                            <Option key={system.id} value={system.id}>{system.systemName}</Option>
                        ))}
                    </Select>
                </Form.Item>
                <Form.Item name="successorJobNames" label="Successor Jobs (Post-keys)">
                    <Select mode="multiple" placeholder="Select successor jobs">
                        {jobs.map(job => (
                            <Option key={job.id} value={job.jobName}>{job.jobName}</Option>
                        ))}
                    </Select>
                </Form.Item>
            </Form>
        </Modal>
    );
};

export default JobFormModal;
