import React, { useState } from "react"; //cream o componenta React
import api from "../services/api"; //am importat api din api.js
import { useNavigate } from "react-router-dom";

const Login = () => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState(""); //backend-ul verifica daca hash ul pentru parola introdusa este acelasi.
    const navigate = useNavigate(); //pentru navigare

    const handleLogin = async (e) => {
        e.preventDefault();

        try {
            const response = await api.post("/auth/login", { username, password });
            console.log("📢 Răspuns primit:", response.data);

            if (!response.data.userId) {
                console.error("⚠️ Eroare: userId nu a fost primit de la backend!");
                alert("Eroare: userId nu a fost primit!");
                return;
            }

            localStorage.setItem("userId", response.data.userId);
            console.log("UserId salvat:", localStorage.getItem("userId"));

            navigate("/tasks");

        } catch (error) {
            console.error("Eroare backend:", error.response?.data || error.message);
            alert("Autentificare eșuată!");
        }
    };

    return (
        <form onSubmit={handleLogin}>
            <h2>Autentificare</h2>
            <input
                type="text"
                placeholder="Username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
            />
            <input
                type="password"
                placeholder="Password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
            />
            <button type="submit">Login</button>
        </form>
    );
};

export default Login;