import React from 'react';
import { Modal, Descriptions, Tag, List, Typography } from 'antd';
import NodeActions from './NodeActions';

const { Title } = Typography;

const NodeDetailsModal = ({ open, onCancel, nodeData }) => {
    if (!nodeData) {
        return null;
    }

    const isJob = nodeData.jobName !== undefined;

    const handleActionSuccess = () => {
        onCancel(); // Close the modal after a successful action
    };

    const renderJobDetails = () => (
        <>
            <Descriptions bordered column={1}>
                <Descriptions.Item label="Job Name">{nodeData.jobName}</Descriptions.Item>
                <Descriptions.Item label="Description">{nodeData.description}</Descriptions.Item>
                <Descriptions.Item label="Job Type"><Tag>{nodeData.jobType}</Tag></Descriptions.Item>
                <Descriptions.Item label="Status"><Tag>{nodeData.status}</Tag></Descriptions.Item>
                <Descriptions.Item label="System">{nodeData.systemInfo?.systemName}</Descriptions.Item>
                <Descriptions.Item label="Inputs">
                    <List
                        dataSource={nodeData.inputs}
                        renderItem={item => <List.Item>{item.inputName} ({item.inputType})</List.Item>}
                        size="small"
                    />
                </Descriptions.Item>
                <Descriptions.Item label="Outputs">
                    <List
                        dataSource={nodeData.outputs}
                        renderItem={item => <List.Item>{item.outputName} ({item.outputType})</List.Item>}
                        size="small"
                    />
                </Descriptions.Item>
            </Descriptions>
            <NodeActions job={nodeData} onActionSuccess={handleActionSuccess} />
        </>
    );

    const renderSystemDetails = () => (
        <>
            <Descriptions bordered column={1}>
                <Descriptions.Item label="System Name">{nodeData.label}</Descriptions.Item>
                <Descriptions.Item label="System ID">{nodeData.id}</Descriptions.Item>
            </Descriptions>
            <Title level={5} style={{ marginTop: 20 }}>Jobs in this System</Title>
            <List
                bordered
                dataSource={nodeData.jobs}
                renderItem={job => <List.Item>{job.jobName}</List.Item>}
            />
        </>
    );

    return (
        <Modal
            title={isJob ? "Job Details" : "System Details"}
            open={open}
            onCancel={onCancel}
            footer={null}
            width={800}
        >
            {isJob ? renderJobDetails() : renderSystemDetails()}
        </Modal>
    );
};

export default NodeDetailsModal;
