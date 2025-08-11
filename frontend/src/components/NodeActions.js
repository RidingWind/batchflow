import React from 'react';
import { Button, Space, message } from 'antd';
import api from '../services/api';

const NodeActions = ({ job, onActionSuccess }) => {

    const handleAction = async (action, successMessage) => {
        try {
            await action(job.id);
            message.success(successMessage);
            if (onActionSuccess) {
                onActionSuccess();
            }
        } catch (error) {
            message.error('Action failed.');
            console.error('Action failed:', error);
        }
    };

    if (!job) {
        return null;
    }

    return (
        <Space style={{ marginTop: 20 }}>
            {job.status === 'IDLE' && (
                <Button type="primary" onClick={() => handleAction(api.startWorkflow, 'Workflow started.')}>
                    Start
                </Button>
            )}
            {(job.status === 'SUCCESS' || job.status === 'STOPPED') && (
                <Button onClick={() => handleAction(api.rerunWorkflow, 'Workflow will be rerun.')}>
                    Rerun
                </Button>
            )}
            {job.status === 'FAILED' && (
                <Button onClick={() => handleAction(api.resetJob, 'Job has been reset.')}>
                    Reset
                </Button>
            )}
            {job.status === 'RUNNING' && (
                <Button danger onClick={() => handleAction(api.stopJob, 'Stop signal sent.')}>
                    Stop
                </Button>
            )}
            {job.status === 'PAUSED' && (
                <Button onClick={() => handleAction(api.resumeJob, 'Resume signal sent.')}>
                    Resume
                </Button>
            )}
        </Space>
    );
};

export default NodeActions;
