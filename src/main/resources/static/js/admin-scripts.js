function validatePassword() {
    const newPassword = document.getElementById('newPassword').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    const errorMsg = document.getElementById('passwordErrorMsg');

    const requirements = [
        { regex: /.{8,}/, label: "At least 8 characters" },
        { regex: /[a-z]/, label: "Lowercase letter" },
        { regex: /[A-Z]/, label: "Uppercase letter" },
        { regex: /\d/, label: "Number" },
        { regex: /[^a-zA-Z0-9]/, label: "Special character" }
    ];

    for (const req of requirements) {
        if (!req.regex.test(newPassword)) {
            errorMsg.textContent = "Missing requirement: " + req.label;
            errorMsg.classList.remove("d-none");
            return false;
        }
    }

    if (newPassword !== confirmPassword) {
        errorMsg.textContent = "Passwords do not match.";
        errorMsg.classList.remove("d-none");
        return false;
    }

    errorMsg.classList.add("d-none");
    return true;
}

document.addEventListener("DOMContentLoaded", () => {
    const newPassword = document.getElementById("newPassword");
    const feedbackList = document.getElementById("passwordRequirements").querySelectorAll("li");

    if (newPassword) {
        newPassword.addEventListener("keyup", () => {
            const value = newPassword.value;
            const rules = [
                /.{8,}/,
                /[a-z]/,
                /[A-Z]/,
                /\d/,
                /[^a-zA-Z0-9]/
            ];
            rules.forEach((regex, i) => {
                if (regex.test(value)) {
                    feedbackList[i].classList.remove("text-danger");
                    feedbackList[i].classList.add("text-success");
                    feedbackList[i].innerHTML = "✅ " + feedbackList[i].getAttribute("data-label");
                } else {
                    feedbackList[i].classList.remove("text-success");
                    feedbackList[i].classList.add("text-danger");
                    feedbackList[i].innerHTML = "❌ " + feedbackList[i].getAttribute("data-label");
                }
            });
        });
    }

    const editing = document.body.getAttribute("data-editing");
    if (editing === "true") {
        const firstForm = document.querySelector("form");
        if (firstForm) {
            window.scrollTo(0, firstForm.offsetTop - 100);
        }
    }
});
