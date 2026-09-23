async function handleLogin(event) {
    event.preventDefault();
    const errorBox = document.getElementById("error-box");
    errorBox.innerHTML = "";

    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value;

    try {
        const data = await apiFetch("/api/auth/login", {
            method: "POST",
            body: JSON.stringify({ username, password }),
        });
        localStorage.setItem("token", data.token);
        localStorage.setItem("user", JSON.stringify(data.user));
        window.location.href = "index.html";
    } catch (err) {
        showAlert(errorBox, err.message);
    }
}

async function handleRegister(event) {
    event.preventDefault();
    const errorBox = document.getElementById("error-box");
    errorBox.innerHTML = "";

    const payload = {
        username: document.getElementById("username").value.trim(),
        email: document.getElementById("email").value.trim(),
        password: document.getElementById("password").value,
        fullName: document.getElementById("fullName").value.trim(),
        phone: document.getElementById("phone").value.trim(),
        address: document.getElementById("address").value.trim(),
    };

    try {
        const data = await apiFetch("/api/auth/register", {
            method: "POST",
            body: JSON.stringify(payload),
        });
        localStorage.setItem("token", data.token);
        localStorage.setItem("user", JSON.stringify(data.user));
        window.location.href = "index.html";
    } catch (err) {
        showAlert(errorBox, err.message);
    }
}
