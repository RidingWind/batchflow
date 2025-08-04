import axios from 'axios';

const apiClient = axios.create({
    baseURL: '/api', // The proxy will handle redirecting this to http://localhost:8080/api
    headers: {
        'Content-Type': 'application/json',
    },
});

// System API
export const getSystems = () => apiClient.get('/systems');
export const createSystem = (systemData) => apiClient.post('/systems', systemData);

// Job API
export const getJobs = () => apiClient.get('/jobs');
export const getJobById = (id) => apiClient.get(`/jobs/${id}`);
export const createJob = (jobData) => apiClient.post('/jobs', jobData);
export const updateJob = (id, jobData) => apiClient.put(`/jobs/${id}`, jobData);
export const deleteJob = (id) => apiClient.delete(`/jobs/${id}`);

// Graph API
export const getJobGraph = () => apiClient.get('/graph/jobs');
export const getSystemGraph = () => apiClient.get('/graph/systems');

// Other API
export const getJobsBySystemId = (systemId) => apiClient.get(`/systems/${systemId}/jobs`);

// Execution API
export const startWorkflow = (jobId) => apiClient.post(`/executions/start/${jobId}`);
export const rerunWorkflow = (jobId) => apiClient.post(`/executions/rerun/${jobId}`);
export const resetJob = (jobId) => apiClient.post(`/executions/reset/${jobId}`);
export const stopJob = (jobId) => apiClient.post(`/executions/stop/${jobId}`);
export const resumeJob = (jobId) => apiClient.post(`/executions/resume/${jobId}`);


// This will be expanded later for other features like execution control
const api = {
    getSystems,
    createSystem,
    getJobs,
    getJobById,
    createJob,
    updateJob,
    deleteJob,
};

export default api;
