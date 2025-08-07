import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import { Layout, Menu } from 'antd';
import { Link } from 'react-router-dom';
import JobList from './pages/JobList';
import GraphViewer from './pages/GraphViewer';
import './App.css';

const { Header, Content } = Layout;

function App() {
  const menuItems = [
    {
      key: '1',
      label: <Link to="/">Job List</Link>,
    },
    {
      key: '2',
      label: <Link to="/graph">Workflow Graph</Link>,
    },
  ];

  return (
    <Router>
      <Layout style={{ minHeight: '100vh' }}>
        <Header>
          <Menu theme="dark" mode="horizontal" defaultSelectedKeys={['1']} items={menuItems} />
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
