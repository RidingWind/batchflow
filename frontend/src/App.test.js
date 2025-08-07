import { render, screen } from '@testing-library/react';
import App from './App';
import '@ant-design/v5-patch-for-react-19';

test('renders learn react link', () => {
  render(<App />);
  const linkElement = screen.getByText(/learn react/i);
  expect(linkElement).toBeInTheDocument();
});
