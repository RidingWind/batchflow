import React, { useState, useEffect, useCallback } from 'react';
import { Typography, message, Layout, Switch, Space } from 'antd';
import CytoscapeComponent from 'react-cytoscapejs';
import cytoscape from 'cytoscape';
import dagre from 'cytoscape-dagre';
import api from '../services/api';
import NodeDetailsModal from '../components/NodeDetailsModal';
import useWebSocket from '../hooks/useWebSocket';

cytoscape.use(dagre);

const { Title } = Typography;

const jobTypeColors = {
    START_JOB: '#52c41a',
    CONVERSION: '#1890ff',
    VALIDATION: '#faad14',
    REPORT: '#722ed1',
    DISPATCH_NODE: '#8c8c8c',
    SYSTEM: '#003a8c'
};

const statusColors = {
    IDLE: { bg: null, text: null }, // Use default type color
    RUNNING: { bg: '#1890ff', text: '#fff' },
    SUCCESS: { bg: '#52c41a', text: '#fff' },
    FAILED: { bg: '#f5222d', text: '#fff' },
    PAUSED: { bg: '#faad14', text: '#fff' },
    STOPPED: { bg: '#d9d9d9', text: '#000' }
};

const GraphViewer = () => {
    const [elements, setElements] = useState([]);
    const [loading, setLoading] = useState(false);
    const [viewMode, setViewMode] = useState('jobs');
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [selectedNodeData, setSelectedNodeData] = useState(null);

    const handleStatusUpdate = useCallback((jobDto) => {
        console.log('Received status update:', jobDto);
        setElements(prevElements =>
            prevElements.map(el => {
                if (el.data.id === String(jobDto.id)) {
                    // This is a job node, update its status
                    return { ...el, data: { ...el.data, status: jobDto.status } };
                }
                return el;
            })
        );
    }, []); // Empty dependency array means this function is created once

    useWebSocket('/topic/job-status', handleStatusUpdate);

    const fetchData = useCallback(async () => {
        setLoading(true);
        try {
            const apiCall = viewMode === 'jobs' ? api.getJobGraph : api.getSystemGraph;
            const response = await apiCall();
            const graphData = response.data;
            const cyElements = [
                ...graphData.nodes.map(node => ({ data: { id: node.id, label: node.label, type: node.type, status: node.status || 'IDLE' } })),
                ...graphData.edges.map(edge => ({ data: { source: edge.source, target: edge.target, label: edge.label } }))
            ];
            setElements(cyElements);
        } catch (error) {
            message.error(`Failed to fetch ${viewMode} graph.`);
        } finally {
            setLoading(false);
        }
    }, [viewMode]);

    useEffect(() => {
        fetchData();
    }, [fetchData]);

    const handleNodeTap = async (evt) => {
        const node = evt.target;
        const nodeId = node.id();
        const nodeType = node.data('type');

        try {
            if (nodeType === 'SYSTEM') {
                const response = await api.getJobsBySystemId(nodeId);
                setSelectedNodeData({ id: nodeId, label: node.data('label'), jobs: response.data });
            } else {
                const response = await api.getJobById(nodeId);
                setSelectedNodeData(response.data);
            }
            setIsModalVisible(true);
        } catch (error) {
            message.error('Failed to fetch node details.');
        }
    };

    const layout = { name: 'dagre', rankDir: 'TB' };
    const stylesheet = [
        {
            selector: 'node',
            style: {
                'label': 'data(label)',
                'width': '120px', 'height': '50px', 'shape': 'round-rectangle',
                'text-valign': 'center', 'text-halign': 'center', 'font-size': '12px',
                'text-wrap': 'wrap', 'text-max-width': '110px',
                'color': 'white', // Default text color
            }
        },
        // Default coloring by node type
        ...Object.entries(jobTypeColors).map(([type, color]) => ({
            selector: `node[type = "${type}"]`,
            style: { 'background-color': color }
        })),
        // Override coloring by status
        ...Object.entries(statusColors).map(([status, colors]) => ({
            selector: `node[status = "${status}"]`,
            style: {
                'background-color': colors.bg || (node => jobTypeColors[node.data('type')]),
                'color': colors.text || 'white',
            }
        })),
        {
            selector: 'edge',
            style: {
                'width': 2, 'line-color': '#ccc', 'target-arrow-color': '#ccc',
                'target-arrow-shape': 'triangle', 'curve-style': 'bezier'
            }
        }
    ];

    return (
        <Layout>
            <Space align="center" style={{ marginBottom: 16 }}>
                <Title level={2} style={{ margin: 0 }}>Workflow Visualization</Title>
                <Switch
                    checkedChildren="Jobs" unCheckedChildren="Systems"
                    checked={viewMode === 'jobs'}
                    onChange={(checked) => setViewMode(checked ? 'jobs' : 'systems')}
                />
            </Space>
            <CytoscapeComponent
                elements={elements}
                style={{ width: '100%', height: '800px', border: '1px solid #ddd' }}
                layout={layout}
                stylesheet={stylesheet}
                cy={(cy) => { cy.on('tap', 'node', handleNodeTap); }}
            />
            <NodeDetailsModal
                visible={isModalVisible}
                onCancel={() => setIsModalVisible(false)}
                nodeData={selectedNodeData}
            />
        </Layout>
    );
};

export default GraphViewer;
