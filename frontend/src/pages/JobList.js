import React, { useState, useEffect } from 'react';
import { Table, Button, Typography, Space, message } from 'antd';
import api from '../services/api';
import JobFormModal from '../components/JobFormModal';

const { Title } = Typography;

const JobList = () => {
    const [jobs, setJobs] = useState([]);
    const [loading, setLoading] = useState(false);
    const [isModalVisible, setIsModalVisible] = useState(false);

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

    const handleCreate = async (values) => {
        try {
            await api.createJob(values);
            setIsModalVisible(false);
            message.success('Job created successfully!');
            fetchJobs(); // Refresh the list
        } catch (error) {
            message.error('Failed to create job.');
            console.error('Failed to create job:', error);
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
                    <Button type="link">View Details</Button>
                    <Button type="link">Edit</Button>
                    <Button type="link">Delete</Button>
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
                onClick={() => setIsModalVisible(true)}
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
                visible={isModalVisible}
                onCancel={() => setIsModalVisible(false)}
                onFinish={handleCreate}
            />
        </div>
    );
};

export default JobList;
