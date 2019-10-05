import React from 'react';
import logo from './logo.svg';
import './App.css';

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <form>
          <label>
            f(x):
            <input type="text" name="name" />
          </label>
          <input type="submit" value="Отправить" />
        </form>

      </header>
    </div>
  );
}

export default App;
