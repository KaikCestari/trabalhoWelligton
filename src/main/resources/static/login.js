const form = document.querySelector("#loginForm");
const mensagem = document.querySelector("#loginMensagem");

async function verificarLogin() {
    const resposta = await fetch("/api/auth/me");
    if (resposta.ok) {
        window.location.href = "cursos.html";
    }
}

form.addEventListener("submit", async (event) => {
    event.preventDefault();
    mensagem.textContent = "";

    const dados = {
        usuario: document.querySelector("#usuario").value.trim(),
        senha: document.querySelector("#senha").value.trim()
    };

    const resposta = await fetch("/api/auth/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(dados)
    });

    if (resposta.ok) {
        window.location.href = "cursos.html";
        return;
    }

    mensagem.textContent = "Usuario ou senha invalidos.";
});

verificarLogin();
