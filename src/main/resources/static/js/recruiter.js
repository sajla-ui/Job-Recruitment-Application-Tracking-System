document.addEventListener("DOMContentLoaded", function () {

    const registerForm = document.getElementById("companyRegisterForm");

    if (!registerForm) {
        return;
    }

    registerForm.addEventListener("submit", async function (event) {

        event.preventDefault();

        const registerBtn = document.getElementById("registerBtn");
        const message = document.getElementById("message");

        const recruiter = {
            companyName: document.getElementById("companyName").value,
            companyDescription: document.getElementById("companyDescription").value,
            website: document.getElementById("website").value,
            companyLocation: document.getElementById("companyLocation").value,
            contactPerson: document.getElementById("contactPerson").value,
            phone: document.getElementById("phone").value,
            email: document.getElementById("email").value,
            password: document.getElementById("password").value
        };

        registerBtn.disabled = true;
        registerBtn.textContent = "Registering...";

        try {

            const response = await fetch("/api/recruiters/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(recruiter)
            });

            const data = await response.json();

            if (response.ok) {

                message.textContent =
                    "Company registered successfully! Redirecting to login...";

                message.className = "success-message";

                setTimeout(function () {
                    window.location.href = "recruiter-login.html";
                }, 1500);

            } else {

                message.textContent =
                    data.message || "Registration failed.";

                message.className = "error-message";
            }

       } catch (error) {

    console.error("Registration error:", error);

    message.textContent =
        "Error: " + error.message;

    message.className = "error-message";

        } finally {

            registerBtn.disabled = false;
            registerBtn.textContent = "Register Company";
        }
    });

});