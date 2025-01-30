import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import api from "../services/api";

const TaskDetails = () => {
    const { id } = useParams(); // Extragem ID-ul din URL
    const [task, setTask] = useState(null);

    useEffect(() => {
        console.log("Task ID primit:", id); // Debugging
        if (!id) return;

        const fetchTask = async () => {
            try {
                const response = await api.get(`/tasks/${id}`);
                setTask(response.data);
            } catch (error) {
                console.error("Eroare la preluarea detaliilor task-ului:", error);
            }
        };

        fetchTask();
    }, [id]);

    if (!task) return <p>Se încarcă detaliile task-ului...</p>;

    return (
        <div>
            <h2>{task.title}</h2>
            <p><strong>Descriere:</strong> {task.description}</p>
            <p><strong>Prioritate:</strong> {task.priority}</p>
            <p><strong>Completat:</strong> {task.completed ? "✅ Da" : "❌ Nu"}</p>
        </div>
    );
};

export default TaskDetails;
