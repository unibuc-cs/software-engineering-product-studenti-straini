import React, { useEffect, useState } from "react";
import api from "../services/api";
import PropTypes from "prop-types";
import { Link } from "react-router-dom";


const Tasks = ({ userId }) => {
    const [tasks, setTasks] = useState([]);
    const [showForm, setShowForm] = useState(false); // pentru a afisa formularul
    const [newTask, setNewTask] = useState({
        title: "",
        description: "",
        priority: "MEDIUM",
        deadline: "",
    });

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

    const handleAddTask = async (e) => {
        e.preventDefault();
        try {
            const taskToSend = {
                ...newTask,
                user: { id: userId } //atribuim userId-ul la task
            };
            await api.post("/tasks", taskToSend);
            setShowForm(false); //ascundem formularul dupa adaugare
            setNewTask({ title: "", description: "", priority: "MEDIUM", deadline: "" });

            //reincarcam lista de task uri
            const response = await api.get(`/tasks/user/${userId}`);
            setTasks(response.data);
        } catch (error) {
            console.error("Eroare la adăugare:", error);
        }
    };

    return (
        <div>
            <h2>Task-urile tale</h2>

            <button onClick={() => setShowForm(true)}>➕ Adaugă Task</button>

            {showForm && (
                <form onSubmit={handleAddTask} style={{ marginTop: "10px", display: "flex", flexDirection: "column", gap: "5px" }}>
                    <input
                        type="text"
                        placeholder="Titlu"
                        value={newTask.title}
                        onChange={(e) => setNewTask({ ...newTask, title: e.target.value })}
                        required
                    />
                    <textarea
                        placeholder="Descriere"
                        value={newTask.description}
                        onChange={(e) => setNewTask({ ...newTask, description: e.target.value })}
                    />
                    <select
                        value={newTask.priority}
                        onChange={(e) => setNewTask({ ...newTask, priority: e.target.value })}
                    >
                        <option value="HIGH">🟥 High</option>
                        <option value="MEDIUM">🟧 Medium</option>
                        <option value="LOW">🟩 Low</option>
                    </select>
                    <input
                        type="date"
                        value={newTask.deadline}
                        onChange={(e) => setNewTask({ ...newTask, deadline: e.target.value })}
                    />
                    <button type="submit">✅ Adaugă</button>
                </form>
            )}

            <ul>
                {tasks.map((task) => (
                    <li key={task.taskId}>
                        <Link to={`/tasks/${task.taskId}`} style={{textDecoration: 'none', color: 'black'}}>
                            <strong>{task.title}</strong> - {task.completed ? "✅ Completat" : "❌ Necompletat"}
                            {!task.completed && (
                                <p>
                                    📅 Deadline:{" "}
                                    {task.deadline
                                        ? new Date(task.deadline).toLocaleString()
                                        : "Acest task nu are deadline"}
                                </p>
                            )}
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
