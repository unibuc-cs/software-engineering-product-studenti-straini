import React, { useEffect, useState, useRef } from "react";
import api from "../services/api";
import PropTypes from "prop-types";
import { Link, useNavigate } from "react-router-dom";
import "../styles/Tasks.css";



const Tasks = ({ userId }) => {
    const [tasks, setTasks] = useState([]);
    const [showForm, setShowForm] = useState(false); // pentru a afisa formularul
    const [newTask, setNewTask] = useState({
        title: "",
        description: "",
        priority: "MEDIUM",
        deadline: "",
    });

    const [sortBy, setSortBy] = useState("priority"); //sortare default dupa prioritati
    const [filterStatus, setFilterStatus] = useState("all"); // "all", "completed", "incomplete"
    const [filterPriority, setFilterPriority] = useState("ALL"); // "ALL", "HIGH", "MEDIUM", "LOW"
    const [filterDeadline, setFilterDeadline] = useState("ALL"); // "ALL", "Overdue", "Upcoming"
    const formRef = useRef(null);

    useEffect(() => {
        const handleClickOutside = (event) => {
            if (formRef.current && !formRef.current.contains(event.target)) {
                setShowForm(false);
            }
        };

        if (showForm) {
            document.addEventListener("mousedown", handleClickOutside);
        } else {
            document.removeEventListener("mousedown", handleClickOutside);
        }

        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, [showForm]);

    const navigate = useNavigate();
    const handleLogout = () => {
        localStorage.removeItem("userId");
        navigate("/auth");
    };

    useEffect(() => {
        if (!userId) {
            navigate("/auth");
        } else {
            const fetchTasks = async () => {
                try {
                    const response = await api.get(`/tasks/user/${userId}`);
                    setTasks(response.data);
                } catch (error) {
                    console.error("Eroare la preluarea task-urilor:", error);
                }
            };
            fetchTasks();
        }
    }, [userId, navigate]);

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

    //sortare task uri
    const sortedTasks = [...tasks].sort((a, b) => {
        if (sortBy === "priority") {
            const priorityOrder = { HIGH: 1, MEDIUM: 2, LOW: 3 };
            return priorityOrder[a.priority] - priorityOrder[b.priority];
        } else if (sortBy === "deadline") {
            return new Date(a.deadline || "9999-12-31") - new Date(b.deadline || "9999-12-31");
        }
        return 0;
    });

    //buton de marcare ca finalizat
    const handleCompleteToggle = async (taskId, completed) => {
        try {
            await api.patch(`/tasks/${taskId}`, { completed: !completed });
            setTasks(tasks.map(task =>
                task.taskId === taskId ? { ...task, completed: !completed } : task
            ));
        } catch (error) {
            console.error("Eroare la actualizarea statusului:", error);
        }
    };

    //stergere cu confirmare
    const handleDeleteTask = async (taskId) => {
        const confirmDelete = window.confirm("Sigur vrei să ștergi acest task?");
        if (!confirmDelete) return;

        try {
            await api.delete(`/tasks/${taskId}`);
            setTasks(tasks.filter(task => task.taskId !== taskId));
        } catch (error) {
            console.error("Eroare la ștergerea task-ului:", error);
        }
    };


    //filtrare task uri
    const filteredTasks = [...sortedTasks].filter(task => {
        if (filterStatus === "completed" && !task.completed) return false;
        if (filterStatus === "incomplete" && task.completed) return false;
        if (filterPriority !== "ALL" && task.priority !== filterPriority) return false;

        const today = new Date();
        if (filterDeadline === "Overdue" && (!task.deadline || new Date(task.deadline) > today)) return false;
        if (filterDeadline === "Upcoming" && (!task.deadline || new Date(task.deadline) < today)) return false;

        return true;
    });

    const completedTasks = tasks.filter(task => task.completed).length;
    const progress = (completedTasks / tasks.length) * 100 || 0;

    return (
        <div className="container">

            {/* Buton de logout */}
            <button onClick={handleLogout} className="logout-button">
                Logout
            </button>

            <h2 className="title">Task-urile tale</h2>

            {/*afisare bara progres*/}
            <div className="progress-bar">
                <span className="progress-text">
                    {completedTasks} din {tasks.length} task-uri completate
                </span>
                <div className="progress" style={{width: `${progress}%`}}></div>
            </div>


            {/*filtrare si sortare*/}
            <div className="filters-container">
                <select onChange={(e) => setSortBy(e.target.value)} value={sortBy}>
                    <option value="priority">📌 Sortare: Prioritate</option>
                    <option value="deadline">📅 Sortare: Deadline</option>
                </select>

                <select onChange={(e) => setFilterStatus(e.target.value)} value={filterStatus}>
                    <option value="all">✅ Toate</option>
                    <option value="completed">✅ Completate</option>
                    <option value="incomplete">❌ Necompletate</option>
                </select>

                <select onChange={(e) => setFilterPriority(e.target.value)} value={filterPriority}>
                    <option value="ALL">🔥 Toate Prioritățile</option>
                    <option value="HIGH">🟥 High</option>
                    <option value="MEDIUM">🟧 Medium</option>
                    <option value="LOW">🟩 Low</option>
                </select>

                <select onChange={(e) => setFilterDeadline(e.target.value)} value={filterDeadline}>
                    <option value="ALL">📆 Toate Deadline-urile</option>
                    <option value="Overdue">⏳ Depășite</option>
                    <option value="Upcoming">🔜 În Viitor</option>
                </select>
            </div>


            {/*buton pentru adaugarea unui task*/}
            <button className="add-task-button" onClick={() => setShowForm(true)}>
            </button>


            {showForm && (
                <form
                    ref={formRef}
                    onSubmit={handleAddTask}
                    className="task-form"
                >
                    <input
                        type="text"
                        placeholder="Titlu"
                        value={newTask.title}
                        onChange={(e) => setNewTask({...newTask, title: e.target.value})}
                        required
                    />
                    <textarea
                        placeholder="Descriere"
                        value={newTask.description}
                        onChange={(e) => setNewTask({...newTask, description: e.target.value})}
                    />
                    <select
                        value={newTask.priority}
                        onChange={(e) => setNewTask({...newTask, priority: e.target.value})}
                    >
                        <option value="HIGH">🟥 High</option>
                        <option value="MEDIUM">🟧 Medium</option>
                        <option value="LOW">🟩 Low</option>
                    </select>
                    <input
                        type="date"
                        value={newTask.deadline}
                        onChange={(e) => setNewTask({...newTask, deadline: e.target.value})}
                    />
                    <button type="submit">Adaugă</button>
                </form>
            )}

            <hr className="divider"/>
            {/*linia despartitoare */}

            <ul className="task-list">
                {filteredTasks.map((task) => (
                    <li
                        key={task.taskId}
                        className={`task-item ${task.priority.toLowerCase()} ${task.completed ? 'completed' : ''}`}>
                        <div className="task-left">
                            <input
                                type="checkbox"
                                className="task-checkbox"
                                checked={task.completed}
                                onChange={() => handleCompleteToggle(task.taskId, task.completed)}
                            />
                            <Link to={`/tasks/${task.taskId}`} style={{ textDecoration: 'none', color: 'black' }}>
                                <div className="task-content">
                                    <strong className="task-title">{task.title}</strong>
                                    <span className="status-separator"> - </span>
                                    <span className={`status ${task.completed ? 'completed-status' : ''}`}>
                                        {task.completed ? "✅ Completat" : "❌ În curs de realizare"}
                                    </span>
                                    {task.deadline && (
                                        <>
                                            <span className="status-separator"> - </span>
                                            <span className="deadline">{task.deadline}</span>
                                        </>
                                    )}
                                </div>
                            </Link>
                        </div>

                        <button
                            className="delete-button"
                            onClick={() => handleDeleteTask(task.taskId)}
                        >
                            🗑️
                        </button>

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
