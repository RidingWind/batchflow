import React, { useState, useEffect } from 'react';
import { Table, Button, Typography, Space, message, Modal } from 'antd';
import api from '../services/api';
import JobFormModal from '../components/JobFormModal';
import NodeDetailsModal from '../components/NodeDetailsModal';

const { Title } = Typography;

const JobList = () => {
    const [jobs, setJobs] = useState([]);
    const [loading, setLoading] = useState(false);
    const [isFormModalVisible, setIsFormModalVisible] = useState(false);
    const [isDetailsModalVisible, setIsDetailsModalVisible] = useState(false);
    const [selectedJobDetails, setSelectedJobDetails] = useState(null);
    const [editingJob, setEditingJob] = useState(null);

    const fetchJobs = async () => {
        setLoading(true);
        try {
            const response = await api.getJobs();
            setJobs(response.data);
        } catch (error) {
            message.error('Failed to fetch jobs.');
            console.error('Failed to fetch jobs:', error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchJobs();
    }, []);

    const handleFormFinish = async (values) => {
        const jobData = {
            ...values,
            // Ensure successorJobNames is an array even if undefined
            successorJobNames: values.successorJobNames || [],
        };

        try {
            if (editingJob) {
                await api.updateJob(editingJob.id, jobData);
                message.success('Job updated successfully!');
            } else {
                await api.createJob(jobData);
                message.success('Job created successfully!');
            }
            setIsFormModalVisible(false);
            setEditingJob(null);
            fetchJobs(); // Refresh the list
        } catch (error) {
            message.error('Failed to create job.');
            console.error('Failed to create job:', error);
        }
    };

    const handleDelete = (jobId) => {
        Modal.confirm({
            title: 'Are you sure you want to delete this job?',
            content: 'This action cannot be undone.',
            okText: 'Yes, Delete',
            okType: 'danger',
            onOk: async () => {
                try {
                    await api.deleteJob(jobId);
                    message.success('Job deleted successfully!');
                    fetchJobs(); // Refresh the list
                } catch (error) {
                    message.error(error.response?.data?.error || 'Failed to delete job.');
                    console.error('Failed to delete job:', error);
                }
            },
        });
    };

    const showEditModal = (job) => {
        setEditingJob(job);
        setIsFormModalVisible(true);
    };

    const handleViewDetails = async (jobId) => {
        try {
            const response = await api.getJobById(jobId);
            setSelectedJobDetails(response.data);
            setIsDetailsModalVisible(true);
        } catch (error) {
            message.error('Failed to fetch job details.');
            console.error('Failed to fetch job details:', error);
        }
    };

    const columns = [
        {
            title: 'Job Name',
            dataIndex: 'jobName',
            key: 'jobName',
        },
        {
            title: 'Job Type',
            dataIndex: 'jobType',
            key: 'jobType',
        },
        {
            title: 'System',
            dataIndex: ['systemInfo', 'systemName'],
            key: 'system',
        },
        {
            title: 'Description',
            dataIndex: 'description',
            key: 'description',
        },
        {
            title: 'Actions',
            key: 'actions',
            render: (_, record) => (
                <Space size="middle">
                    <Button type="link" onClick={() => handleViewDetails(record.id)}>
                        View Details
                    </Button>
                    <Button type="link" onClick={() => showEditModal(record)}>
                        Edit
                    </Button>
                    <Button type="link" danger onClick={() => handleDelete(record.id)}>
                        Delete
                    </Button>
                </Space>
            ),
        },
    ];

    return (
        <div>
            <Title level={2}>Job Management</Title>
            <Button
                type="primary"
                style={{ marginBottom: 16 }}
                onClick={() => setIsFormModalVisible(true)}
            >
                Create Job
            </Button>
            <Table
                columns={columns}
                dataSource={jobs}
                loading={loading}
                rowKey="id"
            />
            <JobFormModal
                open={isFormModalVisible}
                onCancel={() => {
                    setIsFormModalVisible(false);
                    setEditingJob(null);
                }}
                onFinish={handleFormFinish}
                job={editingJob}
            />
            {selectedJobDetails && (
                <NodeDetailsModal
                    open={isDetailsModalVisible}
                    onCancel={() => setIsDetailsModalVisible(false)}
                    nodeData={selectedJobDetails}
                />
            )}
        </div>
    );
};

export default JobList;
