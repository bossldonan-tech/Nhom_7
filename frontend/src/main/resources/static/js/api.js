// Dia chi API Gateway - cong vao duy nhat cho tat ca microservice
const API_BASE = "http://localhost:8080";

function getToken() {
    return localStorage.getItem("token");
}

function getUser() {
    const raw = localStorage.getItem("user");
    return raw ? JSON.parse(raw) : null;
}

function isLoggedIn() {
    return !!getToken();
}

function isAdmin() {
    const user = getUser();
    return !!user && user.role === "ADMIN";
}

function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    window.location.href = "index.html";
}

/**
 * Goi API qua API Gateway. Tu dong gan JWT (neu co) va parse JSON.
 * Nem loi voi message lay tu ApiError tra ve boi backend.
 */
async function apiFetch(path, options = {}) {
    const headers = options.headers ? { ...options.headers } : {};
    if (!(options.body instanceof FormData)) {
        headers["Content-Type"] = "application/json";
    }
    const token = getToken();
    if (token) {
        headers["Authorization"] = "Bearer " + token;
    }

    let response;
    try {
        response = await fetch(API_BASE + path, { ...options, headers });
    } catch (err) {
        throw new Error("Khong the ket noi toi API Gateway (http://localhost:8080). Hay chac chan eureka-server, api-gateway va cac service da duoc khoi dong.");
    }

    if (response.status === 204) {
        return null;
    }

    let data = null;
    const text = await response.text();
    if (text) {
        try { data = JSON.parse(text); } catch (e) { data = text; }
    }

    if (!response.ok) {
        const message = (data && data.message) ? data.message : "Da xay ra loi (" + response.status + ")";
        if (response.status === 401) {
            localStorage.removeItem("token");
            localStorage.removeItem("user");
        }
        throw new Error(message);
    }

    return data;
}

function formatCurrency(amount) {
    return new Intl.NumberFormat("vi-VN", { style: "currency", currency: "VND" }).format(amount || 0);
}

function formatDate(isoString) {
    if (!isoString) return "";
    const d = new Date(isoString);
    return d.toLocaleString("vi-VN");
}

function showAlert(containerEl, message, type = "error") {
    containerEl.innerHTML = `<div class="alert alert-${type}">${message}</div>`;
}
