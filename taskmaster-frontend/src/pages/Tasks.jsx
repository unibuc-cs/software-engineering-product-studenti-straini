import React, { useEffect, useState } from "react";
import api from "../services/api";
import PropTypes from "prop-types";
import { Link } from "react-router-dom";


const Tasks = ({ userId }) => {
    const [tasks, setTasks] = useState([]);

    useEffect(() => {
        const fetchTasks = async () => {
            try {
                const response = await api.get(`/tasks/user/${userId}`);
                setTasks(response.data);
            } catch (error) {
                console.error("Eroare la preluarea task-urilor:", error);
            }
        };

        if (userId) {
            fetchTasks();
        }
    }, [userId]);

    return (
        <div>
            <h2>Task-urile tale</h2>
            <ul>
                {tasks.map((task) => (
                    <li key={task.taskId}>
                        <Link to={`/tasks/${task.taskId}`}>
                            <strong>{task.title}</strong> - {task.completed ? "✅ Completat" : "❌ Necompletat"}
                        </Link>
                    </li>
                ))}
            </ul>
        </div>
    );
};


Tasks.propTypes = {
    userId: PropTypes.number,
};

export default Tasks;
