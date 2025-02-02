import React, { useState } from "react"; //idem Login
import api from "../services/api";
import "../styles/Register.css";
import { useNavigate } from "react-router-dom";

const Register = () => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [email, setEmail] = useState("");
    const navigate = useNavigate();

    const handleRegister = async (e) => {
        e.preventDefault();
        try {
            const response = await api.post("/auth/register", { username, password, email });
            console.log(response.data);
            alert("Inregistrare reusita!");
            navigate("/auth");
        } catch (error) {
            console.error(error.response?.data);
            alert("Eroare la inregistrare!");
        }
    };

    return (
        <form onSubmit={handleRegister}>
            <h2>Inregistrare</h2>
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
            <input
                type="email"
                placeholder="Email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
            />
            <button type="submit">Register</button>
        </form>
    );
};

export default Register;
