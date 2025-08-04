import React, { useState, useEffect } from 'react';
import { Table, Button, Typography, Space, message } from 'antd';
import api from '../services/api';

const { Title } = Typography;

const JobList = () => {
    const [jobs, setJobs] = useState([]);
    const [loading, setLoading] = useState(false);

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
                    <a>View Details</a>
                    <a>Edit</a>
                    <a>Delete</a>
                </Space>
            ),
        },
    ];

    return (
        <div>
            <Title level={2}>Job Management</Title>
            <Button type="primary" style={{ marginBottom: 16 }}>
                Create Job
            </Button>
            <Table
                columns={columns}
                dataSource={jobs}
                loading={loading}
                rowKey="id"
            />
        </div>
    );
};

export default JobList;
