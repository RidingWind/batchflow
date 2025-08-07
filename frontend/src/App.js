import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import { Layout, Menu } from 'antd';
import { Link } from 'react-router-dom';
import JobList from './pages/JobList';
import GraphViewer from './pages/GraphViewer';
import './App.css';

const { Header, Content } = Layout;


function getItem(label, key, icon) {
  return {
    key,
    icon,
    label,
  }
}
const items = [
  getItem('Job List', '1'),
  getItem('Workflow Graph', '2'),
]

function App() {
  return (
    <Router>
      <Layout style={{ minHeight: '100vh' }}>
        <Header>
          <Menu theme="dark" mode="horizontal" defaultSelectedKeys={['1']}>
            <Menu.Item key="1"><Link to="/">Job List</Link></Menu.Item>
            <Menu.Item key="2"><Link to="/graph">Workflow Graph</Link></Menu.Item>
          </Menu>
        </Header>
        <Content style={{ padding: '50px' }}>
          <Routes>
            <Route path="/" element={<JobList />} />
            <Route path="/graph" element={<GraphViewer />} />
          </Routes>
        </Content>
      </Layout>
    </Router>
  );
}

export default App;
