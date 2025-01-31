import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import api from "../services/api";

const TaskDetails = () => {
    const { id } = useParams(); // Extragem ID-ul din URL
    const [task, setTask] = useState(null);
    const navigate = useNavigate();
    const [isEditing, setIsEditing] = useState(false); //starea pentru modul editare
    const [editedTask, setEditedTask] = useState({
        title: "",
        description: "",
        priority: "MEDIUM",
        deadline: "",
        completed: false
    });

    useEffect(() => {
        console.log("Task ID primit:", id); // Debugging
        if (!id) return;

        const fetchTask = async () => {
            try {
                const response = await api.get(`/tasks/${id}`);
                setTask(response.data);
                setEditedTask(response.data);
            } catch (error) {
                console.error("Eroare la preluarea detaliilor task-ului:", error);
            }
        };

        fetchTask();
    }, [id]);

    const handleUpdateTask = async (e) => {
        e.preventDefault();
        try {
            await api.patch(`/tasks/${id}`, editedTask);
            setTask(editedTask);
            setIsEditing(false); //iesim din modul de editare
        } catch (error) {
            console.error("Eroare la actualizarea task-ului:", error);
        }
    };

    const handleCompleteToggle = async () => {
        try {
            const updatedTask = { ...task, completed: !task.completed };
            await api.patch(`/tasks/${id}`, updatedTask);
            setTask(updatedTask);
        } catch (error) {
            console.error("Eroare la actualizarea statusului:", error);
        }
    };

    if (!task) return <p>Se încarcă detaliile task-ului...</p>;

    return (
        <div>
            <h2>Detalii Task</h2>

            {isEditing ? (
                <form onSubmit={handleUpdateTask} style={{ display: "flex", flexDirection: "column", gap: "10px" }}>
                    <input
                        type="text"
                        value={editedTask.title}
                        onChange={(e) => setEditedTask({ ...editedTask, title: e.target.value })}
                        required
                    />
                    <textarea
                        value={editedTask.description}
                        onChange={(e) => setEditedTask({ ...editedTask, description: e.target.value })}
                    />
                    <select
                        value={editedTask.priority}
                        onChange={(e) => setEditedTask({ ...editedTask, priority: e.target.value })}
                    >
                        <option value="HIGH">🟥 High</option>
                        <option value="MEDIUM">🟧 Medium</option>
                        <option value="LOW">🟩 Low</option>
                    </select>
                    <input
                        type="date"
                        value={editedTask.deadline || ""}
                        onChange={(e) => setEditedTask({ ...editedTask, deadline: e.target.value })}
                    />
                    <button type="submit">💾 Salvează</button>
                </form>
            ) : (
                <div>
                    <h3>{task.title}</h3>
                    <p>{task.description}</p>
                    <p>Prioritate: {task.priority}</p>
                    <p>📅 Deadline: {task.deadline ? task.deadline : "Fără deadline"}</p>
                    <p>Status: {task.completed ? "✅ Complet" : "❌ Incomplet"}</p>
                    <button onClick={() => setIsEditing(true)}>✏️ Editează</button>
                </div>
            )}

            {/* Checkbox pentru completare */}
            <div style={{ marginTop: "10px" }}>
                <label>
                    <input type="checkbox" checked={task.completed} onChange={handleCompleteToggle} />
                    Marchează ca {task.completed ? "necompletat" : "completat"}
                </label>
            </div>

            <button onClick={() => navigate("/tasks")} style={{ marginTop: "10px" }}>⬅️ Înapoi la task-uri</button>
        </div>
    );
};

export default TaskDetails;
