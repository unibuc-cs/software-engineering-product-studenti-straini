import axios from "axios"; //biblioteca care ne permite sa facem cereri HTTP catre un server

const api = axios.create({
    baseURL: "http://localhost:9091/api", // am configurat o instanta custom de
                                          // axios cu URL-ul backend-ului
});

export default api; //in fisiere importam api si folosim fara sa mai scriem mereu URL-ul
