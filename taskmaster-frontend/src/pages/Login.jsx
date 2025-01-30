import React, { useState } from "react"; //cream o componenta React
import api from "../services/api"; //am importat api din api.js
import "../styles/Login.css";

const Login = () => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState(""); //backend-ul verifica daca hash ul pentru parola introdusa este acelasi.

    const handleLogin = async (e) => {
        e.preventDefault(); // previne refresh-ul paginii
        console.log("Trimitem datele:", { username, password });
        try {
            const response = await api.post("/auth/login", { username, password });
            console.log("Raspuns primit:", response.data); // afisam raspunsul primit
            alert(`Autentificare reusita! Bine ai venit, ${response.data}`);
        } catch (error) {
            console.error("Eroare backend:", error.response?.data || error.message); // afisam erorile
            alert("Autentificare esuata!");
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