import { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Tasks from "./pages/Tasks";
import TaskDetails from "./pages/TaskDetails";


function App() {
    const [userId, setUserId] = useState(null);

    useEffect(() => {
        const storedUserId = localStorage.getItem("userId");
        if (storedUserId) {
            setUserId(parseInt(storedUserId, 10));
        }
    }, []);

    return (
        <Router>
            <Routes>
                <Route path="/auth" element={<Login />} />
                <Route path="/auth/register" element={<Register />} />
                <Route path="/tasks" element={<Tasks userId={userId} />} />
                <Route path="/tasks/:id" element={<TaskDetails />} />

            </Routes>
        </Router>
    );
}

export default App;
