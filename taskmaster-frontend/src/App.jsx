import { useState } from 'react'
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import Register from "./pages/Register";

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/auth" element={<Login />} />
                <Route path="/auth/register" element={<Register />} />
            </Routes>
        </Router>
    );
}

export default App;